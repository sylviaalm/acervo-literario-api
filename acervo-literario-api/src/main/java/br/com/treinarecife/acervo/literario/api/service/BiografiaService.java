package br.com.treinarecife.acervo.literario.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.treinarecife.acervo.literario.api.entity.Biografia;
import br.com.treinarecife.acervo.literario.api.repository.BiografiaRepository;

import java.util.List;

@Service
@Transactional
public class BiografiaService {

    private final BiografiaRepository biografiaRepository;

    public BiografiaService(BiografiaRepository biografiaRepository) {
        this.biografiaRepository = biografiaRepository;
    }

    @Transactional(readOnly = true)
    public List<Biografia> listarTodas() {
        return biografiaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Biografia buscarPorId(Long id) {
        return biografiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada."
                ));
    }

    public Biografia salvar(Biografia biografia) {
        return biografiaRepository.save(biografia);
    }

    public void excluir(Long id) {
        Biografia biografia = buscarPorId(id);
        biografiaRepository.delete(biografia);
    }
}

