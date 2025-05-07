package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CFLibDbKeyHash384Converter implements AttributeConverter<CFLibDbKeyHash384, byte[]> {
    
    @Override
    public byte[] convertToDatabaseColumn(CFLibDbKeyHash384 attribute) {
        return attribute != null ? attribute.getBytes() : null;
    }

    @Override
    public CFLibDbKeyHash384 convertToEntityAttribute(byte[] dbData) {
        return dbData != null ? new CFLibDbKeyHash384(dbData) : null;
    }
}