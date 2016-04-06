package model;

import java.util.ArrayList;
import java.util.HashMap;

public class RoosterElement {
	private String datum;
	private HashMap<String,ArrayList<Les>> docenten = new HashMap<>();
	public RoosterElement(String datum)
	{
		this.datum = datum;
	}
	
	public void voegDocentToe(String docentNaam, ArrayList<Les> lessen){
		docenten.put(docentNaam, lessen);
	}
	
	public void voegLesToe(String docentNaam, Les les){
		//voeg les toe aan bestaande docent
		for(HashMap.Entry<String,ArrayList<Les>> entry : docenten.entrySet())
		{
			if(entry.getKey().equals(docentNaam))
			{
				ArrayList<Les> lessen = entry.getValue();
				lessen.add(les);
				entry.setValue(lessen);
				return;
			}
		}
		
		//voeg docent met les toe als hij nog niet bestaat
		ArrayList<Les> lessen = new ArrayList<Les>();
		lessen.add(les);
		this.voegDocentToe(docentNaam,lessen);
		
		
		
	}
	
	public ArrayList<Les> getLessen(String docentNaam){
		for(HashMap.Entry<String,ArrayList<Les>> entry : docenten.entrySet())
		{
			if(entry.getKey().equals(docentNaam))
			{
				return entry.getValue();
			}
		}
		
		return null;
	}
	
	public String getDatum(){
		return this.datum;
	}
}
