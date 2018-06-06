package be.vdab.fietsacademy.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import be.vdab.fietsacademy.entities.Verantwoordelijkheid;

@Repository
class JpaVerantwoordelijkheidRepository implements VerantwoordelijkheidRepository {

	@Override
	public void create(Verantwoordelijkheid verantwoordelijkheid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Verantwoordelijkheid> read(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
