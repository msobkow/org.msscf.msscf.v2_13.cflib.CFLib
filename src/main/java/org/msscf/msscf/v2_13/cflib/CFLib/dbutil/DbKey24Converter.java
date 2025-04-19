package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DbKey24Converter implements AttributeConverter<DbKey24, byte[]> {
    
    @Override
    public byte[] convertToDatabaseColumn(DbKey24 attribute) {
        return attribute != null ? attribute.getKey() : null;
    }

    @Override
    public DbKey24 convertToEntityAttribute(byte[] dbData) {
        return dbData != null ? new DbKey24(dbData) : null;
    }
}