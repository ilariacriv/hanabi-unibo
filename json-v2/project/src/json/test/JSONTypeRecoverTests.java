package json.test;


import json.JSONUtils;

import java.io.IOException;
import java.io.StringReader;

public class JSONTypeRecoverTests
{
	public static void main(String args[]) throws IOException
	{
		double d1 = 123.001d;
		double d2 = 5d;
		String s1 = "123.001";
		String s2 = "5.0";
		String s3 = "1,2";
		String s4 = "10 ";
		String s5 = " 54";
		String s6 = "1a";
		String s7 = "a1";

		StringReader sr = new StringReader(s1);
		System.out.println(JSONUtils.recoverNumber(sr)[0].substring(0,s1.length()));

		sr = new StringReader(s2);
		System.out.println(JSONUtils.recoverNumber(sr)[0].substring(0,s2.length()));

		sr = new StringReader(s3); //Questo dovrebbe smettere di leggere dopo la virgola
		System.out.println(JSONUtils.recoverNumber(sr)[0].substring(0,1));

		sr = new StringReader(s4);
		System.out.println(JSONUtils.recoverNumber(sr)[0].substring(0,2));

		sr = new StringReader(s5);
		System.out.println(JSONUtils.recoverNumber(sr)[0].substring(0,2));

		sr = new StringReader(s6);
		System.out.println(JSONUtils.recoverNumber(sr)[0].substring(0,1));

		sr = new StringReader(s7);
		System.out.println(JSONUtils.recoverNumber(sr)[0]);
	}

}
