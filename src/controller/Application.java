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
import java.util.Arrays;
import java.util.Scanner;

import javax.json.Json;

import model.Docent;
import model.Klas;
import model.Les;
import model.PrIS;
import model.RoosterElement;
import model.Student;
import model.Vak;
import server.JSONFileServer;

public class Application {
	private ArrayList<Klas> klassenLijst = new ArrayList<>();
	private ArrayList<Docent> docentenLijst = new ArrayList<>();
	private static ArrayList<RoosterElement> roosterElementLijst = new ArrayList<>();
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
		Vak aui = new Vak("TCIF-V1AUI-15", "Analysis & User Interfacing");
		Vak ooc = new Vak("TICT-V1OODC-15", "Object Oriented Construction");
		Vak gp = new Vak("TICT-V1GP-15", "Group Project");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1A.csv");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1B.csv");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1C.csv");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1D.csv");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1E.csv");
		leesKlasIn(infoSysteem, "src/resource/SIE_V1F.csv");
		leesDocentenIn(aui, ooc, gp, infoSysteem, "src/resource/Docenten.csv");
		leesRoosterIn(infoSysteem, "src/resource/rooster_C.csv");
		
		/*
		 * {
		 *    "2016-03-31":{
		 *       "Carl van heezik": [{naam:"vak1",begintijd:"16:30",eindtijd:"18:00"}]
		 *    }
		 * }
		 */
		
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
		RoosterController roosterController = new RoosterController(infoSysteem);
		
		server.registerHandler("/login", userController);
		server.registerHandler("/docent/mijnvakken", docentController);
		server.registerHandler("/rooster/lessen", roosterController);
		server.registerHandler("/rooster/absenties", roosterController);
		server.registerHandler("/rooster/aanwezigen", roosterController);
		server.registerHandler("/rooster/aanwezigheid", roosterController);
		server.registerHandler("/rooster", roosterController);
		server.registerHandler("/docent/mijnrapport", docentController);
		server.registerHandler("/student/mijnmedestudenten", studentController);
		server.registerHandler("/student/toonziekmelden", studentController);
		server.registerHandler("/student/vanklas", studentController);
		server.registerHandler("/student", studentController);
		server.registerHandler("/student/allezieken", studentController);
		
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
	
	private static void leesDocentenIn(Vak a, Vak o, Vak g, PrIS pr, String file) throws IOException
	{
		BufferedReader br = null;
		String line = "";
		final String delimiter = ",";
		try{
			br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null)
			{
				String[] l = line.split(delimiter);
				Docent d = new Docent(l[0],l[2],"test");
				if(a.getVakCode().equals(l[1]))
				{
					d.voegVakToe(a);
					a.voegDocentToe(d);
				}
				else if(o.getVakCode().equals(l[1]))
				{
					d.voegVakToe(o);
					o.voegDocentToe(d);
				}
				else if(g.getVakCode().equals(l[1]))
				{
					d.voegVakToe(g);
					g.voegDocentToe(d);
				}
				pr.voegDocentToe(d);
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	private static void leesRoosterIn(PrIS pr, String file) throws IOException
	{
		BufferedReader br = null;
		String line = "";
		final String delimiter = ",";
		try{
			br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null)
			{
				String[] l = line.split(delimiter);
				Boolean bestaatNiet = true;
				//voeg les toe aan bestaande rooster element indien het al bestaat
				for (RoosterElement roosterElement : roosterElementLijst) {
					String currentDatum = roosterElement.getDatum();
					if(currentDatum.equals(l[0])){
						String docentNaam = l[4];
						Les les = new Les(l[3],l[1],l[2],l[5],l[6]);
						roosterElement.voegLesToe(docentNaam, les);
						bestaatNiet = false;
						break;
					}
				}
				
				//maak nieuwe roosterElement
				if(bestaatNiet){
					RoosterElement roosterElement = new RoosterElement(l[0]);
					String docentNaam = l[4];
					Les les = new Les(l[3],l[1],l[2],l[5],l[6]);
					roosterElement.voegLesToe(docentNaam, les);
					roosterElementLijst.add(roosterElement);
				}
			}
			for (RoosterElement roosterElement : roosterElementLijst) {
				pr.voegRoosterElementToe(roosterElement);
			}
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
