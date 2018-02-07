package za.co.samtakie.baking.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by jemanuels on 2018/01/22.
 * Last updated 2018\02\02
 */

public class BakingSyncIntentService extends IntentService{

    public BakingSyncIntentService() {
        super("BakingSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        BakingSyncTask.syncBaking(this);
    }
}
