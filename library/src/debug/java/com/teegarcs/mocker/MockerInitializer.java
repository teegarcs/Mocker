package com.teegarcs.mocker;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import com.teegarcs.mocker.internals.MockerCacheManager;
import com.teegarcs.mocker.internals.MockerDataLayer;
import com.teegarcs.mocker.internals.MockerInternalConstants;
import com.teegarcs.mocker.internals.MockerInternals;

/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 * Created by cteegarden on 2/29/16.
 */
public final class MockerInitializer {

    private static MockerCacheManager mockerCacheManager = null;
    private static boolean mockerMatchingEnabled = false;
    private static boolean updateMade =false;
    private static String defaultMockerFile = null;

    public static void install(Application application, String defaultMockerFile){
        MockerInitializer.defaultMockerFile = defaultMockerFile;
        install(application);
    }

    public static void install(final Application application){
        mockerCacheManager = new MockerCacheManager();
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                MockerInternals.showMockerIntroNotification(application);
                //if(mockerMatchingEnabled)
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if(!MockerInternals.isAppOnForeground(activity)){
                    NotificationManager notificationManager = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(MockerInternalConstants.FLAG_MOCKER_ENTRY_NOTIFICAITON_ID);
                    turnOffMatching(activity);
                   if(mockerCacheManager !=null){
                       mockerCacheManager=null;
                   }
                }
            }
        });
    }


    public static void turnOffMatching(Context context){
        mockerMatchingEnabled = false;
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(MockerInternalConstants.FLAG_MOCKER_MATCHING_NOTIFICATION_ID);
    }

    public static void turnOnMatching(Context context){
        if(!mockerMatchingEnabled){
            mockerMatchingEnabled = true;
            MockerInternals.showMockerMatchingNotificaiton(context);
        }
    }

    public static boolean getMockerMatching(){
        return mockerMatchingEnabled;
    }
    public static MockerCacheManager getMockerCacheManager(){
        if(mockerCacheManager == null)
            mockerCacheManager = new MockerCacheManager();
        return mockerCacheManager;
    }

    public static MockerInterceptor getMockerInterceptor(Context context){
        MockerDataLayer dataLayer = new MockerDataLayer(context);
        return new MockerInterceptor(dataLayer);
    }

    public static MatchingInterceptor getMatchingInterceptor(Context context){
        MockerDataLayer dataLayer = new MockerDataLayer(context);
        return new MatchingInterceptor(dataLayer);
    }

    private static void setDefaultMockerFile(String defaultMockerFile){
        MockerInitializer.defaultMockerFile = defaultMockerFile;
    }

    public static String getDefaultMockerFile(){
        return defaultMockerFile;
    }
    public static boolean checkforUpdate(){
        return updateMade;
    }

    public static void setUpdateMade(boolean updated){
        updateMade = updated;
    }

}
