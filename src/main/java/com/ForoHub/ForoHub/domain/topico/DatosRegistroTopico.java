package com.ForoHub.ForoHub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroTopico(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotNull TopicStatus status,
        @NotBlank String creador,
        @NotBlank String curso
) {
}
