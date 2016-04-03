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
public class MockerResponse implements Parcelable {
    public String responseName;
    public int statusCode = 200;
    public boolean includeGlobalHeader = true;
    public boolean responseEnabled = true;
    public String responseJson = "some sort of Json";
    public ArrayList<MockerHeader> responseHeaders = new ArrayList<>();


    public MockerResponse(){}
    public MockerResponse(Parcel in) {
        responseName = in.readString();
        statusCode = in.readInt();
        includeGlobalHeader = in.readInt()==1;
        responseEnabled = in.readInt()==1;
        responseJson = in.readString();
        in.readTypedList(responseHeaders, MockerHeader.CREATOR);
    }

    public MockerResponse(MockerResponse copy){
        this.responseName = copy.responseName;
        this.statusCode = copy.statusCode;
        this.includeGlobalHeader = copy.includeGlobalHeader;
        this.responseEnabled = copy.responseEnabled;
        this.responseJson = copy.responseJson;
        for(MockerHeader header : copy.responseHeaders){
            this.responseHeaders.add(new MockerHeader(header));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(responseName);
        dest.writeInt(statusCode);
        dest.writeInt(includeGlobalHeader?1:0);
        dest.writeInt(responseEnabled?1:0);
        dest.writeString(responseJson);
        dest.writeTypedList(responseHeaders);
    }

    public static final Creator<MockerResponse> CREATOR = new Creator<MockerResponse>() {
        @Override
        public MockerResponse createFromParcel(Parcel in) {
            return new MockerResponse(in);
        }

        @Override
        public MockerResponse[] newArray(int size) {
            return new MockerResponse[size];
        }
    };
}
