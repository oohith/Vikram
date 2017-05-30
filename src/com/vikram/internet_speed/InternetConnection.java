package com.vikram.internet_speed;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class InternetConnection {
	public boolean checkconnection() throws UnknownHostException,IOException {
		boolean connection=false;
		try {
			try {
				URL url = new URL("http://www.google.com");
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.connect();
				if (con.getResponseCode() == 200){
					connection= true;
				}
			} catch (Exception exception) {
				connection= false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
}
