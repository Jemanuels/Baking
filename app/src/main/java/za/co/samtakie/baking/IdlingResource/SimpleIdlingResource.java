package za.co.samtakie.baking.IdlingResource;


import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jemanuels on 2018/02/18.
 * Last updated 2018/02/19
 */

@SuppressWarnings("ALL")
public class SimpleIdlingResource implements IdlingResource {

    @Nullable private volatile ResourceCallback mCallback;

    // Idleness is controlled with this boolean
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }
}
