package com.teegarcs.mocker.internals;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.teegarcs.mocker.MockerInitializer;
import com.teegarcs.mocker.R;
import com.teegarcs.mocker.internals.ScenarioRecyclerAdapter.ScenarioListener;

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
 */

public class MockerHomeActivity extends MockerToolbarActivity implements ScenarioListener {

    private SwitchCompat mockerToggle;
    private RecyclerView scenarioRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ScenarioRecyclerAdapter adapter;
    private FloatingActionButton faButton;
    private View globalHeaderView, mockerViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocker_home);
        setToolbarTitle("Mocker Home");
        dataLayer = new MockerDataLayer(this);
        mockerDock = dataLayer.getMockerDockData();
        mockerToggle = (SwitchCompat)findViewById(R.id.mockerToggle);
        mockerToggle.setChecked(!mockerDock.mockerDisabled);

       mockerToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked) {
                   mockerDock.mockerDisabled = false;
               } else {
                   mockerDock.mockerDisabled = true;
               }
           }
       });

        scenarioRecyclerView = (RecyclerView)findViewById(R.id.scenarioRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scenarioRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ScenarioRecyclerAdapter(this, mockerDock, this);
        scenarioRecyclerView.setAdapter(adapter);

        faButton = (FloatingActionButton)findViewById(R.id.faButton);
        faButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MockerScenario scenario = new MockerScenario();
                scenario.serviceName = "default name";
                scenario.urlPattern = "default URL pattern";
                scenario.mockerEnabled = false;
                scenario.requestType = RequestType.GET;
                mockerDock.mockerScenario.add(scenario);
                adapter.notifyDataSetChanged();
                scenarioSelected(mockerDock.mockerScenario.size() - 1);
            }
        });

        scenarioRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    faButton.setVisibility(View.GONE);
                } else {
                    faButton.setVisibility(View.VISIBLE);
                }
            }
        });

        globalHeaderView = findViewById(R.id.globalHeaderView);
        globalHeaderView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forward = new Intent(MockerHomeActivity.this, MockerOptionsActivity.class);
                forward.putExtra(MockerOptionsActivity.EXTRA_GLOBAL, true);
                startActivity(forward);
            }
        });


        mockerViewer = findViewById(R.id.mockerViewer);
        mockerViewer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forward = new Intent(MockerHomeActivity.this, MockerJsonViewerActivity.class);
                startActivity(forward);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(MockerInitializer.checkforUpdate()){
            adapter.notifyDataSetChanged();
            MockerInitializer.setUpdateMade(false);
        }

    }

    @Override
    protected void upNavPressed() {
        //no upNav option here.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mocker_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_share) {
//
//            //handle here
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void scenarioToggle(boolean enabled, int pos) {
        mockerDock.mockerScenario.get(pos).setEnabled(enabled);
    }

    @Override
    public void scenarioSelected(int pos) {
        Intent forward = new Intent(this,MockerScenarioActivity.class);
        forward.putExtra(MockerScenarioActivity.EXTRA_SCENARIO_POSITION, pos);
        startActivity(forward);
    }
}
