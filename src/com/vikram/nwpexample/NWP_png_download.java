package com.vikram.nwpexample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.jsoup.Connection.Response;
import org.aeonbits.owner.ConfigFactory;
import org.jsoup.Jsoup;
import com.vikram.config.*;
public class NWP_png_download {
	static serverconfig config=ConfigFactory.create(serverconfig.class);
	public static void main(String[] args) {
		try {
			Response response=Jsoup.connect(config.nwp_old_link()).ignoreContentType(true).execute();
			File file=new File(config.storage_For_nwp_od());
			String date=new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss").format(System.currentTimeMillis());
			String date1=new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis());
			File dir=new File(file+"/"+date1);
			dir.mkdirs();
			String png_file_location=dir+"/"+date+".png";
			FileOutputStream fos=new FileOutputStream(png_file_location);
			fos.write(response.bodyAsBytes());
			fos.close();
			new NWP_prediction().NWP_prediction_work(png_file_location);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
