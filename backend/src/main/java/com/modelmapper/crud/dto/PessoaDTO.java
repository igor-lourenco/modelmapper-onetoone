package com.modelmapper.crud.dto;

import java.io.Serializable;

import com.modelmapper.crud.entities.Pessoa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PessoaDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String sobrenome;
	private CarroDTO carro;
	
	public PessoaDTO(Pessoa obj) {
		id = obj.getId();
		nome = obj.getNome();
		sobrenome = obj.getSobrenome();
		carro = new CarroDTO(obj.getCarro());
	}
}
