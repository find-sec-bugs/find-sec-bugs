package testcode;

import org.apache.commons.lang.math.JVMRandom;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.RandomStringUtils;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class InsecureRandom {
    
    //True positive
    public static void newRandomJVMObj() {
        JVMRandom rand = new JVMRandom();
        System.out.println(rand.nextInt());
    }
    
    //True positive
    public static void newRandomUtilsObj() {
        //All methods are static, hence do not constructing object
        System.out.println(RandomUtils.nextDouble());
    }

    //True positive
    public static void newRandomStrObj() {
        //All methods are static, hence do not constructing object
        System.out.println(RandomStringUtils.random(100));
    }

    //True positive
    public static void newRandomObj() {
        Random rand = new Random();
        System.out.println(rand.nextInt());
    }

    //True positive
    public static void mathRandom() {
        //Indirectly using Random class
        System.out.println(Math.random());
    }

    //True positive if appropriate version
    public static void threadLocalRandom() {
        //Example call to ThreadLocalRandom - random generator since Java 7
        System.out.println(ThreadLocalRandom.current().nextInt(10000));
    }

    //No warnings
    public static void mathOther() {
        //Other Math functions unrelated..
        System.out.println(Math.floor(2.5));
        System.out.println(Math.cos(Math.toRadians(30)));
        System.out.println(Math.PI);

        Random random = new SecureRandom();
        random.nextInt(); //This should not raise any warning
    }

    //True positive PREDICTABLE_RANDOM_SCALA
    public static void scalaRandom() {
        new scala.util.Random();
        new scala.util.Random(new scala.Long());
    }

    //True positive special static nextLong
    public static void staticNextLong() {
        System.out.println(JVMRandom.nextLong(42));
    }
    
    public static void main(String[] args) {
        newRandomJVMObj();
        newRandomUtilsObj();
        newRandomStrObj();       
        newRandomObj();
        mathRandom();
        threadLocalRandom();
        mathOther();
        scalaRandom();
        staticNextLong();
    }
}
