package com.example.MediaStreaming.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true, nullable = false)
    private String correo;
    
@Column(name = "contrasena", nullable = false)
    private String contrasena;
    
    @Column(unique = true, nullable = false)
    private String apodo;
    
    @Convert(converter = TipoUsuarioConverter.class)
    @Column(columnDefinition = "ENUM('admin', 'usuario')")
    private TipoUsuario tipo_usuario;
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String correo, String contrasena, String apodo) {
        this.correo = correo;
        this.contrasena = contrasena;
        this.apodo = apodo;
        this.tipo_usuario = TipoUsuario.USUARIO;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public String getApodo() {
        return apodo;
    }
    
    public void setApodo(String apodo) {
        this.apodo = apodo;
    }
    
    public TipoUsuario getTipo_usuario() {
        return tipo_usuario;
    }
    
    public void setTipo_usuario(TipoUsuario tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", correo='" + correo + '\'' +
                ", apodo='" + apodo + '\'' +
                ", tipo_usuario=" + tipo_usuario +
                '}';
    }
}
