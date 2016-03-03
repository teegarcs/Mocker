package com.teegarcs.mocker;

import com.teegarcs.mocker.internals.MockerDataLayer;
import com.teegarcs.mocker.internals.MockerDock;
import com.teegarcs.mocker.internals.MockerHeader;
import com.teegarcs.mocker.internals.MockerResponse;
import com.teegarcs.mocker.internals.MockerScenario;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import okio.Okio;
import okio.Source;

/**
 * Created by cteegarden on 3/1/16.
 */
public class MockerInterceptor implements Interceptor {
    private MockerDock mockerDock;
    private MockerDataLayer dataLayer;
    public MockerInterceptor(MockerDataLayer dataLayer){
        this.dataLayer = dataLayer;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        this.mockerDock = dataLayer.getMockerDockData();
        if(mockerDock.mockerDisabled || MockerInitializer.getMockerMatching()){
            //entire mocks are disabled... return the normal response
            return chain.proceed(request);
        }else{
            //lets see if we can find a specific scenario..
            MockerScenario scenario = null;
            for(MockerScenario mockerScenario : mockerDock.mockerScenario){
                String urlPattern = mockerScenario.urlPattern.replace("{}", "[^<>#]+");
                if(urlPattern.matches(request.url().toString()) || urlPattern.equalsIgnoreCase(request.url().toString())){
                    //we found a match...
                    scenario = mockerScenario;
                    break;

                }
            }

            if(scenario!=null && scenario.mockerEnabled && scenario.response!=null && scenario.response.size()>0){
                //now we need to find the response to use..
                MockerResponse mockerResponse = null;
                for(MockerResponse response : scenario.response){
                    if(response.responseEnabled){
                        mockerResponse = response;
                        break;
                    }
                }
                //for some reason they were all false.. use first one..
                if(mockerResponse==null)
                   mockerResponse = scenario.response.get(0);

                InputStream stream = new ByteArrayInputStream(mockerResponse.responseJson.getBytes(Charset.forName("UTF-8")));
                Buffer buffer = new Buffer().readFrom(stream);
                ArrayList<String> headers =new ArrayList<>();
                if(mockerResponse.includeGlobalHeader){
                    for(MockerHeader globalHeader : mockerDock.globalHeaders){
                        headers.add(globalHeader.headerName);
                        headers.add(globalHeader.headerValue);
                    }
                }

                for(MockerHeader mockerHeader : mockerResponse.responseHeaders) {
                    headers.add(mockerHeader.headerName);
                    headers.add(mockerHeader.headerValue);
                }
                RealResponseBody body = new RealResponseBody(Headers.of(headers.toArray(new String[headers.size()])), Okio.buffer((Source) buffer));


                okhttp3.Response fakeResponse = new Response.Builder()
                        .request(request)
                        .priorResponse(null)
                        .protocol(Protocol.HTTP_1_1)
                        .code(mockerResponse.statusCode)
                        .headers(Headers.of(headers.toArray(new String[headers.size()])))
                        .body(body)
                        .build();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                chain.proceed(request).body().close();
                return fakeResponse;

            }
        }
        return chain.proceed(request);
    }
}
