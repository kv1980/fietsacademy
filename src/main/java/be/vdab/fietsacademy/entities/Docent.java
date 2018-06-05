package be.vdab.fietsacademy.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import be.vdab.fietsacadamy.enums.Geslacht;

@Entity
@Table(name = "docenten")
public class Docent implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String voornaam;
	private String familienaam;
	private BigDecimal wedde;
	private String emailAdres;
	@Enumerated(EnumType.STRING)
	private Geslacht geslacht;
	@ElementCollection
	@CollectionTable(name = "docentenbijnamen", joinColumns = @JoinColumn(name = "docentid"))
	@Column(name = "bijnaam")
	private Set<String> bijnamen;
/*	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "campusid")
	private Campus campus;*/

	protected Docent() {
	}

	public Docent(String voornaam, String familienaam, BigDecimal wedde, String emailAdres, Geslacht geslacht/*,Campus campus*/) {
		this.voornaam = voornaam;
		this.familienaam = familienaam;
		this.wedde = wedde;
		this.emailAdres = emailAdres;
		this.geslacht = geslacht;
		this.bijnamen = new LinkedHashSet<>();
		/*setCampus(campus);*/
	}

	public long getId() {
		return id;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public String getFamilienaam() {
		return familienaam;
	}

	public BigDecimal getWedde() {
		return wedde;
	}

	public String getEmailAdres() {
		return emailAdres;
	}

	public Geslacht getGeslacht() {
		return geslacht;
	}

	public void opslag(BigDecimal percentage) {
		if (percentage.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal factor = BigDecimal.ONE.add(percentage.divide(BigDecimal.valueOf(100)));
		wedde = wedde.multiply(factor, new MathContext(2, RoundingMode.HALF_UP));
	}

	public Set<String> getBijnamen() {
		return Collections.unmodifiableSet(bijnamen);
	}
	
	public boolean addBijnaam(String bijnaam) {
		if (bijnaam.trim().isEmpty()) {
			throw new IllegalArgumentException();
		}
		return bijnamen.add(bijnaam);
	}

	public boolean removeBijnaam(String bijnaam) {
		return bijnamen.remove(bijnaam);
	}
	
/*	public void setCampus(Campus campus) {
		if (campus == null) {
			throw new NullPointerException();
		}
		this.campus = campus;
	}

	public Campus getCampus() {
		return campus;
	}*/
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Docent)) {
			return false;
		}
		Docent docent = (Docent) object;
		return emailAdres == null ? false : emailAdres.equalsIgnoreCase(docent.emailAdres);
	}
	
	@Override
	public int hashCode() {
		return emailAdres == null ? 0 : emailAdres.toLowerCase().hashCode();
	}
}