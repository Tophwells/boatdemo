package com.tophwells.boats;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoatdemoController {

	@Autowired
	public DataSource dataSource;
	
	@GetMapping("/database")
	public String read() {
		StringBuilder sB = new StringBuilder();
		try {
			Connection connection = dataSource.getConnection();
			
			String sql = "SELECT * FROM Boats";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			//resultSet.beforeFirst();
			resultSet.next();
			for(; !resultSet.isAfterLast();resultSet.next())
			{
				int id = resultSet.getInt("ID");
				String name = resultSet.getString("BoatName");
				sB.append(String.format("ID: %1$d, Name: %2$s,<br/>",id,name));
			}
			return sB.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return "An error occurred at " + e.getStackTrace().toString() + "<br/>" + e.getMessage();
		}
	}

}
