package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appodeal.ads.Appodeal;

import br.com.loadti.radio.liberdadefm.R;

/**
 * Created by TI on 05/05/2017.
 */

public class FragAppodeal extends Fragment {

    private static final String APP_KEY = "5a7fc292edc0b885a7e14648742d80abf4be34f7f579e21c";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_appodeal, container, false);

        inicializarAppoldeal();

        return v;
    }

    /*Inicializa o Appoldeal*/
    private void inicializarAppoldeal() {

       // Appodeal.setBannerViewId(R.id.bannerView);
        //Appodeal.initialize(getActivity(), APP_KEY, Appodeal.BANNER_VIEW);
        //Appodeal.setTesting(true);
        //Appodeal.getBannerView(getActivity());
        //Appodeal.show(getActivity(), Appodeal.BANNER);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Appodeal.onResume(getActivity(), Appodeal.BANNER);
    }
}
