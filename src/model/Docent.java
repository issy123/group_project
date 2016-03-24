package model;

import java.util.ArrayList;

public class Docent {
	private String gebruikersNaam;
	private String wachtwoord;
	private ArrayList<Vak> vakken = new ArrayList<>();
	
	public Docent(String gN, String ww)
	{
		setGebruikersNaam(gN);
		setWachtwoord(ww);
	}
	
	public void voegVakToe(Vak v)
	{
		vakken.add(v);
	}
	
	public ArrayList<Vak> getVakken() {
		return vakken;
	}

	public String getGebruikersNaam() {
		return gebruikersNaam;
	}

	public void setGebruikersNaam(String gebruikersNaam) {
		this.gebruikersNaam = gebruikersNaam;
	}

	public String getWachtwoord() {
		return wachtwoord;
	}

	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}

	public void setVakken(ArrayList<Vak> vakken) {
		this.vakken = vakken;
	}

}
