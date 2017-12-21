package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;

import java.util.Arrays;
import java.util.List;


public class MapsFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    /* BASE */
    MapView mMapView;
    private GoogleMap googleMap;
    private CameraPosition KameraPos;
    private final static int DEFAULT_ZOOM = 15;
    private static Prevoz prevoz;

    /* NASTAVITVE IZ SHARED PREFERENCES */
    private boolean dovoljenjeZaLokacijo = false;
    private boolean pobiramVradiusu = true;
    private LatLng LokacijaPobiranja;
    //private int radius = 1000;

    /* PRAVICE */
    private final static int ZAHTEVAJ_PRAVICE_ZA_LOKACIJO = 1;

    /* MARKERJI IN LIKI */
    private MarkerOptions pobiranjeMarker;
    private MarkerOptions zahtevaPobiranjaMarker;
    private CircleOptions radiusPobiranja;
    //pattern kroga
    private static final int DEFAULT_GAP_SIZE = 10;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(DEFAULT_GAP_SIZE);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP,DOT);


    public MapsFragment() {
        // Required empty public constructor
    }

    public static MapsFragment newInstance(int sectionNumber, Prevoz p) {
        prevoz = p;
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private void ZahtevajPraviceLokacije() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            dovoljenjeZaLokacijo = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ZAHTEVAJ_PRAVICE_ZA_LOKACIJO);
        }
    }

    private void updateLocationUI(){
        if(googleMap == null){
            return;
        }
        try{
            if(dovoljenjeZaLokacijo){
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }else{
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                ZahtevajPraviceLokacije();
            }
        }catch(SecurityException e){
            Log.e("GMAPS: %s",e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ZahtevajPraviceLokacije();
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setOnMapClickListener(onClickListener);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                googleMap.getUiSettings().setTiltGesturesEnabled(true);
                try {
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getContext(), R.raw.maps_style));

                    if (!success) {
                        Log.e("MAP STYLE:", "Style parsing failed.");
                    }
                } catch (Exception e) {
                    Log.e("MAP STYLE: %s",e.getMessage());
                }

                LokacijaPobiranja = new LatLng(prevoz.latitude,prevoz.longitude);
                zahtevaPobiranjaMarker = new MarkerOptions();
                pobiranjeMarker = new MarkerOptions().position(LokacijaPobiranja).title("Lokacija pobiranja potnikov").snippet("Uporabnik ki ponuja prevoz pobira potnike tukaj!");
                if(pobiramVradiusu) {
                    radiusPobiranja = new CircleOptions().center(LokacijaPobiranja).radius(prevoz.radius).strokePattern(PATTERN_POLYLINE_DOTTED)
                            .strokeColor(Color.rgb(242, 104, 13)).fillColor(0x22886349); // prve dve 22 so za opacity!!!
                    googleMap.addCircle(radiusPobiranja);
                }
                googleMap.addMarker(pobiranjeMarker);
                KameraPos = new CameraPosition.Builder().target(LokacijaPobiranja).zoom(DEFAULT_ZOOM).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(KameraPos));
            }
        });
        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        dovoljenjeZaLokacijo = false;
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dovoljenjeZaLokacijo = true;
                }
            }
        }
        updateLocationUI();
    }

    private GoogleMap.OnMapClickListener onClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            googleMap.clear();
            zahtevaPobiranjaMarker.position(latLng).title("ZAHTEVA").snippet("Ponudnika lahko zaprosite da vas pobere tukaj!");
            googleMap.addMarker(pobiranjeMarker);
            if(pobiramVradiusu)
                googleMap.addCircle(radiusPobiranja);
            googleMap.addMarker(zahtevaPobiranjaMarker);
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
