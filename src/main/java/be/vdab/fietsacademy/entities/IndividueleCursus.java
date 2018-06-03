package be.vdab.fietsacademy.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "individuelecursussen")
public class IndividueleCursus extends Cursus {
	private static final long serialVersionUID = 1L;
	private int duurtijd;
	
	protected IndividueleCursus() {
	}
	
	public IndividueleCursus(String naam, int duurtijd) {
		super(naam);
		this.duurtijd = duurtijd;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public int getDuurtijd() {
		return duurtijd;
	}
}