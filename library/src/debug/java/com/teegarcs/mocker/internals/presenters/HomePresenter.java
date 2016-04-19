package com.teegarcs.mocker.internals.presenters;

import com.teegarcs.mocker.MockerInitializer;
import com.teegarcs.mocker.internals.MockerDataLayer;
import com.teegarcs.mocker.internals.interactors.HomeInteractor;
import com.teegarcs.mocker.internals.model.MockerScenario;
import com.teegarcs.mocker.internals.model.RequestType;

/**
 * Created by cteegarden on 4/19/16.
 */
public class HomePresenter extends BasePresenter<HomeInteractor> {


    public HomePresenter(MockerDataLayer dataLayer){
        this.mockerDataLayer = dataLayer;

    }

    @Override
    protected void onLoad() {
        super.onLoad();
        this.mockerDock = mockerDataLayer.getMockerDockData();
        view.setMockerDock(mockerDock);
        view.setMockerToggle(!mockerDock.mockerDisabled);
    }

    public void refreshData(){
        if(mockerDock==null)
            return;

        if(MockerInitializer.checkforUpdate()){
            view.updateAdapter();
            MockerInitializer.setUpdateMade(false);
        }
    }

    public void addMockerScenario(){
        MockerScenario scenario = new MockerScenario();
        scenario.serviceName = "default name";
        scenario.urlPattern = "default URL pattern";
        scenario.mockerEnabled = false;
        scenario.requestType = RequestType.GET;
        mockerDock.mockerScenario.add(scenario);
        view.updateAdapter();
        view.selectScenario(mockerDock.mockerScenario.size() - 1);
    }

    public void toggleMocker(boolean onOff){
        if(mockerDock!=null)
            mockerDock.mockerDisabled = onOff;
    }

    public void toggleScenario(boolean onOff, int pos){
        if(mockerDock!=null)
            mockerDock.mockerScenario.get(pos).setEnabled(onOff);
    }
}
