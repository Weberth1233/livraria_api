package com.weberth.libraryapi.repository;

import com.weberth.libraryapi.model.Autor;
import com.weberth.libraryapi.model.GeneroLivro;
import com.weberth.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository repository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest() {
        Autor autor = new Autor();
        autor.setNome("Weberth Erik");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(2000, 9, 26));

        var autorSalvo = repository.save(autor);
        System.out.println("Autor Salvo: " + autorSalvo);
    }

   // @Test
    public void atualizarTest(){
        var id = UUID.fromString("c7234b31-aace-42de-bbb5-1bc4dc91b3b6");
        Optional<Autor> possivelAutor = repository.findById(id);
        if(possivelAutor.isPresent()){
            Autor autorEncontrado = possivelAutor.get();
            System.out.println("Dados do Autor");
            System.out.println(autorEncontrado);

            autorEncontrado.setDataNascimento(LocalDate.of(2000, 9, 18));
            repository.save(autorEncontrado);
        }
    }

    //@Test
    public void listarTest() {
        List<Autor> lista = repository.findAll();
        lista.forEach(System.out::println);
    }

    @Test
    public void countTest(){
        System.out.println("Contagem de autores" + repository.count());
    }

    @Test
    public void deletePorIdTest(){
        var id = UUID.fromString("c892c991-b2bb-4bd8-b80e-73e5a3f841d0");
        repository.deleteById(id);
    }

    //@Test
    public void deleteTest(){
        var id = UUID.fromString("6cfbeb37-a3ee-4e71-97e1-03c159f956a6");
        var item = repository.findById(id).get();
        repository.delete(item);
    }

    @Test
    void salvarAutorComLivros(){
        Autor autor = new Autor();
        autor.setNome("Vitor Alencar");
        autor.setNacionalidade("americano");
        autor.setDataNascimento(LocalDate.of(2000, 12, 30));

        Livro livro = new Livro();
        livro.setIsbn("20847-84874");
        livro.setPreco(BigDecimal.valueOf(204));
        livro.setGenero(GeneroLivro.MISTERIO);
        livro.setTitulo("O roubo da casa assombrada");
        livro.setDataPublicacao(LocalDate.of(1999, 1, 2));
        livro.setAutor(autor);

        Livro livro2 = new Livro();
        livro2.setIsbn("99999-84874");
        livro2.setPreco(BigDecimal.valueOf(650));
        livro2.setGenero(GeneroLivro.MISTERIO);
        livro2.setTitulo("O roubo da casa assombrada");
        livro2.setDataPublicacao(LocalDate.of(2000, 1, 2));
        livro2.setAutor(autor);

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);

        repository.save(autor);

    }

    @Test
    void listarLivrosAutor(){
        var id = UUID.fromString("54ce1ba8-abf2-48a7-aa7d-0c5e30150582");
        var autor = repository.findById(id).get();

        //Buscar os livros do autor
        List<Livro> livrosLista = livroRepository.findByAutor(autor);
        autor.setLivros(livrosLista);
        autor.getLivros().forEach(System.out::println);
    }
}