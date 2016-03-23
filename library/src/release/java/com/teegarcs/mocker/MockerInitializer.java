package com.teegarcs.mocker;

import android.app.Application;
import android.content.Context;
import android.util.Log;


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

 * Created by cteegarden on 2/29/16.
 */
public final class MockerInitializer {



    public static void install(Application application, String defaultMockerFile){

    }

    public static void install(final Application application){

    }


    public static void turnOffMatching(Context context){

    }

    public static void turnOnMatching(Context context){

    }

    public static boolean getMockerMatching(){
        return false;
    }

    public static MockerInterceptor getMockerInterceptor(Context context){
       return new MockerInterceptor();
    }

    public static MatchingInterceptor getMatchingInterceptor(Context context){
       return new MatchingInterceptor();
    }

    private static void setDefaultMockerFile(String defaultMockerFile){

    }

    public static String getDefaultMockerFile(){
        return null;
    }
    public static boolean checkforUpdate(){
        return false;
    }

    public static void setUpdateMade(boolean updated){

    }

    /**
     * You can solve it easily using regular expressions:

     if (em1.matches("524..646"))
     for instance.

     (The . is a wildcard that stands for any character. You could replace it with \\d if you like to restrict the wildcard to digits.)

     Here is a more general variant that matches "0" against any character:

     String em1 = "52494646";
     String em2 = "52400646";

     if (em1.matches(em2.replaceAll("0", "\\\\d"))){
     System.out.println("Matches");
     }
     */
}
