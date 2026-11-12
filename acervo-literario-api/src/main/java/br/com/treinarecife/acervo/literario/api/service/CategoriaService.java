package br.com.treinarecife.acervo.literario.api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.treinarecife.acervo.literario.api.dto.request.CategoriaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.CategoriaResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.mapper.CategoriaMapper;
import br.com.treinarecife.acervo.literario.api.repository.CategoriaRepository;

@Service
@Transactional 
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    // 1. Criar nova categoria
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return categoriaMapper.toResponseDTO(categoriaSalva);
    }

    // 2. Listar todas as categorias
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();
    }

    // 3. Buscar categoria por ID (usando .map no Optional)
    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(categoriaMapper::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
    }

    // 4. Atualizar categoria existente
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));

        categoria.setNome(dto.nome());
        Categoria categoriaAtualizada = categoriaRepository.save(categoria);

        return categoriaMapper.toResponseDTO(categoriaAtualizada);
    }

    // 5. Deletar categoria
    public void deletar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));

        if (categoria.getLivros() != null && !categoria.getLivros().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível deletar uma categoria que possui livros vinculados.");
        }

        categoriaRepository.delete(categoria);
    }
}

