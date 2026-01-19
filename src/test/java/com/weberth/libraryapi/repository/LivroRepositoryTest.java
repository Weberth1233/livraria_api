package com.weberth.libraryapi.repository;

import com.weberth.libraryapi.model.GeneroLivro;
import com.weberth.libraryapi.model.Livro;
import com.weberth.libraryapi.model.Autor;

import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class LivroRepositoryTest {

    @Autowired
    LivroRepository repository;

    @Autowired
    AutorRepository autorRepository;

  @Test
    void salvarTest(){
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Harry Potter 2");
        livro.setDataPublicacao(LocalDate.of(1980, 1, 2));

        Autor autor = autorRepository.findById(UUID.fromString("32434550-d644-4689-baae-c6093474423a")).orElse(null);
        livro.setAutor(autor);

        repository.save(livro);
    }

   // @Test
    void salvarAutorELivroTest(){
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Terceiro Livro");
        livro.setDataPublicacao(LocalDate.of(1980, 1, 2));

        Autor autor = new Autor();
        autor.setNome("José");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951, 1, 31));

        autorRepository.save(autor);
        livro.setAutor(autor);
        repository.save(livro);
    }
    //@Test - Necessário que o campo autor em livros tenha a propriedade cascade
    void salvarCascadeTest(){
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Outro Livro");
        livro.setDataPublicacao(LocalDate.of(1980, 1, 2));

        Autor autor = new Autor();
        autor.setNome("João");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951, 1, 31));

        livro.setAutor(autor);

        repository.save(livro);
    }


   // @Test
    void atualizarAutorDoLivro(){
         UUID id = UUID.fromString("abf6cfdd-4716-46d5-b9e1-6645cd333f58");
         var livroParaAtualizar = repository.findById(id).orElse(null);

         UUID idAutor = UUID.fromString("c7234b31-aace-42de-bbb5-1bc4dc91b3b6");
         Autor autor = autorRepository.findById(idAutor).orElse(null);

         livroParaAtualizar.setAutor(autor);

         repository.save(livroParaAtualizar);
    }
   // @Test
    void deletar(){
        UUID id = UUID.fromString("67819440-d555-46ae-a379-5ea6a00bbafd");
        repository.deleteById(id);
    }
    //@Test - Necessário que o campo autor em livros tenha a propriedade cascade fazendo deletar tanto o livro como o autor do mesmo
    void deletarCascade(){
        UUID id = UUID.fromString("ac876622-f231-4234-bf7f-1fb9a93e117d");
        repository.deleteById(id);
    }

    //@Test
   // @Transactional
    void buscarLivroTest(){
        UUID id = UUID.fromString("abf6cfdd-4716-46d5-b9e1-6645cd333f58");
        Livro livro = repository.findById(id).orElse(null);
        System.out.println("Livro");
        System.out.println(livro.getTitulo());
    }

   // @Test
    void pesquisarPorTituloTest(){
        List<Livro> lista = repository.findByTitulo("O roubo da casa assombrada");
        lista.forEach(System.out::println);
    }
    //@Test
    void pesquisarPorISBNTest(){
        List<Livro> lista = repository.findByIsbn("20847-84874");
        lista.forEach(System.out::println);
    }

   // @Test
    void pesquisarPorTituloEPrecoTest(){
        var preco = BigDecimal.valueOf(204.00);
        var tituloPesquisa = "O roubo da casa assombrada";

        List<Livro> lista = repository.findByTituloAndPreco(tituloPesquisa, preco);
        lista.forEach(System.out::println);
    }

    //@Test
    void ListarLivrosComQueryJPQL(){
        var resultado = repository.listarTodosOrdenadoPorTituloAndPreco();
        resultado.forEach(System.out::println);
    }

  //  @Test
    void listarAutoresDosLivros(){
        var resultado = repository.listarAutoresDosLivros();
        resultado.forEach(System.out::println);
    }

    //@Test
    void listarTitulosNaoRepetidosDosLivros(){
        var resultado = repository.listarNomesDiferentesLivros();
        resultado.forEach(System.out::println);
    }

   // @Test
    void listarGenerosAutoresBrasileiros(){
        var resultado = repository.listarGenerosAutoresBrasileiros();
        resultado.forEach(System.out::println);
    }

    //@Test
    void listarPorGeneroQueryParamTest(){
        var resultado = repository.findByGenero(GeneroLivro.FICCAO, "preco");
        resultado.forEach(System.out::println);
    }

    //@Test
    void listarPorGeneroPositionalParamTest(){
        var resultado = repository.findByGeneroPositionalParameters("preco", GeneroLivro.FICCAO);
        resultado.forEach(System.out::println);
    }

   // @Test
    void updateDataPublicaoTest(){
        repository.updateDataPublicao(LocalDate.of(2000,1,1));
    }
}
