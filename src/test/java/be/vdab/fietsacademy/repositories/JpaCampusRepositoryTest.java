package be.vdab.fietsacademy.repositories;

import static org.junit.Assert.assertEquals;

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

import be.vdab.fietsacademy.entities.Campus;
import be.vdab.fietsacademy.valueobjects.Adres;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(JpaCampusRepository.class)
@Sql("/insertCampus.sql")
public class JpaCampusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static final String CAMPUSSEN = "campussen";
	@Autowired
	private JpaCampusRepository repository;
	private Campus campus;

	@Before
	public void before() {
		campus = new Campus("Naam", new Adres("Straat", "HuisNr", "PostCode", "Gemeente"));
	}

	private long idVanTestCampus() {
		return super.jdbcTemplate.queryForObject("select id from campussen where naam='testNaam'", Long.class);
	}

	@Test
	public void read() {
		Campus campus = repository.read(idVanTestCampus()).get();
		assertEquals("testNaam", campus.getNaam());
		assertEquals("testGemeente", campus.getAdres().getGemeente());
	}

	@Test
	public void create() {
		int aantalCampussen = super.countRowsInTable(CAMPUSSEN);
		repository.create(campus);
		assertEquals(aantalCampussen + 1, super.countRowsInTable(CAMPUSSEN));
		assertEquals(1, super.countRowsInTableWhere(CAMPUSSEN, "id=" + campus.getId()));
	}
}