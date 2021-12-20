package com.modelmapper.crud.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.modelmapper.crud.dto.PessoaDTO;
import com.modelmapper.crud.services.PessoaService;

@RestController
@RequestMapping(value = "pessoas")
public class PessoaResource {

	@Autowired
	private PessoaService service;
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<PessoaDTO> update(@PathVariable Long id, @RequestBody PessoaDTO dto){
		var obj = service.update(id, dto);
		return ResponseEntity.ok(obj);
	}
	
	@PostMapping
	public ResponseEntity<PessoaDTO> insert(@RequestBody PessoaDTO dto){
		var obj = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<PessoaDTO> findById(@PathVariable Long id){
		var obj = service.findById(id);
		return ResponseEntity.ok(obj);
	}
	
	@GetMapping
	public ResponseEntity<List<PessoaDTO>> findAll(){
		var obj = service.findAll();
		return ResponseEntity.ok(obj);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<PessoaDTO> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
