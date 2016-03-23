package com.teegarcs.mocker;

import com.teegarcs.mocker.internals.MockerDataLayer;
import com.teegarcs.mocker.internals.MockerDock;
import com.teegarcs.mocker.internals.MockerHeader;
import com.teegarcs.mocker.internals.MockerResponse;
import com.teegarcs.mocker.internals.MockerScenario;
import com.teegarcs.mocker.internals.RequestType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

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

 * Created by cteegarden on 3/2/16.
 */
public class MatchingInterceptor implements Interceptor {
    private MockerDock mockerDock;
    private MockerDataLayer dataLayer;
    public MatchingInterceptor(MockerDataLayer dataLayer){
        this.dataLayer = dataLayer;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        this.mockerDock = dataLayer.getMockerDockData();
        if(MockerInitializer.getMockerMatching()){
            Response response = chain.proceed(request);
            MockerScenario scenario = new MockerScenario();
            scenario.requestType = RequestType.GET;
            scenario.mockerEnabled = false;
            scenario.serviceName = request.url().toString();
            scenario.urlPattern = request.url().toString();
            MockerResponse mockerResponse = new MockerResponse();
            mockerResponse.responseEnabled = false;
            mockerResponse.includeGlobalHeader = true;
            mockerResponse.statusCode = response.code();
            mockerResponse.responseName = String.valueOf(response.code());
            //mockerResponse.responseJson = response.body().toString();
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = response.body().contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(charset);
                    mockerResponse.responseJson = (buffer.clone().readString(charset));
                } catch (UnsupportedCharsetException e) {
                    return response;
                }
            }
            Headers headers = response.headers();
            for(int i =0; i<headers.size(); i++){
                MockerHeader mockerHeader = new MockerHeader();
                mockerHeader.headerName = headers.name(i);
                mockerHeader.headerValue = headers.value(i);
                mockerResponse.responseHeaders.add(mockerHeader);
            }
            scenario.response.add(mockerResponse);
            mockerDock.mockerScenario.add(scenario);
            return response;

        }else {
            return chain.proceed(request);
        }

    }
}

