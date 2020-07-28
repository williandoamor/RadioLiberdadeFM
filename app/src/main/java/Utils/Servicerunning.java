package Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by TI on 12/05/2017.
 */

public class Servicerunning {

    /*Verifica se o serviÃ§o do Media Player da rÃ¡dio liberdade estÃ¡ em execuÃ§Ã£o*/
    public static boolean isServiceRunning(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        String STRmanager = (String) manager.toString();
        Log.d("ContainerPrincipal", "STRmanager = " + STRmanager);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("br.com.loadti.radio.liberdadefm.radio.RadioPlayerService".equals(service.service.getClassName())) {
                Log.d("ContainerPrincipal", "Serviço liberdadeFM está em execução");
                return true;
            }
        }
        Log.d("Principal", "Serviço liberdadeFM não está em execução");
        return false;
    }
}
