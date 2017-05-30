package com.vikram.NDVI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.util.ArrayList;

import org.aeonbits.owner.ConfigFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vikram.config.serverconfig;
import com.vikram.httpUtility.*;
import com.vikram.internet_speed.InternetConnection;

public class NDVIDataDownload {
	static serverconfig config = ConfigFactory.create(serverconfig.class);
	static Logger log=LoggerFactory.getLogger(NDVIDataDownload.class);
	
	public static void main(String[] args) throws IOException {
		if(new InternetConnection().checkconnection()){
			
			
			File ndviTextPathFile = new File("C:/KSDMA_ESRI/NDVI_DATA/ndviDownloadPaths.txt");
			if (ndviTextPathFile.exists()) {
				ndviTextPathFile.delete();
			}
			System.out.println("\nRetrieving data from " + config.NDVILinkForPresentData() + "...");
			ArrayList<String> yearlist = new ArrayList<String>();
	
			// String link=config.NDVILinkForPreviousData();//for past years data
	
			String link = config.NDVILinkForPresentData();
	
			Document document = Jsoup.connect(link).userAgent("chrome").validateTLSCertificates(false).get();
			Elements elements = document.getAllElements();
			for (Element element : elements) {
				Elements tableElements = element.getElementsByTag("table");
				for (Element element_tr : tableElements) {
					Elements tdElements = element_tr.getElementsByTag("td");
					for (Element element_a : tdElements) {
						Elements hrefElements = element_a.getElementsByTag("a");
						for (Element element_href : hrefElements) {
							String href = element_href.attr("href");
	
							try {
								Integer.parseInt(href.replaceAll("/", ""));
								yearlist.add(href);
							} catch (Exception e) {
								continue;
							}
						}
					}
					break;
				}
				break;
			}
			log.info("downloading Ndvi Data");
			System.out.println(" \nDownloading NDVI Data.... ");
			System.out.println(" \nChecking files....\n ");
	
			processyear(link, yearlist.get(yearlist.size() - 1));
	
			System.out.println(" ______________________________");
			System.out.println("|                              |");
			System.out.println("|  NDVI Data was downloaded    |");
			System.out.println("|______________________________|");
		}else {
			System.out.println("No Internet Connection");
		}

	}

	private static void processyear(String link, String href) throws IOException {
		String week = "";
		Document yeardocument = Jsoup.connect(link + href).userAgent("chrome").validateTLSCertificates(false).get();
		Elements yearelements = yeardocument.getAllElements();
		for (Element yearelement : yearelements) {
			Elements yeartableElements = yearelement.getElementsByTag("table");
			for (Element yearelement_tr : yeartableElements) {
				Elements yeartdElements = yearelement_tr.getElementsByTag("td");
				for (Element yearelement_a : yeartdElements) {
					Elements yearhrefElements = yearelement_a.getElementsByTag("a");
					for (Element yearelement_href : yearhrefElements) {
						String yearhref = yearelement_href.attr("href");

						try {
							Integer.parseInt(yearhref.replaceAll("/", ""));
							week = yearhref;
						} catch (Exception e) {
							continue;
						}
					}
				}
				break;
			}
			break;
		}

		processdays(link, href, week);

	}

	private static void processdays(String link, String href, String yearhref) throws IOException {
		link = link + href;
		String text = "";
		Document yeardocument = Jsoup.connect(link + yearhref).userAgent("chrome").validateTLSCertificates(false).get();
		Elements yearelements = yeardocument.getAllElements();
		for (Element yearelement : yearelements) {
			Elements yeartableElements = yearelement.getElementsByTag("table");
			for (Element yearelement_tr : yeartableElements) {
				Elements yeartdElements = yearelement_tr.getElementsByTag("td");
				for (Element yearelement_a : yeartdElements) {
					Elements yearhrefElements = yearelement_a.getElementsByTag("a");
					for (Element yearelement_href : yearhrefElements) {
						String dayhref = yearelement_href.attr("href");

						if (yearhref.contains("MODIS") || dayhref.contains("MODIS")) {
							continue;
						}
						if (dayhref.contains("x28y08") || dayhref.contains("x28y09")) {
							File file = new File(config.NDVIStorage() + href + yearhref + dayhref);
							if (file.exists()) {
								System.out.println("File '" + dayhref + "' Already exists in " + config.NDVIStorage()+ href + yearhref + "'\n");
								continue;
							}
							System.out.println("Downloading '" + dayhref + "'\n");

							try {
								HTTPDownloadUtility.downloadFile(link + yearhref + dayhref,
										config.NDVIStorage() + href + yearhref, dayhref);
								System.out.println("File Stored in '" + config.NDVIStorage() + href + yearhref + "'\n");
								
							} catch (KeyManagementException e) {
								text = "Something went wrong in Downloading Data...";
								writeDownloadPathinfile(text);
								e.printStackTrace();
							}

						}
					}
				}
				break;
			}
			break;
		}

		text = config.NDVIStorage() + href;
		writeDownloadPathinfile(text);

	}

	private static void writeDownloadPathinfile(String Text) throws IOException {
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			File ndviTextPath = new File(config.NDVITextFileLocation());
			fileWriter = new FileWriter(ndviTextPath);
			if (!ndviTextPath.exists()) {
				ndviTextPath.createNewFile();
			}
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(Text);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedWriter.close();
			fileWriter.close();
		}

	}
}
