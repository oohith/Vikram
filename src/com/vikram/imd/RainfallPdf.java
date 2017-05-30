package com.vikram.imd;
import java.io.IOException;
import org.aeonbits.owner.ConfigFactory;
import org.apache.tika.exception.TikaException;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import com.vikram.config.serverconfig;
public class RainfallPdf {

	  static serverconfig config=ConfigFactory.create(serverconfig.class);
	       public static void main(String[] args) throws IOException, SAXException, TikaException {
	               String href = "";
	               Document document = Jsoup.connect(config.imd_link()).userAgent("chrome").get();
	               Elements elements = document.getAllElements(); 
	               for(Element element : elements){
	            	   Elements tableElements = element.getElementsByTag("table");
	            	   for(Element element_tr : tableElements){
	            		   Elements tdElements = element_tr.getElementsByTag("td");
	            		   for(Element element_a : tdElements){
	            			   Elements hrefElements = element_a.getElementsByTag("a");
	            			   for(Element element_href : hrefElements){
	            				   // Writing hyper links to the temp file 
	            				   href=element_href.attr("href");
	            				   break;
	            			   }
	            			   break;
	            		   }
	            		   break;
	            	   }
	            	   break;
	               }
	               System.out.println(config.storage_For_imd());
	               SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-YYYY");
	               String date=dateFormat.format(System.currentTimeMillis());
	               
	               Response response=Jsoup.connect(href).ignoreContentType(true).execute();
	               File dir=new File(config.storage_For_imd()+date);
	               dir.mkdirs();
	               File fileToStore = new File(config.storage_For_imd()+date+"/"+date+".pdf");
	               
	               // Save the file 
	               FileOutputStream out = new FileOutputStream(fileToStore);
	               out.write(response.bodyAsBytes());
	               out.close();
	               new Rainfallprediction().rainfall(date);
	               
	       }
	

	
}