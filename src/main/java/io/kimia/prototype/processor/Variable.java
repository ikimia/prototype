package io.kimia.prototype.processor;

import javax.lang.model.type.TypeKind;

interface Variable {
    boolean isMutable();
    boolean isNullable();
    boolean needsNullCheck();
    String getName();
    TypeKind getTypeKind();
    String getTypeName();
}
