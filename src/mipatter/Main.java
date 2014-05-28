package mipatter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
	
	// this program takes arguments representing what should be searched for
	// it searches indeed.com for that term and grabs the lat/long from every result
	// it builds a xml string compatible with the google api, then it will draw the results
	
	
	// built results into qgis with this tutorial:
	// http://horta.ulb.ac.be/cours/SIS-TP-Tutoriels/QGIS%20Tutorials%20%28Ujaval%20Gandhi%29/qgis.spatialthoughts.com/2012/07/tutorial-making-heatmaps-using-qgis-and.html
	// http://schoolofdata.org/2013/04/27/creating-a-map-using-qgis/
	// http://qgis.spatialthoughts.com/2012/07/tutorial-making-heatmaps-using-qgis-and.html
	
	public void writeToFile(String toWrite, String fileName) {
		try {
			PrintWriter writer = new PrintWriter(fileName);
			writer.print(toWrite);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String search = "";
		for (int i = 0; i < args.length; ++i) {
			search += args[i];
		}
		Parser parse = new Parser(search);
		String xml = parse.getAllResults();
		try {
			PrintWriter writer = new PrintWriter("gis_points.csv");
			writer.print(xml);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
