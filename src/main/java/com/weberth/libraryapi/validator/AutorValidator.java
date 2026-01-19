package com.weberth.libraryapi.validator;

import com.weberth.libraryapi.exceptions.RegistroDuplicadoException;
import com.weberth.libraryapi.model.Autor;
import com.weberth.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {

    private AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    public void validar(Autor autor){
        if(existeAutorCadastrado(autor)){
            throw new RegistroDuplicadoException("Autor já cadastrado!");
        }
    }

    private boolean existeAutorCadastrado(Autor autor){
        Optional<Autor> autorEncontrado =
                repository.findByNomeAndDataNascimentoAndNacionalidade(autor.getNome(),autor.getDataNascimento(), autor.getNacionalidade());

        //Quer dizer que o autor não foi cadastrado ainda, ou seja, ele aida não possui id
        if(autor.getId() == null){
            System.out.println(autorEncontrado.isPresent());
            return autorEncontrado.isPresent();
        }

        //Verificar se o autor que estou encontrei ma busca pelo findByNomeAndDataNascimentoAndNacionalidade não é o mesmo que eu quero atualizar
        //Verificar se tem outro autor com nome, dataNacionalidade ou dataDeNascimento iguais que não seja o autor passado como parâmetro
        return !autor.getId().equals(autorEncontrado.get().getId()) && autorEncontrado.isPresent();

    }
}
