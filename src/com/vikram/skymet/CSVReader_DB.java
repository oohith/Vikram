package com.vikram.skymet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.aeonbits.owner.ConfigFactory;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.vikram.config.serverconfig;

public class CSVReader_DB {
	static serverconfig config=ConfigFactory.create(serverconfig.class);
	public static void doWork(String fileName) throws Exception
	{
		Connection con=DriverManager.getConnection(config.jtdsDriver(),config.sqlUsername(),config.sqlPassword());
		BufferedReader bufferreader = null;
		int s_No=0;
		PreparedStatement selectforsno=con.prepareStatement("SELECT * FROM dbo.Skymet_Rainfall_Prediction");
		ResultSet rs_selectforsno=selectforsno.executeQuery();
		ArrayList<String> stations=new ArrayList<String>();
		ArrayList<String> dates=new ArrayList<String>();
		while (rs_selectforsno.next()) {	
			s_No++;
			stations.add(rs_selectforsno.getString(3));
			dates.add(rs_selectforsno.getString(11));
		}
//		System.out.println(s_No);
		try{
			try{
				if(!new File(fileName).exists()){
					throw new FileNotFoundException("No such file:"+fileName);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Processig .csv sheet at :"+fileName);
			bufferreader = new BufferedReader(new FileReader(fileName));
			String line = bufferreader.readLine();	
			String linesFromsecondLine=null;
			String[] firstLine = line.split(",");
			linesFromsecondLine = bufferreader.readLine();
			int s_No_for_Insert=0;
				while (true) 
				{
					String[] linetokens=null;
					if(linesFromsecondLine!=null){
						linetokens = linesFromsecondLine.split(","); 
						String jsonObjectString = createJson(firstLine, linetokens);
						JSONObject jsonObject = new JSONObject(jsonObjectString);
//						System.out.println(jsonObject);
						PreparedStatement select=con.prepareStatement("SELECT * FROM dbo.Skymet_Rainfall_Prediction");
						ResultSet resultSet=select.executeQuery();
						if (s_No==0) {
							//If the table to insert has no rows in it 
							PreparedStatement insert = null;
							try{
								insert=con.prepareStatement("INSERT INTO dbo.Skymet_Rainfall_Prediction "
										+ "(SNo,StationName,StationCode,DistrictName,DistrictCode,TehsilName,TehsilCode,LocationName,LocationCode,"
										+ "Date,Max_Temp,Min_Temp,Humidity,Humidity_1,Max_Wind,Cumulative_Rain)"
										+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
								
								insert.setInt(1, ++s_No_for_Insert);
								insert.setString(2, jsonObject.get("Station Name").toString());
								insert.setString(3, jsonObject.get(" Station Code ").toString());
								insert.setString(4, jsonObject.get("District Name").toString());
								insert.setString(5, jsonObject.get("District Code").toString());
								insert.setString(6, jsonObject.get("Tehsil Name").toString());
								insert.setString(7, jsonObject.get("Tehsil Code").toString());
								insert.setString(8, jsonObject.get("Location Name").toString());
								insert.setString(9, jsonObject.get("Location Code").toString());
								insert.setDate(10, getTimeStamp(jsonObject.get("Date").toString()));
								insert.setFloat(11, Float.parseFloat(jsonObject.get("Max_Temp").toString()));
								insert.setFloat(12, Float.parseFloat(jsonObject.get("Min_Temp").toString()));
								insert.setFloat(13, Float.parseFloat(jsonObject.get("Humidity").toString()));
								insert.setFloat(14, Float.parseFloat(jsonObject.get("Humidity_1").toString()));
								insert.setFloat(15, Float.parseFloat(jsonObject.get("Max_Wind").toString()));
								insert.setFloat(16, Float.parseFloat(jsonObject.get("Cumulative_Rain").toString()));
								System.out.println("inserting for the 1st time");
								con.setAutoCommit(false);
					    		// Executing the insert query
					    		insert.executeUpdate();
					    		// Commiting the transaction
					    		con.commit();
					    		// Setting auto commit as 'TRUE'
					    		con.setAutoCommit(true);
					    		// Closing the statement
					    		insert.close();		    		
								
							}catch (Exception e) {
								e.printStackTrace();
							}finally {
								insert.close();
							}
						}
						if(s_No>0){
							int result=0;
							int count=0;
							while (resultSet.next()) {
								PreparedStatement updateQuery = null;
								// to update the table 
								try{
//							System.out.println(stations.get(count)+(jsonObject.get("Station Name").toString())+(dates.get(count)).equals(getdateformate(jsonObject.get("Date").toString()))+count);
									if(stations.get(count).equals(jsonObject.get("Station Name").toString())&&(dates.get(count)).equals(getdateformate(jsonObject.get("Date").toString()))){
										updateQuery=con.prepareStatement("UPDATE dbo.Skymet_Rainfall_Prediction SET Max_Temp=?,Min_Temp=?,Humidity=?,Humidity_1=?,Max_Wind=?,Cumulative_Rain=? WHERE "
																		+ " StationName='"+jsonObject.get("Station Name").toString()+"' AND Date='"+getTimeStamp(jsonObject.get("Date").toString())+"'");
										updateQuery.setFloat(1, Float.parseFloat(jsonObject.get("Max_Temp").toString()));
										updateQuery.setFloat(2, Float.parseFloat(jsonObject.get("Min_Temp").toString()));
										updateQuery.setFloat(3, Float.parseFloat(jsonObject.get("Humidity").toString()));
										updateQuery.setFloat(4, Float.parseFloat(jsonObject.get("Humidity_1").toString()));
										updateQuery.setFloat(5, Float.parseFloat(jsonObject.get("Max_Wind").toString()));
										updateQuery.setFloat(6, Float.parseFloat(jsonObject.get("Cumulative_Rain").toString()));
										result=updateQuery.executeUpdate();
										if(result==1){
											System.out.println("updated");
												break;
										}	
									}
								}catch (Exception e) {
									
								}finally {
									count++;
								}
							}
//							System.out.println("update="+result);
							if(result==0){
										try{			
											//To insert the data if the same date or station name doesn't exist 
											
											PreparedStatement insert=con.prepareStatement("INSERT INTO dbo.Skymet_Rainfall_Prediction "
													+ "(SNo,StationName,StationCode,DistrictName,DistrictCode,TehsilName,TehsilCode,LocationName,LocationCode,"
													+ "Date,Max_Temp,Min_Temp,Humidity,Humidity_1,Max_Wind,Cumulative_Rain)"
													+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
											insert.setInt(1, ++s_No_for_Insert);
											insert.setString(2, jsonObject.get("Station Name").toString());
											insert.setString(3, jsonObject.get(" Station Code ").toString());
											insert.setString(4, jsonObject.get("District Name").toString());
											insert.setString(5, jsonObject.get("District Code").toString());
											insert.setString(6, jsonObject.get("Tehsil Name").toString());
											insert.setString(7, jsonObject.get("Tehsil Code").toString());
											insert.setString(8, jsonObject.get("Location Name").toString());
											insert.setString(9, jsonObject.get("Location Code").toString());
											insert.setDate(10, getTimeStamp(jsonObject.get("Date").toString()));
											insert.setFloat(11, Float.parseFloat(jsonObject.get("Max_Temp").toString()));
											insert.setFloat(12, Float.parseFloat(jsonObject.get("Min_Temp").toString()));
											insert.setFloat(13, Float.parseFloat(jsonObject.get("Humidity").toString()));
											insert.setFloat(14, Float.parseFloat(jsonObject.get("Humidity_1").toString()));
											insert.setFloat(15, Float.parseFloat(jsonObject.get("Max_Wind").toString()));
											insert.setFloat(16, Float.parseFloat(jsonObject.get("Cumulative_Rain").toString()));
											con.setAutoCommit(false);
											System.out.println("inserted new rows");
								    		// Executing the insert query
											insert.executeUpdate();
								    		// Committing the transaction
								    		con.commit();
								    		// Setting auto commit as 'TRUE'
								    		con.setAutoCommit(true);
								    		// Closing the statement
								    		insert.close();		    		
										}catch (Exception e) {
											e.printStackTrace();
										}
								}
								
							}
						}
						if(linesFromsecondLine==null){
							break;
						}
						linesFromsecondLine = bufferreader.readLine();
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally {
				bufferreader.close();
				con.close();
			} 
	}
	private static String getdateformate(String string) throws ParseException {
		String dateString1 = string;
		Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString1);
		String dateString2 = new SimpleDateFormat("yyyy-MM-dd").format(date);

		return dateString2;
	}
	public static java.sql.Date getTimeStamp(String sqldate){
		java.sql.Date sql=null;
		try{
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		    java.util.Date parsed = format.parse(sqldate);
		    sql = new java.sql.Date(parsed.getTime());
		}catch(Exception e){
			e.printStackTrace();
		}
		return sql;
	}
	public static String createJson(String[] firstLine, String[] linetokens)
	{
		JsonObject obj = new JsonObject(); 
		try{
			for(int i=0;i<linetokens.length;i++){
				for(int j=0;j<firstLine.length;j++)
				{
					obj.addProperty(firstLine[i], linetokens[i]);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj.toString(); 
	}
	public static void main(String[] args) {
		String fileName = config.DownloadsFolder()+"SKYMET.csv";
		try {
			CSVReader_DB.doWork(fileName);
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
