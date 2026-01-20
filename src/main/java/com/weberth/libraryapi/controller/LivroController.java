package com.weberth.libraryapi.controller;

import com.weberth.libraryapi.controller.dto.CadastroLivroDTO;
import com.weberth.libraryapi.controller.dto.ErrorResposta;
import com.weberth.libraryapi.controller.mappers.LivroMapper;
import com.weberth.libraryapi.exceptions.RegistroDuplicadoException;
import com.weberth.libraryapi.model.Livro;
import com.weberth.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController {
    private final LivroService service;
    private final LivroMapper mapper;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        //Mapear dto para entidade
        Livro livro = mapper.toEntity(dto);
        //Enviar a entidade para o serviço validar e salvar na base
        service.salvar(livro);
        //criar url para acesso dos dados do livro
        var url = gerarHeaderLocation(livro.getId());
        //retornar codigo created com header location
        return ResponseEntity.created(url).build();
    }
}
