package br.com.loadti.radio.liberdadefm;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.utils.PermissionsHelper;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import Fragments.FragContato;
import Fragments.FragNoAr;
import Fragments.FragProgramacao;
import Fragments.FragSobre;
import Fragments.FragSobreaRadio;
import br.com.loadti.radio.liberdadefm.broadcast.PlayerControllerBroadcast;
import br.com.loadti.radio.liberdadefm.radio.ConnectionService;
import Utils.Servicerunning;
import br.com.loadti.radio.liberdadefm.radio.RadioListener;
import br.com.loadti.radio.liberdadefm.radio.RadioManager;
import Utils.DB;
import io.fabric.sdk.android.Fabric;


/**
 * Created by TI on 18/03/2016.
 */

public class ContainerPrincipal extends AppCompatActivity implements RadioListener, NavigationView.OnNavigationItemSelectedListener, ConnectionService.ConnectionServiceCallback {

    private TextView nomeMusica;

    private ImageView imgPlayer, imgVolumeDown, imgVolumeUp, imgStop;

    private final String[] RADIO_URL = {"http://stm4.srvstm.com:16194"};
    private DB bd;
    private SQLiteDatabase dbase;

    private ShareActionProvider mShareActionProvider;
    RadioManager mRadioManager;

    private String musicaTocando = "";

    private Toolbar barRadio;

    private static final String TAG = "ContainerPrincipal";

    private static final String APP_KEY = "5a7fc292edc0b885a7e14648742d80abf4be34f7f579e21c";

    private boolean isStopped = false;

    public static final int REQUEST_PERMISSIONS_CODE = 128;

    private com.google.android.gms.ads.AdView mAdView;

    private boolean disconnected = false;

