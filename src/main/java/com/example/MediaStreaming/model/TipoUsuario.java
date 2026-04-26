package com.example.MediaStreaming.model;

public enum TipoUsuario {
    ADMIN("admin"),
    USUARIO("usuario");
    
    private String valor;
    
    TipoUsuario(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
    
    public static TipoUsuario fromValor(String valor) {
        if (valor == null) {
            return null;
        }
        for (TipoUsuario tipo : values()) {
            if (tipo.valor.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valor desconocido para TipoUsuario: " + valor);
    }
}
