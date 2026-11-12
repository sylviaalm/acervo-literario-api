package br.com.treinarecife.acervo.literario.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.treinarecife.acervo.literario.api.dto.request.LivroRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.LivroResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.entity.Livro;
import br.com.treinarecife.acervo.literario.api.mapper.LivroMapper;
import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.CategoriaRepository;
import br.com.treinarecife.acervo.literario.api.repository.LivroRepository;

@Service
@Transactional
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final LivroMapper livroMapper;

    public LivroService(
            LivroRepository livroRepository,
            AutorRepository autorRepository,
            CategoriaRepository categoriaRepository,
            LivroMapper livroMapper) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.livroMapper = livroMapper;
    }

    // 1. Criar novo livro
    public LivroResponseDTO criar(LivroRequestDTO dto) {
        Autor autor = validarEBuscarAutor(dto.autorId());
        Set<Categoria> categorias = validarEBuscarCategorias(dto.categoriasIds());

        Livro livro = livroMapper.toEntity(dto, autor, categorias);
        Livro livroSalvo = livroRepository.save(livro);
        return livroMapper.toResponseDTO(livroSalvo);
    }

    // 2. Listar todos os livros
    @Transactional(readOnly = true)
    public List<LivroResponseDTO> listarTodos() {
        return livroRepository.findAll()
                .stream()
                .map(livroMapper::toResponseDTO)
                .toList();
    }

    // 3. Buscar livro por ID (usando .map no Optional)
    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorId(Long id) {
        return livroRepository.findById(id)
                .map(livroMapper::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado."));
    }

    // 4. Atualizar livro existente
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado."));

        Autor autor = validarEBuscarAutor(dto.autorId());
        Set<Categoria> categorias = validarEBuscarCategorias(dto.categoriasIds());

        livro.setTitulo(dto.titulo());
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setAutor(autor);
        livro.setCategorias(categorias);

        Livro livroAtualizado = livroRepository.save(livro);
        return livroMapper.toResponseDTO(livroAtualizado);
    }

    // 5. Deletar livro
    public void deletar(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado."));

        livroRepository.delete(livro);
    }

    // Métodos auxiliares privados para reaproveitar as regras de negócio no criar e no atualizar
    private Autor validarEBuscarAutor(Long autorId) {
        if (autorId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O livro deve estar vinculado a um autor existente."
            );
        }

        return autorRepository.findById(autorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "O autor informado não está cadastrado."
                ));
    }

    private Set<Categoria> validarEBuscarCategorias(List<Long> categoriasIds) {
        if (categoriasIds == null || categoriasIds.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O livro deve estar vinculado a pelo menos uma categoria."
            );
        }

        Set<Long> idsUnicos = new HashSet<>(categoriasIds);

        if (idsUnicos.contains(null)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Todas as categorias informadas devem existir no banco de dados."
            );
        }

        List<Categoria> categoriasEncontradas = categoriaRepository.findAllById(idsUnicos);

        if (categoriasEncontradas.size() != idsUnicos.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Uma ou mais categorias informadas não estão cadastradas."
            );
        }

        return new HashSet<>(categoriasEncontradas);
    }
}
