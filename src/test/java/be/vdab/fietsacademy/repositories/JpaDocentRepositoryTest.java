package be.vdab.fietsacademy.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import be.vdab.fietsacadamy.enums.Geslacht;
import be.vdab.fietsacademy.entities.Campus;
import be.vdab.fietsacademy.entities.Docent;
import be.vdab.fietsacademy.entities.Verantwoordelijkheid;
import be.vdab.fietsacademy.queryresults.AantalDocentenPerWedde;
import be.vdab.fietsacademy.queryresults.IdEnEmailAdres;
import be.vdab.fietsacademy.valueobjects.Adres;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("/insertCampus.sql")
@Sql("/insertVerantwoordelijkheid.sql")
@Sql("/insertDocent.sql")
@Sql("/insertDocentVerantwoordelijkheid.sql")
@Import(JpaDocentRepository.class)
public class JpaDocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private JpaDocentRepository repository;
	@Autowired
	private EntityManager manager;
	private static final String DOCENTEN = "docenten";
	private static final String DOCENTENBIJNAMEN = "docentenbijnamen";
	private Campus campus1,campus2;
	private Docent docent1;


	@Before
	public void before() {
		campus1 = new Campus("testNaam1", new Adres("testStraat1","testHuisNr1","testPostCode1","testGemeente1"));
		campus2 = new Campus("testNaam2", new Adres("testStraat2","testHuisnr2","testPostcode2","testGemeente2"));
		docent1 = new Docent("testVoornaam1", "testFamilienaam1", BigDecimal.ONE, "test1@fietsacademy.be", Geslacht.MAN,campus1);
	}

	private long idVanTestMan() {
		return super.jdbcTemplate.queryForObject("select id from docenten where voornaam = 'testM'", Long.class);
	}

	private long idVanTestVrouw() {
		return super.jdbcTemplate.queryForObject("select id from docenten where voornaam = 'testV'", Long.class);
	}

	@Test
	public void create_voegt_een_docent_toe() {
		int aantalDocenten = super.countRowsInTable(DOCENTEN);
		manager.persist(campus1);
		repository.create(docent1);
		manager.flush();
		assertEquals(aantalDocenten + 1, super.countRowsInTable(DOCENTEN));
		assertNotEquals(0, docent1.getId());
		assertEquals(1, super.countRowsInTableWhere(DOCENTEN, "id = " + docent1.getId()));
		assertEquals(campus1.getId(),super.jdbcTemplate.queryForObject("select campusid from docenten where id=?", Long.class, docent1.getId()).longValue());
		assertTrue(campus1.getDocenten().contains(docent1));
	}

	@Test
	public void read_leest_bestaande_docent() {
		Docent docent = repository.read(idVanTestMan()).get();
		assertEquals("testM", docent.getVoornaam());
	}

	@Test
	public void read_leest_onbestaande_docent_niet() {
		assertFalse(repository.read(-1L).isPresent());
	}

	@Test
	public void read_leest_correct_geslacht_in() {
		assertEquals(Geslacht.MAN, repository.read(idVanTestMan()).get().getGeslacht());
		assertEquals(Geslacht.VROUW, repository.read(idVanTestVrouw()).get().getGeslacht());
	}

	@Test
	public void delete_verwijdert_een_docent() {
		long id = idVanTestMan();
		int aantalDocenten = super.countRowsInTable(DOCENTEN);
		repository.delete(id);
		manager.flush();
		assertEquals(aantalDocenten - 1, super.countRowsInTable(DOCENTEN));
		assertEquals(0, super.countRowsInTableWhere(DOCENTEN, "id = " + id));
	}

	@Test
	public void findAll() {
		List<Docent> docenten = repository.findAll();
		manager.clear();
		assertEquals(super.countRowsInTable(DOCENTEN), docenten.size());
		BigDecimal vorigeWedde = BigDecimal.ZERO;
		for (Docent docent : docenten) {
			assertTrue(docent.getWedde().compareTo(vorigeWedde) >= 0);
			vorigeWedde = docent.getWedde();
			//System.out.println(docent.getFamilienaam()+" : "+docent.getWedde()+" en "+docent.getCampus().getNaam());
		}
	}

	// -----------------------mogelijke verbetering------------------------------------------
	@Test
	public void findByWeddeBetween() {
		BigDecimal duizend = BigDecimal.valueOf(1000);
		BigDecimal tweeduizend = BigDecimal.valueOf(2000);
		List<Docent> docenten = repository.findByWeddeBetween(duizend, tweeduizend);
		manager.clear();
		long aantalDocenten = super.countRowsInTableWhere(DOCENTEN, "wedde between 1000 and 2000");
		assertEquals(aantalDocenten, docenten.size());
		docenten.forEach(docent -> {
			assertTrue(docent.getWedde().compareTo(duizend) >= 0);
			assertTrue(docent.getWedde().compareTo(tweeduizend) <= 0);
		//System.out.println(docent.getFamilienaam()+" : "+docent.getWedde()+" en "+docent.getCampus().getNaam());
		});
	}

	// -----------------------mogelijke verbetering------------------------------------------
	@Test
	public void findEmailAdressen() {
		List<String> adressen = repository.findEmailAdressen();
		long aantal = super.jdbcTemplate.queryForObject("select count(distinct emailadres) from docenten", Long.class);
		assertEquals(aantal, adressen.size());
		adressen.forEach(adres -> Pattern.matches("^[a-z.]+@[a-z]+[.]{1}[a-z]{2,3}$", adres));
	}

	@Test
	public void findIdsEnEmailAdressen() {
		List<IdEnEmailAdres> idsEnEmailAdressen = repository.findIdsEnEmailAdressen();
		assertEquals(super.countRowsInTable(DOCENTEN), idsEnEmailAdressen.size());
		idsEnEmailAdressen.forEach(
				idEnEmailAdres -> Pattern.matches("^[a-z.]+@[a-z]+[.]{1}[a-z]{2,3}$", idEnEmailAdres.getEmailAdres()));
	}

	@Test
	public void findGrootsteWedde() {
		BigDecimal grootsteWeddeA = repository.findGrootsteWedde();
		BigDecimal grootsteWeddeB = super.jdbcTemplate.queryForObject("select max(wedde) from docenten",
				BigDecimal.class);
		assertEquals(0, grootsteWeddeA.compareTo(grootsteWeddeB));
	}

	@Test
	public void findAantalDocentenPerWedde() {
		List<AantalDocentenPerWedde> aantalDocentenPerWedde = repository.findAantalDocentenPerWedde();
		long aantalUniekeWeddes = super.jdbcTemplate.queryForObject("select count(distinct wedde) from docenten",
				Long.class);
		assertEquals(aantalUniekeWeddes, aantalDocentenPerWedde.size());
		long aantalDocentenMetWedde1000 = super.countRowsInTableWhere(DOCENTEN, "wedde = 1000");
		aantalDocentenPerWedde.stream()
				.filter(WeddeEnAantal -> WeddeEnAantal.getWedde().compareTo(BigDecimal.valueOf(1000)) == 0)
				.forEach(WeddeEnAantal -> assertEquals(aantalDocentenMetWedde1000, WeddeEnAantal.getAantal()));
	}

	@Test
	public void algemeneOpslag() {
		int aantalRecordsAangepast = repository.algemeneOpslag(BigDecimal.TEN);
		assertEquals(super.countRowsInTable(DOCENTEN), aantalRecordsAangepast);
		BigDecimal nieuweWeddeVanTestM = super.jdbcTemplate.queryForObject("select wedde from docenten where id=?",
				BigDecimal.class, idVanTestMan());
		assertEquals(0, nieuweWeddeVanTestM.compareTo(BigDecimal.valueOf(1_100)));
	}
	
	@Test
	public void bijnamenLezen() {
		Docent docent = repository.read(idVanTestMan()).get();
		assertEquals(1,docent.getBijnamen().size());
		assertTrue(docent.getBijnamen().contains("testBijnaam"));
	}
	
	@Test 
	public void bijnaamToevoegen() {
		manager.persist(campus1);
		repository.create(docent1);
		docent1.addBijnaam("testBijnaam");
		manager.flush();
		assertEquals("testBijnaam",super.jdbcTemplate.queryForObject(
				"select bijnaam from docentenbijnamen where docentid=?",String.class,docent1.getId()));
	}
	
	@Test 
	public void bijnaamVerwijderen() {
		Docent docent = repository.read(idVanTestMan()).get();
		docent.removeBijnaam("testBijnaam");
		manager.flush();
		assertEquals(0,super.countRowsInTableWhere(DOCENTENBIJNAMEN,"docentid = "+docent.getId()+" and bijnaam = 'testBijnaam'"));		
	}
	
	@Test
	public void eenDocentWordtCorrectToegevoegdAanEenCampus() {
		manager.persist(campus1);
		repository.create(docent1);
		manager.flush();
		assertEquals("test1@fietsacademy.be",super.jdbcTemplate.queryForObject(
				"select emailAdres from docenten where campusid=?",String.class,campus1.getId()));
	}
	
	@Test
	public void eenDocentIsNietMeerToegevoegdAanDeOudeCampusNaVerhuis() {
		manager.persist(campus1);
		manager.persist(campus2);
		repository.create(docent1);
		docent1.setCampus(campus2);
		manager.flush();
		assertEquals(0,super.countRowsInTableWhere("docenten","campusid = "+campus1.getId()));
	}
	
	@Test
	public void eenDocentWordtCorrectToegevoegdAanEenNieuweCampusNaVerhuis() {
		manager.persist(campus1);
		manager.persist(campus2);
		repository.create(docent1);
		docent1.setCampus(campus2);
		manager.flush();
		assertEquals("test1@fietsacademy.be",super.jdbcTemplate.queryForObject(
				"select emailAdres from docenten where campusid=?",String.class,campus2.getId()));
	}
	
	@Test
	public void verantwoordelijkhedenLezen() {
		Docent docent = repository.read(idVanTestMan()).get();
		assertEquals(1, docent.getVerantwoordelijkheden().size());
		assertTrue(docent.getVerantwoordelijkheden().contains(new Verantwoordelijkheid("testMetDocenten")));
		}
	
	@Test 
	public void verantwoordelijkheidToevoegen() {
		Verantwoordelijkheid verantwoordelijkheid = new Verantwoordelijkheid("test2");
		manager.persist(verantwoordelijkheid);
		manager.persist(campus1);
		repository.create(docent1);
		docent1.addVerantwoordelijkheid(verantwoordelijkheid);
		manager.flush();
		assertEquals(verantwoordelijkheid.getId(), super.jdbcTemplate.queryForObject("select verantwoordelijkheidid from docentenverantwoordelijkheden where docentid=?", Long.class, docent1.getId()).longValue());
		}	
}
