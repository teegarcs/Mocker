# Mocker

A response Mocking framework for Android applications using the OkHTTP3 client.

Mocker allows you to create mocked responses for your requests at runtime without running a special build. Each with the ability 
to be toggled on and off individually so that you may test one specific service call without affecting the rest of your application.
Mocker also gives you the ability to create mocked responses from real requests and responses being made by your application. This is useful
when you need to be sure that you can recreate a scenario over and over again without relying on your service tier to remain unchanged.
All this in a simple to follow UI which is launched by selecting the notification that appears when your application is launched. 

![mocker-home.png](screenshots/mocker-home.png)

## Getting started

*Currently Mocker is not offered as a Jar file so you have to clone the repository locally in order to add it to your application. 
this is temporary and will be offered as a Jar file soon.*

To begin, first clone the Mocker repository to your workstation. 

Next in the application you wish to add Mocker to, open the ```settings.gradle``` file and add the following:

```gradle
  include ':mocker'
  project(':mocker').projectDir = new File(settingsDir, "../mocker/library")
```
Where the settings directory is the path to your mocker project. The /library is needed after the path.  

In your `build.gradle`:
```gradle
 dependencies {
   compile project(path: ':mocker')
 }
```

In your `Application` class:

```java
public class ExampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    MockerInitializer.install(this);
  }
}
```

In your `OkHttpClient` builder:

```java
OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(MockerInitializer.getMockerInterceptor(context));
        builder.addNetworkInterceptor(MockerInitializer.getMatchingInterceptor(context)); //only needed if you wish to match
        ......
        return builder.build();
```

**You're all set!** Mocker will automatically show a notification once your application is launched which,
will be your entry point into Mocker. The notification will automatically dismiss itself once the application is closed. 

## How do I use Mocker?

###Mocker Home

The below screen shot is the entry point into Mocker. From here you can disable all mocked responses or enable Mocker to allow individual 
selections to be made.  You can also select, "Global Header" which are headers attached to every mocked response.  Select any scenario 
to enter into the individual editing or select the add action button to add a new scenario.  

*From every screen you have the ability to turn Mocker Matching on which enables you to create mocked responses based on real requests/responses
made.  To enable Mocker Matching, toggle it on using the overflow menu.  To disable Mocker Matching, open the notification and select the disable action.*

![mocker-home.png](screenshots/mocker-home.png)

###Mocker Scenarios

The below screen shot is where you can edit a specific scenario.  From here you can change the name of your scenario, update the url pattern, 
and create more mocked responses. The to add wild cards to your Url pattern place the following brackets as desired {}. The url pattern 
is what is leveraged in order to match an outgoing request from your application to a mocked response.  From this screen you can also delete 
the scenario via the overflwo menu. 

![mocker-scenario.png](screenshots/mocker-scenario.png)

###Mocked Responses

The below screen shot is where you can edit a specific mocked response.  From here you can change the name of the response, change the status
code, and updated the mocked response coming back.  You also have the ability to adjust whether you want the response to inherit global headers 
and assign response specific headers as desired.  In the overflow menu you will find the ability to delete the response.  

![mocker-response.png](screenshots/mocker-response.png)

###Headers

The below screen shot is where you can edit global headers and response specific headers depending upon your selection. 

![mocker-headers.png](screenshots/mocker-headers.png)

Mocker also gives you the ability to *Share* your entire Mocker setup.  This is useful when you would like to share what you have set up with 
a colleague.  To leverage what one has shared with you, open the Mocker library project in your local repository and find the MockerDock.json 
file located in the assets folder. Paste your shared json here and relaunch.  

##Saving

Mocker has three mechanisms for saving. 1. The local cache (memory), the disk cache, and the default assets file referenced in the last step above.
Any changes you make to your Mocker setup is automatically saved to memory.  However, in order to persist this data you must press save found in the 
overflow menu. If save is not pressed, your changes will be lost.  Once you press save, a disk cache will be made. From here on out Mocker 
will default to using your setup found inside of your disk cache.  If no disk cache is present Mocker uses the MockerDock file found in the 
Assets directory of the Mocker project.  

## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.