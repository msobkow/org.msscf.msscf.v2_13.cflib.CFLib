package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CFLibDbKeyHash224Converter implements AttributeConverter<CFLibDbKeyHash224, byte[]> {
    
    @Override
    public byte[] convertToDatabaseColumn(CFLibDbKeyHash224 attribute) {
        return attribute != null ? attribute.getBytes() : null;
    }

    @Override
    public CFLibDbKeyHash224 convertToEntityAttribute(byte[] dbData) {
        return dbData != null ? new CFLibDbKeyHash224(dbData) : null;
    }
}