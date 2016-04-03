package com.teegarcs.mocker.internals;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.teegarcs.mocker.R;
import com.teegarcs.mocker.internals.HeaderRecyclerAdapter.HeaderListener;

import java.util.ArrayList;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 * Created by cteegarden on 3/1/16.
 */
public class MockerOptionsActivity extends MockerToolbarActivity implements HeaderListener {
    public static final String EXTRA_SCENARIO_POSITION = "EXTRA_SCENARIO_POSITION";
    public static final String EXTRA_RESPONSE_POSITION = "EXTRA_RESPONSE_POSITION";
    public static final String EXTRA_GLOBAL = "EXTRA_GLOBAL";

    private RecyclerView headerRecyclerView;
    private LinearLayoutManager layoutManager;
    private HeaderRecyclerAdapter adapter;
    private FloatingActionButton faButton;
    private ArrayList<MockerHeader> headers;
    private boolean globalHeader = false;
    private int scenarioPosition, responsePosition;
    private SeekBar requestDurationBar;
    private TextView requestDurationValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocker_header);
        dataLayer = new MockerDataLayer(this);
        mockerDock = dataLayer.getMockerDockData();

        globalHeader = getIntent().getExtras().getBoolean(EXTRA_GLOBAL);


        if (globalHeader) {
            setToolbarTitle("Global Options");
            headers = mockerDock.globalHeaders;
            findViewById(R.id.request_duration_seekbar_container).setVisibility(View.VISIBLE);
            findViewById(R.id.divider).setVisibility(View.VISIBLE);
        } else {
            scenarioPosition = getIntent().getExtras().getInt(EXTRA_SCENARIO_POSITION);
            responsePosition = getIntent().getExtras().getInt(EXTRA_RESPONSE_POSITION);
            MockerScenario scenario = mockerDock.mockerScenario.get(scenarioPosition);
            MockerResponse response = scenario.response.get(responsePosition);
            headers = response.responseHeaders;
            setToolbarTitle(response.responseName);
        }

        setUpNav(true);


        faButton = (FloatingActionButton) findViewById(R.id.faButton);


        faButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MockerHeader header = new MockerHeader();
                header.headerName = "name";
                header.headerValue = "value";
                headers.add(header);
                adapter.notifyDataSetChanged();
            }
        });

        headerRecyclerView = (RecyclerView) findViewById(R.id.headerRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        headerRecyclerView.setLayoutManager(layoutManager);
        adapter = new HeaderRecyclerAdapter(this, headers, this);
        headerRecyclerView.setAdapter(adapter);

        // request duration views
        requestDurationBar = (SeekBar) findViewById(R.id.request_duration_seekbar);
        requestDurationValue = (TextView) findViewById(R.id.request_duration_value);
        requestDurationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                requestDurationValue.setText(i + " seconds");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int i = seekBar.getProgress();
                mockerDock.globalRequestDuration = seekBar.getProgress();
            }
        });
        requestDurationBar.setProgress(mockerDock.globalRequestDuration);


    }

    @Override
    protected void upNavPressed() {
        //no upNav option here.
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mocker_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void nameChanged(String name, int pos) {
        headers.get(pos).headerName = name;
    }

    @Override
    public void valueChanged(String value, int pos) {
        headers.get(pos).headerValue = value;
    }

    @Override
    public void deleteHeader(int pos) {
        headers.remove(pos);
        adapter.notifyDataSetChanged();
    }
}
