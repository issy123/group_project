package controller;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.Docent;
import model.Klas;
import model.PrIS;
import model.Student;
import model.Vak;
import server.JSONFileServer;

public class Application {
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
	 * 
	 */
	public static void main(String[] args) throws SQLException {
		
		//tests
		PrIS infoSysteem = new PrIS();
		Docent testD = new Docent("testd", "testd");
		infoSysteem.voegDocentToe(testD);
		Vak aui = new Vak("AUI", "Analysis & User Interfacing");
		testD.voegVakToe(aui);
		aui.voegDocentToe(testD);
		Klas testKlas = new Klas("TestKlas");
		Student test = new Student(101769, "Ulysses", "Moore",testKlas, "test","test");
		infoSysteem.voegStudentToe(test);
		Student test2 = new Student(101770, "Harry", "Potter", testKlas, "test2","test");
		infoSysteem.voegStudentToe(test2);
		testKlas.voegStudentAanKlasToe(test);
		testKlas.voegStudentAanKlasToe(test2);
		
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
}
