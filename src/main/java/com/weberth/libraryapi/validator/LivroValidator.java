package com.weberth.libraryapi.validator;

import com.weberth.libraryapi.exceptions.RegistroDuplicadoException;
import com.weberth.libraryapi.model.Livro;
import com.weberth.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {
    private final LivroRepository repository;

    public void validar(Livro livro){
        if(existeLivroComIsbn(livro)){
            throw new RegistroDuplicadoException("ISBN já cadastrado!");
        }
    }

    private boolean existeLivroComIsbn(Livro livro){
        Optional<Livro> livroEncontrado = repository.findByIsbn(livro.getIsbn());
        System.out.println("Encontrou o livro:" + livroEncontrado.isPresent());
        System.out.println("Dados do livro: " +livroEncontrado);

        //Se for id ==null o livro ainda não existe
        if(livro.getId() == null){
            return livroEncontrado.isPresent();
        }

        //Aqui o livro já tem id e esta sendo editado
        //Existe algum livro com esse ISBN que NÃO seja o mesmo livro que estou editando?”
        var resultado = livroEncontrado.
                map(Livro::getId).
                stream().
                // NÃO seja o mesmo livro que estou editando
                        anyMatch(id -> !id.equals((livro.getId())));
        System.out.println(resultado);
        return resultado;
//        return livroEncontrado.
//                map(Livro::getId).
//                stream().
//                anyMatch(id -> !id.equals((livro.getId())));

        /* FOrma mais facil para entender a logica acima
        if (livroEncontrado.isPresent()) {
            return !livroEncontrado.get().getId().equals(livro.getId());
        }
            return false
        * */
    }
}
