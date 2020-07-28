package Fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import Adapter.ExpandivelAdapter;
import Serializable.ProgramacaoSerial;
import br.com.loadti.radio.liberdadefm.R;

/**
 * Created by TI on 18/03/2016.
 */
public class FragProgramacao extends Fragment {

    //private ArrayList<ProgramacaoSerial> aProg;
    private ExpandableListView expandableListView;
    private ArrayList<ProgramacaoSerial> aTaskProg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_programacao, container, false);

        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView1);

        expandableListView.setGroupIndicator(ContextCompat.getDrawable(getActivity(), R.drawable.action_expandable_list));


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        expandableListView.setAdapter(new ExpandivelAdapter(getActivity()));

    }
}
