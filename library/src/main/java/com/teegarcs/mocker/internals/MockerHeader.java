package com.teegarcs.mocker.internals;

import android.os.Parcel;
import android.os.Parcelable;

/**
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
