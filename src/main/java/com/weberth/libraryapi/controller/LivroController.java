package com.weberth.libraryapi.controller;

import com.weberth.libraryapi.controller.dto.CadastroLivroDTO;
import com.weberth.libraryapi.controller.dto.ErrorResposta;
import com.weberth.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import com.weberth.libraryapi.controller.mappers.LivroMapper;
import com.weberth.libraryapi.exceptions.RegistroDuplicadoException;
import com.weberth.libraryapi.model.GeneroLivro;
import com.weberth.libraryapi.model.Livro;
import com.weberth.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(
            @PathVariable("id") String id) {
        return service.obterPorId(UUID.fromString(id)).map(livro -> {
            var dto = mapper.toDTO(livro);
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(
            @PathVariable("id") String id) {
        return service.obterPorId(UUID.fromString(id)).map(
                livro -> {
                    service.deletarLivro(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nomeAutor", required = false) String nomeAutor,
            @RequestParam(value = "genero", required = false) GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false) Integer anoPublicacao
    ){
        var resultado = service.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao);
        var lista = resultado.
                stream().
                map(mapper::toDTO).
                toList();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody @Valid CadastroLivroDTO dto){
        return  service.obterPorId(UUID.fromString(id)).map(livro -> {
            Livro entidadeAux = mapper.toEntity(dto);

            livro.setIsbn(entidadeAux.getIsbn());
            livro.setTitulo(entidadeAux.getTitulo());
            livro.setAutor(entidadeAux.getAutor());
            livro.setGenero(entidadeAux.getGenero());
            livro.setPreco(entidadeAux.getPreco());
            livro.setDataPublicacao(entidadeAux.getDataPublicacao());

            service.atualizar(livro);
            return  ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
