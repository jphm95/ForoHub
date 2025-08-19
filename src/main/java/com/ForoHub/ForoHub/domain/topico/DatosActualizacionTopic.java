package com.ForoHub.ForoHub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosActualizacionTopic(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotNull TopicStatus status,
        @NotBlank String curso,
        @NotBlank String creador

) {
}
