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

public class VerantwoordelijkheidTest {
	private Verantwoordelijkheid verantwoordelijkheid1, kopieVerantwoordelijkheid1, verantwoordelijkheid2;
	private Campus campus1;
	private Docent docent1;
	
	@Before
	public void before() {
		verantwoordelijkheid1 = new Verantwoordelijkheid("EHBO");
		kopieVerantwoordelijkheid1 = new Verantwoordelijkheid("EHBO");
		verantwoordelijkheid2 = new Verantwoordelijkheid("OBHE");
		campus1 = new Campus("testNaam1", new Adres("testStraat1","testHuisNr1","testPostCode1","testGemeente1"));
		docent1 = new Docent("testVoornaam1", "testFamilienaam1", BigDecimal.ONE, "test1@fietsacademy.be", Geslacht.MAN,campus1);
	}
	
	@Test
	public void nieuweVerantwoordelijkheidHeeftGeenDocentenToegekend() {
		assertTrue(verantwoordelijkheid1.getDocenten().isEmpty());
	}
	
	@Test
	public void docentToevoegen() {
		assertTrue(verantwoordelijkheid1.add(docent1));
		assertEquals(1,verantwoordelijkheid1.getDocenten().size());
		assertTrue(verantwoordelijkheid1.getDocenten().contains(docent1));
		assertEquals(1,docent1.getVerantwoordelijkheden().size());
		assertTrue(docent1.getVerantwoordelijkheden().contains(verantwoordelijkheid1));
	}
	
	@Test
	public void tweeMaalDezelfdeDocentToevoegenKanNiet() {
		assertTrue(verantwoordelijkheid1.add(docent1));
		assertFalse(verantwoordelijkheid1.add(docent1));
		assertEquals(1,verantwoordelijkheid1.getDocenten().size());
		assertTrue(verantwoordelijkheid1.getDocenten().contains(docent1));
		assertEquals(1,docent1.getVerantwoordelijkheden().size());
		assertTrue(docent1.getVerantwoordelijkheden().contains(verantwoordelijkheid1));
	}
	
	@Test
	public void docentVerwijderen() {
		assertTrue(verantwoordelijkheid1.add(docent1));
		assertTrue(verantwoordelijkheid1.remove(docent1));
		assertTrue(verantwoordelijkheid1.getDocenten().isEmpty());
		assertTrue(docent1.getVerantwoordelijkheden().isEmpty());
	}
	
	@Test
	public void tweeMaalDezelfdeDocentVerwijderenKanNiet() {
		assertTrue(verantwoordelijkheid1.add(docent1));
		assertTrue(verantwoordelijkheid1.remove(docent1));
		assertFalse(verantwoordelijkheid1.remove(docent1));
		assertTrue(verantwoordelijkheid1.getDocenten().isEmpty());
		assertTrue(docent1.getVerantwoordelijkheden().isEmpty());
	}
	
	@Test 
	public void verantwoordelijkhedenZijnGelijkAlsHunNamenGelijkZijn() {
		assertEquals(verantwoordelijkheid1, kopieVerantwoordelijkheid1);
	}
	
	@Test 
	public void verantwoordelijkhedenZijnVerschillendAlsHunNamenVerschillen() {
		assertNotEquals(verantwoordelijkheid1,verantwoordelijkheid2);
	}
	
	@Test 
	public void eenVerantwoordelijkheidVerschiltVanNull() {
		assertNotEquals(verantwoordelijkheid1, null);
	}
	
	@Test 
	public void	eenVerantwoordelijkheidVerschiltVanEenAnderTypeObject() {
		assertNotEquals(verantwoordelijkheid1, "");
	} 
	
	@Test 
	public void gelijkeVerantwoordelijkhedenGevenDezelfdeHashCode() {
		assertEquals(verantwoordelijkheid1.hashCode(), kopieVerantwoordelijkheid1.hashCode()); 
	}

	@Test 
	public void verschillendeVerantwoordelijkhedenGevenVerschillendeHashCode() {
		assertNotEquals(verantwoordelijkheid1.hashCode(), verantwoordelijkheid2.hashCode()); 
	}
}
