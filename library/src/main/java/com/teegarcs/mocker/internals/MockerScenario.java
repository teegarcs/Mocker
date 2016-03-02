package com.teegarcs.mocker.internals;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
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

}
