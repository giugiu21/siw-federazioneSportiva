package it.uniroma3.siw.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.President;

public interface PresidentRepository extends CrudRepository<President, Long>{

	public List<President> findByNameAndLastname(String name, String lastname);
}
