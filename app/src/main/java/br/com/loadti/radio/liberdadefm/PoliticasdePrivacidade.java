package br.com.loadti.radio.liberdadefm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * Created by TI on 07/05/2017.
 */

public class PoliticasdePrivacidade extends AppCompatActivity {

    private Toolbar barPolicies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.politicas_de_privacidade);

        inicializaToolbar();

        WebView wv = (WebView) findViewById(R.id.wv_polices);
        wv.loadUrl("file:///android_asset/politica_privacidade.html");
    }

    private void inicializaToolbar() {

        barPolicies = (Toolbar) this.findViewById(R.id.tb_policies);

        if (barPolicies != null) {

            this.setSupportActionBar(barPolicies);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(R.string.potilicas_de_privacidade);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
