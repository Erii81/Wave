package byr.wave;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link hisFra#newInstance} factory method to
 * create an instance of this fragment.
 */
public class hisFra extends Fragment {

    public hisFra() {
        // Required empty public constructor
    }
    public static hisFra newInstance(String param1, String param2) {
        hisFra fragment = new hisFra();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_his, container, false);
    }

}
