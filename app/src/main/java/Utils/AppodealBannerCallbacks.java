package Utils;

import android.app.Activity;

import com.appodeal.ads.BannerCallbacks;

/**
 * Created by TI on 04/05/2017.
 */

public class AppodealBannerCallbacks implements BannerCallbacks {

    private final Activity mActivity;

    public AppodealBannerCallbacks(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onBannerLoaded(int i, boolean b) {

    }

    @Override
    public void onBannerFailedToLoad() {

    }

    @Override
    public void onBannerShown() {

    }

    @Override
    public void onBannerClicked() {

    }
}
