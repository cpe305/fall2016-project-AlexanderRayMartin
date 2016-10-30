package alexanderRayMartin.save;

import java.io.Serializable;

public class Schedule implements Serializable {

	private static final long serialVersionUID = 1L;

	private int classOneNumber;
	private int classTwoNumber;
	private int classThreeNumber;
	private int classFourNumber;
	private int classFiveNumber;

	public Schedule() {
		classOneNumber = -1;
		classTwoNumber = -1;
		classThreeNumber = -1;
		classFourNumber = -1;
		classFiveNumber = -1;
	}

	public int getClassOneNumber() {
		return classOneNumber;
	}

	public int getClassTwoNumber() {
		return classTwoNumber;
	}

	public int getClassThreeNumber() {
		return classThreeNumber;
	}

	public int getClassFourNumber() {
		return classFourNumber;
	}

	public int getClassFiveNumber() {
		return classFiveNumber;
	}

}
