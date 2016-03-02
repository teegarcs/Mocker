package com.teegarcs.mocker.internals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.teegarcs.mocker.MockerInitializer;
import com.teegarcs.mocker.R;

/**
 * Created by cteegarden on 3/1/16.
 */
public abstract class MockerToolbarActivity extends AppCompatActivity {
    private static final String EXTRA_TOOLBAR_TITLE = "EXTRA_TOOLBAR_TITLE";
    private Toolbar toolbar;
    private String toolbarTitle;
    private boolean upNav = false;
    protected MockerDataLayer dataLayer;
    protected MockerDock mockerDock;

    protected abstract void upNavPressed();

    protected void setWindowTransitions() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            toolbarTitle = savedInstanceState.getString(EXTRA_TOOLBAR_TITLE);
        } else {
            toolbarTitle = getString(R.string.app_name);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setWindowTransitions();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upNav);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_TOOLBAR_TITLE, toolbarTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            dataLayer.saveContent(dataLayer.convertObjectToJson(mockerDock), MockerInternalConstants.INTERNAL_JSON_STORAGE, this);
        }else if (id == R.id.action_share) {
            String tempdata = dataLayer.convertObjectToJson(mockerDock);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, tempdata);
            startActivity(sendIntent);
            return true;
        }else if(id == R.id.action_matching){
            MockerInitializer.turnOnMatching(this);
        }else if(id == android.R.id.home){
            upNavPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        view.setLayoutParams(params);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        ViewGroup drawerLayout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.toolbar_activity, null);
        ViewGroup contentView = (ViewGroup) drawerLayout.findViewById(R.id.content_view);
        contentView.addView(view);
        super.setContentView(drawerLayout);
    }

    public Toolbar getToolbar() {
        if (toolbar == null)
            toolbar = (Toolbar) findViewById(R.id.toolbar);

        return toolbar;
    }

    public void setToolbarTitle(String title) {
        toolbarTitle = title;
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public void setUpNav(boolean upNav) {
        this.upNav = upNav;
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(upNav);
        }
    }
}