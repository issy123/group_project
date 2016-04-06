package model;

import java.util.ArrayList;
import java.util.HashMap;

public class PrIS {
	private HashMap<String,Docent> mijnDocenten = new HashMap<>();
	private HashMap<String,Student> mijnStudenten = new HashMap<>();
	private HashMap<String,RoosterElement> mijnRoosterElementen = new HashMap<>();
	
	public void voegStudentToe(Student s)
	{
		mijnStudenten.put(s.getGebruikersNaam(), s);
	}
	
	public void voegDocentToe(Docent d)
	{
		mijnDocenten.put(d.getGebruikersNaam(), d);
	}
	
	public void voegRoosterElementToe(RoosterElement roosterElement){
		mijnRoosterElementen.put(roosterElement.getDatum(),roosterElement);
	}

	public String login(String gebruikersnaam, String wachtwoord) {
		String rol = "undefined";
		for(HashMap.Entry<String,Docent> entry : mijnDocenten.entrySet())
		{
			if(entry.getKey().equals(gebruikersnaam)&&entry.getValue().getWachtwoord().equals(wachtwoord))
			{
				rol = "docent";
			}
		}
		for(HashMap.Entry<String,Student> entry : mijnStudenten.entrySet())
		{
			if(entry.getKey().equals(gebruikersnaam)&&entry.getValue().getWachtwoord().equals(wachtwoord))
			{
				rol = "student";
			}
		}
		return rol;
	}
	
	public ArrayList<Les> getLessen(String datum, String docentNaam){
		Docent d = null;
		for(HashMap.Entry<String,RoosterElement> entry : mijnRoosterElementen.entrySet())
		{
			if(entry.getKey().equals(datum))
			{
				return entry.getValue().getLessen(docentNaam);
			}
		}
		
		return null;
	}

	public Docent getDocent(String gebruikersnaam) {
		Docent d = null;
		for(HashMap.Entry<String,Docent> entry : mijnDocenten.entrySet())
		{
			if(entry.getKey().equals(gebruikersnaam))
			{
				d=entry.getValue();
			}
		}
		return d;
	}

	public Student getStudent(String gebruikersnaam) {
		Student s = null;
		for(HashMap.Entry<String,Student> entry : mijnStudenten.entrySet())
		{
			if(entry.getKey().equals(gebruikersnaam))
			{
				s=entry.getValue();
			}
		}
		return s;
	}

	public ArrayList<Student> getStudentenVanKlas(String klasCode) {
		ArrayList<Student> klassenLijst = new ArrayList<>();
		for(HashMap.Entry<String,Student> entry : mijnStudenten.entrySet())
		{
			if(entry.getValue().getMijnKlas().getKlasCode().equals(klasCode))
			{
				klassenLijst.add(entry.getValue());
			}
		}
		return klassenLijst;
	}

}
