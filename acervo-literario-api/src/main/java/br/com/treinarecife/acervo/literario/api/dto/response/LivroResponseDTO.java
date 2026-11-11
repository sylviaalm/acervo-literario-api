package br.com.treinarecife.acervo.literario.api.dto.response;

import java.util.List;

public record LivroResponseDTO(
    Long id,
    String titulo,
    String isbn,
    Integer anoPublicacao,
    AutorResponseDTO autor,
    List<CategoriaResponseDTO> categorias
) {
}

