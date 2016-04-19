package com.teegarcs.mocker.internals.interactors;

import com.teegarcs.mocker.internals.model.MockerResponse;

/**
 * Created by cteegarden on 4/19/16.
 */
public interface ResponseInteractor {

    void setResponse(MockerResponse response);
    void setTitle(String title);
}
