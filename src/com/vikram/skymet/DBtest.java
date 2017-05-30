package com.vikram.skymet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.aeonbits.owner.ConfigFactory;

import com.vikram.config.serverconfig;

public class DBtest {
	static serverconfig config=ConfigFactory.create(serverconfig.class);
	public static void main(String[] args) {
		
		Connection con;
		try {
			con = DriverManager.getConnection(config.jtdsDriver(),config.sqlUsername(),config.sqlPassword());
			PreparedStatement select=con.prepareStatement("select * from skymet.dbo.Skymet_Rainfall_Prediction ");
			ResultSetMetaData result=select.getMetaData();
			int count=result.getColumnCount();
			int count1=0;
			for (int i = 1; i <= count; i++) {
				System.out.println(result.getColumnLabel(i));;
			}
			ResultSet rs=select.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString(3)+"     "+rs.getDate(11));
				count1++;
			}
			rs.close();
		System.out.println(count1);
//		PreparedStatement delete=con.prepareStatement("DELETE FROM dbo.Skymet_Rainfall_Prediction");
//		int x=delete.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
