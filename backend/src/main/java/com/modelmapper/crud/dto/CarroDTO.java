package com.modelmapper.crud.dto;

import java.io.Serializable;

import com.modelmapper.crud.entities.Carro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarroDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String modelo;
	private Long pessoaId;
	
	public CarroDTO(Carro obj) {
		id = obj.getId();
		nome = obj.getNome();
		modelo = obj.getModelo();
		pessoaId = obj.getPessoa().getId();
	}
}
