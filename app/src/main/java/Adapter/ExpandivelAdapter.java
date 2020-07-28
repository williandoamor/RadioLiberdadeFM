package Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import br.com.loadti.radio.liberdadefm.R;

/**
 * Created by TI on 29/03/2016.
 */
public class ExpandivelAdapter extends BaseExpandableListAdapter {

    Context context;

// Definindo o conteúdo de nossa lista e sublista

    String[] listaPai = {"Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"};
    String[][] listafilho = {{"05:00 ás 24:00   - Programação Especial de Domingo"},
            {"05:00 ás 08:00 – Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano.", "08:00 às 08:30 – Jornal da Manhã – Noticiário, Nacional, Regional e Local.", "08:30 ás 09:20 – Continuação do Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano.", "09:20 às 10:00 - Programa Evangélico Heróis da Fé.", "10:00 ás 11:30 – Show de Sucessos – Sucessos Nacionais, Internacionais, Temas de Novelas, Pagode, Axé, Sertanejo Universitário.", "11:30 ás 12:00 - Clube do Rei.", "12:00 ás 12:30 - Programa Evangélico No Compasso da Graça."
                    + "12:30 às 13:00 – Música Popular Brasileira.", "13:00 às 17:00 - Love Time(Flash Back) com Mauro Vitullo.", "17:00 às 19:00 – Brasil Sertanejo – Os maiores Clássicos da Música Sertaneja com Wanderley Vitoriano.", "19:00 às 20:00 - A Voz do Brasil.", "20:00 às 24:00 – Sertanejo - Os maiores Clássicos da Música Sertaneja."},
            {"05:00 ás 08:00 – Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano.", "08:00 às 08:30 – Jornal da Manhã – Noticiário, Nacional, Regional e Local.", "08:30 ás 09:20 – Continuação do Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano.", "09:20 às 10:00 - Programa Evangélico Heróis da Fé.", "10:00 ás 11:30 – Show de Sucessos – Sucessos Nacionais, Internacionais, Temas de Novelas, Pagode, Axé, Sertanejo Universitário.", "11:30 ás 12:00 - Clube do Rei.", "12:00 ás 12:30 - Programa Evangélico No Compasso da Graça."
                    + "12:30 às 13:00 – Música Popular Brasileira.", "13:00 às 17:00 - Love Time(Flash Back) com Mauro Vitullo.", "17:00 às 19:00 – Brasil Sertanejo – Os maiores Clássicos da Música Sertaneja com Wanderley Vitoriano.", "19:00 às 20:00 - A Voz do Brasil.", "20:00 às 24:00 – Sertanejo - Os maiores Clássicos da Música Sertaneja"},
            {"05:00 ás 08:00 – Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano.", "08:00 às 08:30 – Jornal da Manhã – Noticiário, Nacional, Regional e Local", "08:30 ás 09:20 – Continuação do Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano.", "09:20 às 10:00 - Programa Evangélico Heróis da Fé.", "10:00 ás 11:30 – Show de Sucessos – Sucessos Nacionais, Internacionais, Temas de Novelas, Pagode, Axé, Sertanejo Universitário.", "11:30 ás 12:00 - Clube do Rei.", "12:00 ás 12:30 - Programa Evangélico No Compasso da Graça"
                    + "12:30 às 13:00 – Música Popular Brasileira", "13:00 às 17:00 - Love Time(Flash Back) com Mauro Vitullo.", "17:00 às 19:00 – Brasil Sertanejo – Os maiores Clássicos da Música Sertaneja com Wanderley Vitoriano", "19:00 às 20:00 - A Voz do Brasil.", "20:00 às 24:00 – Sertanejo - Os maiores Clássicos da Música Sertaneja."},
            {"05:00 ás 08:00 – Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano.", "08:00 às 08:30 – Jornal da Manhã – Noticiário, Nacional, Regional e Local", "08:30 ás 09:20 – Continuação do Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano.", "09:20 às 10:00 - Programa Evangélico Heróis da Fé.", "10:00 ás 11:30 – Show de Sucessos – Sucessos Nacionais, Internacionais, Temas de Novelas, Pagode, Axé, Sertanejo Universitário.", "11:30 ás 12:00 - Clube do Rei.", "12:00 ás 12:30 - Programa Evangélico No Compasso da Graça"
                    + "12:30 às 13:00 – Música Popular Brasileira", "13:00 às 17:00 - Love Time(Flash Back) com Mauro Vitullo.", "17:00 às 19:00 – Brasil Sertanejo – Os maiores Clássicos da Música Sertaneja com Wanderley Vitoriano", "19:00 às 20:00 - A Voz do Brasil.", "20:00 às 24:00 – Sertanejo - Os maiores Clássicos da Música Sertaneja"},
            {"05:00 ás 08:00 – Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano.", "08:00 às 08:30 – Jornal da Manhã – Noticiário, Nacional, Regional e Local.", "08:30 ás 09:20 – Continuação do Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano.", "09:20 às 10:00 - Programa Evangélico Heróis da Fé.", "10:00 ás 11:30 – Show de Sucessos – Sucessos Nacionais, Internacionais, Temas de Novelas, Pagode, Axé, Sertanejo Universitário.", "11:30 ás 12:00 - Clube do Rei.", "12:00 ás 12:30 - Programa Evangélico No Compasso da Graça"
                    + "12:30 às 13:00 – Música Popular Brasileira", "13:00 às 17:00 - Love Time(Flash Back) com Mauro Vitullo.", "17:00 às 19:00 – Brasil Sertanejo – Os maiores Clássicos da Música Sertaneja com Wanderley Vitoriano.", "19:00 às 20:00 - A Voz do Brasil ", "20:00 às 24:00 – Sertanejo - Os maiores Clássicos da Música Sertaneja."},
            {"05:00 ás 08:00 – Programa Sertanejo – Os maiores clássicos da Música Sertaneja com Wanderley Vitoriano", "08:00 às 10:00 – Samba & Pagode", "10:00 às 12:00 - Programa Luz no Caminho",
                    "12:00 ás 12:30 - Programa Evangélico No Compasso da Graça", "12:30 às 13:00 – Música Popular Brasileira", "13:00 às 17:00 - Love Time(Flash Back) com Mauro Vitullo",
                    "17:00 às 19:00 – Brasil Sertanejo – Os maiores Clássicos da Música Sertaneja com Wanderley Vitoriano", "19:00 às 24:00 – Sertanejo - Os maiores Clássicos da Música Sertaneja"}};

