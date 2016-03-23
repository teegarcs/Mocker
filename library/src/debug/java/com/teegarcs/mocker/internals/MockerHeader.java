package com.teegarcs.mocker.internals;

import android.os.Parcel;
import android.os.Parcelable;

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
public class MockerHeader implements Parcelable {

    public String headerName = "name";
    public String headerValue = "value";

    public MockerHeader(){}

    public MockerHeader(Parcel in) {
        headerName = in.readString();
        headerValue = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headerName);
        dest.writeString(headerValue);
    }

    public static final Creator<MockerHeader> CREATOR = new Creator<MockerHeader>() {
        @Override
        public MockerHeader createFromParcel(Parcel in) {
            return new MockerHeader(in);
        }

        @Override
        public MockerHeader[] newArray(int size) {
            return new MockerHeader[size];
        }
    };
}
