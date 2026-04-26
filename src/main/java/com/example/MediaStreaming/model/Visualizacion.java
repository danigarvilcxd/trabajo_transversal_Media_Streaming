package com.example.MediaStreaming.model;

import jakarta.persistence.*;

@Entity
@Table(name = "visualizacion")
public class Visualizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contenido_id", nullable = false)
    private Contenido contenido;

    @Column(columnDefinition = "ENUM('viendo', 'completado')")
    private String estado = "viendo";

    private Integer tiempoVisto = 0;

    public Visualizacion() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Contenido getContenido() {
        return contenido;
    }

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getTiempoVisto() {
        return tiempoVisto;
    }

    public void setTiempoVisto(Integer tiempoVisto) {
        this.tiempoVisto = tiempoVisto;
    }
}
