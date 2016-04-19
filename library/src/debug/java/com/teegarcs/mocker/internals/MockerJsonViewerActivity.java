package com.teegarcs.mocker.internals;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.TextView;

import com.teegarcs.mocker.R;
import com.teegarcs.mocker.internals.interactors.JsonViewerInteractor;
import com.teegarcs.mocker.internals.presenters.JsonViewerPresenter;

/**
 * Created by cteegarden on 3/23/16.
 */
public class MockerJsonViewerActivity extends MockerToolbarActivity implements JsonViewerInteractor {

    public JsonViewerPresenter presenter;
    private TextView jsonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mocker_json_viewer);
        setToolbarTitle("Mocker Viewer");
        setUpNav(true);
        presenter = new JsonViewerPresenter(new MockerDataLayer(this));
        setBasePresenter(presenter);
        jsonView = (TextView)findViewById(R.id.jsonText);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.takeView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Override
    protected void upNavPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mocker_home, menu);
        return true;
    }

    @Override
    public void setJson(String mockerDock) {
        if(!TextUtils.isEmpty(mockerDock)){
            jsonView.setText(mockerDock);
        }
    }
}
