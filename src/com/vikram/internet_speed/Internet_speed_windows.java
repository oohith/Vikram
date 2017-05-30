package com.vikram.internet_speed;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Internet_speed_windows {

	public static void main(String[] args)  {
		Logger logger=LoggerFactory.getLogger(Internet_speed_windows.class);
		try {
			
			//Assigning download start time
			SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss");
			int startingTime=Integer.parseInt(new SimpleDateFormat("HHmmss").format(System.currentTimeMillis()));
			System.out.println("Download started at : "+time.format(System.currentTimeMillis()));
			System.out.println("Downloading...");
			
			//Connect to the link provided 
			
			Response response=Jsoup.connect("http://speedtest.ftp.otenet.gr/files/test1Mb.db").ignoreContentType(true).execute();
			String fileToStore="G:/Vikram-Workspace/Test.db";
			
			//storing the Downloaded file in provided path 
			FileOutputStream Testfile=new FileOutputStream(fileToStore);
			Testfile.write(response.bodyAsBytes());
			Testfile.close();
			
			//assigning the end time
			int endingTime=Integer.parseInt(new SimpleDateFormat("HHmmss").format(System.currentTimeMillis()));
			System.out.println("Download ended at : "+time.format(System.currentTimeMillis()));
			
			//time taken to download file
			double timetaken=convertIntoSec(endingTime)-convertIntoSec(startingTime);
			System.out.println("Time taken to download : "+timetaken+" sec");
			DecimalFormat decimalFormat=new DecimalFormat("0.00");
			
			//gets Internet speed in Kb/sec
			double internet_speed=Double.parseDouble(decimalFormat.format(1024/timetaken));
			logger.info("Internet speed:"+internet_speed+"Kb/sec");
		} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.toString());
		}

	}
	//method to convert hhmmss to seconds
	private static double convertIntoSec(double time) {
		
		double hrs=((int)time/10000)*3600;
		double min=((int)(time%10000)/100)*60;
		double sec=(time%100);
		double seconds=hrs+min+sec;
		return seconds;
	}

}
