package be.vdab.fietsacademy.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import be.vdab.fietsacademy.valueobjects.Adres;
import be.vdab.fietsacademy.valueobjects.TelefoonNr;

public class CampusTest {
	private Campus campus;
	private TelefoonNr telefoonNr;
	
	@Before
	public void before() {
		campus = new Campus("naam", new Adres("straat","huisNr","postCode","gemeente"));
		telefoonNr = new TelefoonNr("1",false,"");
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
}
