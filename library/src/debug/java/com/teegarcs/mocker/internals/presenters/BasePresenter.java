package com.teegarcs.mocker.internals.presenters;

import android.content.Context;
import android.support.annotation.Nullable;

import com.teegarcs.mocker.internals.MockerDataLayer;
import com.teegarcs.mocker.internals.MockerInternalConstants;
import com.teegarcs.mocker.internals.model.MockerDock;

/**
 * Created by cteegarden on 4/19/16.
 */
public abstract class BasePresenter<V> {

    protected V view = null;
    protected MockerDataLayer mockerDataLayer;
    protected MockerDock mockerDock;

    public void takeView(V view){
        this.view = view;
        onLoad();
    }


    public void dropView() {
        view = null;
        onDropView();
    }

    protected void onDropView() {

    }


    @Nullable
    protected V getView() {
        return view;
    }


    protected void onLoad() {

    }

    public void saveData(Context context){
        if(mockerDataLayer!=null && mockerDock!=null)
            mockerDataLayer.saveContent(mockerDataLayer.convertObjectToJson(mockerDock), MockerInternalConstants.INTERNAL_JSON_STORAGE, context);
    }



}
