package com.modelmapper.crud.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
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
	@Autowired
	private ModelMapper modelMapper;

	@Transactional
	public PessoaDTO insert(PessoaDTO dto) {
		Pessoa pessoa = new Pessoa();	
		copiaEntidade(pessoa, dto);
		pessoa = repository.save(pessoa);
		return modelMapper.map(pessoa, PessoaDTO.class);
	}

	@Transactional
	public PessoaDTO update(Long id, PessoaDTO dto) {
		try {
			Pessoa pessoa = repository.getById(id);	
			copiaEntidade(pessoa, dto);
			pessoa = repository.save(pessoa);
			return modelMapper.map(pessoa, PessoaDTO.class);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Pessoa não existe: " + id);
		}
	}

	@Transactional(readOnly = true)
	public List<PessoaDTO> findAll() {
		List<Pessoa> obj = repository.findAll(Sort.by("nome"));
		return obj.stream().map(p -> modelMapper.map(p, PessoaDTO.class)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public PessoaDTO findById(Long id) {
		Pessoa obj = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Pessoa não existe: " + id));
		return modelMapper.map(obj, PessoaDTO.class);
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
	
	private void copiaEntidade(Pessoa pessoa, PessoaDTO dto) {
		modelMapper.map(dto, pessoa);
		Carro carro = (pessoa.getCarro().getId() == null) ? new Carro() : carroRepository.getById(pessoa.getCarro().getId());
		carro.setPessoa(pessoa);
		modelMapper.map(dto.getCarro(), carro); // mapeia o dto dos campos do objeto carro para a entidade carro
		carroRepository.save(carro);
		pessoa.setCarro(carro);	
	}
}
