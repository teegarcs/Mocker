package com.teegarcs.mocker.internals.presenters;

import android.text.Editable;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;

import com.teegarcs.mocker.MockerInitializer;
import com.teegarcs.mocker.internals.MockerDataLayer;
import com.teegarcs.mocker.internals.interactors.ResponseInteractor;
import com.teegarcs.mocker.internals.model.MockerResponse;
import com.teegarcs.mocker.internals.model.MockerScenario;

/**
 * Created by cteegarden on 4/19/16.
 */
public class ResponsePresenter extends BasePresenter<ResponseInteractor> {
    private int scenarioPos, responsePos;
    private MockerResponse response;
    private MockerScenario scenario;

    public ResponsePresenter(MockerDataLayer dataLayer, int scenarioPos, int responsePos){
        this.mockerDataLayer = dataLayer;
        this.scenarioPos = scenarioPos;
        this.responsePos = responsePos;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        this.mockerDock = mockerDataLayer.getMockerDockData();
        scenario = mockerDock.mockerScenario.get(scenarioPos);
        response = scenario.response.get(responsePos);
        view.setResponse(response);
    }


    public void toggleResponse(boolean onOff){
        if(response==null)
            return;
        response.responseEnabled = onOff;
        if (onOff) {
            //if we are enabling one... disable the rest
            for (int i = 0; i < scenario.response.size(); i++) {
                if (responsePos == i)
                    continue;
                scenario.response.get(i).responseEnabled = false;
            }
        }
        MockerInitializer.setUpdateMade(true);
    }


    public void toggleGlobalHeader(boolean onOff){
        if(response!=null)
            response.includeGlobalHeader = onOff;
    }


    public void updateResponseName(String name){
        if(response==null)
            return;
        if(!TextUtils.isEmpty(name)){
            response.responseName = name;
        }else{
            response.responseName = "";
        }

        MockerInitializer.setUpdateMade(true);
        view.setTitle(response.responseName);
    }

    public void updateStatusCode(String statusCode){
        if(response==null)
            return;
        if(!TextUtils.isEmpty(statusCode)){
            response.statusCode = Integer.parseInt(statusCode);
        }else{
            response.statusCode = 0;
        }
    }

    public void updateResponseJson(Editable json){
        if(response==null)
            return;
        CharacterStyle[] toBeRemovedSpans = json.getSpans(0, json.length(), MetricAffectingSpan.class);
        for (int i = 0; i < toBeRemovedSpans.length; i++){
            json.removeSpan(toBeRemovedSpans[i]);
        }

        if(!TextUtils.isEmpty(json.toString())){
            response.responseJson = json.toString();
        }else{
            response.responseJson = "";
        }
    }

    public void duplicateResponse(){
        if(response==null)
            return;
        scenario.response.add(new MockerResponse(response));
        MockerInitializer.setUpdateMade(true);
    }

    public void deleteResponse(){
        if(response==null)
            return;
        scenario.response.remove(responsePos);
        MockerInitializer.setUpdateMade(true);
    }

    public int getResponsePos(){
        return responsePos;
    }

    public int getScenarioPos(){
        return scenarioPos;
    }

    public String getShareData(){
        return mockerDataLayer.convertObjectToJson(response);
    }
}
