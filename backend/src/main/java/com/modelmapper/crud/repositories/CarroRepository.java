package com.modelmapper.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.modelmapper.crud.entities.Carro;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long>{

}
