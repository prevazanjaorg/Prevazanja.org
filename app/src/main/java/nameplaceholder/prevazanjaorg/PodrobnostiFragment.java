package nameplaceholder.prevazanjaorg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class PodrobnostiFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static Prevoz prevoz;

    public PodrobnostiFragment() {
        // Required empty public constructor
    }

    public static PodrobnostiFragment newInstance(int sectionNumber, Prevoz p) {
        PodrobnostiFragment fragment = new PodrobnostiFragment();
        prevoz = p;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_podrobnosti, container, false);
        return rootView;
    }

}
