package com.teegarcs.mocker.internals;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teegarcs.mocker.MockerInitializer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by cteegarden on 3/1/16.
 */
public class MockerDataLayer {
    public static final String TAG = "MOCKERDATALAYER";
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private Context context;
    private MockerCacheManager cacheManager;


    public MockerDataLayer(Context context){
        this.context = context;
        cacheManager = MockerInitializer.getMockerDataManager();
    }

    public MockerDock getMockerDockData(){
        MockerDock dock = (MockerDock) cacheManager.getMockerDock(MockerInternalConstants.INTERNAL_JSON_STORAGE);

        if(dock!=null)
            return dock;

        String json = readContent(MockerInternalConstants.INTERNAL_JSON_STORAGE);
        if(!TextUtils.isEmpty(json)){
            dock = (MockerDock)convertJsonToObject(MockerDock.class, json);

        }

        if(dock!=null){
            cacheManager.putMockerDock(MockerInternalConstants.INTERNAL_JSON_STORAGE, dock);
            return dock;
        }


        json = readAsset(MockerInternalConstants.INTERNAL_JSON_STORAGE);
        if(!TextUtils.isEmpty(json)){
            dock = (MockerDock)convertJsonToObject(MockerDock.class, json);
        }

        if(dock!=null){
            cacheManager.putMockerDock(MockerInternalConstants.INTERNAL_JSON_STORAGE, dock);
            return dock;
        }


        //if we are here something bad happened
        return null;

    }

    public Object convertJsonToObject(Class<?> clazz, String json){
        if(TextUtils.isEmpty(json))
            return null;

        return gson.fromJson(json, clazz);
    }

    public String convertObjectToJson(Object object){
        return gson.toJson(object);
    }

    /**
     * Util method that will read a file and return string
     *
     * @param fileName
     * @return String
     */
    protected String readContent(String fileName) {
        StringBuilder content = new StringBuilder();
        FileInputStream input = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;

        try {
            input = context.openFileInput(fileName);
            isr = new InputStreamReader(input);
            reader = new BufferedReader(isr);
            String receiveString = "";
            while ((receiveString = reader.readLine()) != null) {
                content.append(receiveString);
            }
        } catch (FileNotFoundException e) {
            // shoot something went wrong
            Log.e(TAG, "Error reading "+fileName+" file", e);
            return null;
        } catch (IOException e) {
            // shoot something went wrong
            Log.e(TAG, "Error reading "+fileName+" file", e);
            return null;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing stream", e);
            }
        }

        return content.toString();
    }


    public String readAsset(String fileName) {
        AssetManager assetManager = context.getAssets();
        StringBuilder content = new StringBuilder();
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            isr = new InputStreamReader(assetManager.open(fileName));
            reader = new BufferedReader(isr);
            String receiveString = "";
            while ((receiveString = reader.readLine()) != null) {
                content.append(receiveString);
            }
        } catch (FileNotFoundException e) {
            // this should never happen
            Log.e(TAG, "Error reading "+fileName+" file", e);
            return null;
        } catch (IOException e) {
            // this should never happen
            Log.e(TAG, "Error reading "+fileName+" file", e);
            return null;
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing stream", e);
            }
        }
        return content.toString();
    }

    /**
     * util method that will take care of the saving process for us
     *
     * @param content
     * @param name
     * @param context
     * @return boolean
     */
    protected boolean saveContent(String content, String name,
                                  Context context) {
        boolean success = false;
        FileOutputStream output = null;
        OutputStreamWriter writer = null;
        try {
            output = context.openFileOutput(name, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(output);
            writer.write(content);
            success = true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Error finding" + name + " file", e);
            success = false;
        } catch (IOException e) {
            Log.e(TAG, "Error writing to" + name + " file", e);
            success = false;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing writer/stream", e);
            }
        }

        return success;
    }




}
