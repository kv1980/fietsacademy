package be.vdab.fietsacademy.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import be.vdab.fietsacadamy.enums.Geslacht;
import be.vdab.fietsacademy.valueobjects.Adres;
import be.vdab.fietsacademy.valueobjects.TelefoonNr;

public class CampusTest {
	private Campus campus;
	private TelefoonNr telefoonNr;
	private Docent docent1, docent2;
	
	@Before
	public void before() {
		campus = new Campus("naam", new Adres("straat","huisNr","postCode","gemeente"));
		telefoonNr = new TelefoonNr("1",false,"");
		docent1 = new Docent("test", "test", BigDecimal.ONE, "test@fietsacademy.be", Geslacht.MAN/*,campus*/);
		docent2 = new Docent("test2", "test2", BigDecimal.ONE, "test2@fietsacademy.be", Geslacht.MAN/*,campus*/);
	}
	
	@Test
	public void eenNieuweCampusHeeftGeenTelefoonNrs() {
		assertEquals(0,campus.getTelefoonNrs().size());
	}
	
	@Test
	public void addTelefoonNrVoegtEenTelefoonNrToe() {
		assertTrue(campus.addTelefoonNr(telefoonNr));
		assertEquals(1,campus.getTelefoonNrs().size());
		assertTrue(campus.getTelefoonNrs().contains(telefoonNr));
	}
	
	@Test
	public void addTelefoonNrMagGeenDezelfdeTelefoonNrsToevoegen() {
		TelefoonNr telefoonNr = new TelefoonNr("1",false,"");
		assertTrue(campus.addTelefoonNr(telefoonNr));
		assertFalse(campus.addTelefoonNr(telefoonNr));
		assertEquals(1,campus.getTelefoonNrs().size());
		assertTrue(campus.getTelefoonNrs().contains(telefoonNr));
	}
	
	@Test(expected = NullPointerException.class)
	public void addTelefoonNrMagGeenNullToevoegen() {
		campus.addTelefoonNr(null);
	}
	
/*	@Test
	public void removeTelefoonNrVerwijdertBestaandeTelefoonNr() {
		campus.addTelefoonNr(telefoonNr);
		assertTrue(campus.removeTelefoonNr(telefoonNr));
		assertEquals(0,campus.getTelefoonNrs().size());
	}*/
	
/*	@Test
	public void removeTelefoonNrVerwijdertGeenOnbestaandeTelefoonNr() {
		campus.addTelefoonNr(telefoonNr);
		assertFalse(campus.removeTelefoonNr(new TelefoonNr("2",false,"")));
		assertEquals(1,campus.getTelefoonNrs().size());
		assertTrue(campus.getTelefoonNrs().contains(telefoonNr));
	}*/
	
	@Test
	public void eenCampusKanMeerdereDocentenBevatten() {
		assertTrue(campus.addDocent(docent1));
		assertTrue(campus.addDocent(docent2));
	}
}
