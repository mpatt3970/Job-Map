package mipatter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {

	private static final int NUMBER = 25;
	private static final int UPPER_LIMIT = 500000;
	private static final String URL_SMALL_BASE = "http://api.indeed.com/ads/apisearch?publisher=1611676990423118&v=2&limit=1&q=";
	private static final String URL_SEARCH_BASE = "http://api.indeed.com/ads/apisearch?publisher=1611676990423118&latlong=1&v=2&q=";

	private int totalSize;
	private String queryString;

	public Parser(String q) {
		queryString = q;
		String urlSizeCheck = URL_SMALL_BASE + queryString;
		totalSize = checkSize(urlSizeCheck);
	}

	public int checkSize(String url) {
		// xml query this string for totalresults
		try {
			// create and open the url
			URL urlObject = new URL(url);
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = parser.parse(urlObject.openStream());
			// totalresults holds the number of you guessed it..
			NodeList nodes = document.getElementsByTagName("totalresults");
			// turn the string result into a number
			String n = nodes.item(0).getTextContent();
			int num = Integer.parseInt(n);
			if (num > UPPER_LIMIT) {
				return UPPER_LIMIT;
			} else {
				return num;
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public String getAllResults() {
		String results = "";
		int current = 0;
		while (current < totalSize) {
			System.out.println(current + "/" + totalSize);
			// xml query this url 'constructUrlSearchString(NUMBER, current, queryString);' for latitude and longitude
			try {
				// create and open the url
				String urlStr = constructUrlSearchString(((Integer) NUMBER).toString(), ((Integer) current).toString(), queryString);
				URL urlObject = new URL(urlStr);
				DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document document = parser.parse(urlObject.openStream());
				// get results at result
				NodeList nodes = document.getElementsByTagName("result");
				// for each result, get lat and long and then build a string
				for (int i = 0; i < nodes.getLength(); ++i) {
					Element result = (Element) nodes.item(i);
					try {
						String la = result.getElementsByTagName("latitude").item(0).getTextContent();
						String lo = result.getElementsByTagName("longitude").item(0).getTextContent();
						String name = result.getElementsByTagName("jobtitle").item(0).getTextContent();
						String invalid_pattern = "[^\\w\\s]";
						String validName = name.replaceAll(invalid_pattern, "");
						// add a string like \n\t<marker lat="num" long="val"/>
						results += constructPlaceMarkString(i + 1 ,validName, la, lo);
					} catch (NullPointerException e) {
						// don't care but don't add it
					}
				}
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			current += NUMBER;
		}
		// strip the last new line char
		results = results.substring(0, results.length() - 1);
		return results;
	}

	public String constructUrlSearchString(String limitNum, String startNum, String searchWord) {
		String url = URL_SEARCH_BASE + searchWord + "&limit=" + limitNum + "&start=" + startNum;
		return url;
	}
	
	public String constructPlaceMarkString(int id, String name, String lat, String lon) {
		return id + ", " + name + ", " + lat + ", " + lon + "\n";
	}

}
