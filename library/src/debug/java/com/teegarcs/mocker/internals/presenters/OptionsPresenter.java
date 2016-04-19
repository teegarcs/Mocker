package com.teegarcs.mocker.internals.presenters;

import com.teegarcs.mocker.internals.MockerDataLayer;
import com.teegarcs.mocker.internals.interactors.OptionsInteractor;
import com.teegarcs.mocker.internals.model.MockerHeader;
import com.teegarcs.mocker.internals.model.MockerResponse;
import com.teegarcs.mocker.internals.model.MockerScenario;

import java.util.ArrayList;

/**
 * Created by cteegarden on 4/19/16.
 */
public class OptionsPresenter extends BasePresenter<OptionsInteractor> {
    private boolean globalFlag = false;
    private ArrayList<MockerHeader> headers;
    private int scenarioPosition, responsePosition;

    public OptionsPresenter(MockerDataLayer dataLayer, boolean globalFlag, int scenarioPos, int responsePos){
        this.mockerDataLayer = dataLayer;
        this.globalFlag = globalFlag;
        this.scenarioPosition = scenarioPos;
        this.responsePosition = responsePos;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        this.mockerDock = mockerDataLayer.getMockerDockData();
        if (globalFlag) {
            view.setTitle("Global Options");
            view.showGlobalViews();
            headers = mockerDock.globalHeaders;
            view.setRequestDuration(mockerDock.globalRequestDuration);
        } else {
            MockerScenario scenario = mockerDock.mockerScenario.get(scenarioPosition);
            MockerResponse response = scenario.response.get(responsePosition);
            headers = response.responseHeaders;
            view.setTitle(response.responseName);
        }

        view.setHeaders(headers);
    }

    public void removeHeader(int pos){
        headers.remove(pos);
        view.updateAdapter();
    }

    public void addHeader(){
        MockerHeader header = new MockerHeader();
        header.headerName = "name";
        header.headerValue = "value";
        headers.add(header);
        view.updateAdapter();
    }

    public void updateName(String name, int pos) {
        if(headers!=null)
            headers.get(pos).headerName = name;
    }

    public void updateValue(String value, int pos) {
        if(headers!=null)
            headers.get(pos).headerValue = value;
    }

    public void setGlobalDuration(int duration){
        if(mockerDock!=null)
            mockerDock.globalRequestDuration = duration;
    }

}
