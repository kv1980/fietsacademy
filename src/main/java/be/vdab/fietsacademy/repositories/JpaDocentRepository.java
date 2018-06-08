package be.vdab.fietsacademy.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.springframework.stereotype.Repository;

import be.vdab.fietsacademy.entities.Docent;
import be.vdab.fietsacademy.queryresults.AantalDocentenPerWedde;
import be.vdab.fietsacademy.queryresults.IdEnEmailAdres;

@Repository
class JpaDocentRepository implements DocentRepository {
	private final EntityManager manager;
	
	public JpaDocentRepository(EntityManager manager) {
		this.manager = manager;
	}

	@Override
	public void create(Docent docent) {
		manager.persist(docent);		
	}
	
	@Override
	public Optional<Docent> read(long id) {
		return Optional.ofNullable(manager.find(Docent.class, id));
	}
	
	@Override
	public Optional<Docent> readWithLock(long id) {
		return Optional.ofNullable(manager.find(Docent.class, id,LockModeType.PESSIMISTIC_WRITE));
	}

	@Override
	public void delete(long id) {
		read(id).ifPresent(docent -> manager.remove(docent));
	}

	@Override
	public List<Docent> findAll() {
		return manager.createNamedQuery("Docent.findAll",Docent.class)
				      .setHint("javax.persistence.loadgraph",manager.createEntityGraph(Docent.MET_CAMPUS_EN_VERANTWOORDELIJKHEDEN))
					  .getResultList();
	}

	@Override
	public List<Docent> findByWeddeBetween(BigDecimal van, BigDecimal tot) {
		return manager.createNamedQuery("Docent.findByWeddeBetween",Docent.class)
				      .setParameter("van",van)
				      .setParameter("tot",tot)
				      .setHint("javax.persistence.loadgraph",manager.createEntityGraph(Docent.MET_CAMPUS_EN_VERANTWOORDELIJKHEDEN))
				      .getResultList();
	}

	@Override
	public List<String> findEmailAdressen() {
		return manager.createNamedQuery("Docent.findEmailAdressen",String.class)
					  .getResultList();
	}

	@Override
	public List<IdEnEmailAdres> findIdsEnEmailAdressen() {
		return manager.createNamedQuery("Docent.findIdsEnEmailAdressen", IdEnEmailAdres.class)
				      .getResultList();
	}

	@Override
	public BigDecimal findGrootsteWedde() {
		return manager.createNamedQuery("Docent.findGrootsteWedde",BigDecimal.class)
				      .getSingleResult();
	}

	@Override
	public List<AantalDocentenPerWedde> findAantalDocentenPerWedde() {
		return manager.createNamedQuery("Docent.findAantalDocentenPerWedde",AantalDocentenPerWedde.class)
					  .getResultList();
	}

	@Override
	public int algemeneOpslag(BigDecimal percentage) {
		return manager.createNamedQuery("Docent.algemeneOpslag")
					  .setParameter("factor",BigDecimal.ONE.add(percentage.divide(BigDecimal.valueOf(100))))
					  .setLockMode(LockModeType.PESSIMISTIC_WRITE)
					  .executeUpdate();
	}
}