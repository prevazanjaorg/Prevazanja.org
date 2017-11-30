package nameplaceholder.prevazanjaorg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class LandingFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public LandingFragment() {
        // Required empty public constructor
    }

    public static LandingFragment newInstance(int sectionNumber) {
        LandingFragment fragment = new LandingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_landing, container, false);
        return rootView;
    }

}
