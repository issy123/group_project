package model;

import java.util.ArrayList;

public class Klas {
	private String klasCode;
	private ArrayList<Student> studenten = new ArrayList<>();
	
	public Klas(String kC)
	{
		klasCode = kC;
	}
	
	public void voegStudentAanKlasToe(Student s)
	{
		studenten.add(s);
	}
	
	public String getKlasCode()
	{
		return klasCode;
	}

	public ArrayList<Student> getStudenten() {
		return studenten;
	}

	public void setStudenten(ArrayList<Student> studenten) {
		this.studenten = studenten;
	}
}
