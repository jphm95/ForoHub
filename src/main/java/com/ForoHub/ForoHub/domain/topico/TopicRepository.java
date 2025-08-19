package com.ForoHub.ForoHub.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topico, Long> {

    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    boolean existsByTituloAndMensajeAndIdNot(String titulo, String mensaje, Long id);
}
