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

import com.modelmapper.crud.dto.CarroDTO;
import com.modelmapper.crud.services.CarroService;

@RestController
@RequestMapping(value = "carros")
public class CarroResource {

	@Autowired
	private CarroService service;
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CarroDTO> update(@PathVariable Long id, @RequestBody CarroDTO dto){
		var obj = service.update(id, dto);
		return ResponseEntity.ok(obj);
	}
	
	@PostMapping
	public ResponseEntity<CarroDTO> insert(@RequestBody CarroDTO dto){
		var obj = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<CarroDTO> findById(@PathVariable Long id){
		var obj = service.findById(id);
		return ResponseEntity.ok(obj);
	}
	
	@GetMapping
	public ResponseEntity<List<CarroDTO>> findAll(){
		var obj = service.findAll();
		return ResponseEntity.ok(obj);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<CarroDTO> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
