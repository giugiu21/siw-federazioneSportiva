package it.uniroma3.siw.model;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private String lastname;
	
	
	private Boolean isPresident;

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

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public Boolean getIsPresident() {
		return isPresident;
	}

	public void setIsPresident(Boolean isPresident) {
		this.isPresident = isPresident;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, isPresident, lastname, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return  Objects.equals(id, other.id)
				&& Objects.equals(isPresident, other.isPresident) && Objects.equals(lastname, other.lastname)
				&& Objects.equals(name, other.name);
	}
	

}
