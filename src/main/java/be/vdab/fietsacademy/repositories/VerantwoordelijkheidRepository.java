package be.vdab.fietsacademy.repositories;

import java.util.Optional;

import be.vdab.fietsacademy.entities.Verantwoordelijkheid;

public interface VerantwoordelijkheidRepository {
	void create(Verantwoordelijkheid verantwoordelijkheid);
	Optional<Verantwoordelijkheid> read(long id);
}
