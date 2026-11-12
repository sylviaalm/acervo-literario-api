package br.com.treinarecife.acervo.literario.api.dto.request;

public record BiografiaRequestDTO(
    String resumo,
    Long autorId
) {
}
