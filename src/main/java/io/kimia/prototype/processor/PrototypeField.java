package io.kimia.prototype.processor;

import io.kimia.prototype.Mutable;
import io.kimia.prototype.Nullable;

import java.util.EnumSet;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;

class PrototypeField implements Variable {
    private static final EnumSet<TypeKind> NULLABLE_TYPES = EnumSet.of(TypeKind.DECLARED, TypeKind.ARRAY);

    private final Element field;
    private final boolean mutablePrototype;

    public PrototypeField(Element field, boolean mutablePrototype) {
        this.field = field;
        this.mutablePrototype = mutablePrototype;
    }

    @Override
    public boolean isMutable() {
        Mutable mutable = field.getAnnotation(Mutable.class);
        return mutable == null ? mutablePrototype : mutable.value();
    }

    @Override
    public boolean isNullable() {
        return NULLABLE_TYPES.contains(field.asType().getKind())
                && field.getAnnotation(Nullable.class) != null;
    }

    @Override
    public boolean needsNullCheck() {
        return NULLABLE_TYPES.contains(field.asType().getKind())
                && field.getAnnotation(Nullable.class) == null;
    }

    @Override
    public String getName() {
        return field.getSimpleName().toString();
    }

    @Override
    public TypeKind getTypeKind() {
        return field.asType().getKind();
    }

    @Override
    public String getTypeName() {
        TypeMirror typeMirror = field.asType();
        return typeMirror.accept(new SimpleTypeVisitor6<String, Void>(typeMirror.toString()) {
            @Override
            public String visitDeclared(DeclaredType t, Void o) {
                String fullName = t.toString();
                if (fullName.startsWith("java.lang.")) {
                    return fullName.substring("java.lang.".length());
                } else {
                    return fullName;
                }
            }
        }, null);
    }
}
