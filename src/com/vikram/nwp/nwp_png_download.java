package com.vikram.nwp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aeonbits.owner.ConfigFactory;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import com.asprise.ocr.Ocr;
import com.vikram.config.serverconfig;

public class nwp_png_download {
	 static serverconfig config=ConfigFactory.create(serverconfig.class);
	 static ArrayList<String> tempPNGnames=new ArrayList<String>();
	 static  ArrayList<String> ForecastValidTime=new ArrayList<String>();
     static ArrayList<String> CorrectedTime=new ArrayList<String>();
     static ArrayList<String> DateTime=new ArrayList<String>();
     public static void main(String[] args) throws IOException, SAXException, ParseException{
    	   String date ="";
    	   String date1="";
    	   File file=new File(config.temp_folder_nwp()+"temp.txt");
    	   FileWriter fileWriter = new FileWriter(file);
    	   Document document = Jsoup.connect(config.nwp_new_link()).userAgent("chrome").get();
    	   Elements elements = document.getAllElements(); 
    	   for(Element element : elements){
    		   Elements tableElements = element.getElementsByTag("table");
          	   	for(Element element_tr : tableElements){
          	   		Elements tdElements = element_tr.getElementsByTag("td");
          	   		for(Element element_a : tdElements){
          	   			Elements hrefElements = element_a.getElementsByTag("img");
          	   			for(Element element_href : hrefElements){
          	   				fileWriter.write(element_href.attr("src")+",");
          	   			}	
          	   		}
          	   		break;
          	   	}
          	   	break;
    	   }
	        fileWriter.close();
	        FileReader fileString=new FileReader(config.temp_folder_nwp()+"temp.txt");
	        BufferedReader bReader=new BufferedReader(fileString);
	        String[] imageLinks=bReader.readLine().split(",");
	        bReader.close();
	        
	        SimpleDateFormat dateFormat=new SimpleDateFormat("ddMMMYYYY");
		 	date = dateFormat.format(System.currentTimeMillis());
			Calendar c = Calendar.getInstance();  
			c.add(Calendar.DATE, 1);
			date1 = dateFormat.format(c.getTime());
	        File dir=new File(config.temp_folder_nwp());
	        dir.mkdirs();
	        for (String string : imageLinks) {
	           Response resultImageResponse = Jsoup.connect(config.nwp_images_link()+string).ignoreContentType(true).execute();	
	           File temp=new File(config.temp_folder_nwp()+string);
	           FileOutputStream out = new FileOutputStream(temp); 
	           out.write(resultImageResponse.bodyAsBytes());
	           out.close();
	           tempPNGnames.add(string.substring(0, string.length()-4)); 
	         }
	         Pattern ForcastValidTime=Pattern.compile("OO(.*)");
	         for (String string : imageLinks) {
	        	 System.out.println(string);
	        	 Ocr.setUp();
				Ocr ocrObject=new Ocr();
				ocrObject.startEngine("eng",Ocr.SPEED_FASTEST );
				File image=new File(config.temp_folder_nwp()+string);
				String s = ocrObject.recognize(new File[] {image},Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
//				System.out.println(s);
				Matcher ForcastValidTimeMatcher=ForcastValidTime.matcher(s);
				while (ForcastValidTimeMatcher.find()) {
					String str=ForcastValidTimeMatcher.group();
					System.out.println(str);
					ForecastValidTime.add(str);
					String correction=str.replaceAll("O", "0").replaceAll("l", "1").substring(3, str.length()).replaceAll("Z", "2").replaceAll("G", "6");
					CorrectedTime.add(correction);
//					System.out.println("Corrected time = "+correction);
					
				}
	         }
//	         for (String string : CorrectedTime) {
//				System.out.println(string);
//			}
	         String string1="";
	         String reference="";
			try {
//				System.out.println("trying parsing....");
				Integer.parseInt(CorrectedTime.get(0).substring(0, 2));
				reference=CorrectedTime.get(0);
				System.out.println("reference="+reference);
			} catch (Exception e1) {
//				System.out.println("error in parsing.../n trying convert to date");
				
				reference = convertToDate(CorrectedTime.get(0));
				System.out.println("reference="+reference);
			}
	         for (String string : CorrectedTime) {
//	        	 System.out.println( "reference="+reference);
				try {
					Integer.parseInt(string.substring(0,2));
//					System.out.println(reference+"\n\n");
					reference=string;
//					System.out.println(reference);
					DateTime.add(reference);
					createPng(reference,date,date1);
				} catch (Exception e) {
					int refDate=Integer.parseInt(reference.substring(0,2));
					
//					System.out.println(refDate);
					String strDate=string.substring(0,2);
					if(refDate<10){
						string1=string.replaceAll(strDate,"0"+refDate);
						DateTime.add(string1);
						reference=string1;
						createPng(reference, date, date1);
					}else {
						string1=string.replaceAll(strDate,""+refDate);
						DateTime.add(string1);
						reference=string1;
						createPng(reference, date, date1);
					}
					
				}
			}
	       
		}
	     
	 private static void createPng(String reference, String date, String date1) {
		 
//		 System.out.println(reference+"   "+date+"   "+date1);

		 	if(reference.equalsIgnoreCase(date)){
				 date=date_for_folder(date);
				 date1=date_for_folder(date1);
		 		try {
	 			 	String imagename=tempPNGnames.get(DateTime.indexOf(reference));
	 		        File sourceFile = new File(config.temp_folder_nwp()+imagename+".png");
	 		        File dir1 = new File(config.storage_For_nwp()+date);
	 		        dir1.mkdirs();
	 		        File destFile=new File(config.storage_For_nwp()+date+"/"+date+".png");
	 		        copyFile(sourceFile,destFile);
	 		        } catch (IOException ex) {
	 		           ex.printStackTrace();
	 		         }
	         }
	         if(reference.equalsIgnoreCase(date1)){
	        	 try {
	        		 date=date_for_folder(date);
	        		 date1=date_for_folder(date1);
	 			 	String imagename=tempPNGnames.get(DateTime.indexOf(reference));
	 		        File sourceFile = new File(config.temp_folder_nwp()+imagename+".png");
	 		       
//	 		        File dir1 = new File("G:/Vikram-Workspace/KSDMA/NWP/NWP_DATA/"+date);
//	 		        dir1.mkdirs();
	 		        File destFile=new File(config.storage_For_nwp()+date+"/"+date1+".png");
//	 		        System.out.println(sourceFile.getPath()+"    "+destFile.getPath());
	 		        copyFile(sourceFile,destFile);
	 		        } catch (IOException ex) {
	 		           ex.printStackTrace();
	 		         }
			 }
	}
	 private static String date_for_folder(String date){
		 
		 try { 
			 Calendar cal = Calendar.getInstance(); 
			 cal.setTime(new SimpleDateFormat("ddMMMyyyy").parse(date)); 
			 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			return sdf.format(cal.getTime()); 
			 } catch (ParseException e) {
				 e.printStackTrace();
				 }
			 
		 return null;
	 }
 private static String convertToDate(String refDatestr) {
	 String convertedDate="";	 
	 for (String string : CorrectedTime) {
//			System.out.println("processing"+string);	
			 try {
					
//					System.out.println(Integer.parseInt(string.substring(0, 2)));
					
					int datelocation=CorrectedTime.indexOf(string);
					System.out.println("datelocation="+datelocation);
					SimpleDateFormat dateFormat = new SimpleDateFormat( "ddMMMyyyy" );   
					Calendar cal = Calendar.getInstance();    
					cal.setTime( dateFormat.parse(string));    
					cal.add( Calendar.DATE, -datelocation );    
					convertedDate=dateFormat.format(cal.getTime());    
					System.out.println("Date ..."+convertedDate);
					return convertedDate;
				} catch (Exception e) {
					continue;
				}
				
			}
		return convertedDate;
	}
	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File destFile)
		            throws IOException {
		        FileChannel source = null;
		        FileChannel destination = null;
		        try {
		            source = new FileInputStream(sourceFile).getChannel();
		            destination = new FileOutputStream(destFile).getChannel();
		            long count = 0;
		            long size = source.size();
		            while ((count += destination.transferFrom(source, count, size - count)) < size);
		        } finally {
		            source.close();
		            destination.close(); 
		        }
	}	
}
