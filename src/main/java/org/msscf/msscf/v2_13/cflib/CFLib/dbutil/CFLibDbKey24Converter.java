package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CFLibDbKey24Converter implements AttributeConverter<CFLibDbKey24, byte[]> {
    
    @Override
    public byte[] convertToDatabaseColumn(CFLibDbKey24 attribute) {
        return attribute != null ? attribute.getBytes() : null;
    }

    @Override
    public CFLibDbKey24 convertToEntityAttribute(byte[] dbData) {
        return dbData != null ? new CFLibDbKey24(dbData) : null;
    }
}