package com.teegarcs.mocker.internals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.teegarcs.mocker.MockerInitializer;
import com.teegarcs.mocker.R;

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
public class MockerResponseActivity extends MockerToolbarActivity{
    public static final String EXTRA_SCENARIO_POSITION = "EXTRA_SCENARIO_POSITION";
    public static final String EXTRA_RESPONSE_POSITION = "EXTRA_RESPONSE_POSITION";
    private MockerScenario scenario;
    private MockerResponse response;

    private SwitchCompat mockerToggle, globalHeaderToggle;
    private EditText responseNameEditText, statusCodeEditText, mockedResponseEditText;
    private View repsonseHeaderView;

    private int scenarioPosition, responsePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocker_response);
        dataLayer = new MockerDataLayer(this);
        mockerDock = dataLayer.getMockerDockData();

        scenarioPosition = getIntent().getExtras().getInt(EXTRA_SCENARIO_POSITION);
        responsePosition = getIntent().getExtras().getInt(EXTRA_RESPONSE_POSITION);

        scenario = mockerDock.mockerScenario.get(scenarioPosition);
        response = scenario.response.get(responsePosition);
        setToolbarTitle(response.responseName);
        setUpNav(true);

        mockerToggle = (SwitchCompat)findViewById(R.id.mockerToggle);
        globalHeaderToggle = (SwitchCompat)findViewById(R.id.globalHeaderToggle);
        responseNameEditText = (EditText)findViewById(R.id.responseNameEditText);
        statusCodeEditText = (EditText)findViewById(R.id.statusCodeEditText);
        mockedResponseEditText = (EditText)findViewById(R.id.mockedResponseEditText);



        if(!TextUtils.isEmpty(response.responseName))
            responseNameEditText.setText(response.responseName);
        statusCodeEditText.setText(String.valueOf(response.statusCode));
        if(!TextUtils.isEmpty(response.responseJson))
            mockedResponseEditText.setText(response.responseJson);

        mockerToggle.setChecked(response.responseEnabled);
        mockerToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                response.responseEnabled = isChecked;
                if (isChecked) {
                    //if we are enabling one... disable the rest
                    for (int i = 0; i < scenario.response.size(); i++) {
                        if (responsePosition == i)
                            continue;
                        scenario.response.get(i).responseEnabled = false;
                    }
                }
                MockerInitializer.setUpdateMade(true);
            }
        });

        globalHeaderToggle.setChecked(response.includeGlobalHeader);
        globalHeaderToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                response.includeGlobalHeader = isChecked;
            }
        });


        mockedResponseEditText.addTextChangedListener(mockedResponseWatcher);
        responseNameEditText.addTextChangedListener(responseNameWatcher);
        statusCodeEditText.addTextChangedListener(statusCodeWatcher);

        repsonseHeaderView = findViewById(R.id.repsonseHeaderView);
        repsonseHeaderView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forward = new Intent(MockerResponseActivity.this, MockerHeaderActivity.class);
                forward.putExtra(MockerHeaderActivity.EXTRA_GLOBAL, false);
                forward.putExtra(MockerHeaderActivity.EXTRA_RESPONSE_POSITION, responsePosition);
                forward.putExtra(MockerHeaderActivity.EXTRA_SCENARIO_POSITION, scenarioPosition);
                startActivity(forward);
            }
        });




    }

    @Override
    protected void upNavPressed() {
        //no upNav option here.
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mocker_response, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_delete){
            scenario.response.remove(responsePosition);
            MockerInitializer.setUpdateMade(true);
            finish();
            return true;

        }else{
            return super.onOptionsItemSelected(item);
        }


    }



    private TextWatcher responseNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int t) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!TextUtils.isEmpty(s.toString())){
                response.responseName = s.toString();
            }else{
                response.responseName = "";
            }

            MockerInitializer.setUpdateMade(true);
            setToolbarTitle(response.responseName);
        }
    };

    private TextWatcher statusCodeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!TextUtils.isEmpty(s.toString())){
               response.statusCode = Integer.parseInt(s.toString());
            }else{
                response.statusCode = 0;
            }
        }
    };

    private TextWatcher mockedResponseWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            CharacterStyle[] toBeRemovedSpans = s.getSpans(0, s.length(), MetricAffectingSpan.class);
            for (int i = 0; i < toBeRemovedSpans.length; i++){
                s.removeSpan(toBeRemovedSpans[i]);
            }

            if(!TextUtils.isEmpty(s.toString())){
                response.responseJson = s.toString();
            }else{
                response.responseJson = "";
            }

        }
    };
}
