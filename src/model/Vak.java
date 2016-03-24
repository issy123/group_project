package model;

import java.util.ArrayList;

public class Vak {
	private ArrayList<Docent> docenten = new ArrayList<>();
	private String vakCode;
	private String vakNaam;
	
	public Vak(String vC, String vN)
	{
		vakCode = vC;
		vakNaam = vN;
	}
	
	public void voegDocentToe(Docent d)
	{
		docenten.add(d);
	}

	public String getVakCode() {
		return vakCode;
	}

	public String getVakNaam() {
		return vakNaam;
	}

	public ArrayList<Docent> getDocenten() {
		return docenten;
	}

	public void setDocenten(ArrayList<Docent> docenten) {
		this.docenten = docenten;
	}

}
