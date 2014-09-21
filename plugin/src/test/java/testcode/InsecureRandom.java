package testcode;

import java.security.SecureRandom;
import java.util.Random;

public class InsecureRandom {

    public static void newRandomObj() {
        Random rand = new Random();

        System.out.println(rand.nextInt());
    }

    public static void mathRandom() {
        //Indirectly using Random class
        System.out.println(Math.random());
    }

    public static void mathOther() {
        //Other Math functions unrelated..
        System.out.println(Math.floor(2.5));
        System.out.println(Math.cos(Math.toRadians(30)));
        System.out.println(Math.PI);

        Random random = new SecureRandom();
        random.nextInt(); //This should not raise any warning
    }

    public static void scalaRandom() {
        new scala.util.Random();
        new scala.util.Random(new scala.Long());
    }

    public static void main(String[] args) {
        newRandomObj();
        mathRandom();
        mathOther();
        scalaRandom();
    }
}
