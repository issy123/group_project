package controller;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import model.Les;
import model.PrIS;
import model.Student;
import server.Conversation;
import server.Handler;

public class RoosterController implements Handler {
	private PrIS informatieSysteem;
	public RoosterController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/rooster/lessen")) {
			mijnLessen(conversation);
		}
	}
	
	public void mijnLessen(Conversation conversation){
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String datum = jsonObjectIn.getString("datum");
		String docentNaam = jsonObjectIn.getString("naam");
		
		ArrayList<Les> lessen = this.informatieSysteem.getLessen(datum,docentNaam);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...

		for (Les les : lessen) {									// met daarin voor elke medestudent een JSON-object... 
				jab.add(
						Json.createObjectBuilder()
						.add("vaknaam", les.getVaknaam())
						.add("begindatum", les.getBegindatum())
						.add("einddatum", les.getBegindatum())
						.add("lokaal", les.getLokaal())
						.add("klas", les.getKlas())
						.add("einddatum", les.getEinddatum())
						);
		}
		
		conversation.sendJSONMessage(jab.build().toString());
		
	}
}
