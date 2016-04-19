package com.teegarcs.mocker.internals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teegarcs.mocker.R;
import com.teegarcs.mocker.internals.model.MockerDock;
import com.teegarcs.mocker.internals.model.MockerScenario;

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
public class ScenarioRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private MockerDock mockerDock;
    private ScenarioListener listener;
    public ScenarioRecyclerAdapter(Context context, MockerDock mockerDock, ScenarioListener listener){
        this.context = context;
        this.mockerDock = mockerDock;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScenarioHolder(LayoutInflater.from(context).inflate(R.layout.mocker_scenario_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MockerScenario scenario = mockerDock.mockerScenario.get(position);

        ((ScenarioHolder)holder).scenarioName.setText(scenario.serviceName);
        ((ScenarioHolder)holder).scenarioToggle.setOnCheckedChangeListener(null);
        ((ScenarioHolder)holder).scenarioToggle.setChecked(scenario.mockerEnabled);
        ((ScenarioHolder)holder).scenarioToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.scenarioToggle(isChecked, position);
            }
        });

        ((ScenarioHolder)holder).scenarioRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.scenarioSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mockerDock.mockerScenario.size();
    }

    public static final class ScenarioHolder extends ViewHolder{
        private TextView scenarioName;
        private SwitchCompat scenarioToggle;
        private RelativeLayout scenarioRow;

        private ScenarioHolder(View parent){
            super(parent);
            scenarioName = (TextView)parent.findViewById(R.id.scenarioName);
            scenarioToggle = (SwitchCompat)parent.findViewById(R.id.scenarioToggle);
            scenarioRow = (RelativeLayout)parent.findViewById(R.id.scenarioRow);

        }
    }

    public interface ScenarioListener{
        void scenarioToggle(boolean enabled, int pos);
        void scenarioSelected(int pos);
    }
}
