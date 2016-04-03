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

import com.teegarcs.mocker.MockerInitializer;
import com.teegarcs.mocker.R;
import com.teegarcs.mocker.internals.ResponseRecyclerAdapter.ResponseListener;

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
public class MockerScenarioActivity extends MockerToolbarActivity implements ResponseListener {
    public static final String EXTRA_SCENARIO_POSITION = "EXTRA_SCENARIO_POSITION";
    private MockerScenario scenario;
    private SwitchCompat mockerToggle;
    private EditText serviceNameEditText, urlPatternEditText;
    private RecyclerView responseRecyclerView;
    private FloatingActionButton faButton;
    private ResponseRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    int scenarioPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocker_scenario);
        dataLayer = new MockerDataLayer(this);
        mockerDock = dataLayer.getMockerDockData();
        scenarioPosition = getIntent().getExtras().getInt(EXTRA_SCENARIO_POSITION);
        scenario = mockerDock.mockerScenario.get(scenarioPosition);
        setToolbarTitle(scenario.serviceName);
        setUpNav(true);

        mockerToggle = (SwitchCompat)findViewById(R.id.mockerToggle);
        serviceNameEditText = (EditText)findViewById(R.id.serviceNameEditText);
        urlPatternEditText = (EditText)findViewById(R.id.urlPatternEditText);
        responseRecyclerView = (RecyclerView)findViewById(R.id.responseRecyclerView);
        faButton = (FloatingActionButton)findViewById(R.id.faButton);


        if(!TextUtils.isEmpty(scenario.serviceName))
            serviceNameEditText.setText(scenario.serviceName);
        if(!TextUtils.isEmpty(scenario.urlPattern))
            urlPatternEditText.setText(scenario.urlPattern);
        mockerToggle.setChecked(scenario.mockerEnabled);
        mockerToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(scenario.setEnabled(isChecked)) {
                    // the first response was enabled by default
                    // update recyclerview to reflect this
                    adapter.notifyItemChanged(0);
                }
                MockerInitializer.setUpdateMade(true);
            }
        });

        faButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MockerResponse response = new MockerResponse();
                response.responseJson = "";
                response.statusCode = 200;
                response.responseName = "default name";
                response.includeGlobalHeader = false;
                response.responseEnabled = false;
                scenario.response.add(response);
                adapter.notifyDataSetChanged();
                responseSelected(scenario.response.size()-1);
            }
        });

        serviceNameEditText.addTextChangedListener(scenarioNameWatcher);
        urlPatternEditText.addTextChangedListener(urlPatternWatcher);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new ResponseRecyclerAdapter(this, scenario, this);

        responseRecyclerView.setLayoutManager(linearLayoutManager);
        responseRecyclerView.setAdapter(adapter);



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MockerInitializer.checkforUpdate()){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void upNavPressed() {
        //no upNav option here.
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
            mockerDock.mockerScenario.remove(scenarioPosition);
            MockerInitializer.setUpdateMade(true);
            finish();
            return true;
        }else if(id == R.id.action_duplicate){
            mockerDock.mockerScenario.add(new MockerScenario(mockerDock.mockerScenario.get(scenarioPosition)));
            MockerInitializer.setUpdateMade(true);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void responseToggle(boolean enabled, int pos) {
        scenario.response.get(pos).responseEnabled = enabled;

        if(enabled){
            //if we are enabling one... disable the rest
           for(int i=0; i<scenario.response.size(); i++){
               if(pos ==i)
                   continue;
               scenario.response.get(i).responseEnabled = false;
           }
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void responseSelected(int pos) {
        Intent forward = new Intent(this, MockerResponseActivity.class);
        forward.putExtra(MockerResponseActivity.EXTRA_SCENARIO_POSITION, scenarioPosition);
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
            if(!TextUtils.isEmpty(s.toString())){
                scenario.serviceName = s.toString();
            }else{
                scenario.serviceName = "";
            }

            MockerInitializer.setUpdateMade(true);
            setToolbarTitle(scenario.serviceName);
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
            if(!TextUtils.isEmpty(s.toString())){
                scenario.urlPattern = s.toString();
            }else{
                scenario.urlPattern = "";
            }

        }
    };
}
