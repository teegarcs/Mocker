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
public class ResponseRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private MockerScenario scenario;
    private ResponseListener listener;
    public ResponseRecyclerAdapter(Context context, MockerScenario scenario, ResponseListener listener){
        this.context = context;
        this.scenario = scenario;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScenarioHolder(LayoutInflater.from(context).inflate(R.layout.mocker_scenario_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MockerResponse response = scenario.response.get(position);

        ((ScenarioHolder)holder).scenarioName.setText(response.responseName);
        ((ScenarioHolder)holder).scenarioToggle.setOnCheckedChangeListener(null);
        ((ScenarioHolder)holder).scenarioToggle.setChecked(response.responseEnabled);
        ((ScenarioHolder)holder).scenarioToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.responseToggle(isChecked, position);

            }
        });

        ((ScenarioHolder)holder).scenarioRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.responseSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scenario.response.size();
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

    interface ResponseListener{
        void responseToggle(boolean enabled, int pos);
        void responseSelected(int pos);
    }
}
