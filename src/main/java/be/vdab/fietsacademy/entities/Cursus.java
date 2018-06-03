package be.vdab.fietsacademy.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Cursus implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String naam;
	
	protected Cursus() {
	}

	protected Cursus(String naam) {
		id = UUID.randomUUID().toString();
		this.naam = naam;
	}

	public String getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}
}
