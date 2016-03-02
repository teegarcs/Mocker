package com.teegarcs.mocker.internals;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cteegarden on 3/1/16.
 */
public enum RequestType implements Parcelable {
    POST, GET, DELETE, PUT;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name());
    }
}