    public ExpandivelAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
// TODO Auto-generated method stub
        return listafilho[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
// TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

// Criamos um TextView que conterá as informações da listafilho que
// criamos
        TextView textViewSubLista = new TextView(context);
        textViewSubLista.setText(listafilho[groupPosition][childPosition]);
// Definimos um alinhamento
        textViewSubLista.setPadding(10, 5, 0, 5);

        ///textViewSubLista.setTextColor(ContextCompat.getColor(context, R.color.branco));
        textViewSubLista.setTextSize(16);

        //textViewSubLista.setTypeface(null, Typeface.BOLD);

        return textViewSubLista;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
// TODO Auto-generated method stub
        return listafilho[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
// TODO Auto-generated method stub
        return listaPai[groupPosition];
    }

    @Override
    public int getGroupCount() {
// TODO Auto-generated method stub
        return listaPai.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
// TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

// Criamos um TextView que conterá as informações da listaPai que
// criamos
        TextView textViewCategorias = new TextView(context);
        textViewCategorias.setText(listaPai[groupPosition]);
// Definimos um alinhamento
        textViewCategorias.setPadding(70, 5, 0, 5);
// Definimos o tamanho do texto
        textViewCategorias.setTextSize(20);
// Definimos que o texto estará em negrito
        textViewCategorias.setTypeface(null, Typeface.BOLD);
        //textViewCategorias.setTextColor(ContextCompat.getColor(context, R.color.branco));

        return textViewCategorias;
    }

    @Override
    public boolean hasStableIds() {
// TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
// Defina o return como sendo true se vc desejar que sua sublista seja selecionável
        return false;
    }
}
