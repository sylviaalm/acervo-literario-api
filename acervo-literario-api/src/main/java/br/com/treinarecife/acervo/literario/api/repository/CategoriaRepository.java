package br.com.treinarecife.acervo.literario.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.treinarecife.acervo.literario.api.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
