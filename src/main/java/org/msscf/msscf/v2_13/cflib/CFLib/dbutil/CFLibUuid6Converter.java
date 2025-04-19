package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CFLibUuid6Converter implements AttributeConverter<CFLibUuid6, byte[]> {
    
    @Override
    public byte[] convertToDatabaseColumn(CFLibUuid6 attribute) {
        return attribute != null ? attribute.getBytes() : null;
    }

    @Override
    public CFLibUuid6 convertToEntityAttribute(byte[] dbData) {
        return dbData != null ? new CFLibUuid6(dbData) : null;
    }
}