package br.com.treinarecife.acervo.literario.api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.treinarecife.acervo.literario.api.dto.request.AutorRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.AutorResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.mapper.AutorMapper;
import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;

@Service
@Transactional
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    public AutorService(AutorRepository autorRepository, AutorMapper autorMapper) {
        this.autorRepository = autorRepository;
        this.autorMapper = autorMapper;
    }

    // 1. Criar novo autor
    public AutorResponseDTO criar(AutorRequestDTO dto) {
        Autor autor = autorMapper.toEntity(dto);
        Autor autorSalvo = autorRepository.save(autor);
        return autorMapper.toResponseDTO(autorSalvo);
    }

    // 2. Listar todos os autores
    @Transactional(readOnly = true)
    public List<AutorResponseDTO> listarTodos() {
        return autorRepository.findAll()
                .stream()
                .map(autorMapper::toResponseDTO)
                .toList();
    }

    // 3. Buscar autor por ID (usando .map no Optional)
    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(Long id) {
        return autorRepository.findById(id)
                .map(autorMapper::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado."));
    }

    // 4. Atualizar autor existente
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado."));

        autor.setNome(dto.nome());
        autor.setNacionalidade(dto.nacionalidade());
        Autor autorAtualizado = autorRepository.save(autor);

        return autorMapper.toResponseDTO(autorAtualizado);
    }

    // 5. Deletar autor
    public void deletar(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado."));

        if (autor.getLivros() != null && !autor.getLivros().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir este autor, pois existem livros cadastrados em seu nome."
            );
        }

        autorRepository.delete(autor);
    }
}
