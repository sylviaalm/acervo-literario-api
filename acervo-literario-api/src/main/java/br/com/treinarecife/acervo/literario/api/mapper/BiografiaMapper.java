package br.com.treinarecife.acervo.literario.api.mapper;

import org.springframework.stereotype.Component;

import br.com.treinarecife.acervo.literario.api.dto.request.BiografiaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.BiografiaResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Biografia;

@Component
public class BiografiaMapper {

    public Biografia toEntity(BiografiaRequestDTO dto, Autor autor) {
        Biografia biografia = new Biografia();
        biografia.setResumo(dto.resumo());
        biografia.setAutor(autor);
        return biografia;
    }

    public BiografiaResponseDTO toResponseDTO(Biografia biografia) {
        Long autorId = (biografia.getAutor() != null) ? biografia.getAutor().getId() : null;

        return new BiografiaResponseDTO(
            biografia.getId(),
            biografia.getResumo(),
            autorId
        );
    }
}
