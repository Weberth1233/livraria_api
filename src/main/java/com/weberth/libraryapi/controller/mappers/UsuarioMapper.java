package com.weberth.libraryapi.controller.mappers;

import com.weberth.libraryapi.controller.dto.UsuarioDTO;
import com.weberth.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO dto);
}
