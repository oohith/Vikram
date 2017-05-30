package com.vikram.httpUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.KeyManagementException;
 

public class HTTPDownloadUtility {
 
    public static void downloadFile(String fileURL, String saveDir,String filename) throws IOException, KeyManagementException {
    	
        URL url = new URL(fileURL);
        HttpsTrustManager.allowAllSSL();
        File folder=new File(saveDir);
        folder.mkdirs();
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(saveDir+filename);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        
        fos.close();
        

    }
}