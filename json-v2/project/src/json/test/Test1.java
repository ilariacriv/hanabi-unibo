package json.test;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;

import java.io.StringReader;

public class Test1
{
	public static void main(String args[]) throws JSONException
	{
		String s = "{\"campo1\":3,\"campo2\":true,\"campo3\":null,\"campo4\":\"aa\nasd\"}";
		JSONObject o = new JSONObject(new StringReader(s));
		JSONArray arr = new JSONArray();
		arr.add("bellaaa\naaaaaa");
		arr.add(false);
		arr.add(null);
		o.put("array", arr);
		JSONObject o1 = new JSONObject(new StringReader("{\"campo1\":1,\"campo2\":\"saluto\"}"));
		arr.add(o1);
		o.put("obj", o1);
		System.out.println(o);

		System.out.println(o.get(String.class,"obj","campo2"));
		System.out.println(o.get("array","1"));
		System.out.println(o.get("array","2"));
		System.out.println(o.get(Byte.class,"obj","campo1").getClass());
	}
}
