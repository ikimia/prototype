package io.kimia.prototype.processor;

import javax.lang.model.type.TypeKind;

class LocalVariable implements Variable {
    private final TypeKind typeKind;
    private final String name;

    public LocalVariable(TypeKind typeKind, String name) {
        if (!typeKind.isPrimitive()) {
            throw new IllegalArgumentException("The variable must be primitive");
        }
        this.typeKind = typeKind;
        this.name = name;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean isNullable() {
        return !typeKind.isPrimitive();
    }

    @Override
    public boolean needsNullCheck() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TypeKind getTypeKind() {
        return typeKind;
    }

    @Override
    public String getTypeName() {
        return typeKind.name().toLowerCase();
    }
}
