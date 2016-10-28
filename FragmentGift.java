package com.codeshastra.coderr.provideameal;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGift extends Fragment {

    private Button box8;
    private Button holachef;
    private Button foodpanda;

    public FragmentGift() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gift, container, false);
        box8 = (Button) view.findViewById(R.id.button_box8);
        holachef = (Button) view.findViewById(R.id.button_holachef);
        foodpanda = (Button) view.findViewById(R.id.button_foodpanda);
        return view;
    }
}
