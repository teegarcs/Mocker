package com.teegarcs.mocker.internals;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.teegarcs.mocker.MockMatchingReceiver;

import java.util.List;

/**
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
public final class MockerInternals {


    public static void showMockerIntroNotification(Context context) {
        Intent notificationIntent = new Intent(context, MockerHomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        showNotification(context, "Mocker", "View Mocker Resources", pendingIntent, true, MockerInternalConstants.FLAG_MOCKER_ENTRY_NOTIFICAITON_ID);
    }

    public static void showMockerMatchingNotificaiton(Context context) {
        String contentTitle = "Service Matching Enabled";
        String contentText = "Requests will be matched and stored.";
        Intent notificationIntent = new Intent(context, MockerHomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Intent dismissIntent = new Intent(context, MockMatchingReceiver.class);
        dismissIntent.setAction(MockMatchingReceiver.DISABLE_MATCHING);
        PendingIntent piDismiss = PendingIntent.getBroadcast(context, 1, dismissIntent, 0);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Disable", piDismiss);
        notificationManager.notify(MockerInternalConstants.FLAG_MOCKER_MATCHING_NOTIFICATION_ID, notificationBuilder.build());

    }

    public static void showNotification(Context context, CharSequence contentTitle,
                                        CharSequence contentText, PendingIntent pendingIntent, boolean persistent, int notificationID) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(!persistent)
                        .setOngoing(persistent);

        notificationManager.notify(notificationID, notificationBuilder.build());
    }


    public static boolean isAppOnForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        return taskInfo.get(0).topActivity.getPackageName().equals(context.getApplicationInfo().packageName);
    }

    public static Intent generateShareViewIntent(Context context, Uri uri){
        //share intent
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType(context.getContentResolver().getType(uri));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //view intent
        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.setData(uri);
        viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent chooserIntent = Intent.createChooser(shareIntent, "Share");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{viewIntent});

        return chooserIntent;
    }
}
