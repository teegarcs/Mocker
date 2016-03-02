package com.teegarcs.mocker.internals;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
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
