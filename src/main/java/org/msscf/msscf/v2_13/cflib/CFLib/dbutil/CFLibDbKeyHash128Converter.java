package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CFLibDbKeyHash128Converter implements AttributeConverter<CFLibDbKeyHash128, byte[]> {
    
    @Override
    public byte[] convertToDatabaseColumn(CFLibDbKeyHash128 attribute) {
        return attribute != null ? attribute.getBytes() : null;
    }

    @Override
    public CFLibDbKeyHash128 convertToEntityAttribute(byte[] dbData) {
        return dbData != null ? new CFLibDbKeyHash128(dbData) : null;
    }
}