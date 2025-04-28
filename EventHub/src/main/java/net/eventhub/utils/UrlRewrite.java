package net.eventhub.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UrlRewrite extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {
		   
		String uri = request.getRequestURI();
		//if ( uri != null && uri.contains("eventId"))
		{
			String pathInfo = request.getPathInfo(); 
			// int lastIndex =		  pathInfo.lastIndexOf("/");	  
			  //String eventId = pathInfo.substring(lastIndex + 1);
			 
			   
			     String eventId = request.getParameter("eventId");
			      
			    RequestDispatcher dispatcher = getServletContext()
			    	   .getRequestDispatcher("/main.html?" + eventId);
			    dispatcher.forward(request, response);
		}
		
		  

		      
	}


}
