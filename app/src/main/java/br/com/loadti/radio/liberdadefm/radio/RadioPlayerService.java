package br.com.loadti.radio.liberdadefm.radio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.flurry.sdk.is;
import com.spoledge.aacdecoder.MultiPlayer;
import com.spoledge.aacdecoder.PlayerCallback;

import java.util.ArrayList;
import java.util.List;

import DAO.RadioServiceDAO;
import Serializable.RadioServiceSerializable;
import br.com.loadti.radio.liberdadefm.ContainerPrincipal;
import br.com.loadti.radio.liberdadefm.R;


/**
 * Created by mertsimsek on 01/07/15.
 */
public class RadioPlayerService extends Service implements PlayerCallback {

    /**
     * Notification action intent strings
     */
    public static final String NOTIFICATION_INTENT_PLAY_PAUSE = "br.com.loadti.radio.liberdadefm.notification.radio.INTENT_PLAYPAUSE";

    public static final String NOTIFICATION_INTENT_CANCEL = "br.com.loadti.radio.liberdadefm.notification.radio.INTENT_CANCEL";


    /**
     * Notification current values
     */
    private String singerName = "";
    private String songName = "";
    private int smallImage = R.drawable.logo_liberdade;
    private Bitmap artImage;

    /**
     * Notification ID
     */
    private static final int NOTIFICATION_ID = 001;

    /**
     * Logging control variable
     */
    private static boolean isLogging = false;

    /**
     * Radio buffer and decode capacity(DEFAULT VALUES)
     */
    private final int AUDIO_BUFFER_CAPACITY_MS = 800;
    private final int AUDIO_DECODE_CAPACITY_MS = 400;

    /**
     * Stream url suffix
     */
    private final String SUFFIX_PLS = ".pls";
    private final String SUFFIX_RAM = ".ram";
    private final String SUFFIX_WAX = ".wax";

    /**
     * State enum for Radio Player state (IDLE, PLAYING, STOPPED, INTERRUPTED)
     */
    public enum State {
        IDLE,
        PLAYING,
        STOPPED,
        //BUFFERING,
    }

    List<RadioListener> mListenerList;

    /**
     * Radio State
     */
    private State mRadioState;

    /**
     * Current radio URL
     */
    private String mRadioUrl;

    /**
     * AAC Radio Player
     */
    private MultiPlayer mRadioPlayer;

    /**
     * Will be controlled on incoming calls and stop and start player.
     */
    private TelephonyManager mTelephonyManager;

    /**
     * While current radio playing, if you give another play command with different
     * source, you need to stop it first. This value is responsible for control
     * after radio stopped.
     */
    private boolean isSwitching;

    /**
     * If closed from notification, it will be checked
     * on Stop method and notification will not be created
     */
    private boolean isClosedFromNotification = false;

    /**
     * Incoming calls interrupt radio if it is playing.
     * Check if this is true or not after hang up;
     */
    private boolean isInterrupted;

    /**
     * If play method is called repeatedly, AAC Decoder will be failed.
     * play and stop methods will be turned mLock = true when they called,
     *
     * @onRadioStarted and @onRadioStopped methods will be release lock.
     */
    private boolean mLock;

    private Notification notification;

    /**
     * Notification manager
     */
    //private NotificationManager mNotificationManager;

    private ArrayList<RadioServiceSerializable> iRadio;
    private RadioServiceSerializable radio;


    /**
     * Binder
     */
    public final IBinder mLocalBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    /**
     * Binder
     */
    public class LocalBinder extends Binder {
        public RadioPlayerService getService() {
            return RadioPlayerService.this;
        }
    }

