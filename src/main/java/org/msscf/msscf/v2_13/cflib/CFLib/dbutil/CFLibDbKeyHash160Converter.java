package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CFLibDbKeyHash160Converter implements AttributeConverter<CFLibDbKeyHash160, byte[]> {
    
    @Override
    public byte[] convertToDatabaseColumn(CFLibDbKeyHash160 attribute) {
        return attribute != null ? attribute.getBytes() : null;
    }

    @Override
    public CFLibDbKeyHash160 convertToEntityAttribute(byte[] dbData) {
        return dbData != null ? new CFLibDbKeyHash160(dbData) : null;
    }
}