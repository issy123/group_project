package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import model.PrIS;
import model.Student;
import server.Conversation;
import server.Handler;

public class StudentController implements Handler {
	private PrIS informatieSysteem;
	
	/**
	 * De StudentController klasse moet alle student-gerelateerde aanvragen
	 * afhandelen. Methode handle() kijkt welke URI is opgevraagd en laat
	 * dan de juiste methode het werk doen. Je kunt voor elke nieuwe URI
	 * een nieuwe methode schrijven.
	 * 
	 * @param infoSys - het toegangspunt tot het domeinmodel
	 */
	public StudentController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/mijnmedestudenten")) {
			mijnMedestudenten(conversation);
		}
		if(conversation.getRequestedURI().startsWith("/student/toonziekmelden")) {
			try {
				meldZiek(conversation);
			}
			catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		if (conversation.getRequestedURI().startsWith("/student/vanklas")) {
			studentenVanKlas(conversation);
		}
	}

	/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Daarna worden
	 * de benodigde gegevens uit het domeinmodel gehaald. Deze gegevens worden
	 * dan weer omgezet naar JSON en teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation - alle informatie over het request
	 */
	private void mijnMedestudenten(Conversation conversation) {
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String gebruikersnaam = jsonObjectIn.getString("username");
		
		Student student = informatieSysteem.getStudent(gebruikersnaam);			// Student-object opzoeken
		String klasCode = student.getMijnKlas().getKlasCode();					// klascode van de student opzoeken
		ArrayList<Student> studentenVanKlas = informatieSysteem.getStudentenVanKlas(klasCode);	// medestudenten opzoeken
		
		JsonArrayBuilder jab = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...

		for (Student s : studentenVanKlas) {									// met daarin voor elke medestudent een JSON-object...
			if (s.getGebruikersNaam().equals(gebruikersnaam)) 					// behalve de student zelf...
				continue;
			else {
				jab.add(Json.createObjectBuilder()
						.add("studentnummer", s.getStudentNummer())
						.add("voornaam", s.getVoorNaam())
						.add("achternaam", s.getAchterNaam()));
			}
		}
		
		conversation.sendJSONMessage(jab.build().toString());					// terug naar de Polymer-GUI!
	}

	public void meldZiek(Conversation conversation) throws IOException {
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String gebruikersnaam = jsonObjectIn.getString("username");


		Student student = informatieSysteem.getStudent(gebruikersnaam);

		if (!student.isZiek()){
			student.setZiek(true);
		} else {
			student.setZiek(false);
		}

		updateCSV(student);

		conversation.sendJSONMessage(Json.createObjectBuilder().add("ziek", student.isZiek()).build().toString());
	}

	
	private void studentenVanKlas(Conversation conversation){
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String klasCode = jsonObjectIn.getString("klas");
		ArrayList<Student> studentenVanKlas = informatieSysteem.getStudentenVanKlas(klasCode);	// medestudenten opzoeken
		
		JsonArrayBuilder jab = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...

		for (Student s : studentenVanKlas) {									// met daarin voor elke medestudent een JSON-object... 
				jab.add(Json.createObjectBuilder()
						.add("studentnummer", s.getStudentNummer())
						.add("voornaam", s.getVoorNaam())
						.add("achternaam", s.getAchterNaam()));
		}
		
		conversation.sendJSONMessage(jab.build().toString());	
	}

	public void updateCSV(Student st) throws IOException{
		BufferedReader br = null;
		String line = "";
		final String delimiter = ";";
		ArrayList<String> ar = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader("src/resource/klassen.csv"));
			while ((line = br.readLine()) != null) {
				String[] l = line.split(delimiter);
				ar.add(line);
			}

			for (int i = 0; i <  ar.size(); i++) {
				String[] p = ar.get(i).split(delimiter);
				if (Integer.parseInt(p[0]) == st.getStudentNummer()) {
					if (st.isZiek()) {
						p[5] = "TRUE";
					} else {
						p[5] = "FALSE";
					}
				}
				ar.set(i, p[0] + ";" + p[1] + ";" +p[2] + ";" +p[3] + ";" +p[4] + ";" +p[5] );
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter("src/resource/klassen.csv",false));
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<ar.size();i++)
			{
				sb.append(ar.get(i) + "\n");
			}

			bw.write(sb.toString());
			bw.close();

		}

		catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
}