    /**
     * Service called
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        /**
         * If cancel clicked on notification, then set state to
         * IDLE, stop player and cancel notification
         */
        if (action.equals(NOTIFICATION_INTENT_CANCEL)) {
            if (isPlaying()) {
                isClosedFromNotification = true;
                stop();
            }

            /*Diz ao Android para remover a notificação de
            * primeiro planto*/
            stopForeground(true);
        }
        /**
         * If play/pause action clicked on notification,
         * Check player state and stop/play streaming.
         */
        else if (action.equals(NOTIFICATION_INTENT_PLAY_PAUSE)) {
            if (isPlaying())
                stop();
            else if (mRadioUrl != null)
                play(mRadioUrl);

        }

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /*Busca o status do servico*/
        getStatusRadio();
        mListenerList = new ArrayList<>();
        mRadioState = State.IDLE;
        isSwitching = false;
        isInterrupted = false;
        mLock = false;
        getPlayer();

        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (mTelephonyManager != null)
            mTelephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * Play url if different from previous streaming url.
     *
     * @param mRadioUrl
     */
    public void play(String mRadioUrl) {

        notifyRadioLoading();

        if (checkSuffix(mRadioUrl))
            decodeStremLink(mRadioUrl);
        else {
            this.mRadioUrl = mRadioUrl;
            isSwitching = false;

            if (isPlaying()) {
                log("Switching Radio");
                isSwitching = true;
                stop();

            } else if (!mLock) {
                log("Play requested.");
                mLock = true;
                getPlayer().playAsync(mRadioUrl);


            }
        }
    }

    public void stop() {
        if (!mLock && mRadioState != State.STOPPED) {
            log("Stop requested.");
            mLock = true;
            getPlayer().stop();
            /*Passa como N o status do servico
            * pois aqui ele para
            * */
            salvarStatusRadioService("N");

        }
    }

    @Override
    public void playerStarted() {

        mRadioState = State.PLAYING;
        buildNotification();
        mLock = false;
        notifyRadioStarted();
         /*Passa como S o status do servico
            * pois aqui ele inicia o servico
            * */
        salvarStatusRadioService("S");

        log("Player started. tate : " + mRadioState);

        if (isInterrupted)
            isInterrupted = false;

    }

    public boolean isPlaying() {
        if (mRadioPlayer != null) {

            if (State.PLAYING == mRadioState) {

                return true;
            }
        }

        return false;
    }

    public void resume() {
        if (mRadioUrl != null)
            play(mRadioUrl);
    }

    public void stopFromNotification() {
        isClosedFromNotification = true;
        stopForeground(true);
        stop();
    }

    @Override
    public void playerPCMFeedBuffer(boolean inPlayer, int bufSizeMs, int bufCapacityMs) {

        if (inPlayer) {

            mRadioState = State.PLAYING;
        }
        Log.d("RadioPlayerservice", "Buffer - inPlayer " + inPlayer);
        Log.d("RadioPlayerservice", "Buffer - bufSizeMs " + bufSizeMs);
        Log.d("RadioPlayerservice", "Buffer - bufCapacityMs " + bufCapacityMs);

    }

    @Override
    public void playerStopped(int i) {

        mRadioState = State.STOPPED;
         /*Passa como N o status do servico
            * pois aqui ele para o servico
            * */
        salvarStatusRadioService("N");
        /**
         * If player stopped from notification then dont
         * call buildNotification().
         */
        if (!isClosedFromNotification)
            buildNotification();
        else
            isClosedFromNotification = false;

        mLock = false;
        notifyRadioStopped();
        log("Player stopped. State : " + mRadioState);

        if (isSwitching)
            play(mRadioUrl);


    }

    @Override
    public void playerException(Throwable throwable) {
        mLock = false;
        mRadioPlayer = null;
         /*Passa como N o status do servico
            * pois aqui ele para o servico
            * */
        salvarStatusRadioService("N");
        getPlayer();
        notifyErrorOccured();
        log("ERROR OCCURED.");
    }

    @Override
    public void playerMetadata(String s, String s2) {
        notifyMetaDataChanged(s, s2);
    }

    @Override
    public void playerAudioTrackCreated(AudioTrack audioTrack) {

        Log.d("RadioPlayerService", "playerAudioTrackCreate" + audioTrack.toString());
    }

    public void registerListener(RadioListener mListener) {
        mListenerList.add(mListener);
    }

