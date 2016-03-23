package com.teegarcs.mocker.internals;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.TextView;

import com.teegarcs.mocker.R;

/**
 * Created by cteegarden on 3/23/16.
 */
public class MockerJsonViewerActivity extends MockerToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mocker_json_viewer);
        setToolbarTitle("Mocker Viewer");
        setUpNav(true);
        dataLayer = new MockerDataLayer(this);
        mockerDock = dataLayer.getMockerDockData();
        TextView jsonView = (TextView)findViewById(R.id.jsonText);
        String tempdata = dataLayer.convertObjectToJson(mockerDock);
        if(!TextUtils.isEmpty(tempdata)){
            jsonView.setText(tempdata);
        }
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
}
