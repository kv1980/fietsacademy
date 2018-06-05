package be.vdab.fietsacademy.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

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
import be.vdab.fietsacademy.valueobjects.Adres;
import be.vdab.fietsacademy.valueobjects.TelefoonNr;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(JpaCampusRepository.class)
@Sql("/insertCampus.sql")
@Sql("/insertDocent.sql")
public class JpaCampusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final String CAMPUSSEN = "campussen";
	/*private static final String CAMPUSSENTELEFOONNUMMERS = "campussentelefoonnummers";*/
	@Autowired
	private JpaCampusRepository repository;
	@Autowired
	private EntityManager manager;
	private Campus campus1,campus2;
	private TelefoonNr telefoonNr;
	private Docent docent1;
	

	@Before
	public void before() {
		campus1 = new Campus("testNaam1", new Adres("testStraat1","testHuisNr1","testPostCode1","testGemeente1"));
		campus2 = new Campus("testNaam2", new Adres("testStraat2","testHuisnr2","testPostcode2","testGemeente2"));
		telefoonNr = new TelefoonNr("testNummer",false,"");
		docent1 = new Docent("testVoornaam1", "testFamilienaam1", BigDecimal.ONE, "test1@fietsacademy.be", Geslacht.MAN,campus1);

	}

	private long idVanTestCampus() {
		return super.jdbcTemplate.queryForObject("select id from campussen where naam='testNaam'", Long.class);
	}
	
	@Test
	public void create_creert_een_campus() {
		int aantalCampussen = super.countRowsInTable(CAMPUSSEN);
		repository.create(campus1);
		assertEquals(aantalCampussen + 1, super.countRowsInTable(CAMPUSSEN));
		assertEquals(1, super.countRowsInTableWhere(CAMPUSSEN, "id=" + campus1.getId()));
	}

	@Test
	public void read_leest_een_bestaande_campus() {
		Campus campus = repository.read(idVanTestCampus()).get();
		assertEquals("testNaam", campus.getNaam());
		assertEquals("testGemeente", campus.getAdres().getGemeente());
	}
	
	@Test
	public void read_leest_onbestaande_campus_niet() {
		assertFalse(repository.read(-1L).isPresent());
	}
	
/*	@Test
	public void delete_verwijdert_een_campus() {
		long id = idVanTestCampus();
		int aantalCampussen = super.countRowsInTable(CAMPUSSEN);
		repository.delete(id);
		manager.flush();
		assertEquals(aantalCampussen - 1, super.countRowsInTable(CAMPUSSEN));
		assertEquals(0,super.countRowsInTableWhere(CAMPUSSEN,"id = "+id));
	}*/
	
	@Test
	public void telefoonNrsLezen() {
		Campus campus = repository.read(idVanTestCampus()).get();
		assertEquals(1,campus.getTelefoonNrs().size());
		assertTrue(campus.getTelefoonNrs().contains(new TelefoonNr("1",false,"testOpmerking")));
	}
	
	@Test 
	public void telefoonNrToevoegen() {
		repository.create(campus1);
		campus1.addTelefoonNr(telefoonNr);
		manager.flush();
		assertEquals("testNummer",super.jdbcTemplate.queryForObject(
				"select nummer from campussentelefoonnrs where campusid=?",String.class,campus1.getId()));
	}
	
/*	@Test 
	public void telefoonNrVerwijderen() {
		Campus campus = repository.read(idVanTestCampus()).get();
		campus.removeTelefoonNr(telefoonNr);
		manager.flush();
		assertEquals(0,super.countRowsInTableWhere(CAMPUSSENTELEFOONNUMMERS,"campusid = "+campus.getId()+" and nummer = '1'"));		
	}*/
	
	@Test 
	public void eenCampusKrijgtEenDocentCorrectToegewezen() {
		repository.create(campus1);
		manager.persist(docent1);
		campus1.addDocent(docent1);
		manager.flush();
		assertEquals("test1@fietsacademy.be",super.jdbcTemplate.queryForObject(
				"select emailAdres from docenten where campusid=?",String.class,campus1.getId()));
	}
	
	@Test 
	public void deNieuweCampusKrijgtEenDocentCorrectToegewezenNaVerhuis() {
		repository.create(campus1);
		repository.create(campus2);
		manager.persist(docent1);
		campus1.addDocent(docent1);
		campus2.addDocent(docent1);
		manager.flush();
		assertEquals("test1@fietsacademy.be",super.jdbcTemplate.queryForObject(
				"select emailAdres from docenten where campusid=?",String.class,campus2.getId()));
	}
	
	@Test 
	public void deOudeCampusKrijgtEenDocentNietMeerToegewezenNaVerhuis() {
		repository.create(campus1);
		repository.create(campus2);
		manager.persist(docent1);
		campus1.addDocent(docent1);
		campus2.addDocent(docent1);
		manager.flush();
		assertEquals(0,super.countRowsInTableWhere("docenten","campusid = "+campus1.getId()));
	}
	
	@Test
	public void docentenLazyLoaded() {
		Campus campus = repository.read(idVanTestCampus()).get();
		assertEquals(2,campus.getDocenten().size());
		assertEquals("testM",campus.getDocenten().stream().findFirst().get().getVoornaam());
	}
}