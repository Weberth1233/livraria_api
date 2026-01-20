package com.weberth.libraryapi.controller.mappers;

import com.weberth.libraryapi.controller.dto.CadastroLivroDTO;
import com.weberth.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import com.weberth.libraryapi.model.Livro;
import com.weberth.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
//uses diz ao Mapper do Livro para usar o AutorMapper como referencia do objeto autor em livro
@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java( autorRepository.findById(dto.idAutor()).orElse(null))")
    public abstract Livro toEntity(CadastroLivroDTO dto);

    public abstract ResultadoPesquisaLivroDTO toDTO(Livro livro);
}
