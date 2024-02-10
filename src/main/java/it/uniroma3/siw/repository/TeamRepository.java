package it.uniroma3.siw.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long>{
	
	public boolean existsByName(String name);
	
	public void deleteByName(String name);
	
//	@Query(value = "SELECT t FROM Teams t WHERE t.president = :president",  nativeQuery=true)
//	public Team getTeamFromPresident(@Param("president") President president);
	
	
	public Team findByPresident(President president);

	public List<Team> findByPresidentIsNotNull();
}
