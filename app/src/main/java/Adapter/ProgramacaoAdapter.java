package Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Interface.RecyclerViewOnClickListenerHack;
import Serializable.ProgramacaoSerial;
import br.com.loadti.radio.liberdadefm.R;

/**
 * Created by TI on 22/03/2016.
 */
/*
     * ***********************************************************************
	 * Classe para marcar os itens da lista que já fora adicionados no pedidos
	 * ***********************************************************************
	 */

public class ProgramacaoAdapter extends RecyclerView.Adapter<ProgramacaoAdapter.MyViewHolder> {


    private LayoutInflater mLayoutInflater;
    private Context context;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private ArrayList<ProgramacaoSerial> lProg;


    public ProgramacaoAdapter(Context c, ArrayList<ProgramacaoSerial> prog) {

        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = c;
        lProg = prog;
    }

    @Override
    public int getItemCount() {
        return lProg.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.listar_programacao, viewGroup, false);

        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        Log.i("LOG", "onBindViewHolder()");

        myViewHolder.tvHorario.setText(lProg.get(position).getHorarios());
        myViewHolder.tvDescricaoProg.setText(lProg.get(position).getDescricaoPrograma());
        myViewHolder.tvDiaSemanaProg.setText(lProg.get(position).getDiaprograma());

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvHorario;
        public TextView tvDescricaoProg;
        public TextView tvDiaSemanaProg;
        //public TextView tvUnidadeProd;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvHorario = (TextView) itemView.findViewById(R.id.tv_horario);
            tvDescricaoProg = (TextView) itemView.findViewById(R.id.tv_model);
            tvDiaSemanaProg = (TextView) itemView.findViewById(R.id.tv_brand);
            //tvEstoqueProd = (TextView) itemView.findViewById(R.id.tvEstoqueProdPesqProd);
            //tvUnidadeProd = (TextView) itemView.findViewById(R.id.tvUnidadeProdPesqProd);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());

            }
        }
    }


}



