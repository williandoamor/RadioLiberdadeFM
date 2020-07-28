package br.com.loadti.radio.liberdadefm.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import Utils.Servicerunning;
import br.com.loadti.radio.liberdadefm.ContainerPrincipal;
import br.com.loadti.radio.liberdadefm.radio.RadioManager;
import br.com.loadti.radio.liberdadefm.radio.RadioPlayerService;

/**
 * Created by mertsimsek on 04/11/15.
 */
public class PlayerControllerBroadcast extends BroadcastReceiver {

    public boolean stopPlayer = false;
    public static final String BROADCAST_STATUS_PLAYER = "br.com.loadti.radio.liberdadefm.PlayerControllerBroadcast.stop";
    private Intent intentStatusPlayer;
    private Context ctx;
    /**
     * This receiver receives STOP controlling between player services
     * For instances, If MediaPlayerService is running and playing stream
     * and then RadioPlayerService requested play radio stream, Service sends broadcast
     * that stop MediaPlayerService. And If RadioPlayerService is running and
     * playing radio stream, RadioPlayerService will be stopped here when MediaPlayerService
     * requested play media. This is the way we communicate between services to stop
     * each other.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isRadioServiceBinded = RadioManager.getService() == null ? false : true;
        String action = intent.getAction();
        ctx = context;

        if (action.equals(RadioPlayerService.NOTIFICATION_INTENT_PLAY_PAUSE)
                && isRadioServiceBinded) {

            if (RadioManager.getService().isPlaying()) {

                stopPlayer = true;
                sendBroadCastStatusPlayer();
                RadioManager.getService().stop();


            } else {

                stopPlayer = false;
                sendBroadCastStatusPlayer();
                RadioManager.getService().resume();

            }
        } else if (action.equals(RadioPlayerService.NOTIFICATION_INTENT_CANCEL)
                && isRadioServiceBinded) {

            stopPlayer = true;
            sendBroadCastStatusPlayer();
            RadioManager.getService().stopFromNotification();

        }

    }

    private void sendBroadCastStatusPlayer() {

        intentStatusPlayer = new Intent(BROADCAST_STATUS_PLAYER).putExtra("parado", stopPlayer);
        ctx.sendBroadcast(intentStatusPlayer);

    }

}
