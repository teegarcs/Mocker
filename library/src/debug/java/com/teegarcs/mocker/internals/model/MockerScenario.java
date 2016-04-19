package com.teegarcs.mocker.internals.model;

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
public class MockerScenario implements Parcelable {

    public String serviceName = "name";
    public String urlPattern = "https";
    public RequestType requestType = RequestType.GET;
    public boolean mockerEnabled = true;
    public ArrayList<MockerResponse> response = new ArrayList<>();


    public MockerScenario(){}

    public MockerScenario(Parcel in) {
        serviceName = in.readString();
        urlPattern = in.readString();
        requestType = in.readParcelable(RequestType.class.getClassLoader());
        mockerEnabled = in.readInt()==1;
        in.readTypedList(response, MockerResponse.CREATOR);
    }

    public MockerScenario(MockerScenario copy){
        this.serviceName = copy.serviceName;
        this.urlPattern = copy.urlPattern;
        this.requestType = copy.requestType;
        this.mockerEnabled = copy.mockerEnabled;
        for(MockerResponse rp : copy.response){
            response.add(new MockerResponse(rp));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceName);
        dest.writeString(urlPattern);
        dest.writeParcelable(requestType, flags);
        dest.writeInt(mockerEnabled?1:0);
        dest.writeTypedList(response);
    }

    public static final Creator<MockerScenario> CREATOR = new Creator<MockerScenario>() {
        @Override
        public MockerScenario createFromParcel(Parcel in) {
            return new MockerScenario(in);
        }

        @Override
        public MockerScenario[] newArray(int size) {
            return new MockerScenario[size];
        }
    };

    /**
     * Controls whether this scenario should be turned on or off for mocking
     *
     * <p>If no {@link MockerResponse} is already enabled for this scenario, this method will enable
     * the first response ({@link MockerScenario#response} index 0) by default.
     *
     * @param onOff If true, this scenario will be enabled for mocking. False to disable mocking.
     * @return true if {@link MockerScenario#response} did not have a {@link MockerResponse} enabled prior to this method call, otherwise false
     */
    public boolean setEnabled(boolean onOff) {
        mockerEnabled = onOff;

        // if no response is enabled for this scenario, enable the first response by default
        if(onOff && response.size() > 0) {
            for(MockerResponse mockerResponse : response) {
                if(mockerResponse.responseEnabled) {
                    // a response is enabled, do nothing
                    return false;
                }
            }
            // enable the first response
            response.get(0).responseEnabled = true;
            return true;
        }

        return false;
    }
}
