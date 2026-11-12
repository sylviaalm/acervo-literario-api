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

import br.com.treinarecife.acervo.literario.api.dto.request.AutorRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.AutorResponseDTO;
import br.com.treinarecife.acervo.literario.api.service.AutorService;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    public ResponseEntity<AutorResponseDTO> criar(@RequestBody AutorRequestDTO dto) {
        AutorResponseDTO novoAutor = autorService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAutor);
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(autorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AutorRequestDTO dto) {
        return ResponseEntity.ok(autorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        autorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
