package br.com.treinarecife.acervo.literario.api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.treinarecife.acervo.literario.api.dto.request.BiografiaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.BiografiaResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Biografia;
import br.com.treinarecife.acervo.literario.api.mapper.BiografiaMapper;
import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.BiografiaRepository;

@Service
@Transactional
public class BiografiaService {

    private final BiografiaRepository biografiaRepository;
    private final AutorRepository autorRepository;
    private final BiografiaMapper biografiaMapper;

    public BiografiaService(
            BiografiaRepository biografiaRepository,
            AutorRepository autorRepository,
            BiografiaMapper biografiaMapper) {
        this.biografiaRepository = biografiaRepository;
        this.autorRepository = autorRepository;
        this.biografiaMapper = biografiaMapper;
    }

    // 1. Criar nova biografia
    public BiografiaResponseDTO criar(BiografiaRequestDTO dto) {
        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado."));

        // Garante a regra 1:1 (um autor não pode ter duas biografias)
        if (autor.getBiografia() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este autor já possui uma biografia cadastrada.");
        }

        Biografia biografia = biografiaMapper.toEntity(dto, autor);
        Biografia biografiaSalva = biografiaRepository.save(biografia);
        return biografiaMapper.toResponseDTO(biografiaSalva);
    }

    // 2. Listar todas as biografias
    @Transactional(readOnly = true)
    public List<BiografiaResponseDTO> listarTodas() {
        return biografiaRepository.findAll()
                .stream()
                .map(biografiaMapper::toResponseDTO)
                .toList();
    }

    // 3. Buscar biografia por ID (usando .map no Optional)
    @Transactional(readOnly = true)
    public BiografiaResponseDTO buscarPorId(Long id) {
        return biografiaRepository.findById(id)
                .map(biografiaMapper::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biografia não encontrada."));
    }

    // 4. Atualizar biografia existente
    public BiografiaResponseDTO atualizar(Long id, BiografiaRequestDTO dto) {
        Biografia biografia = biografiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biografia não encontrada."));

        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado."));

        biografia.setResumo(dto.resumo());
        biografia.setAutor(autor);
        
        Biografia biografiaAtualizada = biografiaRepository.save(biografia);
        return biografiaMapper.toResponseDTO(biografiaAtualizada);
    }

    // 5. Deletar biografia
    public void deletar(Long id) {
        Biografia biografia = biografiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Biografia não encontrada."));

        // Desvincula do autor na memória antes de deletar para evitar erro de referência no Hibernate
        if (biografia.getAutor() != null) {
            biografia.getAutor().setBiografia(null);
        }

        biografiaRepository.delete(biografia);
    }
}
