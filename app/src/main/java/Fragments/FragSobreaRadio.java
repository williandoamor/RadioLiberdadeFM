package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.loadti.radio.liberdadefm.R;

/**
 * Created by TI on 17/03/2016.
 */
public class FragSobreaRadio extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_sobre_a_radio, container, false);

        return v;
    }
}
