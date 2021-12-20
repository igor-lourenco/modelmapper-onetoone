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

import com.modelmapper.crud.dto.PessoaDTO;
import com.modelmapper.crud.entities.Carro;
import com.modelmapper.crud.entities.Pessoa;
import com.modelmapper.crud.repositories.CarroRepository;
import com.modelmapper.crud.repositories.PessoaRepository;
import com.modelmapper.crud.services.exceptions.DatabaseException;
import com.modelmapper.crud.services.exceptions.ResourceNotFoundException;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository repository;
	@Autowired
	private CarroRepository carroRepository;

	@Transactional
	public PessoaDTO insert(PessoaDTO dto) {
		Pessoa entity = new Pessoa();
		Carro carro = new Carro();
		copiaEntidade(entity, carro, dto);

		entity = repository.save(entity);
		return new PessoaDTO(entity);
	}

	@Transactional
	public PessoaDTO update(Long id, PessoaDTO dto) {
		try {
			Pessoa entity = repository.getById(id);
			Carro carro = carroRepository.getById(dto.getCarro().getId());
			copiaEntidade(entity, carro, dto);

			entity = repository.save(entity);
			return new PessoaDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Pessoa não existe: " + id);
		}
	}

	@Transactional(readOnly = true)
	public List<PessoaDTO> findAll() {
		List<Pessoa> obj = repository.findAll(Sort.by("nome"));
		return obj.stream().map(p -> new PessoaDTO(p)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public PessoaDTO findById(Long id) {
		Pessoa obj = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Pessoa não existe: " + id));
		return new PessoaDTO(obj);
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Pessoa não existe: " + id);

		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Pessoa não existe: " + id);
		}
	}

	private void copiaEntidade(Pessoa entity, Carro carro, PessoaDTO dto) {
		entity.setNome(dto.getNome());
		entity.setSobrenome(dto.getSobrenome());

		carro.setNome(dto.getCarro().getNome());
		carro.setModelo(dto.getCarro().getModelo());
		carro.setPessoa(entity);
		carroRepository.save(carro);

		entity.setCarro(carro);
	}
}
