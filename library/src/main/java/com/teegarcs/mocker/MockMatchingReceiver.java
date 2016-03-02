package com.teegarcs.mocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by cteegarden on 3/1/16.
 */
public class MockMatchingReceiver extends BroadcastReceiver {
    public static final String DISABLE_MATCHING = "com.teegarcs.mocker.DISABLE_MATCHING";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(DISABLE_MATCHING.equalsIgnoreCase(intent.getAction())){
            MockerInitializer.turnOffMatching(context);
        }
    }
}
