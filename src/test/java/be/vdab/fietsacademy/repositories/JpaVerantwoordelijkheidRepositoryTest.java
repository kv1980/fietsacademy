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
import be.vdab.fietsacademy.entities.Verantwoordelijkheid;
import be.vdab.fietsacademy.valueobjects.Adres;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("/insertCampus.sql")
@Sql("/insertVerantwoordelijkheid.sql")
@Sql("/insertDocent.sql")
@Sql("/insertDocentVerantwoordelijkheid.sql")
@Import(JpaVerantwoordelijkheidRepository.class)
public class JpaVerantwoordelijkheidRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	JpaVerantwoordelijkheidRepository repository;
	private static final String VERANTWOORDELIJKHEDEN = "verantwoordelijkheden";
	private static final String DOCENTENVERANTWOORDELIJKHEDEN = "docentenverantwoordelijkheden";
	@Autowired
	private EntityManager manager;
	private Verantwoordelijkheid verantwoordelijkheid;
	private Campus campus;
	private Docent docent;
	
	@Before
	public void before() {
		verantwoordelijkheid = new Verantwoordelijkheid("testNaam");
		campus = new Campus("test",new Adres("test","test","test","test"));
		docent = new Docent("testM","test",BigDecimal.ZERO,"test",Geslacht.MAN,campus);
	}
	
	private long idVanTestVerantwoordelijkheidZonderDocenten() {
		return super.jdbcTemplate.queryForObject("select id from verantwoordelijkheden where naam='testZonderDocenten'",Long.class);
	}
	
	private long idVanTestVerantwoordelijkheidMetDocenten() {
		return super.jdbcTemplate.queryForObject("select id from verantwoordelijkheden where naam='testMetDocenten'",Long.class);
	}
	
	@Test
	public void create_voegt_een_verantwoordelijkheid_correct_toe() {
		int aantalVerantwoordelijkheden = super.countRowsInTable(VERANTWOORDELIJKHEDEN);
		repository.create(verantwoordelijkheid);
		manager.flush();
		assertEquals(aantalVerantwoordelijkheden+1,super.countRowsInTable(VERANTWOORDELIJKHEDEN));
		assertEquals(1,super.countRowsInTableWhere(VERANTWOORDELIJKHEDEN,"id = "+verantwoordelijkheid.getId()));
		assertEquals("testNaam",super.jdbcTemplate.queryForObject("select naam from verantwoordelijkheden where id=?",String.class,verantwoordelijkheid.getId()));
	}
	
	@Test 
	public void read_leest_een_onbestaande_verantwoordelijkheid_niet_uit() {
		assertFalse(repository.read(-1L).isPresent());
	}
	
	@Test
	public void read_leest_een_bestaande_verantwoordelijkheid_zonder_docenten_correct_uit() {
		Verantwoordelijkheid verantwoordelijkheid = repository.read(idVanTestVerantwoordelijkheidZonderDocenten()).get();
		assertEquals("testZonderDocenten",verantwoordelijkheid.getNaam());
		assertTrue(verantwoordelijkheid.getDocenten().isEmpty());
	}
	
	@Test
	public void read_leest_een_bestaande_verantwoordelijkheid_met_docenten_correct_uit() {
		Verantwoordelijkheid verantwoordelijkheid = repository.read(idVanTestVerantwoordelijkheidMetDocenten()).get();
		assertEquals("testMetDocenten",verantwoordelijkheid.getNaam());
		assertEquals(1, verantwoordelijkheid.getDocenten().size());
		assertEquals(1L,verantwoordelijkheid.getDocenten().stream()
				                                          .filter(docent -> docent.getEmailAdres().equals("test@fietsacademy.be"))
				                                          .count());
	}
	
	@Test
	public void add_voegt_een_docent_toe_aan_een_verantwoordelijkheid() {
		manager.persist(campus);
		manager.persist(docent);
		Verantwoordelijkheid verantwoordelijkheid = repository.read(idVanTestVerantwoordelijkheidZonderDocenten()).get();
		long aantalDocentenVoorToevoeging = super.countRowsInTableWhere(DOCENTENVERANTWOORDELIJKHEDEN,"verantwoordelijkheidid = "+verantwoordelijkheid.getId());
		verantwoordelijkheid.add(docent);
		manager.flush();
		assertEquals(aantalDocentenVoorToevoeging+1,super.countRowsInTableWhere(DOCENTENVERANTWOORDELIJKHEDEN,"verantwoordelijkheidid = "+verantwoordelijkheid.getId()));
		assertEquals(docent.getId(), super.jdbcTemplate.queryForObject(
				"select docentid from docentenverantwoordelijkheden where verantwoordelijkheidid=?", Long.class, verantwoordelijkheid.getId()).longValue());
	}
	
	@Test
	public void remove_verwijdert_een_docent_toe_aan_een_verantwoordelijkheid() {
		Verantwoordelijkheid verantwoordelijkheid = repository.read(idVanTestVerantwoordelijkheidMetDocenten()).get();
		long aantalDocentenVoorVerwijdering = super.countRowsInTableWhere(DOCENTENVERANTWOORDELIJKHEDEN,"verantwoordelijkheidid = "+verantwoordelijkheid.getId());
		Docent docent = verantwoordelijkheid.getDocenten().stream()
													      .findFirst()
													      .get();
		verantwoordelijkheid.remove(docent);
		manager.flush();
		assertEquals(aantalDocentenVoorVerwijdering-1,super.countRowsInTableWhere(DOCENTENVERANTWOORDELIJKHEDEN,"verantwoordelijkheidid = "+verantwoordelijkheid.getId()));
		assertEquals(0,super.countRowsInTableWhere(DOCENTENVERANTWOORDELIJKHEDEN,"verantwoordelijkheidid ="+verantwoordelijkheid.getId()+" and docentid = "+docent.getId()));
	}
}
