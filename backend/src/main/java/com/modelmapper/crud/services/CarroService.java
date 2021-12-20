package com.modelmapper.crud.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modelmapper.crud.dto.CarroDTO;
import com.modelmapper.crud.entities.Carro;
import com.modelmapper.crud.entities.Pessoa;
import com.modelmapper.crud.repositories.CarroRepository;
import com.modelmapper.crud.repositories.PessoaRepository;
import com.modelmapper.crud.services.exceptions.DatabaseException;
import com.modelmapper.crud.services.exceptions.ResourceNotFoundException;

@Service
public class CarroService {

	@Autowired
	private CarroRepository repository;
	@Autowired
	private PessoaRepository pessoaRepository;

	@Transactional
	public CarroDTO insert(CarroDTO dto) {
		Carro entity = new Carro();
		entity.setNome(dto.getNome());
		entity.setModelo(dto.getModelo());
		
		Pessoa pessoa = new Pessoa();
		pessoa.setCarro(entity);
		pessoaRepository.save(pessoa);
		
		entity.setPessoa(pessoa);
		entity = repository.save(entity);
		return new CarroDTO(entity);
	}

	@Transactional
	public CarroDTO update(Long id, CarroDTO dto) {
		try {
			Carro entity = repository.getById(id);
			entity.setNome(dto.getNome());
			entity.setModelo(dto.getModelo());
			
			Pessoa pessoa = pessoaRepository.getById(dto.getPessoaId());
			pessoaRepository.save(pessoa);
			
			entity.setPessoa(pessoa);
			entity = repository.save(entity);
			return new CarroDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Carro não existe: " + id);
		}
	}

	@Transactional(readOnly = true)
	public List<CarroDTO> findAll() {
		List<Carro> obj = repository.findAll(Sort.by("nome"));
		return obj.stream().map(CarroDTO::new).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CarroDTO findById(Long id) {
		Carro obj = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Carro não existe: " + id));
		return new CarroDTO(obj);
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Carro não existe: " + id);

		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Carro não existe: " + id);

		}
	}
}
