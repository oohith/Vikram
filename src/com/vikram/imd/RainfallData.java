package com.vikram.imd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RainfallData extends Rainfallprediction {

	public void PdfData(String pdfdata) {
		String dayRegExp="\\bDAY(.*?)[0-5]\\b";
		String dateRegExp1="\\b[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]\\b";
		String dateRegExp2="\\b[0-9][0-9]/[0-9][0-9]\\b";
		String dateRegExp3="\\b[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]\\b";
		String districtRegExp="\\bDISTRICT :(.*?)[A-Z]\\b";
		String rainfallRegExp="\\bRainfall(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+\\b";
		String maxTemperatureRegExp="\\bMax Temperature(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+\\b";
		String minTemperatureRegExp="\\bMin Temperature(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+\\b";
		String totalCloudCoverRegExp="\\bTotal cloud cover(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+\\b";
		String maxRelativeHumidityRegExp="\\bMax Relative Humidity(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+\\b";
		String minRelativeHumidityRegExp="\\bMin Relative Humidity(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+\\b";
		String windSpeedRegExp="\\bWind speed(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+\\b";
		String windDirectionRegExp="\\bWind direction(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+(.*?)\\d+\\b";
		
		//Regular Expressions to regex
		Pattern day_pattern=Pattern.compile(dayRegExp);
		Pattern date1_pattern=Pattern.compile(dateRegExp1);
		Pattern date2_pattern=Pattern.compile(dateRegExp2);
		Pattern date3_pattern=Pattern.compile(dateRegExp3);
		Pattern district_pattern=Pattern.compile(districtRegExp);
		Pattern rainfall_pattern=Pattern.compile(rainfallRegExp);
		Pattern maxTemprature_pattern=Pattern.compile(maxTemperatureRegExp);
		Pattern minTemperature_pattern=Pattern.compile(minTemperatureRegExp);
		Pattern totalCloudCovered_pattern=Pattern.compile(totalCloudCoverRegExp);
		Pattern maxRelativeHumidity_pattern=Pattern.compile(maxRelativeHumidityRegExp);
		Pattern minRelativeHumidity_pattern=Pattern.compile(minRelativeHumidityRegExp);
		Pattern windSpeed_pattern=Pattern.compile(windSpeedRegExp);
		Pattern windDirection_pattern=Pattern.compile(windDirectionRegExp);
		
		
		int noOfDays=noOFDays(day_pattern,pdfdata);
		int noOfDistricts=noOfDistricts(district_pattern,pdfdata);
		String[] dateRegExp1List=getDaysAndDatesData(date1_pattern,pdfdata,noOfDays);
		String[] dateRegExp2List=getDaysAndDatesData(date2_pattern,pdfdata,noOfDays);
		String[] dateRegExp3List=getDaysAndDatesData(date3_pattern,pdfdata,noOfDays);
		String[] districtsList=districtsData(district_pattern,pdfdata,noOfDistricts);
		String[] daysList=getDaysAndDatesData(day_pattern,pdfdata,noOfDays);
		String [][]rainfallData=respectiveFieldData(rainfall_pattern,pdfdata,noOfDays,noOfDistricts,"Rainfall (mm)","Rainfall");
		String [][]maxTempratureData=respectiveFieldData(maxTemprature_pattern,pdfdata,noOfDays,noOfDistricts,"Max Temperature ( deg C)","Max Temperature");
		String [][]minTempratureData=respectiveFieldData(minTemperature_pattern,pdfdata,noOfDays,noOfDistricts,"Min Temperature ( deg C)","Min Temperature");
		String [][]totalCloudCoveredData=respectiveFieldData(totalCloudCovered_pattern,pdfdata,noOfDays,noOfDistricts,"Total cloud cover (octa)","Total cloud cover");
		String [][]maxRelativeHumidityData=respectiveFieldData(maxRelativeHumidity_pattern,pdfdata,noOfDays,noOfDistricts,"Max Relative Humidity (%)","Max Relative Humidity");
		String [][]minRelativeHumidityData=respectiveFieldData(minRelativeHumidity_pattern,pdfdata,noOfDays,noOfDistricts,"Min Relative Humidity (%)","Min Relative Humidity");
		String [][]windDirectionData=respectiveFieldData(windDirection_pattern,pdfdata,noOfDays,noOfDistricts,"Wind direction (deg)","Wind direction");
		String [][]windSpeedData=respectiveFieldData(windSpeed_pattern,pdfdata,noOfDays,noOfDistricts,"Wind speed (kmph)","Wind speed");
	
		
		for (int days = 0; days < noOfDays; days++) {
			for (int districts = 0; districts < noOfDistricts; districts++) {
				System.out.print(" District : "+districtsList[districts]);
				System.out.print(" Day : "+daysList[days]);
				if(dateRegExp1List[days]!=null){
					System.out.print(" Date : "+dateRegExp1List[days]);
				}else if (dateRegExp2List[days]!=null) {
					System.out.print(" Date : "+dateRegExp2List[days]);
				}else{
					System.out.print(" Date : "+dateRegExp3List[days]);
				}
				System.out.print(" Rainfall : "+ rainfallData[districts][days]);
				System.out.print(" Max Tempereture : "+ maxTempratureData[districts][days]);
				System.out.print(" Min Tempereture : "+ minTempratureData[districts][days]);
				System.out.print(" Total Cloud Covered : "+ totalCloudCoveredData[districts][days]);
				System.out.print(" maxRelativeHumidity : "+ maxRelativeHumidityData[districts][days]);
				System.out.print(" minRelativeHumidity : "+ minRelativeHumidityData[districts][days]);
				System.out.print(" Wind Direction : "+ windDirectionData[districts][days]);
				System.out.println(" Wind Speed : "+ windSpeedData[districts][days]);
			}
		}
		
	}
	
	//Method to return the Respective Field data in 2-D(14*5) Array format
	
	private String[][] respectiveFieldData(Pattern pattern, String pdfdata, int noOfDays, int noOfDistricts, String respectiveElement1, String respectiveElement2) {
		Matcher matcher=pattern.matcher(pdfdata);
		int days=0;
		int districts=0;
		String[][] RespectiveFielddata=new String[noOfDistricts][noOfDays];
		while(matcher.find()){
			if(matcher.group().contains(respectiveElement2)){
				String[] RespectiveFieldStr = matcher.group().replaceAll(respectiveElement1, "").split("\n");
				for(int count=0;count<RespectiveFieldStr.length;count++){
					if(districts<noOfDistricts){
						String[] RespectiveFieldValues = RespectiveFieldStr[count].split(" ");
						for(int counter=0;counter<RespectiveFieldValues.length;counter++){
								if(RespectiveFieldValues[counter].length()>0){
									try{
										Integer.parseInt(RespectiveFieldValues[counter]);
									}catch(Exception e){
										continue;
									}
									RespectiveFielddata[districts][days]=RespectiveFieldValues[counter].trim();
									days++;
									if(days==noOfDays){
										days=0;
										break;
									}
								}
						}
						districts++;
					}
					if(districts==noOfDistricts){
						break;
					}
				}
			}
		}
		return RespectiveFielddata;
		}
	
	//Method to return the Days and Dates in 1-D Array format
	
	private String[] getDaysAndDatesData(Pattern day_pattern, String pdfdata, int noOfDays) {
		String[] daysList=new String[noOfDays];
		int days=0;
			Matcher matcher=day_pattern.matcher(pdfdata);
			while (matcher.find()) {
				String[] daystr=matcher.group().split("\n");
				for (int i = 0; i < daystr.length; i++) {
					daysList[days]=daystr[i].trim();
					days++;
					if(days==5){
						break;
					}
				}
			}
		return daysList;
	}
	
	//Method to return the Districts Names in 1-D Array format
	
	private String[] districtsData(Pattern district_pattern, String pdfdata, int noOfDistricts) {
		String[] districtsList=new String[noOfDistricts];
		int dis=0;
			Matcher matcher=district_pattern.matcher(pdfdata);
			while (matcher.find()) {
				String[] districtstr=matcher.group().split(":");
				for (int i = 0; i < districtstr.length; i++) {
					if(dis==noOfDistricts){
						break;
					}
					districtsList[dis]=districtstr[1].trim();
				}
				dis++;
			}
		return districtsList;
	}
	
	//Method used to get number of Days
	
	private int noOFDays(Pattern Days_pattern,String pdfdata) {
		Matcher days=Days_pattern.matcher(pdfdata);
		int noOfDays=0;
		while (days.find()) {
			noOfDays++;
		}
		return noOfDays;
	}
	
	//Method used to get total number of Districts
	
	private int noOfDistricts(Pattern Districts_pattern,String pdfdata) {
		Matcher districts=Districts_pattern.matcher(pdfdata);
		int noOfDistricts=0;
		while (districts.find()) {
			noOfDistricts++;
		}
		return noOfDistricts;
	}
}
