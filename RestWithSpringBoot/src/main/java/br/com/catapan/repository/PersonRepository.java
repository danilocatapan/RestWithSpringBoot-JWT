package br.com.catapan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.catapan.data.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

}