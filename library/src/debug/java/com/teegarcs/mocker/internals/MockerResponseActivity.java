package com.teegarcs.mocker.internals;

import android.content.Intent;
import android.os.Bundle;
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
import com.teegarcs.mocker.internals.interactors.ResponseInteractor;
import com.teegarcs.mocker.internals.model.MockerResponse;
import com.teegarcs.mocker.internals.presenters.ResponsePresenter;

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
public class MockerResponseActivity extends MockerToolbarActivity implements ResponseInteractor{
    public static final String EXTRA_SCENARIO_POSITION = "EXTRA_SCENARIO_POSITION";
    public static final String EXTRA_RESPONSE_POSITION = "EXTRA_RESPONSE_POSITION";
    private SwitchCompat mockerToggle, globalHeaderToggle;
    private EditText responseNameEditText, statusCodeEditText, mockedResponseEditText;
    private View responseHeaderView;

    private ResponsePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocker_response);
        presenter = new ResponsePresenter(new MockerDataLayer(this),getIntent().getExtras().getInt(EXTRA_SCENARIO_POSITION),
                getIntent().getExtras().getInt(EXTRA_RESPONSE_POSITION));
        setBasePresenter(presenter);
        setUpNav(true);

        mockerToggle = (SwitchCompat)findViewById(R.id.mockerToggle);
        globalHeaderToggle = (SwitchCompat)findViewById(R.id.globalHeaderToggle);
        responseNameEditText = (EditText)findViewById(R.id.responseNameEditText);
        statusCodeEditText = (EditText)findViewById(R.id.statusCodeEditText);
        mockedResponseEditText = (EditText)findViewById(R.id.mockedResponseEditText);
        responseHeaderView = findViewById(R.id.repsonseHeaderView);

        mockedResponseEditText.addTextChangedListener(mockedResponseWatcher);
        responseNameEditText.addTextChangedListener(responseNameWatcher);
        statusCodeEditText.addTextChangedListener(statusCodeWatcher);

        mockerToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.toggleResponse(isChecked);
            }
        });


        globalHeaderToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.toggleGlobalHeader(isChecked);
            }
        });



        responseHeaderView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forward = new Intent(MockerResponseActivity.this, MockerOptionsActivity.class);
                forward.putExtra(MockerOptionsActivity.EXTRA_GLOBAL, false);
                forward.putExtra(MockerOptionsActivity.EXTRA_RESPONSE_POSITION, presenter.getResponsePos());
                forward.putExtra(MockerOptionsActivity.EXTRA_SCENARIO_POSITION, presenter.getScenarioPos());
                startActivity(forward);
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
            presenter.deleteResponse();
            finish();
            return true;

        }else if(id == R.id.action_duplicate){
            presenter.duplicateResponse();
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



    private TextWatcher responseNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int t) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            presenter.updateResponseName(s.toString());
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
            presenter.updateStatusCode(s.toString());
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
            presenter.updateResponseJson(s);
        }
    };

    @Override
    public void setResponse(MockerResponse response) {
        setToolbarTitle(response.responseName);

        if(!TextUtils.isEmpty(response.responseName))
            responseNameEditText.setText(response.responseName);
        statusCodeEditText.setText(String.valueOf(response.statusCode));
        if(!TextUtils.isEmpty(response.responseJson))
            mockedResponseEditText.setText(response.responseJson);

        mockerToggle.setChecked(response.responseEnabled);
        globalHeaderToggle.setChecked(response.includeGlobalHeader);
    }

    @Override
    public void setTitle(String title) {
        setToolbarTitle(title);
    }
}
