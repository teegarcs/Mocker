package com.teegarcs.mocker.internals.interactors;

import com.teegarcs.mocker.internals.model.MockerHeader;

import java.util.ArrayList;

/**
 * Created by cteegarden on 4/19/16.
 */
public interface OptionsInteractor {
    void updateAdapter();
    void setTitle(String title);
    void setHeaders(ArrayList<MockerHeader> headers);
    void showGlobalViews();
    void setRequestDuration(int duration);
}
