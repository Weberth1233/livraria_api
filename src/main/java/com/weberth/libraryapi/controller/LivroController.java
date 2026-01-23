package com.weberth.libraryapi.controller;
import com.weberth.libraryapi.controller.dto.CadastroLivroDTO;
import com.weberth.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import com.weberth.libraryapi.controller.mappers.LivroMapper;
import com.weberth.libraryapi.model.GeneroLivro;
import com.weberth.libraryapi.model.Livro;
import com.weberth.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController {
    private final LivroService service;
    private final LivroMapper mapper;

    @PostMapping
    //Os dois vão poder salvar livro
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
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
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(
            @PathVariable("id") String id) {
        return service.obterPorId(UUID.fromString(id)).map(livro -> {
            var dto = mapper.toDTO(livro);
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> deletar(
            @PathVariable("id") String id) {
        return service.obterPorId(UUID.fromString(id)).map(
                livro -> {
                    service.deletarLivro(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nomeAutor", required = false) String nomeAutor,
            @RequestParam(value = "genero", required = false) GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false) Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho-pagina",  defaultValue = "10") Integer tamanhoPagina

    ){
        Page<Livro> paginaResultado = service.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao, pagina, tamanhoPagina);

        Page<ResultadoPesquisaLivroDTO> resultado =  paginaResultado.map(mapper::toDTO);

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
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
