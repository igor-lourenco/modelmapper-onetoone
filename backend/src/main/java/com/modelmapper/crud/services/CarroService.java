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
	@Autowired
	private ModelMapper modelMapper;

	@Transactional
	public CarroDTO insert(CarroDTO dto) {
		Carro carro = new Carro();
		copiaEntidade(carro, dto);		
		carro = repository.save(carro);
		return modelMapper.map(carro, CarroDTO.class);
	}

	@Transactional
	public CarroDTO update(Long id, CarroDTO dto) {
		try {
			Carro carro = repository.getById(id);
			copiaEntidade(carro, dto);	
			carro = repository.save(carro);
			return modelMapper.map(carro, CarroDTO.class);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Carro não existe: " + id);
		}
	}

	@Transactional(readOnly = true)
	public List<CarroDTO> findAll() {
		List<Carro> obj = repository.findAll(Sort.by("nome"));
		return obj.stream().map(c -> modelMapper.map(c , CarroDTO.class)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CarroDTO findById(Long id) {
		Carro obj = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Carro não existe: " + id));
		return modelMapper.map(obj, CarroDTO.class);
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
	
	private void copiaEntidade(Carro carro, CarroDTO dto) {
		modelMapper.map(dto, carro);
		Pessoa pessoa = (carro.getPessoa() == null) ? new Pessoa()  : pessoaRepository.getById(carro.getPessoa().getId());
		pessoa.setCarro(carro);
		pessoaRepository.save(pessoa);
		carro.setPessoa(pessoa);	
	}

}
