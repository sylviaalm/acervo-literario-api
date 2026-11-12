package br.com.treinarecife.acervo.literario.api.mapper;

import org.springframework.stereotype.Component;

import br.com.treinarecife.acervo.literario.api.dto.request.CategoriaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.CategoriaResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        return categoria;
    }

    public CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
            categoria.getId(),
            categoria.getNome()
        );
    }
}

