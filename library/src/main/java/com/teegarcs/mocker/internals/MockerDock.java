package com.teegarcs.mocker.internals;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by cteegarden on 3/1/16.
 */
public class MockerDock implements Parcelable {

    public ArrayList<MockerHeader> globalHeaders = new ArrayList<>();
    public boolean mockerDisabled = false;
    public ArrayList<MockerScenario> mockerScenario = new ArrayList<>();

    public MockerDock(){}

    public MockerDock(Parcel in) {
        in.readTypedList(globalHeaders, MockerHeader.CREATOR);
        mockerDisabled = in.readInt()==1;
        in.readTypedList(mockerScenario, MockerScenario.CREATOR);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(globalHeaders);
        dest.writeInt(mockerDisabled?1:0);
        dest.writeTypedList(mockerScenario);
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
