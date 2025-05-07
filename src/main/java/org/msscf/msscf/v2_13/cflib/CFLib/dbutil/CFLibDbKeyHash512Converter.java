package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CFLibDbKeyHash512Converter implements AttributeConverter<CFLibDbKeyHash512, byte[]> {
    
    @Override
    public byte[] convertToDatabaseColumn(CFLibDbKeyHash512 attribute) {
        return attribute != null ? attribute.getBytes() : null;
    }

    @Override
    public CFLibDbKeyHash512 convertToEntityAttribute(byte[] dbData) {
        return dbData != null ? new CFLibDbKeyHash512(dbData) : null;
    }
}