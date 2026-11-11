package br.com.treinarecife.acervo.literario.api.dto.response;

public record AutorResponseDTO(
    Long id,
    String nome,
    String nacionalidade,
    BiografiaResponseDTO biografia
) {
}
