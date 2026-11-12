package br.com.treinarecife.acervo.literario.api.dto.response;

public record BiografiaResponseDTO(
    Long id,
    String resumo,
    Long autorId
) {
}
