package it.uniroma3.siw.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long>{
	
	public boolean existsByName(String name);
	
	public void deleteByName(String name);

	
	public Team findByPresident(President president);

	public List<Team> findByPresidentIsNotNull();
	
	
	
}
