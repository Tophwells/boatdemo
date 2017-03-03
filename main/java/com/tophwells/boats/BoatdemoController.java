package com.tophwells.boats;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BoatdemoController {

	@Autowired
	public DataSource dataSource;
	
	@GetMapping("/database")
	public String getDatabase(Model model) {
		List<Boat> boats = readDatabase();
		model.addAttribute("boats",boats);
		return "database";
	}
	
	private List<Boat> readDatabase()
	{
		List<Boat> boats = new ArrayList<Boat>();
		try {
			Connection connection = dataSource.getConnection();
			
			String sql = "SELECT * FROM Boats";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			//resultSet.beforeFirst(); //not supported by database
			resultSet.next();
			for(; !resultSet.isAfterLast();resultSet.next())
			{
				int id = resultSet.getInt("ID");
				String name = resultSet.getString("BoatName");
				boats.add(new Boat(id,name));
			}
			return boats;
		} catch (SQLException e) {
			e.printStackTrace();
			return boats;
		}
	}
	
	@PostMapping("/database")
	public String postDatabase(@RequestParam("boatname") String boatName, Model model)
	{
		addBoat(boatName);
		List<Boat> boats = readDatabase();
		model.addAttribute("boats",boats);
		return "database";
	}

	private void addBoat(String name) {
		try {
			Connection connection = dataSource.getConnection();
			
			String sql = "INSERT INTO Boats (ID,BoatName) VALUES (NULL,?)";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.execute();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

}
