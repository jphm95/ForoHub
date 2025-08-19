package com.ForoHub.ForoHub.domain.topico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "topics")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TopicStatus status;
    private String creador;
    private String curso;
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;


    public Topico(DatosRegistroTopico datos) {
        this.id = null;
        this.status = datos.status();
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.creador = datos.creador();
        this.curso = datos.curso();
    }

    public void actualizarTopic(@Valid DatosActualizacionTopic datos) {
        if (datos.titulo() != null) {
            this.titulo = datos.titulo();
        }
        if (datos.mensaje() != null) {
            this.mensaje = datos.mensaje();
        }
        if (datos.status() != null) {
            this.status = datos.status();
        }
        if (datos.curso() != null) {
            this.curso = datos.curso();
        }
        if (datos.creador() != null) {
            this.creador = datos.creador();
        }
    }
}
