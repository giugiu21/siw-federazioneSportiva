package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long>{
	
	public boolean existsByNameAndLastname(String name, String lastname);
	
	public List<Player> findAllByEndDateIsNull();
	
	public List<Player> findAllByEndDateBefore(LocalDate now);

}
