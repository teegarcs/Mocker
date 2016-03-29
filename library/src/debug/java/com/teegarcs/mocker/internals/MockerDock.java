package com.teegarcs.mocker.internals;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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

 * Created by cteegarden on 3/1/16.
 */
public class MockerDock implements Parcelable {
    public static final int DEFAULT_GLOBAL_REQUEST_DURATION = 1;
    public ArrayList<MockerHeader> globalHeaders = new ArrayList<>();
    public boolean mockerDisabled = false;
    public ArrayList<MockerScenario> mockerScenario = new ArrayList<>();
    public int globalRequestDuration = DEFAULT_GLOBAL_REQUEST_DURATION;

    public MockerDock(){}

    public MockerDock(Parcel in) {
        in.readTypedList(globalHeaders, MockerHeader.CREATOR);
        mockerDisabled = in.readInt()==1;
        in.readTypedList(mockerScenario, MockerScenario.CREATOR);
        globalRequestDuration = in.readInt();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(globalHeaders);
        dest.writeInt(mockerDisabled ? 1 : 0);
        dest.writeTypedList(mockerScenario);
        dest.writeInt(globalRequestDuration);
    }

    public static final Creator<MockerDock> CREATOR = new Creator<MockerDock>() {
        @Override
        public MockerDock createFromParcel(Parcel in) {
            return new MockerDock(in);
        }

        @Override
        public MockerDock[] newArray(int size) {
            return new MockerDock[size];
        }
    };
}
