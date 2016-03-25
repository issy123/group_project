package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Docent;
import model.Klas;
import model.PrIS;
import model.Student;
import model.Vak;
import server.JSONFileServer;

public class Application {
	private ArrayList<Klas> klassenLijst = new ArrayList<>();
	/**
	 * Deze klasse is het startpunt voor de applicatie. Hierin maak je een server 
	 * op een bepaalde poort (bijv. 8888). Daarna is er een PrIS-object gemaakt. Dit
	 * object fungeert als toegangspunt van het domeinmodel. Hiervandaan kan alle
	 * informatie bereikt worden.
	 * 
	 * Om het domeinmodel en de Polymer-GUI aan elkaar te koppelen zijn diverse controllers
	 * gemaakt. Er zijn meerdere controllers om het overzichtelijk te houden. Je mag zoveel
	 * controller-klassen maken als je nodig denkt te hebben. Elke controller krijgt een
	 * koppeling naar het PrIS-object om benodigde informatie op te halen.
	 * 
	 * Je moet wel elke URL die vanaf Polymer aangeroepen kan worden registreren! Dat is
	 * hieronder gedaan voor een drietal URLs. Je geeft daarbij ook aan welke controller
	 * de URL moet afhandelen.
	 * 
	 * Als je alle URLs hebt geregistreerd kun je de server starten en de applicatie in de
	 * browser opvragen! Zie ook de controller-klassen voor een voorbeeld!
	 * @throws SQLException 
	 * @throws IOException 
	 * 
	 */
	public static void main(String[] args) throws SQLException, IOException {
		
		//tests
		PrIS infoSysteem = new PrIS();
		Docent testD = new Docent("testd", "testd");
		infoSysteem.voegDocentToe(testD);
		Vak aui = new Vak("AUI", "Analysis & User Interfacing");
		testD.voegVakToe(aui);
		aui.voegDocentToe(testD);
		leesKlasIn(infoSysteem, "src/resource/SIE_V1A.csv");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1B.csv");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1C.csv");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1D.csv");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1E.csv");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1F.csv");
		
		/*database-connectie
		try{
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sportschool","root","");
			System.out.println("Database Connected!");
		}
		catch(SQLException sqle)
		{
			System.out.println("Er is iets foutgegaan");
			sqle.printStackTrace();
		}*/
		
		//effectieve code
		JSONFileServer server = new JSONFileServer(new File("webapp/app"), 8888);
		
		UserController userController = new UserController(infoSysteem);
		DocentController docentController = new DocentController(infoSysteem);
		StudentController studentController = new StudentController(infoSysteem);
		
		server.registerHandler("/login", userController);
		server.registerHandler("/docent/mijnvakken", docentController);
		server.registerHandler("/student/mijnmedestudenten", studentController);
		
		server.start();
	}

	private static void leesKlasIn(PrIS pr, String file) throws IOException {
		BufferedReader br = null;
		String klas = file.substring(13,20);
		Klas k = new Klas(klas);
		String line = "";
		final String delimiter = ",";
		try{
			br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null){
				String[] l = line.split(delimiter);
				Student s = new Student(Integer.parseInt(l[0]), l[3], l[2] + " " + l[1], k, l[0], "test");
				System.out.println(s);
				pr.voegStudentToe(s);
				k.voegStudentAanKlasToe(s);
			}
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		finally{
			br.close();
		}
		
	}
}
