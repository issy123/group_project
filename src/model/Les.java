package model;

public class Les {
	private String vaknaam;
	private String begindatum;
	private String einddatum;
	private String lokaal;
	private String klas;
	private String docentNaam;
	
	public Les(String vaknaam,String begindatum, String einddatum, String lokaal, String klas){
		this.vaknaam = vaknaam;
		this.begindatum = begindatum;
		this.einddatum = einddatum;
		this.lokaal = lokaal;
		this.klas = klas;
	}
	
	public void setDocent(String docentNaam){
		this.docentNaam = docentNaam;
	}
	
	public String getBegindatum(){
		return this.begindatum;
	}
	
	public String getEinddatum(){
		return this.einddatum;
	}
	
	public String getVaknaam(){
		return this.vaknaam;
	}
	
	public String getLokaal(){
		return this.lokaal;
	}
	
	public String getKlas(){
		return this.klas;
	}
	public String getDocent(){
		return this.docentNaam;
	}
}