    public void unregisterListener(RadioListener mListener) {
        mListenerList.remove(mListener);
    }

    private void notifyRadioStarted() {
        for (RadioListener mRadioListener : mListenerList) {
            mRadioListener.onRadioStarted();
        }
    }

    private void notifyRadioStopped() {
        for (RadioListener mRadioListener : mListenerList)
            mRadioListener.onRadioStopped();
    }

    private void notifyMetaDataChanged(String s, String s2) {
        for (RadioListener mRadioListener : mListenerList)
            mRadioListener.onMetaDataReceived(s, s2);
    }

    private void notifyRadioLoading() {
        for (RadioListener mRadioListener : mListenerList) {
            mRadioListener.onRadioLoading();
        }
    }

    private void notifyErrorOccured() {
        for (RadioListener mRadioListener : mListenerList) {
            mRadioListener.onError();
        }
    }


    /**
     * Return AAC player. If it is not initialized, creates and returns.
     *
     * @return MultiPlayer
     */
    private MultiPlayer getPlayer() {
        try {

            java.net.URL.setURLStreamHandlerFactory(new java.net.URLStreamHandlerFactory() {

                public java.net.URLStreamHandler createURLStreamHandler(String protocol) {
                    Log.d("LOG", "Asking for stream handler for protocol: '" + protocol + "'");
                    if ("icy".equals(protocol))
                        return new com.spoledge.aacdecoder.IcyURLStreamHandler();
                    return null;
                }
            });
        } catch (Throwable t) {
            Log.w("LOG", "Cannot set the ICY URLStreamHandler - maybe already set ? - " + t);
        }

        if (mRadioPlayer == null) {
            mRadioPlayer = new MultiPlayer(this, AUDIO_BUFFER_CAPACITY_MS, AUDIO_DECODE_CAPACITY_MS);
            mRadioPlayer.setResponseCodeCheckEnabled(false);
            mRadioPlayer.setPlayerCallback(this);
        }
        return mRadioPlayer;
    }

    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {

                /**
                 * Stop radio and set interrupted if it is playing on incoming call.
                 */
                if (isPlaying()) {
                    isInterrupted = true;
                    stop();
                }

            } else if (state == TelephonyManager.CALL_STATE_IDLE) {

                /**
                 * Keep playing if it is interrupted.
                 */
                if (isInterrupted)
                    play(mRadioUrl);

            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {

                /**
                 * Stop radio and set interrupted if it is playing on outgoing call.
                 */
                if (isPlaying()) {
                    isInterrupted = true;
                    stop();
                }

            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };

    /**
     * Check supported suffix
     *
     * @param streamUrl
     * @return
     */
    public boolean checkSuffix(String streamUrl) {
        if (streamUrl.contains(SUFFIX_PLS) ||
                streamUrl.contains(SUFFIX_RAM) ||
                streamUrl.contains(SUFFIX_WAX))
            return true;
        else
            return false;
    }

    /**
     * Enable/Disable log
     *
     * @param logging
     */
    public void setLogging(boolean logging) {
        isLogging = logging;
    }

    /**
     * Logger
     *
     * @param log
     */
    private void log(String log) {
        if (isLogging)
            Log.v("RadioManager", "RadioPlayerService : " + log);
    }

    /**
     * If stream link is a file, then we
     * call stream decoder to get HTTP stream link
     * from that file.
     *
     * @param streamLink
     */
    private void decodeStremLink(String streamLink) {
        new StreamLinkDecoder(streamLink) {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                play(s);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * Build notification
     */
    private void buildNotification() {

        /**
         * Intents
         */
        Intent intentPlayPause = new Intent(NOTIFICATION_INTENT_PLAY_PAUSE);
        Intent intentOpenPlayer = new Intent(this, ContainerPrincipal.class);
        Intent intentCancel = new Intent(NOTIFICATION_INTENT_CANCEL);

        /**
         * Pending intents
         */
        PendingIntent playPausePending = PendingIntent.getBroadcast(this, 23, intentPlayPause, 0);
        PendingIntent openPending = PendingIntent.getActivity(this, 0, intentOpenPlayer, 0);
        PendingIntent cancelPending = PendingIntent.getBroadcast(this, 12, intentCancel, 0);

        /**
         * Remote view for normal view
         */
        RemoteViews mNotificationTemplate = new RemoteViews(this.getPackageName(), R.layout.notification);

        /**
         * set small notification texts and image
         */
        if (artImage == null)
            artImage = BitmapFactory.decodeResource(getResources(), R.drawable.liberdade_notification);

        mNotificationTemplate.setTextViewText(R.id.notification_line_one, singerName);
        mNotificationTemplate.setTextViewText(R.id.notification_line_two, songName);
        mNotificationTemplate.setImageViewResource(R.id.notification_play, isPlaying() ? R.drawable.btn_playback_pause : R.drawable.btn_playback_play);
        mNotificationTemplate.setImageViewBitmap(R.id.notification_image, artImage);

        /**
         * OnClickPending intent for collapsed notification
         */
        mNotificationTemplate.setOnClickPendingIntent(R.id.notification_collapse, cancelPending);
        mNotificationTemplate.setOnClickPendingIntent(R.id.notification_play, playPausePending);

        /**
         * Expanded notification*/
        RemoteViews mExpandedView = new RemoteViews(this.getPackageName(), R.layout.notification_expanded);

        mExpandedView.setTextViewText(R.id.notification_line_one, singerName);
        mExpandedView.setTextViewText(R.id.notification_line_two, songName);
        mExpandedView.setImageViewResource(R.id.notification_expanded_play, isPlaying() ? R.drawable.btn_playback_pause : R.drawable.btn_playback_play);
        mExpandedView.setImageViewBitmap(R.id.notification_image, artImage);

        mExpandedView.setOnClickPendingIntent(R.id.notification_collapse, cancelPending);
        mExpandedView.setOnClickPendingIntent(R.id.notification_expanded_play, playPausePending);

        /**
         * Create notification instance
         */
        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(smallImage)
                .setContentTitle("Liberdade FM - 87,9")
                .setContentText("Tocando só as melhores")
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.liberdade_notification))
                .setContentIntent(openPending)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContent(mNotificationTemplate)
                .setCustomBigContentView(mExpandedView)
                .setUsesChronometer(true)
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        /*Starta o service em segundo plano*/
        startForeground(NOTIFICATION_ID, notification);

    }

    public void updateNotification(String singerName, String songName, int smallImage, int artImage) {
        this.singerName = singerName;
        this.songName = songName;
        this.smallImage = smallImage;
        this.artImage = BitmapFactory.decodeResource(getResources(), artImage);
        buildNotification();
    }


    public void updateNotification(String singerName, String songName, int smallImage, Bitmap artImage) {
        this.singerName = singerName;
        this.songName = songName;
        this.smallImage = smallImage;
        this.artImage = artImage;
        buildNotification();
    }

    /*Atualiza o status do radio*/
    private void salvarStatusRadioService(String status) {

        try {

             /*Seta  o status da radio no objeto radio*/
            radio.setSitu_radioservice(status);

            RadioServiceDAO dao = new RadioServiceDAO(this);
            /*Salva o objeto no banco*/
            dao.updateRadioService(radio);

        } catch (Exception e) {

            Log.e("RadioPlayerService", "Erro ao salvar o status do Servico " + e.getMessage());
        }

    }

    /*Le o status da radio*/
    private void getStatusRadio() {

        try {

            iRadio = new ArrayList<>();
            radio = new RadioServiceSerializable();

            iRadio = new RadioServiceDAO(this).getRadio();
            for (RadioServiceSerializable mRadio : iRadio) {

                radio.setId_radioservice(mRadio.getId_radioservice());
                radio.setSitu_radioservice(mRadio.getSitu_radioservice());

            }

        } catch (Exception e) {

            Log.e("RadioPlayerService", "getStatusRadio - Erro ao buscar status radio " + e.getMessage());
        }

    }

}
