package be.vdab.fietsacademy.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import be.vdab.fietsacademy.valueobjects.Adres;
import be.vdab.fietsacademy.valueobjects.TelefoonNr;

@Entity
@Table(name = "campussen")
public class Campus implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String naam;
	@Embedded
	private Adres adres;
	@ElementCollection
	@CollectionTable(name = "campussentelefoonnrs",joinColumns = @JoinColumn(name = "campusId"))
	@OrderBy("fax")
	private Set<TelefoonNr> telefoonNrs;
	@OneToMany
	@JoinColumn(name = "campusid")
	@OrderBy("voornaam, familienaam")
	private Set<Docent> docenten;
	
	protected Campus() {
	}

	public Campus(String naam, Adres adres) {
		this.naam = naam;
		this.adres = adres;
		this.telefoonNrs = new LinkedHashSet();
		this.docenten = new LinkedHashSet();
	}

	public long getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}

	public Adres getAdres() {
		return adres;
	}
	
	public void setAdres(Adres adres) {
		this.adres = adres;
	}
	
	public Set<TelefoonNr> getTelefoonNrs() {
		return Collections.unmodifiableSet(telefoonNrs);
	}
	
	public boolean addTelefoonNr(TelefoonNr telefoonNr) {
		if (telefoonNr==null) {
			throw new NullPointerException();
		}
		return telefoonNrs.add(telefoonNr);
	}
	
/*	public boolean removeTelefoonNr(TelefoonNr telefoonNr) {
		return telefoonNrs.remove(telefoonNr);
	}*/
	
	public Set<Docent> getDocenten(){
		return Collections.unmodifiableSet(docenten);
	}
	
	public boolean addDocent(Docent docent) {
		if (docent == null) {
			throw new NullPointerException();
		}
		return docenten.add(docent);
	}
}