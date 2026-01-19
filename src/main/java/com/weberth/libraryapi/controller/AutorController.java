package com.weberth.libraryapi.controller;

import com.weberth.libraryapi.controller.dto.AutorDTO;
import com.weberth.libraryapi.controller.dto.ErrorResposta;
import com.weberth.libraryapi.exceptions.OperacaoNaoPermitidaException;
import com.weberth.libraryapi.exceptions.RegistroDuplicadoException;
import com.weberth.libraryapi.model.Autor;
import com.weberth.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService service;
//    public AutorController(AutorService service){
//        this.service =service;
//    }

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autor){
        try{
            var autorEntidade = autor.mapearParaAutor();
            service.salvar(autorEntidade);
            URI location =
                    ServletUriComponentsBuilder.
                            fromCurrentRequest().
                            path("/{id}").
                            buildAndExpand(autorEntidade.getId()).
                            toUri();
            return ResponseEntity.created(location).build();
        }catch (RegistroDuplicadoException e){
            var erroDTO = ErrorResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }
    //Obter autor pelo id
    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id){
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);
        if(autorOptional.isPresent()){
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(
                    autor.getId(),
                    autor.getNome(),
                    autor.getDataNascimento(),
                    autor.getNacionalidade()
            );
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        try {
            //Encontrando o usuario pelo o id do parametor id
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            service.deletar(autorOptional.get());
            return ResponseEntity.noContent().build();
        } catch (OperacaoNaoPermitidaException e) {
            var erroResponse = ErrorResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroResponse.status()).body(erroResponse);
        }
    }
    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade){
        List<Autor> resultado = service.pesquisar(nome, nacionalidade);

        //Converter uma lista de autor para autordto
        List<AutorDTO> lista = resultado.
                stream().
                map(autor -> new AutorDTO(
                autor.getId(),
                autor.getNome(),
                autor.getDataNascimento(),
                autor.getNacionalidade()
        )).toList();

        return ResponseEntity.ok(lista);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(
        @PathVariable("id") String id,@RequestBody @Valid AutorDTO dto) {
        try{
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.obterPorId(idAutor);

            //Caso ele não encontre um UUID na base de dados ele vai retornar 404
            if(autorOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            //Caso contrario ele vai atualizar os dados do autor com os novos passados por parametro dto
            var autor = autorOptional.get();
            autor.setNome(dto.nome());
            autor.setNacionalidade(dto.nacionalidade());
            autor.setDataNascimento(dto.dataNascimento());

            service.atualizar(autor);
            return ResponseEntity.noContent().build();
        }catch (RegistroDuplicadoException e){
            var erroDTO = ErrorResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

    }

}
