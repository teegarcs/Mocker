package com.teegarcs.mocker.internals.interactors;

import com.teegarcs.mocker.internals.model.MockerScenario;

/**
 * Created by cteegarden on 4/19/16.
 */
public interface ScenarioInteractor {

    void setScenario(MockerScenario scenario);
    void updateAdapter();
    void updateAdapter(int pos);
    void selectResponse(int pos);
    void setTitle(String title);
}
