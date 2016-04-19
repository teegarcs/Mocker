package com.teegarcs.mocker.internals;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.teegarcs.mocker.R;
import com.teegarcs.mocker.internals.adapters.ResponseRecyclerAdapter;
import com.teegarcs.mocker.internals.adapters.ResponseRecyclerAdapter.ResponseListener;
import com.teegarcs.mocker.internals.interactors.ScenarioInteractor;
import com.teegarcs.mocker.internals.model.MockerScenario;
import com.teegarcs.mocker.internals.presenters.ScenarioPresenter;

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
public class MockerScenarioActivity extends MockerToolbarActivity implements ResponseListener, ScenarioInteractor {
    public static final String EXTRA_SCENARIO_POSITION = "EXTRA_SCENARIO_POSITION";

    private SwitchCompat mockerToggle;
    private EditText serviceNameEditText, urlPatternEditText;
    private RecyclerView responseRecyclerView;
    private FloatingActionButton faButton;
    private ResponseRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ScenarioPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocker_scenario);
        presenter = new ScenarioPresenter(new MockerDataLayer(this), getIntent().getExtras().getInt(EXTRA_SCENARIO_POSITION));
        setBasePresenter(presenter);
        setUpNav(true);

        mockerToggle = (SwitchCompat)findViewById(R.id.mockerToggle);
        serviceNameEditText = (EditText)findViewById(R.id.serviceNameEditText);
        urlPatternEditText = (EditText)findViewById(R.id.urlPatternEditText);
        responseRecyclerView = (RecyclerView)findViewById(R.id.responseRecyclerView);
        faButton = (FloatingActionButton)findViewById(R.id.faButton);

        serviceNameEditText.addTextChangedListener(scenarioNameWatcher);
        urlPatternEditText.addTextChangedListener(urlPatternWatcher);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        responseRecyclerView.setLayoutManager(linearLayoutManager);

        mockerToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.toggleScenario(isChecked);
            }
        });

        faButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               presenter.addResponse();
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
        getMenuInflater().inflate(R.menu.menu_mocker_scenario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_delete){
            presenter.deleteScenario();
            finish();
            return true;
        }else if(id == R.id.action_duplicate){
            presenter.duplicateScenario();
            return true;
        }else if (id == R.id.action_share) {
            String tempdata = presenter.getShareData();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, tempdata);
            startActivity(sendIntent);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void responseToggle(boolean enabled, int pos) {
        presenter.toggleResponse(enabled, pos);

    }

    @Override
    public void responseSelected(int pos) {
        Intent forward = new Intent(this, MockerResponseActivity.class);
        forward.putExtra(MockerResponseActivity.EXTRA_SCENARIO_POSITION, presenter.getScenarioPos());
        forward.putExtra(MockerResponseActivity.EXTRA_RESPONSE_POSITION, pos);
        startActivity(forward);
    }

    private TextWatcher scenarioNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            presenter.updateScenarioName(s.toString());
        }
    };

    private TextWatcher urlPatternWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            presenter.updateUrlPattern(s.toString());

        }
    };

    @Override
    public void setScenario(MockerScenario scenario) {
        setToolbarTitle(scenario.serviceName);

        if(!TextUtils.isEmpty(scenario.serviceName))
            serviceNameEditText.setText(scenario.serviceName);
        if(!TextUtils.isEmpty(scenario.urlPattern))
            urlPatternEditText.setText(scenario.urlPattern);
        mockerToggle.setChecked(scenario.mockerEnabled);


        adapter = new ResponseRecyclerAdapter(this, scenario, this);
        responseRecyclerView.setAdapter(adapter);
    }

    @Override
    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateAdapter(int pos) {
        adapter.notifyItemChanged(0);
    }

    @Override
    public void selectResponse(int pos) {
        responseSelected(pos);
    }

    @Override
    public void setTitle(String title) {
        setToolbarTitle(title);
    }
}
