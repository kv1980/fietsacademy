package be.vdab.fietsacademy.repositories;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.EntityManager;

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

import be.vdab.fietsacademy.entities.Cursus;
import be.vdab.fietsacademy.entities.GroepsCursus;
import be.vdab.fietsacademy.entities.IndividueleCursus;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("/insertCursus.sql")
@Import(JpaCursusRepository.class)
public class JpaCursusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private CursusRepository repository;
	@Autowired
	private EntityManager manager;
	private static final String GROEPS_CURSUSSEN = "groepscursussen";   
	private static final String INDIVIDUELE_CURSUSSEN = "individuelecursussen";
	
	private String idVanTestGroep() {
		return super.jdbcTemplate.queryForObject("select id from groepscursussen where naam='testGroep'",String.class);
	}
	
	private String idVanTestIndividu() {
		return super.jdbcTemplate.queryForObject("select id from individuelecursussen where naam='testIndividu'",String.class);
	}
	
	@Test
	public void read_leest__bestaande_groepscursus_in() {
		Optional<Cursus> optioneleCursus = repository.read(idVanTestGroep());
		assertEquals("testGroep", ((GroepsCursus)optioneleCursus.get()).getNaam());
	}
	
	@Test
	public void read_leest__bestaande_individuele_cursus_in() {
		Optional<Cursus> optioneleCursus = repository.read(idVanTestIndividu());
		assertEquals("testIndividu",((IndividueleCursus) optioneleCursus.get()).getNaam());
	}
	
	@Test   
	public void createGroepsCursus() {    
		int aantalGroepsCursussen = super.countRowsInTable(GROEPS_CURSUSSEN);     
		GroepsCursus cursus = new GroepsCursus("testGroep2", LocalDate.of(2018,01,01), LocalDate.of(2018, 12, 31));     
		repository.create(cursus); 
		manager.flush();
		assertEquals(aantalGroepsCursussen + 1, super.countRowsInTable(GROEPS_CURSUSSEN));
		assertEquals(1,super.countRowsInTableWhere(GROEPS_CURSUSSEN, "id='"+cursus.getId()+"'")); 
	}   
	
	@Test   
	public void createIndividueleCursus() {   
		int aantalIndividueleCursussen = super.countRowsInTable(INDIVIDUELE_CURSUSSEN);    
		IndividueleCursus cursus = new IndividueleCursus("testIndividueel2", 7);     
		repository.create(cursus);    
		manager.flush();
		assertEquals(aantalIndividueleCursussen + 1, super.countRowsInTable(INDIVIDUELE_CURSUSSEN));
		assertEquals(1,super.countRowsInTableWhere(INDIVIDUELE_CURSUSSEN, "id='"+cursus.getId()+"'"));    
	} 
}
