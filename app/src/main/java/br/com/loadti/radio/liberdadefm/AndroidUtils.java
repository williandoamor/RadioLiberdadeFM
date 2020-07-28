package br.com.loadti.radio.liberdadefm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by TI on 15/09/2015.
 */
public class AndroidUtils {

    /*Criar notificacao para o servico do player*/
    public static final void criarNotificacao(Context ctx, int NOTIFICATION_ID, Class notificar) {

        String ns = Context.NOTIFICATION_SERVICE;
        Context context = ctx;
        Intent notificationIntent = new Intent(ctx, notificar);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nNotificationManager = (NotificationManager) context.getSystemService(ns);
        int iconSmall = R.mipmap.liberdade_notification;
        CharSequence tickertext = "Rádio Liberdade FM";
        long when = System.currentTimeMillis();
        CharSequence contentTitle = "LiberdadeFM - 87,9";
        CharSequence contenText = "Tocando só as melhores";
        Resources res = context.getResources();

        // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

        //     Notification notification = new Notification(iconSmall, tickertext, when);
        //    notification.flags = Notification.FLAG_ONGOING_EVENT;
        //     notification.
        //     notification.setLatestEventInfo(context, contentTitle, contenText, contentIntent);
        //     nNotificationManager.notify(NOTIFICATION_ID, notification);
        // } else {

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(contentTitle)
                .setContentText(contenText)
                .setTicker(tickertext)
                .setWhen(when)
                .setAutoCancel(true)
                .setSmallIcon(iconSmall)
                .setContentIntent(contentIntent)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.liberdade_notification));

        Notification n = notification.build();

        nNotificationManager.notify(NOTIFICATION_ID, n);


        //  }


    }

    /* Verificando se a conexao esta disponivel */
    public static boolean isNeworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {

            return false;

        } else {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {

                 return true;

            }

        }

        return false;
    }

}
