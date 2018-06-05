package be.vdab.fietsacademy.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class JpaCampusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final String CAMPUSSEN = "campussen";
	private static final String CAMPUSSENTELEFOONNUMMERS = "campussentelefoonnummers";
	@Autowired
	private JpaCampusRepository repository;
	@Autowired
	private EntityManager manager;
	private Campus campus;
	private TelefoonNr telefoonNr;
	private Docent docent;
	

	@Before
	public void before() {
		campus = new Campus("Naam", new Adres("Straat", "HuisNr", "PostCode", "Gemeente"));
		telefoonNr = new TelefoonNr("1",false,"testOpmerking");
		docent = new Docent("Voornaam","Familienaam",BigDecimal.TEN,"EmailAdres@fietsacademy.be",Geslacht.MAN);
	}

	private long idVanTestCampus() {
		return super.jdbcTemplate.queryForObject("select id from campussen where naam='testNaam'", Long.class);
	}
	
	@Test
	public void create_creert_een_campus() {
		int aantalCampussen = super.countRowsInTable(CAMPUSSEN);
		repository.create(campus);
		assertEquals(aantalCampussen + 1, super.countRowsInTable(CAMPUSSEN));
		assertEquals(1, super.countRowsInTableWhere(CAMPUSSEN, "id=" + campus.getId()));
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
		repository.create(campus);
		campus.addTelefoonNr(telefoonNr);
		manager.flush();
		assertEquals("1",super.jdbcTemplate.queryForObject(
				"select nummer from campussentelefoonnrs where campusid=?",String.class,campus.getId()));
	}
	
/*	@Test 
	public void telefoonNrVerwijderen() {
		Campus campus = repository.read(idVanTestCampus()).get();
		campus.removeTelefoonNr(telefoonNr);
		manager.flush();
		assertEquals(0,super.countRowsInTableWhere(CAMPUSSENTELEFOONNUMMERS,"campusid = "+campus.getId()+" and nummer = '1'"));		
	}*/
	
	@Test 
	public void docentToevoegen() {
		manager.persist(docent);
		repository.create(campus);
		campus.addDocent(docent);
		manager.flush();
		assertEquals("EmailAdres@fietsacademy.be",super.jdbcTemplate.queryForObject(
				"select emailAdres from docenten where campusid=?",String.class,campus.getId()));
	}
}