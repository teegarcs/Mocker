package com.teegarcs.mocker.internals.presenters;

import android.text.TextUtils;

import com.teegarcs.mocker.MockerInitializer;
import com.teegarcs.mocker.internals.MockerDataLayer;
import com.teegarcs.mocker.internals.interactors.ScenarioInteractor;
import com.teegarcs.mocker.internals.model.MockerResponse;
import com.teegarcs.mocker.internals.model.MockerScenario;

/**
 * Created by cteegarden on 4/19/16.
 */
public class ScenarioPresenter extends BasePresenter<ScenarioInteractor> {
    private int scenarioPos;
    private MockerScenario scenario;

    public ScenarioPresenter(MockerDataLayer dataLayer, int scenarioPos){
        this.mockerDataLayer = dataLayer;
        this.scenarioPos = scenarioPos;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        this.mockerDock = mockerDataLayer.getMockerDockData();
        scenario = mockerDock.mockerScenario.get(scenarioPos);
        view.setScenario(scenario);
    }

    public void toggleScenario(boolean onOff){
        if(scenario==null)
            return;

        if(scenario.setEnabled(onOff)) {
            // the first response was enabled by default
            // update recyclerview to reflect this
            view.updateAdapter(0);
        }
        MockerInitializer.setUpdateMade(true);
    }

    public void addResponse(){
        MockerResponse response = new MockerResponse();
        response.responseJson = "";
        response.statusCode = 200;
        response.responseName = "default name";
        response.includeGlobalHeader = false;
        response.responseEnabled = false;
        scenario.response.add(response);
        view.updateAdapter();
        view.selectResponse(scenario.response.size()-1);
    }

    public void deleteScenario(){
        if(scenario==null)
            return;

        mockerDock.mockerScenario.remove(scenarioPos);
        MockerInitializer.setUpdateMade(true);
    }

    public void duplicateScenario(){
        if(scenario==null)
            return;
        mockerDock.mockerScenario.add(new MockerScenario(mockerDock.mockerScenario.get(scenarioPos)));
        MockerInitializer.setUpdateMade(true);
    }

    public void updateUrlPattern(String pattern){
        if(scenario==null)
            return;

        if(!TextUtils.isEmpty(pattern)){
            scenario.urlPattern = pattern;
        }else{
            scenario.urlPattern = "";
        }
    }

    public void updateScenarioName(String name){
        if(scenario==null)
            return;

        if(!TextUtils.isEmpty(name)){
            scenario.serviceName = name;
        }else{
            scenario.serviceName = "";
        }

        MockerInitializer.setUpdateMade(true);
        view.setTitle(scenario.serviceName);
    }

    public void toggleResponse(boolean onOff, int pos){
        if(scenario==null)
            return;

        scenario.response.get(pos).responseEnabled = onOff;
        if(onOff){
            //if we are enabling one... disable the rest
            for(int i=0; i<scenario.response.size(); i++){
                if(pos ==i)
                    continue;
                scenario.response.get(i).responseEnabled = false;
            }
        }
        view.updateAdapter();
    }

    public int getScenarioPos(){
        return scenarioPos;
    }

    public String getShareData(){
        return mockerDataLayer.convertObjectToJson(mockerDock.mockerScenario.get(scenarioPos));
    }

    public void refreshData(){
        if(scenario==null)
            return;

        if(MockerInitializer.checkforUpdate()){
            view.updateAdapter();
        }
    }



}
