package br.com.treinarecife.acervo.literario.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.treinarecife.acervo.literario.api.entity.Livro;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    // Busca livros pelo nome de uma categoria
    List<Livro> findByCategoriasNome(String nome);

    // Busca livros pelo título, ignorando maiúsculas e minúsculas
    List<Livro> findByTituloContainingIgnoreCase(String termo);

}