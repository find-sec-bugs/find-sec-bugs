package testcode.gadget;

import testcode.gadget.cachedata.CacheData;

import java.util.HashMap;

public class SuperMap extends HashMap<String,CacheData> {

    @Override
    public CacheData get(Object key) {
        CacheData data = super.get(key);
        data.refresh();
        return data;
    }

}
