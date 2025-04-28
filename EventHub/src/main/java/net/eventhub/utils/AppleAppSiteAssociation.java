package net.eventhub.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppleAppSiteAssociation extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {
		      		      
		      InputStream inputStream = getClass().getResourceAsStream("apple-app-site-association.json");
		      //String json = inputStream.toString();
		      
		      String json = new BufferedReader(
		    	      new InputStreamReader(inputStream, StandardCharsets.UTF_8))
		    	        .lines()
		    	        .collect(Collectors.joining());
		      
		      PrintWriter out = response.getWriter();
		      response.setContentType("application/json");
		      response.setCharacterEncoding("UTF-8");
		      response.getWriter().write(json);

		      
		   }


}
