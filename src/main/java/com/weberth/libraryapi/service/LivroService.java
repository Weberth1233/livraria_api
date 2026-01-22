package com.weberth.libraryapi.service;

import com.weberth.libraryapi.model.GeneroLivro;
import com.weberth.libraryapi.model.Livro;
import com.weberth.libraryapi.repository.LivroRepository;
import com.weberth.libraryapi.repository.specs.LivroSpecs;
import com.weberth.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;
    private final LivroValidator validator;


    public Livro salvar(Livro livro){
        validator.validar(livro);
        return repository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id){
        return repository.findById(id);
    }

    public void deletarLivro(Livro livro){
        repository.delete(livro);
    }

    public List<Livro> pesquisa(String isbn,String titulo, String nomeAutor, GeneroLivro genero, Integer anoPublicacao){
//        Specification<Livro> specs = null;
        //select * from livro where 0 = 0
        Specification<Livro> specs = Specification.where(((root, query, cb) -> cb.conjunction() ));
        if(isbn != null){
            specs = specs.and(LivroSpecs.isbnEqual(isbn));
        }
        if(titulo != null){
            specs = specs.and(LivroSpecs.tituloLike(titulo));
        }
        if(genero != null){
            specs = specs.and(LivroSpecs.generoEqual(genero));
        }
        if(anoPublicacao != null){
            specs = specs.and(LivroSpecs.anoPublicacaoEqual(anoPublicacao));
        }
        if(nomeAutor != null){
            specs = specs.and(LivroSpecs.NomeAutorLike(nomeAutor));

        }
        return repository.findAll(specs);
    }

    public void atualizar(Livro livro){
        if(livro.getId() == null){
            throw new IllegalArgumentException("Para atualizar é necessário que o livro já esteja salvo na base.");
        }
        validator.validar(livro);
        repository.save(livro);
    }
}
