package br.com.treinarecife.acervo.literario.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;

import java.util.List;

@Service
@Transactional
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public Autor buscarPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado."
                ));
    }

    public Autor salvar(Autor autor) {
        return autorRepository.save(autor);
    }

    public void excluir(Long id) {
        Autor autor = buscarPorId(id);

        if (!autor.getLivros().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir este autor, pois existem livros cadastrados em seu nome."
            );
        }

        autorRepository.delete(autor);
    }
}