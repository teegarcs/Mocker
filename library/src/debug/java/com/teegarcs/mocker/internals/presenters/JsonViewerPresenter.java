package com.teegarcs.mocker.internals.presenters;

import com.teegarcs.mocker.internals.MockerDataLayer;
import com.teegarcs.mocker.internals.interactors.JsonViewerInteractor;

/**
 * Created by cteegarden on 4/19/16.
 */
public class JsonViewerPresenter extends BasePresenter<JsonViewerInteractor> {

    public JsonViewerPresenter(MockerDataLayer dataLayer){
        this.mockerDataLayer = dataLayer;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        this.mockerDock = mockerDataLayer.getMockerDockData();
        view.setJson(mockerDataLayer.convertObjectToJson(mockerDock));
    }
}
