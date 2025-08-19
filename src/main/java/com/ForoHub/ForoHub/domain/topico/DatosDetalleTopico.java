package com.ForoHub.ForoHub.domain.topico;

import java.time.LocalDateTime;

public record DatosDetalleTopico(
        Long id,
        String titulo,
        String mensaje,
        TopicStatus status,
        String creador,
        String curso,
        LocalDateTime fechaCreacion

) {
    public DatosDetalleTopico(Topico t) {
        this(t.getId(), t.getTitulo(), t.getMensaje(),
                t.getStatus(), t.getCreador(), t.getCurso(),
                t.getFechaCreacion());
    }
}
