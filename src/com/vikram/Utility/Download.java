package com.vikram.Utility;

import java.io.IOException;
import java.security.KeyManagementException;

import com.vikram.httpUtility.HTTPDownloadUtility;

public class Download {
	public static void main(String[] args) {
		String source="http://repo1.maven.org/maven2/org/glassfish/jersey/bundles/jaxrs-ri/2.25.1/jaxrs-ri-2.25.1.zip";
		String Destination="F:/softwares/JAVA jar";
		System.out.println("Downloading data from"+source);
		try {
			new HTTPDownloadUtility();
			HTTPDownloadUtility.downloadFile(source, Destination, "jaxrs-ri-2.25.1.zip");
		} catch (KeyManagementException | IOException e) {
			
			e.printStackTrace();
		}
		System.out.println("File Stored in "+Destination);
	}

}
