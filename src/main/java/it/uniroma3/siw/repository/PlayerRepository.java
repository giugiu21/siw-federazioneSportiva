package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Player;

public interface PlayerRepository extends CrudRepository<Player, Long>{

}