    private boolean mBroadcastReceiverregister = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_principal_drawable);
        bancoDados();

        mRadioManager = RadioManager.with(getApplicationContext());
        mRadioManager.registerListener(this);
        mRadioManager.setLogging(true);

        /*Inicializa os componentes da interface*/
        inicializaComponentes();

        /*Acao para o botao player*/
        actionPlayer();

         /*Action stop Player*/
        actionStopPlayer();

        ativarTabs();

        /*Ativa a navegacao por Drawable*/
        drawerNavegator();

        /*Checa as permissoes do Android*/
        checkPermission();

        /*Inicializa o primeiro fragment para o usuario interagir*/
        inicialiarFragmentNoAr();

        /*Aumentar o som do dispositivo*/
        audioUp();

        actionVolumeDown();

        inicializarAppoldeal();

        /*Inicializa anuncios do Adview*/
        inicializarAdview();

        Log.i("ContainerPrincipal", " onCreate");


    }

    /*Inicializa anuncios do Adview*/
    private void inicializarAdview() {

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_anuncio);
        mAdView = new AdView(ContainerPrincipal.this);
        mAdView.setAdUnitId("ca-app-pub-5753242661952730/9033620401");
        frameLayout.addView(mAdView);
        mAdView.setAdSize(AdSize.SMART_BANNER);

        mAdView.loadAd(new AdRequest.Builder().build());


    }

    /*Inicializa o Appoldeal*/
    private void inicializarAppoldeal() {

        Appodeal.initialize(ContainerPrincipal.this, APP_KEY, Appodeal.INTERSTITIAL);
    }

    /*Aumenta o volume de Streaming do dispositivo*/
    private void audioUp() {

        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        imgVolumeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*Informa ao Android para aumentar somente o volume de musica e exiba o icone na tela*/
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

            }
        });

    }

    /*Diminui o volume de Streaming do dispositivo*/
    private void actionVolumeDown() {
        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        imgVolumeDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Informa ao Android para diminuir somente o volume de musica e exiba o icone na tela*/
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }
        });


    }

    /*Checa as permissoes para o Appoldel gravar no SDCARD*/
    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= 23 && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Appodeal.requestAndroidMPermissions(this, new PermissionsHelper.AppodealPermissionCallbacks() {
                @Override
                public void writeExternalStorageResponse(int result) {

                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Utils.showToast(mActivity, "WRITE_EXTERNAL_STORAGE permission was granted");
                        /*Seta a variavel isstopped para true para que o aplicativo
                        * nao reproduca o som quando cair no onResume
                        * */
                        isStopped = true;
                    } else {
                        /*Se o usuario nao tiver dado a permissao para o aplicativo chama a tela para
                        * mostrar os motivos
                        * */
                        Utils.AndroidUtils.PermissionDialog("O Aplicativo Liberdade FM precisa da permissão WRITE_EXTERNAL_STORAGE para gravar o arquivo de áudio no seu dispositivo.", new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                ContainerPrincipal.this, REQUEST_PERMISSIONS_CODE);
                        //Utils.showToast(mActivity, "WRITE_EXTERNAL_STORAGE permission was NOT granted");
                         /*Seta a variavel isstopped para true para que o aplicativo
                        * nao reproduca o som quando cair no onResume
                        * */
                        isStopped = true;
                    }

                }

                @Override
                public void accessCoarseLocationResponse(int result) {

                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Utils.showToast(mActivity, "ACCESS_COARSE_LOCATION permission was granted");
                           /*Seta a variavel isstopped para true para que o aplicativo
                        * nao reproduca o som quando cair no onResume
                        * */
                        isStopped = true;
                    } else {
                        Utils.AndroidUtils.PermissionDialog("O aplicativo Liberdade FM precisa da permissão ACCESS_COARSE_LOCATION para determinar o melhor servidor de áudio na proximidade.", new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                ContainerPrincipal.this, REQUEST_PERMISSIONS_CODE);
                           /*Seta a variavel isstopped para true para que o aplicativo
                        * nao reproduca o som quando cair no onResume
                        * */
                        isStopped = true;
                    }

                }
            });
        }

    }

    /*Cria o banco de dados*/
    public void bancoDados() {

        try {

            bd = new DB(this);
            dbase = bd.getWritableDatabase();

        } catch (Exception e) {

            Toast.makeText(ContainerPrincipal.this, "Erro ao criar banco de dados " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ContainerPricipal ", " Erro ao criar banco de dados " + e.getMessage());
        }

        if (dbase != null) {

            dbase.close();

        }
        bd.close();

    }


    @Override
    protected void onResume() {
        super.onResume();


        /*Se houver conexao com a internet executa a verificacao do servico*/
        if (AndroidUtils.isNeworkAvailable(ContainerPrincipal.this)) {

               /*Valida se o serviço esta conectado*/
            if (Servicerunning.isServiceRunning(ContainerPrincipal.this)) {

                    /*Verifica se o player esta tocando*/
                if (!mRadioManager.isPlaying()) {

                    Log.i("ContainerPrincipal", "Serviço em execucacao");
                    findViewById(R.id.iv_player).setBackgroundResource(R.drawable.action_player);
                    mRadioManager.disconnect();
                    nomeMusica.setText("Parado");

                } else {


                    Log.i("ContainerPrincipal", "Serviço em execucacao");
                    findViewById(R.id.iv_player).setBackgroundResource(R.drawable.action_pause);
                    nomeMusica.setText("Reproduzindo Áudio...");
                }


            } else {

                /*Se o usuario nao tiver dado stop no servico
                * ele vai rodar caso contrario isStopped vai ser false
                * e o som comexar
                * */
                if (!isStopped) {

                    findViewById(R.id.iv_player).setBackgroundResource(R.drawable.action_player);
                    inicializarSom();
                } else {

                    nomeMusica.setText("Parado.");
                    //isStopped = false;

                }/*Fim o else que verifica se isStopped e true*/


            }

          /*Se não tiver conexao com a internet avisa ao usuario*/
        } else {

            /*Se o servico estiver em execucao verifica se o usuario deu stop*/
            if (Servicerunning.isServiceRunning(ContainerPrincipal.this)) {

                if (!isStopped) {
                    /*Se o usuario nao deu stop informa que esta tentando reconectar*/
                    nomeMusica.setText("Reconectando: Aguardando sinal de internet...");
                    findViewById(R.id.iv_player).setBackgroundResource(R.drawable.action_pause);
                    mRadioManager.updateNotification("Liberdade FM 87,9", "Reconectando: Aguardando sinal de internet...", R.drawable.volume_high, BitmapFactory.decodeResource(getResources(), R.drawable.liberdade_notification));
                }

            } else {

                /*Se o servico nao estiver em execucao informa que nao ha internet*/
                findViewById(R.id.iv_player).setBackgroundResource(R.drawable.action_player);
                nomeMusica.setText("Sem conexão com a internet...");

            }

        }

        /*Ativa adview*/
        if (mAdView != null) {

            mAdView.resume();
        }

    }


    @Override
    protected void onDestroy() {

        if (mAdView != null) {
            mAdView.destroy();
        }

        super.onDestroy();

        Log.d("ContainerPrincipal", "OnDestroy");

    }

    /*Captura o clica na tecla Home do dispositivo*/
    @Override
    protected void onUserLeaveHint() {

        Log.i("ContainerPrincipal", "Usuario precionou Home");
        super.onUserLeaveHint();
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();
        }


    }

    @Override
    public void onRadioLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                nomeMusica.setText("Armazenando Áudio...");
            }
        });
    }

    @Override
    public void onRadioConnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Log.i("ContainerPrincipal", " onRadioConnected");
                nomeMusica.setText("Conectando ao servidor de áudio...");
                mRadioManager.updateNotification("Liberdade FM 87,9", "Carregando áudio...", R.drawable.volume_high, BitmapFactory.decodeResource(getResources(), R.drawable.liberdade_notification));
            }
        });


    }

    @Override
    public void onRadioStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                nomeMusica.setText("Reproduzindo Áudio...");
                findViewById(R.id.iv_player).setBackgroundResource(R.drawable.action_pause);

                if (!isStopped) {

                    if (!mBroadcastReceiverregister) {

                        registerReceiver(receiver, new IntentFilter(PlayerControllerBroadcast.BROADCAST_STATUS_PLAYER));
                        mBroadcastReceiverregister = true;
                    }
                }
            }
        });
    }

    @Override
    public void onRadioStopped() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (isStopped) {

                    nomeMusica.setText("Parado");
                    findViewById(R.id.iv_player).setBackgroundResource(R.drawable.action_player);
                    mRadioManager.updateNotification("Liberdade FM 87,9", "Parado.", R.drawable.volume_high, BitmapFactory.decodeResource(getResources(), R.drawable.liberdade_notification));

                    if (!mBroadcastReceiverregister) {

                        try {

                            unregisterReceiver(receiver);
                            mBroadcastReceiverregister = false;

                        } catch (Exception e) {

                            Log.e("ContainerPrincipal", "onRadioStopped " + e.getMessage());
                        }
                    }

                } else {


                    nomeMusica.setText("Reconectando: Aguardando sinal de internet...");
                    findViewById(R.id.iv_player).setBackgroundResource(R.drawable.action_pause);
                    mRadioManager.updateNotification("Liberdade FM 87,9", "Reconectando: Aguardando sinal de internet...", R.drawable.volume_high, BitmapFactory.decodeResource(getResources(), R.drawable.liberdade_notification));


                }

            }
        });
    }

    @Override
    public void onMetaDataReceived(final String s, final String s2) {
        //Aqui deve ser verifica os metadados como nome do cantor
        //Nome da musica e o que mais o servidor disponibilizar
        //nomeMusica.setText(s2);
        // Log.i("MetaDataReceiver", "Nome da musica: " + s2);
        //Log.i("MetaDataReceiver", "Nome do cantor: " + s);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    /*Verifica se a string s e igual a StreamTitle*/
                    if ("StreamTitle".equalsIgnoreCase(s)) {

                        /*Se for verifica se s2 e igual a Hi-Fi Internet Stream*/
                        if ("Hi-Fi Internet Stream".equalsIgnoreCase(s2)) {

                            /*Se s2 tiver Hi-Fi Internet Stream no conteudo
                            * Seta o nome da musica como reproduzindo e passa
                            * Reproduzindo para a notificacao
                            * */
                            nomeMusica.setText("Reproduzindo Áudio...");
                            mRadioManager.updateNotification("Liberdade FM 87,9", "Reproduzindo Áudio...", R.drawable.volume_high, BitmapFactory.decodeResource(getResources(), R.drawable.liberdade_notification));

                        } else {

                            musicaTocando = s2;
                            nomeMusica.setText(musicaTocando);
                            mRadioManager.updateNotification("Liberdade FM 87,9", s2, R.drawable.volume_high, BitmapFactory.decodeResource(getResources(), R.drawable.liberdade_notification));

                        }

                    } else {

                        nomeMusica.setText("Liberdade FM 87,9");
                    }


                } catch (Exception e) {

                    Log.e("ContainerPrincipal", "Erro ao setar nome da musica" + e.getMessage());
                }


            }


        });


    }


    @Override
    public void onError() {

        Log.e("ContainerPrincipal", "Erro ao executar Rádio");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (disconnected) {

                    nomeMusica.setText("Erro ao conectar a rádio ou a internet.");
                    mRadioManager.updateNotification("Liberdade FM 87,9", "Erro ao conectar a rádio ou a internet.", R.drawable.volume_high, BitmapFactory.decodeResource(getResources(), R.drawable.liberdade_notification));
                    Toast.makeText(ContainerPrincipal.this, "Verifique sua conexão com a internet!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void inicializaComponentes() {

        nomeMusica = (TextView) findViewById(R.id.tv_nome_musica);
        imgPlayer = (ImageView) findViewById(R.id.iv_player);
        imgStop = (ImageView) findViewById(R.id.iv_stop);
        imgVolumeDown = (ImageView) findViewById(R.id.iv_volume_down);
        imgVolumeUp = (ImageView) findViewById(R.id.iv_volume_up);
    }


    /*Acao ao clicar no botao Player/pause*/
    private void actionPlayer() {

        imgPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Verifica se o usuario esta conectado a internet
                * se tiver conecta o audio
                * */
                if (AndroidUtils.isNeworkAvailable(ContainerPrincipal.this)) {
                    /*Se tiver internet e pingando no site do google
                    * inicia o som*/
                    inicializarSom();


                } else {
                    /*Se nao tiver conectado exibe um aviso*/
                    Toast.makeText(ContainerPrincipal.this, "Sem conexão a internet.", Toast.LENGTH_SHORT).show();
                    if (Servicerunning.isServiceRunning(ContainerPrincipal.this)) {

                        playerStop();
                    }
                }


            }
        });


    }

    @Override
    protected void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }

        super.onPause();

    }

    /*Inicializar o som*/
    private void inicializarSom() {

        if (Servicerunning.isServiceRunning(ContainerPrincipal.this)) {

            mRadioManager.stopRadio();
            imgPlayer.setBackgroundResource(R.drawable.btn_action_player);
            mRadioManager.disconnect();
            Log.i(TAG, "actionPlayer Radio Parado");
            stopService(new Intent(ContainerPrincipal.this, ConnectionService.class));
            isStopped = true;

        } else {

            if (!mBroadcastReceiverregister) {

                registerReceiver(receiver, new IntentFilter(PlayerControllerBroadcast.BROADCAST_STATUS_PLAYER));
                mBroadcastReceiverregister = true;

            }


            isStopped = false;

            checkConnection();

            mRadioManager.connect();

            imgPlayer.setBackgroundResource(R.drawable.action_pause);

            /*Valida versao do Android*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mRadioManager.startRadio(RADIO_URL[0]);
                        Log.i(TAG, "actionPlayer Radio Inicializado");
                        nomeMusica.setText("Reproduzindo Áudio...");


                    }
                }, 3000);

            } else {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        mRadioManager.startRadio(RADIO_URL[0]);
                        Log.i(TAG, "actionPlayer Radio Inicializado");
                        nomeMusica.setText("Reproduzindo Áudio...");

                    }
                }, 2000);

            }


        }


    }

    private void playerStop() {

                 /*If que verifica se o radio esta em execucao*/
        if (Servicerunning.isServiceRunning(ContainerPrincipal.this)) {

            mRadioManager.stopRadio();
            imgPlayer.setBackgroundResource(R.drawable.action_player);
            nomeMusica.setText("Parado");
            mRadioManager.disconnect();


            stopService(new Intent(ContainerPrincipal.this, ConnectionService.class));

                    /*Chama o Appoldeal*/
            appodealInstitutial();

                    /*Seta isStop para true para que quando a tela voltar para onResume
                    * o sistema nao inicie o som
                    * */
            isStopped = true;

            try {

                if (mBroadcastReceiverregister) {

                    unregisterReceiver(receiver);
                    mBroadcastReceiverregister = false;
                }

            } catch (Exception e) {

                Log.e("ContainerPrincipal", "actionStopPlayer " + e.getMessage());
            }

        }/*Fim do if que verifica se o serviço esta em execucao*/

    }

    /*Para o media player*/
    private void actionStopPlayer() {

        imgStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playerStop();

            }
        });

    }


    /*Metodo usado para ativar a navegacao por Tabbs*/

    private void ativarTabs() {

        barRadio = (Toolbar) this.findViewById(R.id.tb_main);
        if (barRadio != null) {
            this.setSupportActionBar(barRadio);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    /*Habilita o Navegator Drawer*/
    private void drawerNavegator() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (barRadio != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ContainerPrincipal.this, drawer, barRadio, R.string.navigation_drawer_open, R.string.material_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

        }


    }

    /*Inicializa o primeiro fragment para o usuario interagir*/
    private void inicialiarFragmentNoAr() {

        FragNoAr fragNoAr = new FragNoAr();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_contedudo, fragNoAr);
        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_principal, menu);
        MenuItem item = menu.findItem(R.id.menu_compartilhar);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(createShareIntent());

        return true;

    }

    private void setShareIntent(Intent shareIntent) {

        if (mShareActionProvider != null) {

            mShareActionProvider.setShareIntent(shareIntent);

        }

    }

    /*Navega no menu Drawable*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_no_ar:
                setTitle("No Ar");
                fragment = new FragNoAr();
                break;
            case R.id.nav_a_radio:
                setTitle("Sobre a Rádio");
                fragment = new FragSobreaRadio();
                break;
            case R.id.nav_programacao:
                setTitle("Nossa Programação");
                fragment = new FragProgramacao();
                break;
            case R.id.nav_contato:
                setTitle("Contato");
                fragment = new FragContato();
                break;
            case R.id.nav_sobre:
                setTitle("Sobre o Aplicativo");
                fragment = new FragSobre();
                break;
            case R.id.nav_policies:
                startActivity(new Intent(ContainerPrincipal.this, PoliticasdePrivacidade.class));
                break;
            default:

        }

        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_contedudo, fragment);
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private Intent createShareIntent() {

        Intent shsreIntent = new Intent(Intent.ACTION_SEND);
        shsreIntent.setType("text/plain");
        shsreIntent.putExtra(Intent.EXTRA_TEXT, "Rádio Liberdade FM 87,9 para Android.");
        shsreIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=br.com.loadti.radio.liberdadefm");

        return shsreIntent;
    }

    private void appodealInstitutial() {

        Appodeal.show(ContainerPrincipal.this, Appodeal.INTERSTITIAL);
        Appodeal.setTesting(true);
        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean b) {

            }

            @Override
            public void onInterstitialFailedToLoad() {

            }

            @Override
            public void onInterstitialShown() {

            }

            @Override
            public void onInterstitialClicked() {

                actionStopPlayer();
            }

            @Override
            public void onInterstitialClosed() {


            }
        });

    }

    private void checkConnection() {

        Intent intent = new Intent(this, ConnectionService.class);
        // Interval in seconds
        intent.putExtra(ConnectionService.TAG_INTERVAL, 1);
        // URL to ping
        intent.putExtra(ConnectionService.TAG_URL_PING, "http://www.google.com");
        // Name of the class that is calling this service
        intent.putExtra(ConnectionService.TAG_ACTIVITY_NAME, this.getClass().getName());
        // Starts the service
        startService(intent);
    }

    @Override
    public void hasInternetConnection() {


        if (!isStopped && disconnected) {

            RadioManager.getService().resume();
            disconnected = false;
        }

        Log.d("ContainerPrincipal", "hasInternetConnection isStopped depois " + isStopped);
    }

    @Override
    public void hasNoInternetConnection() {

        Log.d("ContainerPrincipal", "hasNoInternetConnection disconnected " + disconnected);
        disconnected = true;

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            isStopped = intent.getBooleanExtra("parado", false);

            if (isStopped) {

                stopService(new Intent(ContainerPrincipal.this, ConnectionService.class));

            } else {

                checkConnection();

            }

            Log.d("ContainerPrincipal", "BroadcastReceiver  - isStopped " + isStopped);

        }
    };

}

