package com.modelmapper.crud.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.modelmapper.crud.dto.CarroDTO;
import com.modelmapper.crud.dto.PessoaDTO;
import com.modelmapper.crud.entities.Carro;
import com.modelmapper.crud.entities.Pessoa;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();

		// mapeando id da Pessoa com pessoaID do Carro
		mapper.createTypeMap(Pessoa.class, PessoaDTO.class)
		.addMapping(p -> p.getId(),	(dest, valor) -> dest.getCarro().setPessoaId((Long) valor))
		.addMapping(p -> p.getCarro(),	(dest, valor) -> dest.setCarro((CarroDTO) valor))
		.addMapping(p -> p.getCarro().getId(), (dest, valor) -> dest.getCarro().setId((Long)valor));
		
		// método para fazer update no service e associar dto com a entidade
		mapper.addMappings(new PropertyMap<PessoaDTO, Pessoa>() {
			@Override
			protected void configure() {
				skip(destination.getId());
			}
		});
		// método para fazer update no service e associar dto com a entidade
		mapper.addMappings(new PropertyMap<CarroDTO, Carro>() {
			@Override
			protected void configure() {
				skip(destination.getId());
			}
		});

		return mapper;
	}
}