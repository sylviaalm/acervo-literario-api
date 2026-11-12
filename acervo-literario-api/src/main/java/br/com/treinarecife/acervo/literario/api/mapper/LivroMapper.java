package br.com.treinarecife.acervo.literario.api.mapper;



import java.util.List;

import org.springframework.stereotype.Component;

import br.com.treinarecife.acervo.literario.api.dto.request.LivroRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.CategoriaResponseDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.LivroResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.entity.Livro;



@Component
public class LivroMapper {

    private final AutorMapper autorMapper;
    private final CategoriaMapper categoriaMapper;

    public LivroMapper(AutorMapper autorMapper, CategoriaMapper categoriaMapper) {
        this.autorMapper = autorMapper;
        this.categoriaMapper = categoriaMapper;
    }

    public Livro toEntity(LivroRequestDTO dto, Autor autor, List<Categoria> categorias) {
        Livro livro = new Livro();
        livro.setTitulo(dto.titulo());
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setAutor(autor);
        livro.setCategorias(categorias);
        return livro;
    }

    public LivroResponseDTO toResponseDTO(Livro livro) {
        List<CategoriaResponseDTO> categoriasDTO = livro.getCategorias()
                .stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();

        return new LivroResponseDTO(
            livro.getId(),
            livro.getTitulo(),
            livro.getIsbn(),
            livro.getAnoPublicacao(),
            autorMapper.toResponseDTO(livro.getAutor()),
            categoriasDTO
        );
    }
}
