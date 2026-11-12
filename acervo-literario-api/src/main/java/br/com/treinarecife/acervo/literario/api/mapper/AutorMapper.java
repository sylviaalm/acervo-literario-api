package br.com.treinarecife.acervo.literario.api.mapper;

import org.springframework.stereotype.Component;

import br.com.treinarecife.acervo.literario.api.dto.request.AutorRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.AutorResponseDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.BiografiaResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Autor;

@Component
public class AutorMapper {

    private final BiografiaMapper biografiaMapper;

    public AutorMapper(BiografiaMapper biografiaMapper) {
        this.biografiaMapper = biografiaMapper;
    }

    public Autor toEntity(AutorRequestDTO dto) {
        Autor autor = new Autor();
        autor.setNome(dto.nome());
        autor.setNacionalidade(dto.nacionalidade());
        return autor;
    }

    public AutorResponseDTO toResponseDTO(Autor autor) {
        BiografiaResponseDTO biografiaDTO = (autor.getBiografia() != null)
                ? biografiaMapper.toResponseDTO(autor.getBiografia())
                : null;

        return new AutorResponseDTO(
            autor.getId(),
            autor.getNome(),
            autor.getNacionalidade(),
            biografiaDTO
        );
    }
}

