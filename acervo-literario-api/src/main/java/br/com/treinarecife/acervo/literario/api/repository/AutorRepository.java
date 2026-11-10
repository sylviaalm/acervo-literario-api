package br.com.treinarecife.acervo.literario.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.treinarecife.acervo.literario.api.entity.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {

}