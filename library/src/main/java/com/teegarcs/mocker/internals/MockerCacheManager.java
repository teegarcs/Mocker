package com.teegarcs.mocker.internals;

import android.support.v4.util.LruCache;

/**
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
