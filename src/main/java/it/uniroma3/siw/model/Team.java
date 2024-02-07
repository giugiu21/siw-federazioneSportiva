package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "teams")
public class Team {
	
	@Id
	@GeneratedValue ( strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String name;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate foundingYear;
	

	private String address;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "president")
	private President president;
	
	@OneToMany
	private Set<Player> players;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getFoundingYear() {
		return foundingYear;
	}

	public void setFoundingYear(LocalDate foundingYear) {
		this.foundingYear = foundingYear;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public President getPresident() {
		return president;
	}

	public void setPresident(President president) {
		this.president = president;
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

}
