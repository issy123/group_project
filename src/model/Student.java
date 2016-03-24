package model;

import java.util.ArrayList;

public class Student {
	private Klas mijnKlas;
	private ArrayList<Vak> vakken = new ArrayList<>();
	private String voorNaam;
	private String achterNaam;
	private int studentNummer;
	private String gebruikersNaam;
	private String wachtwoord; 
	
	public Student(int sN, String vN, String aN, Klas k, String gN, String ww)
	{
		setStudentNummer(sN);
		setVoorNaam(vN);
		setAchterNaam(aN);
		mijnKlas = k;
		gebruikersNaam = gN;
		wachtwoord = ww;
	}
	
	public Klas getMijnKlas() {
		return mijnKlas;
	}
	
	public String getGebruikersNaam() {
		return gebruikersNaam;
	}

	public String getWachtwoord() {
		return wachtwoord;
	}

	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}

	public ArrayList<Vak> getVakken() {
		return vakken;
	}

	public void setVakken(ArrayList<Vak> vakken) {
		this.vakken = vakken;
	}

	public int getStudentNummer() {
		return studentNummer;
	}

	public void setStudentNummer(int studentNummer) {
		this.studentNummer = studentNummer;
	}

	public String getAchterNaam() {
		return achterNaam;
	}

	public void setAchterNaam(String achterNaam) {
		this.achterNaam = achterNaam;
	}

	public String getVoorNaam() {
		return voorNaam;
	}

	public void setVoorNaam(String voorNaam) {
		this.voorNaam = voorNaam;
	}

}
