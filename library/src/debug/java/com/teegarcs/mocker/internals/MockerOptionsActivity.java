package com.teegarcs.mocker.internals;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.teegarcs.mocker.R;
import com.teegarcs.mocker.internals.adapters.HeaderRecyclerAdapter;
import com.teegarcs.mocker.internals.adapters.HeaderRecyclerAdapter.HeaderListener;
import com.teegarcs.mocker.internals.interactors.OptionsInteractor;
import com.teegarcs.mocker.internals.model.MockerHeader;
import com.teegarcs.mocker.internals.presenters.OptionsPresenter;

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
public class MockerOptionsActivity extends MockerToolbarActivity implements HeaderListener, OptionsInteractor {
    public static final String EXTRA_SCENARIO_POSITION = "EXTRA_SCENARIO_POSITION";
    public static final String EXTRA_RESPONSE_POSITION = "EXTRA_RESPONSE_POSITION";
    public static final String EXTRA_GLOBAL = "EXTRA_GLOBAL";

    private OptionsPresenter presenter;

    private RecyclerView headerRecyclerView;
    private LinearLayoutManager layoutManager;
    private HeaderRecyclerAdapter adapter;
    private FloatingActionButton faButton;

    private SeekBar requestDurationBar;
    private TextView requestDurationValue;
    private View seekBarContainer, divider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocker_header);
        setUpNav(true);
        presenter = new OptionsPresenter(new MockerDataLayer(this), getIntent().getExtras().getBoolean(EXTRA_GLOBAL),
                getIntent().getExtras().getInt(EXTRA_SCENARIO_POSITION, -1), getIntent().getExtras().getInt(EXTRA_RESPONSE_POSITION, -1));
        setBasePresenter(presenter);

        headerRecyclerView = (RecyclerView) findViewById(R.id.headerRecyclerView);
        faButton = (FloatingActionButton) findViewById(R.id.faButton);
        seekBarContainer = findViewById(R.id.request_duration_seekbar_container);
        divider = findViewById(R.id.divider);
        requestDurationBar = (SeekBar) findViewById(R.id.request_duration_seekbar);
        requestDurationValue = (TextView) findViewById(R.id.request_duration_value);


        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        headerRecyclerView.setLayoutManager(layoutManager);


        faButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addHeader();
            }
        });



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
                presenter.setGlobalDuration(seekBar.getProgress());
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
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mocker_home, menu);
        return true;
    }

    @Override
    public void nameChanged(String name, int pos) {
        presenter.updateName(name, pos);
    }

    @Override
    public void valueChanged(String value, int pos) {
       presenter.updateValue(value, pos);
    }

    @Override
    public void deleteHeader(int pos) {
       presenter.removeHeader(pos);
    }

    @Override
    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTitle(String title) {
        setToolbarTitle(title);
    }

    @Override
    public void setHeaders(ArrayList<MockerHeader> headers) {
        adapter = new HeaderRecyclerAdapter(this, headers, this);
        headerRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showGlobalViews() {
        seekBarContainer.setVisibility(View.VISIBLE);
        divider.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRequestDuration(int duration) {
        requestDurationBar.setProgress(duration);
    }

}
