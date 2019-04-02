package testcode.gadget;

import testcode.gadget.cachedata.SpecialCacheData;

public class MaliciousPayload {
    
    public static void main(String[] args) {
//        SuperMap map = new SuperMap();
//        map.put("1234",new FileCacheData("C:/Code/secret.txt"));
//        System.out.println(map.get("1234").getValue());

        SuperMap map = new SuperMap();
        map.put("1234",new SpecialCacheData("calc.exe"));
        System.out.println(map.get("1234").getValue());
    }

}
