package com.example.MediaStreaming.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoUsuarioConverter implements AttributeConverter<TipoUsuario, String> {

    @Override
    public String convertToDatabaseColumn(TipoUsuario attribute) {
        return attribute == null ? null : attribute.getValor();
    }

    @Override
    public TipoUsuario convertToEntityAttribute(String dbData) {
        return TipoUsuario.fromValor(dbData);
    }
}
