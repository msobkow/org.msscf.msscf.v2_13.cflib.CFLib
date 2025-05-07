package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CFLibDbKeyHash256Converter implements AttributeConverter<CFLibDbKeyHash256, byte[]> {
    
    @Override
    public byte[] convertToDatabaseColumn(CFLibDbKeyHash256 attribute) {
        return attribute != null ? attribute.getBytes() : null;
    }

    @Override
    public CFLibDbKeyHash256 convertToEntityAttribute(byte[] dbData) {
        return dbData != null ? new CFLibDbKeyHash256(dbData) : null;
    }
}