package com.teegarcs.mocker.internals;

import android.support.v4.util.LruCache;

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
public class MockerCacheManager {
    private static final int MAX_DATA_SIZE = 100;
    private LruCache<String, Object> mockerDockCache;

    public MockerCacheManager(){
        mockerDockCache = new LruCache<>(MAX_DATA_SIZE);

    }

    public void putMockerDock(String key, Object data) {
        synchronized (mockerDockCache) {
            mockerDockCache.put(key, data);
        }
    }

    public Object getMockerDock(String key) {
        return mockerDockCache.get(key);
    }


}
