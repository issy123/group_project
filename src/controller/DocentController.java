package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import model.Docent;
import model.PrIS;
import model.Vak;
import server.Conversation;
import server.Handler;

public class DocentController implements Handler {
	private PrIS informatieSysteem;
	
	/**
	 * De DocentController klasse moet alle docent-gerelateerde aanvragen
	 * afhandelen. Methode handle() kijkt welke URI is opgevraagd en laat
	 * dan de juiste methode het werk doen. Je kunt voor elke nieuwe URI
	 * een nieuwe methode schrijven.
	 * 
	 * @param infoSys - het toegangspunt tot het domeinmodel
	 */
	public DocentController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/docent/mijnvakken")) {
			mijnVakken(conversation);
		}
		if (conversation.getRequestedURI().startsWith("/docent/mijnrapport")){
			try {
				mijnAanwezigheid(conversation);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void mijnAanwezigheid(Conversation conversation) throws IOException {
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String gebruikersnaam = jsonObjectIn.getString("username");
		
		Docent d = informatieSysteem.getDocent(gebruikersnaam);
		String vakCode = d.getVakken().get(0).getVakCode();
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		String line = "";
		final String delimiter = ",";
		try{
			BufferedReader br = new BufferedReader(new FileReader("src/resource/test_grafiek.csv"));
			while((line = br.readLine()) != null){
				String[] l = line.split(delimiter);
				if(l[0].equals(vakCode)){
					jab.add(Json.createObjectBuilder()
							.add("lesnummer", l[2])
							.add("aantalaanwezig", l[3])
							.add("aantalafwezig",l[4])
							.add("klascode", l[1])
					);
				}
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		conversation.sendJSONMessage(jab.build().toString());
	}

	/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Daarna worden
	 * de benodigde gegevens uit het domeinmodel gehaald. Deze gegevens worden
	 * dan weer omgezet naar JSON en teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation - alle informatie over het request
	 */
	public void mijnVakken(Conversation conversation) {
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String gebruikersnaam = jsonObjectIn.getString("username");
		
		Docent docent = informatieSysteem.getDocent(gebruikersnaam);	// Docent-object ophalen!
		ArrayList<Vak> vakken = docent.getVakken();						// Vakken van de docent ophalen!
		
		JsonArrayBuilder jab = Json.createArrayBuilder(); 				// En uiteindelijk gaat er een JSON-array met...
		
		for (Vak v : vakken) {
			jab.add(Json.createObjectBuilder()							// daarin voor elk vak een JSON-object...
					.add("vakcode", v.getVakCode())
					.add("vaknaam", v.getVakNaam()));
		}
		
		conversation.sendJSONMessage(jab.build().toString());			// terug naar de Polymer-GUI!
	}
}
