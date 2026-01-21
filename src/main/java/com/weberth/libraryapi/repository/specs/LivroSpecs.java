package com.weberth.libraryapi.repository.specs;

import com.weberth.libraryapi.model.GeneroLivro;
import com.weberth.libraryapi.model.Livro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpecs {

    public static Specification<Livro> isbnEqual(String isbn){
        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn) ;
    }

    public static Specification<Livro> tituloLike(String titulo){
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");
    }

    public static Specification<Livro> generoEqual(GeneroLivro genero){
        return (root, query, cb) -> cb.equal(root.get("genero"), genero);
    }

    public static Specification<Livro> anoPublicacaoEqual(Integer anoPublicacao){
        //and to_char(data_publicacao, 'YYYY') =:anoPublicao
        return (root, query, cb) ->
                cb.equal(cb.function("to_char", String.class,
                        root.get("dataPublicacao"),
                        cb.literal("YYYY")),
                        anoPublicacao.toString());
    }

    public static Specification<Livro> NomeAutorLike(String nome){
        return (root, query, cb) -> {
            Join<Object, Object> joinAutor = root.join("autor", JoinType.LEFT);
            return  cb.like(cb.upper(joinAutor.get("nome")), "%" + nome.toUpperCase() + "%");
            //oUTRA MANEIRA DE FAZER A BUSCA POR AUTORES A PARTIR DO LIVRO
            //return cb.like(cb.upper(root.get("autor").get("nome")), "%" + nome.toUpperCase() + "%");

        };
    }
}
