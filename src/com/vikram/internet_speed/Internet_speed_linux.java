package com.vikram.internet_speed;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class Internet_speed_linux {

	public static void main(String[] args)  {
		try {
			double startingTime=Double.parseDouble(new SimpleDateFormat("HHmmss").format(System.currentTimeMillis()));
			System.out.println("download started at : "+startingTime+" sec");
			System.out.println("Downloading...");
			Response response=Jsoup.connect("http://speedtest.ftp.otenet.gr/files/test1Mb.db").ignoreContentType(true).execute();
			String fileToStore="/root/Desktop/Test/Test.db";
			FileOutputStream Testfile=new FileOutputStream(fileToStore);
			Testfile.write(response.bodyAsBytes());
			Testfile.close();
			double endingTime=Double.parseDouble(new SimpleDateFormat("hhmmss").format(System.currentTimeMillis()));
			System.out.println("download ended at : "+endingTime+" sec");
			double timetaken=convertIntoSec(endingTime)-convertIntoSec(startingTime);
			System.out.println("time taken to download : "+timetaken);
			System.out.println("Internet speed :"+(1024/timetaken)+" Kb/Sec");
		} catch (IOException e) {
		e.printStackTrace();
		}

	}
	private static double convertIntoSec(double time) {
		double hrs=((int)time/10000)*3600;
		double min=((int)(time%10000)/100)*60;
		System.out.println(min);
		double sec=(time%100);
		double seconds=hrs+min+sec;
		return seconds;
	}

}
