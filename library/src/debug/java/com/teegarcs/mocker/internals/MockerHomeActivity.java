package com.teegarcs.mocker.internals;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.teegarcs.mocker.R;
import com.teegarcs.mocker.internals.adapters.ScenarioRecyclerAdapter;
import com.teegarcs.mocker.internals.adapters.ScenarioRecyclerAdapter.ScenarioListener;
import com.teegarcs.mocker.internals.interactors.HomeInteractor;
import com.teegarcs.mocker.internals.model.MockerDock;
import com.teegarcs.mocker.internals.presenters.HomePresenter;

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

public class MockerHomeActivity extends MockerToolbarActivity implements ScenarioListener, HomeInteractor {

    private SwitchCompat mockerToggle;
    private RecyclerView scenarioRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ScenarioRecyclerAdapter adapter;
    private FloatingActionButton faButton;
    private View globalHeaderView, mockerViewer;
    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocker_home);
        setToolbarTitle("Mocker Home");
        setUpNav(true);
        presenter = new HomePresenter(new MockerDataLayer(this));
        setBasePresenter(presenter);

        mockerToggle = (SwitchCompat)findViewById(R.id.mockerToggle);
        scenarioRecyclerView = (RecyclerView)findViewById(R.id.scenarioRecyclerView);
        faButton = (FloatingActionButton)findViewById(R.id.faButton);
        globalHeaderView = findViewById(R.id.globalHeaderView);
        mockerViewer = findViewById(R.id.mockerViewer);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scenarioRecyclerView.setLayoutManager(linearLayoutManager);


        mockerToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               presenter.toggleMocker(!isChecked);
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

        globalHeaderView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forward = new Intent(MockerHomeActivity.this, MockerOptionsActivity.class);
                forward.putExtra(MockerOptionsActivity.EXTRA_GLOBAL, true);
                startActivity(forward);
            }
        });


        mockerViewer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forward = new Intent(MockerHomeActivity.this, MockerJsonViewerActivity.class);
                startActivity(forward);
            }
        });

        faButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addMockerScenario();
            }
        });
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
    protected void onResume() {
        super.onResume();
        presenter.refreshData();
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
    public void scenarioToggle(boolean enabled, int pos) {
       presenter.toggleScenario(enabled, pos);
    }

    @Override
    public void scenarioSelected(int pos) {
        Intent forward = new Intent(this,MockerScenarioActivity.class);
        forward.putExtra(MockerScenarioActivity.EXTRA_SCENARIO_POSITION, pos);
        startActivity(forward);
    }

    @Override
    public void setMockerDock(MockerDock mockerDock) {
        adapter = new ScenarioRecyclerAdapter(this, mockerDock, this);
        scenarioRecyclerView.setAdapter(adapter);
    }

    @Override
    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void selectScenario(int pos) {
        scenarioSelected(pos);
    }

    @Override
    public void setMockerToggle(boolean onOff) {
        mockerToggle.setChecked(onOff);
    }
}
