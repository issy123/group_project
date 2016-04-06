package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import model.Les;
import model.PrIS;
import model.RoosterElement;
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
		else if (conversation.getRequestedURI().startsWith("/rooster/absenties")) {
			absentieDoen(conversation);
		}
		else if (conversation.getRequestedURI().startsWith("/rooster/aanwezigen")) {
			getAanwezigen(conversation);
		}
		else if (conversation.getRequestedURI().startsWith("/rooster/aanwezigheid")) {
			getStudentAanwezigheid(conversation);
		}
		else if (conversation.getRequestedURI().startsWith("/rooster")) {
			getRooster(conversation);
		}
	}
	
	public void mijnLessen(Conversation conversation){
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String datum = jsonObjectIn.getString("datum");
		String docentNaam = jsonObjectIn.getString("naam");
		ArrayList<Les> lessen = this.informatieSysteem.getLessen(datum,docentNaam);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...
		if(lessen == null){
			conversation.sendJSONMessage(jab.build().toString());
			return;
		}

		for (Les les : lessen) {									// met daarin voor elke medestudent een JSON-object... 
				jab.add(
						Json.createObjectBuilder()
						.add("vaknaam", les.getVaknaam())
						.add("begintijd", les.getBegindatum())
						.add("eindtijd", les.getEinddatum())
						.add("lokaal", les.getLokaal())
						.add("klas", les.getKlas())
						);
		}
		
		conversation.sendJSONMessage(jab.build().toString());
		
	}
	
	public void absentieDoen(Conversation conversation){
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String datum = jsonObjectIn.getString("datum");
		String les = jsonObjectIn.getString("les");
		String beginTijd = jsonObjectIn.getString("begintijd");
		String eindTijd = jsonObjectIn.getString("eindtijd");
		String docentNaam = jsonObjectIn.getString("docentNaam");
		JsonArray absenties = jsonObjectIn.getJsonArray("absenties");
		String id = les+","+beginTijd+","+eindTijd+","+docentNaam;
		
		try
		{
			String filepath = "src/resource/absenties/"+datum+".csv";
			
			//legen 
			File f = new File(filepath);
			if (f.exists())
			{
			  //delete if exists
			   f.delete();
			}
			
			FileWriter writer;			

			writer = new FileWriter(filepath);
		    for (int i = 0 ; i < absenties.size(); i++) {
		        JsonObject obj = absenties.getJsonObject(i);
		        String naam = ""+obj.getInt("studentnummer");
		        String aanwezig = ""+obj.getBoolean("aanwezig");
		        writer.append(id+","+naam+","+aanwezig+"\n");
		    }				
		    //generate whatever data you want
				
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
		
		
		
	}
	
	public void getRooster(Conversation conversation){
		ArrayList<RoosterElement> roosterElementen = this.informatieSysteem.getRooster();
		
		JsonArrayBuilder jab = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...

		for (RoosterElement roosterElement : roosterElementen) {									// met daarin voor elke medestudent een JSON-object... 
				ArrayList<Les> lessen = roosterElement.getLessen();
				for (Les les : lessen) {
					jab.add(
							Json.createObjectBuilder()
							.add("vaknaam", les.getVaknaam())
							.add("docent", les.getDocent())
							.add("begintijd", les.getBegindatum())
							.add("eindtijd", les.getEinddatum())
							.add("lokaal", les.getLokaal())
							.add("klas", les.getKlas())
							.add("datum", roosterElement.getDatum())
							);
				}
		}
		
		conversation.sendJSONMessage(jab.build().toString());
	}
	
	public void getAanwezigen(Conversation conversation){
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String datum = jsonObjectIn.getString("datum");
		String les = jsonObjectIn.getString("les");
		String beginTijd = jsonObjectIn.getString("beginTijd");
		String eindTijd = jsonObjectIn.getString("eindTijd");
		String docentNaam = jsonObjectIn.getString("docentNaam");
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		System.out.print(datum);
		if(!datum.equals("2-2-2016")){
			conversation.sendJSONMessage(jab.build().toString());
			return;
		}
		
		BufferedReader br = null;
		String line = "";
		final String delimiter = ",";
		try{
			br = new BufferedReader(new FileReader("src/resource/absenties/"+datum+".csv"));//van de datum die we zoeken
			while((line = br.readLine()) != null)
			{
				String[] l = line.split(delimiter);
				if( //van de les die we zoeken
						les.equals(l[0]) &&
						beginTijd.equals(l[1]) &&
						eindTijd.equals(l[2]) &&
						docentNaam.equals(l[3])
						){
					jab.add(
							Json.createObjectBuilder()
							.add("studentnummer",l[4])
							.add("aanwezig",Boolean.valueOf(l[5])));
				}
			}
			
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		conversation.sendJSONMessage(jab.build().toString());
	}
	
	public void getStudentAanwezigheid(Conversation conversation){
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String studentnummer = jsonObjectIn.getString("studentnummer");
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		
		File folder = new File("src/resource/absenties/");
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		        BufferedReader br = null;
				String line = "";
				final String delimiter = ",";
				try{
					br = new BufferedReader(new FileReader("src/resource/absenties/"+listOfFiles[i].getName()));//van de datum die we zoeken
					while((line = br.readLine()) != null)
					{
						String[] l = line.split(delimiter);
						if( //van de les die we zoeken
								studentnummer.equals(l[4])
								){
							jab.add(
									Json.createObjectBuilder()
									.add("datum",listOfFiles[i].getName().replaceFirst("[.][^.]+$", ""))
									.add("les",l[0])
									.add("van",l[1])
									.add("tot",l[2])
									.add("aanwezig",Boolean.valueOf(l[5]))
									);
						}
					}
					
				}
				catch(IOException ioe)
				{
					ioe.printStackTrace();
				}
		      }
		    }
		    
		    conversation.sendJSONMessage(jab.build().toString());
	}
}
