package mipatter;

public class Main {
	
	// this program takes arguments representing what should be searched for
	// it searches indeed.com for that term and grabs the lat/long from every result
	// it builds a xml string compatible with the google api, then it will draw the results
	
	public static void main(String[] args) {
		String search = "";
		for (int i = 0; i < args.length; ++i) {
			search += args[i];
		}
		Parser parse = new Parser(search);
		String xml = parse.constructLatLngXML();
		// use this xml with Google Api to draw markers on the map
		System.out.println(xml);
	}

}
