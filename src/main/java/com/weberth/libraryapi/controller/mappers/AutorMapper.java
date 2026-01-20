package com.weberth.libraryapi.controller.mappers;

import com.weberth.libraryapi.controller.dto.AutorDTO;
import com.weberth.libraryapi.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutorMapper {
    //Convertendo o autor em autor dto e vice versa
    //Caso os nomes seja, diferentes
    @Mapping(source = "nome", target = "nome")
    Autor toEntity(AutorDTO dto);
    AutorDTO toDTO(Autor autor);

}
