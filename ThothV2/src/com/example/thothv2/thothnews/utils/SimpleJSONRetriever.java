package com.example.thothv2.thothnews.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SimpleJSONRetriever {

	String base;
	
	public SimpleJSONRetriever(String baseUrl)
	{
		
		base = baseUrl;
		
	}
	
	public String getJSON(String uri) throws IOException
	{
		URL url = new URL(base + uri);
	
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		InputStream is = con.getInputStream();
        String data = readAllFrom(is);
        con.disconnect();
        return data;       
    }

	private String readAllFrom(InputStream is) {
	    Scanner s = new Scanner(is);
	    try {
	        s.useDelimiter("\\A");
	        return s.hasNext() ? s.next() : null;
	    } finally {
	        s.close();
	    }
	}	
}
