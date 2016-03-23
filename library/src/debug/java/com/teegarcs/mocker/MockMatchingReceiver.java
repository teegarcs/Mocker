package com.teegarcs.mocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
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
