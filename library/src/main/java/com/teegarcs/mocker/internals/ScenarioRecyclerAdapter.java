package com.teegarcs.mocker.internals;

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

/**
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

    public static final class ScenarioHolder extends RecyclerView.ViewHolder{
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

    interface ScenarioListener{
        void scenarioToggle(boolean enabled, int pos);
        void scenarioSelected(int pos);
    }
}
