package com.h3xstream.findsecbugs.testcode;
import java.util.Random;


public class InsecureRandom {
	
	String password = "test";
	String username = "test";
	
	public static void test1() {
		Random rand = new Random();
		
		System.out.println(rand.nextInt());
	}
	
	public void test2() {
		
		System.out.println(Math.random());
	}
	
	private void unused() {
		System.out.println(1+2);
	}
}
