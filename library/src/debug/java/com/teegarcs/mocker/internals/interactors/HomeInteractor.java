package com.teegarcs.mocker.internals.interactors;

import com.teegarcs.mocker.internals.model.MockerDock;

/**
 * Created by cteegarden on 4/19/16.
 */
public interface HomeInteractor {

    void updateAdapter();
    void selectScenario(int pos);
    void setMockerToggle(boolean onOff);
    void setMockerDock(MockerDock mockerDock);
}
