package testcode.crypto;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.SymmetricEncryptionConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

public class HazelcastSymmetric {

    IMap<String, String> cacheMap;

    public void init() {

        //Specific map time to live
        MapConfig myMapConfig = new MapConfig();
        myMapConfig.setName("cachetest");
        myMapConfig.setTimeToLiveSeconds(10);

        //Package config
        Config myConfig = new Config();
        myConfig.addMapConfig(myMapConfig);

        //Symmetric Encryption
        SymmetricEncryptionConfig symmetricEncryptionConfig = new SymmetricEncryptionConfig();
        symmetricEncryptionConfig.setAlgorithm("DESede");
        symmetricEncryptionConfig.setSalt("saltysalt");
        symmetricEncryptionConfig.setPassword("lamepassword");
        symmetricEncryptionConfig.setIterationCount(1337);

        //Weak Network config..
        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.setSymmetricEncryptionConfig(symmetricEncryptionConfig);

        myConfig.setNetworkConfig(networkConfig);

        Hazelcast.init(myConfig);

        cacheMap = Hazelcast.getMap("cachetest");
    }

    public void put(String key, String value) {
        cacheMap.put(key, value);
    }

    public String get(String key) {
        return cacheMap.get(key);
    }
}
