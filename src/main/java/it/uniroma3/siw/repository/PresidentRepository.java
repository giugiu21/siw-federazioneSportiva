package it.uniroma3.siw.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.President;

@Repository
public interface PresidentRepository extends CrudRepository<President, Long>{

	//public President findByNameAndLastname(String name, String lastname);
	
	public President findByUsername(String username);
}
