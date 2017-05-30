package com.vikram.nwpexample;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class test {
public static void main(String[] args) {
	Map<String,String> districtsList=new HashMap<String,String>();
	districtsList.put("A", "1");
	districtsList.put("B", "2");
	districtsList.put("C", "3");
	districtsList.put("D", "4");
	Iterator<String> itr=districtsList.keySet().iterator();
	while (itr.hasNext()) {
	String key=itr.next().toString();
	System.out.println(key +"  "+districtsList.get(key) );
	}
}
}
