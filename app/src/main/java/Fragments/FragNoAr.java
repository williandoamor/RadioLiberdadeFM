package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appodeal.ads.Appodeal;

import Utils.AppodealBannerCallbacks;
import br.com.loadti.radio.liberdadefm.R;

/**
 * Created by TI on 17/03/2016.
 */
public class FragNoAr extends Fragment {

    /*Key do Appoldel*/
    private static final String APP_KEY = "5a7fc292edc0b885a7e14648742d80abf4be34f7f579e21c";
    private Toolbar barBottonAnuncio;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("No Ar");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_no_ar, container, false);

        return v;

    }


}
