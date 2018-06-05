package be.vdab.fietsacademy.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import be.vdab.fietsacadamy.enums.Geslacht;
import be.vdab.fietsacademy.valueobjects.Adres;
import be.vdab.fietsacademy.valueobjects.TelefoonNr;

public class CampusTest {
	private Campus campus1, campus2;
	private TelefoonNr telefoonNr;
	private Docent docent1, docent2;
	
	@Before
	public void before() {
		campus1 = new Campus("testNaam1", new Adres("testStraat1","testHuisNr1","testPostCode1","testGemeente1"));
		campus2 = new Campus("testNaam2", new Adres("testStraat2","testHuisnr2","testPostcode2","testGemeente2"));
		telefoonNr = new TelefoonNr("1",false,"");
		docent1 = new Docent("testVoornaam1", "testFamilienaam1", BigDecimal.ONE, "test1@fietsacademy.be", Geslacht.MAN,campus1);
		docent2 = new Docent("testVoornaam2", "testFamilienaam2", BigDecimal.ONE, "test2@fietsacademy.be", Geslacht.MAN,campus1);
	}
	
	@Test
	public void eenNieuweCampusHeeftGeenTelefoonNrs() {
		assertEquals(0,campus1.getTelefoonNrs().size());
	}
	
	@Test
	public void addTelefoonNrVoegtEenTelefoonNrToe() {
		assertTrue(campus1.addTelefoonNr(telefoonNr));
		assertEquals(1,campus1.getTelefoonNrs().size());
		assertTrue(campus1.getTelefoonNrs().contains(telefoonNr));
	}
	
	@Test
	public void addTelefoonNrMagGeenDezelfdeTelefoonNrsToevoegen() {
		TelefoonNr telefoonNr = new TelefoonNr("1",false,"");
		assertTrue(campus1.addTelefoonNr(telefoonNr));
		assertFalse(campus1.addTelefoonNr(telefoonNr));
		assertEquals(1,campus1.getTelefoonNrs().size());
		assertTrue(campus1.getTelefoonNrs().contains(telefoonNr));
	}
	
	@Test(expected = NullPointerException.class)
	public void addTelefoonNrMagGeenNullToevoegen() {
		campus1.addTelefoonNr(null);
	}
	
/*	@Test
	public void removeTelefoonNrVerwijdertBestaandeTelefoonNr() {
		campus1.addTelefoonNr(telefoonNr);
		assertTrue(campus1.removeTelefoonNr(telefoonNr));
		assertEquals(0,campus1.getTelefoonNrs().size());
	}*/
	
/*	@Test
	public void removeTelefoonNrVerwijdertGeenOnbestaandeTelefoonNr() {
		campus1.addTelefoonNr(telefoonNr);
		assertFalse(campus1.removeTelefoonNr(new TelefoonNr("2",false,"")));
		assertEquals(1,campus1.getTelefoonNrs().size());
		assertTrue(campus1.getTelefoonNrs().contains(telefoonNr));
	}*/
	
	@Test
	public void eenCampusKanMeerdereDocentenBevatten() {
		assertTrue(campus1.getDocenten().contains(docent1));
		assertTrue(campus1.getDocenten().contains(docent2));
	}
	
	@Test
	public void campus1IsDeOrigineleCampusVanDocent1() {
		assertEquals(campus1,docent1.getCampus());
		assertTrue(campus1.getDocenten().contains(docent1));
	}
	
	@Test
	public void campus2WerdeDeNieuweCampusVanDocent1NaVerhuis() {
		campus2.addDocent(docent1);
		assertNotEquals(campus1,docent1.getCampus());
		assertEquals(campus2,docent1.getCampus());
		assertFalse(campus1.getDocenten().contains(docent1));
		assertTrue(campus2.getDocenten().contains(docent1));
	}
}