package br.com.treinarecife.acervo.literario.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.entity.Livro;
import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.CategoriaRepository;
import br.com.treinarecife.acervo.literario.api.repository.LivroRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    public LivroService(
            LivroRepository livroRepository,
            AutorRepository autorRepository,
            CategoriaRepository categoriaRepository) {

        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Livro não encontrado."
                ));
    }

    public Livro salvar(Livro livro) {

        // Validação do autor
        if (livro.getAutor() == null || livro.getAutor().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O livro deve estar vinculado a um autor existente."
            );
        }

        Autor autor = autorRepository.findById(livro.getAutor().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "O autor informado não está cadastrado."
                ));

        // Validação das categorias
        if (livro.getCategorias() == null || livro.getCategorias().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O livro deve estar vinculado a pelo menos uma categoria."
            );
        }

        Set<Long> categoriaIds = livro.getCategorias()
                .stream()
                .map(Categoria::getId)
                .collect(Collectors.toSet());

        if (categoriaIds.contains(null)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Todas as categorias informadas devem existir no banco de dados."
            );
        }

        List<Categoria> categoriasEncontradas =
                categoriaRepository.findAllById(categoriaIds);

        if (categoriasEncontradas.size() != categoriaIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Uma ou mais categorias informadas não estão cadastradas."
            );
        }

        // Associa as entidades existentes ao livro
        livro.setAutor(autor);
        livro.setCategorias(new HashSet<>(categoriasEncontradas));

        return livroRepository.save(livro);
    }
}