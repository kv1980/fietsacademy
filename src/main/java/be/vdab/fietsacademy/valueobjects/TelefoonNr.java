package be.vdab.fietsacademy.valueobjects;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class TelefoonNr implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nummer;
	private boolean fax;
	private String opmerking;
	
	protected TelefoonNr() {
	}
	
	public TelefoonNr(String nummer, boolean fax, String opmerking) {
		this.nummer = nummer;
		this.fax = fax;
		this.opmerking = opmerking;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getNummer() {
		return nummer;
	}
	
	public boolean isFax() {
		return fax;
	}
	
	public String getOpmerking() {
		return opmerking;
	}

	@Override
	public int hashCode() {
		return nummer.toUpperCase().hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if(!(object instanceof TelefoonNr)) {
			return false;
		}
		TelefoonNr telefoonNr = (TelefoonNr) object;
		return nummer.equalsIgnoreCase(telefoonNr.nummer);
	}	
}
