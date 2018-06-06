package be.vdab.fietsacademy.valueobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Adres implements Serializable {
	private static final long serialVersionUID = 1L;
	private String straat;
	private String huisNr;
	private String postcode;
	private String gemeente;

	protected Adres() {
	}

	public Adres(String straat, String huisNr, String postcode, String gemeente) {
		this.straat = straat;
		this.huisNr = huisNr;
		this.postcode = postcode;
		this.gemeente = gemeente;
	}

	public String getStraat() {
		return straat;
	}

	public String getHuisNr() {
		return huisNr;
	}

	public String getPostcode() {
		return postcode;
	}

	public String getGemeente() {
		return gemeente;
	}

	@Override
	public int hashCode() {
		int result = straat.toUpperCase().hashCode() + 
				 huisNr.toUpperCase().hashCode() +
				 postcode.toUpperCase().hashCode() +
				 gemeente.toUpperCase().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Adres))
			return false;
		Adres other = (Adres) obj;
		if (gemeente.equals(other.gemeente) && huisNr.equals(other.huisNr) && postcode.equals(other.postcode) && straat.equals(other.straat)) {
			return true;
		} else {
			return false;
		}
	}
	
}
