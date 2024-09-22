package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.ResultSet;

@SpringBootApplication
public class Application
{
	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM mytable WHERE columnfoo = 500");
		while (rs.next()) {
			System.out.print("Column 1 returned ");
			System.out.println(rs.getString(1));
		}
		rs.close();
		st.close();
	}
}
