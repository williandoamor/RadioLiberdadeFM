package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import br.com.loadti.radio.liberdadefm.R;

/**
 * Created by TI on 17/03/2016.
 */
public class FragContato extends Fragment {

    private Button btnContatoTelefone, btnContatoSite, btnContatoFacebook, btnContatoEmail;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_contato, container, false);

        btnContatoTelefone = (Button) v.findViewById(R.id.btnTelefone);
        btnContatoSite = (Button) v.findViewById(R.id.btnSite);
        btnContatoFacebook = (Button) v.findViewById(R.id.btnFacebook);
        btnContatoEmail = (Button) v.findViewById(R.id.btnEmail);

        chamarFacebook();

        chamaremail();

        chamarsite();

        chamarTelefone();


        return v;
    }

    private void chamarFacebook() {

        btnContatoFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.facebook.com/wanderleyvitorianocustodio.vitoriano?fref=ts"));

                    startActivity(intent);


                } catch (Exception e) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setCancelable(false);
                    alert.setTitle("ERRO!");
                    alert.setMessage("Erro ao abrir o facebook " + e.getMessage());
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog al = alert.create();
                    al.show();

                }


            }
        });


    }

    private void chamaremail() {


        btnContatoEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Contato via Aplicativo");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"liberdadefmtp@hotmail.com"});

                    startActivity(intent);


                } catch (Exception e) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setCancelable(false);
                    alert.setTitle("ERRO!");
                    alert.setMessage("Erro ao abrir email " + e.getMessage());
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog al = alert.create();
                    al.show();

                }
            }
        });

    }

    private void chamarsite() {


        btnContatoSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://www.liberdadefmtp.com.br/"));

                    startActivity(intent);


                } catch (Exception e) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setCancelable(false);
                    alert.setTitle("ERRO!");
                    alert.setMessage("Erro ao abrir site " + e.getMessage());
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog al = alert.create();
                    al.show();

                }

            }
        });

    }

    private void chamarTelefone() {

        btnContatoTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    AlertDialog.Builder alertfone = new AlertDialog.Builder(getActivity());
                    alertfone.setCancelable(false);
                    alertfone.setTitle("ATENÇÂO");
                    alertfone.setMessage("Chamada tarifada pela operadora. Deseja continuar?");
                    alertfone.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:32655161"));
                            startActivity(callIntent);
                        }
                    });

                    alertfone.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Operação cancelada pelo usuário", Toast.LENGTH_LONG).show();
                        }
                    });

                    AlertDialog alertDialog = alertfone.create();
                    alertDialog.show();

                } catch (Exception e) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setCancelable(false);
                    alert.setTitle("ERRO!");
                    alert.setMessage("Erro realizar ligação " + e.getMessage());
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog al = alert.create();
                    al.show();

                }

            }
        });


    }
}


