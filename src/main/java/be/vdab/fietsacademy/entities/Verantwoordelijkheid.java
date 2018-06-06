package be.vdab.fietsacademy.entities;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "verantwoordelijkheden")
public class Verantwoordelijkheid implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String naam;
	@ManyToMany
	@JoinTable(name = "docentenverantwoordelijkheden",
			   joinColumns = @JoinColumn(name = "verantwoordelijkheidid"),
			   inverseJoinColumns = @JoinColumn(name = "docentid"))
	private Set<Docent> docenten = new LinkedHashSet();

	protected Verantwoordelijkheid() {
	}

	public Verantwoordelijkheid(String naam) {
		this.naam = naam;
	}

	public long getId() {
		return id;
	}

	public String getNaam() {
		return naam;
	}
	
	public Set<Docent> getDocenten(){
		return docenten;
	}
	
	public boolean add(Docent docent) {
		boolean toegevoegd = docenten.add(docent);
		if (! docent.getVerantwoordelijkheden().contains(this)) {
			docent.addVerantwoordelijkheid(this);
		}
		return toegevoegd;
	}
	
	public boolean remove(Docent docent) {
		boolean verwijderd = docenten.remove(docent);
		if (docent.getVerantwoordelijkheden().contains(this)) {
			docent.removeVerantwoordelijkheid(this);
		}
		return verwijderd;
	}

	@Override
	public int hashCode() {
		return naam.toUpperCase().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Verantwoordelijkheid)) {
			return false;
		}
		Verantwoordelijkheid other = (Verantwoordelijkheid) obj;
		if (naam.equalsIgnoreCase(other.naam)) {
			return true;
		}
		return false;
	}
}
