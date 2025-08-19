package com.ForoHub.ForoHub.controller;

import com.ForoHub.ForoHub.domain.topico.DatosActualizacionTopic;
import com.ForoHub.ForoHub.domain.topico.DatosDetalleTopico;
import com.ForoHub.ForoHub.domain.topico.DatosListaTopics;
import com.ForoHub.ForoHub.domain.topico.DatosRegistroTopico;
import com.ForoHub.ForoHub.domain.topico.TopicRepository;
import com.ForoHub.ForoHub.domain.topico.Topico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topics")
public class TopicosController {

    @Autowired
    private TopicRepository topicRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<DatosDetalleTopico> createTopic(@RequestBody @Valid DatosRegistroTopico datos, UriComponentsBuilder uriComponentsBuilder) {
        if (topicRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.status(409).build();
        }
        var topic = new Topico(datos);
        topicRepository.save(topic);

        var uri = uriComponentsBuilder.path("/topics/{id}").buildAndExpand(topic.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosDetalleTopico(topic));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaTopics>> getAllTopics(@PageableDefault(size = 10, sort = {"fechaCreacion"}, direction = Sort.Direction.ASC) Pageable pageable) {
        var page = topicRepository.findAll(pageable).map(DatosListaTopics::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> getTopic(@PathVariable Long id) {
        return topicRepository.findById(id)
                .map(topico -> ResponseEntity.ok(new DatosDetalleTopico(topico)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> updateTopic(@PathVariable Long id, @RequestBody @Valid DatosActualizacionTopic datos) {
        var optional = topicRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var topic = optional.get();

        // Regla de negocio: evitar duplicados (mismo titulo + mensaje) en otros registros
        if (datos.titulo() != null && datos.mensaje() != null &&
                topicRepository.existsByTituloAndMensajeAndIdNot(datos.titulo(), datos.mensaje(), id)) {
            return ResponseEntity.status(409).build();
        }

        topic.actualizarTopic(datos);
        return ResponseEntity.ok(new DatosDetalleTopico(topic));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        if (!topicRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        topicRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
