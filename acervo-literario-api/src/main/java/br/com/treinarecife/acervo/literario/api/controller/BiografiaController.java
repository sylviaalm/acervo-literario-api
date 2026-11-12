package br.com.treinarecife.acervo.literario.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.treinarecife.acervo.literario.api.dto.request.BiografiaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.BiografiaResponseDTO;
import br.com.treinarecife.acervo.literario.api.service.BiografiaService;

@RestController
@RequestMapping("/api/biografias")
public class BiografiaController {

    private final BiografiaService biografiaService;

    public BiografiaController(BiografiaService biografiaService) {
        this.biografiaService = biografiaService;
    }

    @PostMapping
    public ResponseEntity<BiografiaResponseDTO> criar(@RequestBody BiografiaRequestDTO dto) {
        BiografiaResponseDTO novaBiografia = biografiaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaBiografia);
    }

    @GetMapping
    public ResponseEntity<List<BiografiaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(biografiaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiografiaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(biografiaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BiografiaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody BiografiaRequestDTO dto) {
        return ResponseEntity.ok(biografiaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        biografiaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
