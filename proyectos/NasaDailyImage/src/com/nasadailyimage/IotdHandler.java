package com.nasadailyimage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class IotdHandler extends DefaultHandler {
	private static final String TAG = IotdHandler.class.getSimpleName();
	private String url = "http://www.nasa.gov/rss/dyn/image_of_the_day.rss";
	private boolean inUrl = false;
	private boolean inTitle = false;
	private boolean inDescription = false;
	private boolean inItem = false;
	private boolean inDate = false;
	private String urlImagen = null;
	private Bitmap image = null;
	private String title = null;
	private StringBuffer description = new StringBuffer();
	private String date = null;
	
	public void processFeed() {
        try {
        	
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setContentHandler(this);
            InputStream inputStream = new URL(url).openStream();
            xr.parse(new InputSource(inputStream));
               
        } catch (IOException e) {
            Log.e("", e.toString());
        } catch (SAXException e) {
            Log.e("", e.toString());
        } catch (ParserConfigurationException e) {
            Log.e("", e.toString());
        }
	}
	
	private Bitmap getBitmap(String url){
		System.out.println("getbitmap");
		System.out.println(url);
		try{
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			input.close();
			return bitmap;
		}
		catch(IOException ioe){
			System.out.println("la url: " + url + "no es valida");
			return null;
		}
	}
	
	//private IotdHandlerListener listener;
	
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		int cont = 0;
		
		if (localName.equals("enclosure")) { 
			urlImagen = attributes.getValue("url");
			System.out.println("url: " + urlImagen );
			inUrl = true;
		} else { inUrl = false; }
		
		if (localName.startsWith("item")) { 
			System.out.println("Item: ");
			inItem = true; } 
		else { if (inItem) { 
				if (localName.equals("title")) { 
					inTitle = true; } 
				else { inTitle = false; }
				
				if (localName.equals("description") && cont == 0) { 
					inDescription = true;
					cont = 1;
				} 
				else { inDescription = false; }
				
				if (localName.equals("pubDate")) { inDate = true; } 
				else { inDate = false; }
			}
		}
		
	}
	
	 @Override
	 public void startDocument() throws SAXException {
	 	 // TODO Auto-generated method stub
	 	 System.out.println("Start document");
	 }
	 
	 
	 @Override
	 public void endDocument() throws SAXException {
	 	 // TODO Auto-generated method stub
		 System.out.println("End document");
	 }

	public void characters(char ch[], int start, int length) {

		 String chars = (new String(ch).substring(start, start + length));
		 
		 if(inUrl && image == null){
			 image = getBitmap(urlImagen);
		 }
		 
		 if (inTitle && title == null) { 
			 title = chars;
			 System.out.println("title: " + title);
		 }
		 
		 if (inDescription) { 
			 description.append(chars);
			 inDescription = false;
		 }
		 
		 if (inDate && date == null) {
			 date = chars;
			 System.out.println("date: " + date);
			 //Example: Tue, 21 Dec 2010 00:00:00 EST
//			 String rawDate = chars;
//			 try { 
//				 SimpleDateFormat parseFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
//				 Date sourceDate = parseFormat.parse(rawDate);
//				 
//				 SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
//				 date = outputFormat.format(sourceDate);
//			 } catch (Exception e) { 
//				 e.printStackTrace();
//			 }
		 }
	 }
	 
	 public Bitmap getImage(){ return image; }
	 
	 public String getTitle(){ return title; }
	 
//	 public void endElement(String uri, String localName, String qName) {
//		if (url != null && title != null && description != null && date != null) { 
//			 if (listener != null) { 
//				 listener.iotdParsed(url, title.toString(), description.toString(), date);
//				 listener = null;
//			 }
//		 }
//	}

	public String getUrl() {
		return url;
	}

	public StringBuffer getDescription() {
		return description;
	}

	public String getDate() {
		return date;
	}
	
//	public IotdHandlerListener getListener() { 
//		return listener; 
//	}
//
//	public void setListener(IotdHandlerListener listener) {
//		this.listener = listener;
//	}
	
	
}

