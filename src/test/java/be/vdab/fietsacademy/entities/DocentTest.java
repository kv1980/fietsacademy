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

public class DocentTest {
	private final static BigDecimal ORIGINELE_WEDDE = BigDecimal.valueOf(200);
	private Docent docent1, nogEenDocent1, docent2;
	private Campus campus1, campus2;
	private Verantwoordelijkheid verantwoordelijkheid1;

	@Before
	public void before() {
		campus1 = new Campus("testNaam1", new Adres("testStraat1","testHuisNr1","testPostCode1","testGemeente1"));
		campus2 = new Campus("testNaam2", new Adres("testStraat2","testHuisNr2","testPostCode2","testGemeente2"));
		docent1 = new Docent("test1", "test1", ORIGINELE_WEDDE, "test1@fietsacademy.be", Geslacht.MAN,campus1);
		nogEenDocent1 = new Docent("test", "test", ORIGINELE_WEDDE, "test1@fietsacademy.be", Geslacht.MAN,campus1);
		docent2 = new Docent("test2", "test2", ORIGINELE_WEDDE, "test2@fietsacademy.be", Geslacht.MAN,campus1);
		verantwoordelijkheid1 = new Verantwoordelijkheid("EHBO");
	}

	@Test
	public void opslag() {
		docent1.opslag(BigDecimal.TEN);
		assertEquals(0, BigDecimal.valueOf(220).compareTo(docent1.getWedde()));
	}

	@Test(expected = NullPointerException.class)
	public void opslagMetNullKanNiet() {
		docent1.opslag(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void opslagMet0KanNiet() {
		docent1.opslag(BigDecimal.ZERO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void opslagMetNegGetalKanNiet() {
		docent1.opslag(BigDecimal.valueOf(-1));
	}

	@Test
	public void eenNieuweDocentHeeftGeenBijnamen() {
		assertTrue(docent1.getBijnamen().isEmpty());
	}

	@Test
	public void bijnaamToevoegen() {
		assertTrue(docent1.addBijnaam("testBijnaam"));
		assertEquals(1, docent1.getBijnamen().size());
		assertTrue(docent1.getBijnamen().contains("testBijnaam"));
	}

	@Test
	public void tweeKeerDezelfdeBijnaamToevoegenKanNiet() {
		docent1.addBijnaam("testBijnaam");
		assertFalse(docent1.addBijnaam("testBijnaam"));
		assertEquals(1, docent1.getBijnamen().size());
	}

	@Test(expected = NullPointerException.class)
	public void nullAlsBijnaamToevoegenKanNiet() {
		docent1.addBijnaam(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void eenLegeBijnaamToevoegenKanNiet() {
		docent1.addBijnaam("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void eenBijnaamMetEnkelSpatiesToevoegenKanNiet() {
		docent1.addBijnaam(" ");
	}

	@Test
	public void bijnaamVerwijderen() {
		docent1.addBijnaam("testBijnaam");
		assertTrue(docent1.removeBijnaam("testBijnaam"));
		assertTrue(docent1.getBijnamen().isEmpty());
	}

	@Test
	public void eenBijnaamVerwijderenDieJeNietToevoegdeKanNiet() {
		docent1.addBijnaam("testBijnaam");
		assertFalse(docent1.removeBijnaam("testGeenBijnaam"));
		assertEquals(1, docent1.getBijnamen().size());
		assertTrue(docent1.getBijnamen().contains("testBijnaam"));
	}
	
	@Test 
	public void docentenZijnGelijkAlsHunEmailAdressenGelijkZijn() {
		assertEquals(docent1, nogEenDocent1);
	}
	
	@Test 
	public void docentenZijnVerschillendAlsHunEmailAdressenVerschillen() {
		assertNotEquals(docent1, docent2);
	}
	
	@Test 
	public void eenDocentVerschiltVanNull() {
		assertNotEquals(docent1, null);
	}
	
	@Test 
	public void	eenDocentVerschiltVanEenAnderTypeObject() {
		assertNotEquals(docent1, "");
	} 
	
	@Test 
	public void gelijkeDocentenGevenDezelfdeHashCode() {
		assertEquals(docent1.hashCode(), nogEenDocent1.hashCode()); 
	}

	@Test 
	public void verschillendeDocentenGevenVerschillendeHashCode() {
		assertNotEquals(docent1.hashCode(), docent2.hashCode()); 
	}
	
	@Test
	public void docent1KomtVoorInCampus1() {
		assertEquals(campus1,docent1.getCampus());
		assertTrue(campus1.getDocenten().contains(docent1));
	}
	
	@Test (expected =  NullPointerException.class)
	public void docent1KanNietVerhuizenNaarNull() {
		docent1.setCampus(null);
	}
	
	@Test
	public void docent1VerhuistNaarCampus2() {
		docent1.setCampus(campus2);
		assertNotEquals(campus1,docent1.getCampus());
		assertEquals(campus2,docent1.getCampus());
		assertFalse(campus1.getDocenten().contains(docent1));
		assertTrue(campus2.getDocenten().contains(docent1));
	}
	
	@Test
	public void verantwoordelijkheidToevoegen() {
		assertTrue(docent1.getVerantwoordelijkheden().isEmpty());
		assertTrue(docent1.addVerantwoordelijkheid(verantwoordelijkheid1));
		assertEquals(1,docent1.getVerantwoordelijkheden().size());
		assertTrue(docent1.getVerantwoordelijkheden().contains(verantwoordelijkheid1));
		assertEquals(1,verantwoordelijkheid1.getDocenten().size());
		assertTrue(verantwoordelijkheid1.getDocenten().contains(docent1));
	}
	
	@Test
	public void eenBestaandeVerantwoordelijkheidVerwijderen() {
		assertTrue(docent1.addVerantwoordelijkheid(verantwoordelijkheid1));
		assertTrue(docent1.removeVerantwoordelijkheid(verantwoordelijkheid1));
		assertTrue(docent1.getVerantwoordelijkheden().isEmpty());
		assertTrue(verantwoordelijkheid1.getDocenten().isEmpty());
	}
	
	@Test
	public void eenNietBestaandeVerantwoordelijkheidVerwijderen() {
		assertTrue(docent1.addVerantwoordelijkheid(verantwoordelijkheid1));
		assertFalse(docent1.removeVerantwoordelijkheid(new Verantwoordelijkheid("test")));
		assertEquals(1,docent1.getVerantwoordelijkheden().size());
		assertTrue(docent1.getVerantwoordelijkheden().contains(verantwoordelijkheid1));
		assertEquals(1,verantwoordelijkheid1.getDocenten().size());
		assertTrue(verantwoordelijkheid1.getDocenten().contains(docent1));
	}
}