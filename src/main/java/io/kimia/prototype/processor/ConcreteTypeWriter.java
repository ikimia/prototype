package io.kimia.prototype.processor;

import static javax.lang.model.type.TypeKind.*;

import io.kimia.prototype.Mutable;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;

class ConcreteTypeWriter implements Closeable {
    private static final String INDENTATION = "    ";

    private final Writer writer;
    private final CharSequence packageName;
    private final CharSequence concreteName;
    private final List<Variable> fields;

    private int indentationLevel = 0;

    public ConcreteTypeWriter(Filer filer, TypeElement prototype, CharSequence packageName, CharSequence concreteName) throws IOException {
        CharSequence sourceFileName = packageName.length() == 0 ? concreteName : packageName + "." + concreteName;
        this.writer = filer.createSourceFile(sourceFileName, prototype).openWriter();
        this.packageName = packageName;
        this.concreteName = concreteName;
        this.fields = fetchFields(prototype);
    }

    private static List<Variable> fetchFields(TypeElement prototype) {
        List<Variable> fields = new ArrayList<Variable>();
        for (Element element : prototype.getEnclosedElements()) {
            if (element.getKind() == ElementKind.FIELD
                    && !element.getModifiers().contains(Modifier.STATIC)) {
                fields.add(new PrototypeField(element, isMutable(prototype)));
            }
        }
        return fields;
    }

    private static boolean isMutable(TypeElement prototype) {
        Mutable mutable = prototype.getAnnotation(Mutable.class);
        return mutable != null && mutable.value();
    }

    public void writePackageDeclaration() throws IOException {
        if (packageName.length() != 0) {
            writer.append("package ").append(packageName).append(";\n\n");
        }
    }

    public void writeGeneratedAnnotation(Class<?> processorType) throws IOException {
        writer.append("import javax.annotation.Generated;\n\n@Generated(\"")
                .append(processorType.getCanonicalName())
                .append("\")\n");
    }

    public void startClassDefinition() throws IOException {
        writer.append("public final class ").append(concreteName);
        startBlock();
    }

    public void startBlock() throws IOException {
        writer.write(" {\n");
        indentationLevel += 1;
    }

    public void endBlock() throws IOException {
        indentationLevel -= 1;
        writer.write('\n');
        indent().append("}\n");
    }

    public void writeMembers() throws IOException {
        for (Variable field : fields) {
            indent().append("private ");
            if (!field.isMutable()) {
                writer.append("final ");
            }
            writer.append(field.getTypeName())
                    .append(' ')
                    .append(field.getName())
                    .append(";\n");
        }
    }

    public void writeConstructor() throws IOException {
        List<Variable> immutableFields = new ArrayList<Variable>(fields.size());
        for (Variable field : fields) {
            if (!field.isMutable()) {
                immutableFields.add(field);
            }
        }
        if (immutableFields.isEmpty()) {
            return;
        }
        writer.append('\n');
        indent().append("public ").append(concreteName).append('(');
        int i = immutableFields.size();
        for (Variable field : immutableFields) {
            writer.append(field.getTypeName())
                    .append(' ')
                    .append(field.getName());
            if (--i != 0) {
                writer.append(", ");
            }
        }
        writer.append(')');
        startBlock();
        for (Variable field : immutableFields) {
            if (field.needsNullCheck()) {
                writeNullCheck(field);
            }
        }
        i = immutableFields.size();
        for (Variable field : immutableFields) {
            indent().append("this.")
                    .append(field.getName())
                    .append(" = ")
                    .append(field.getName())
                    .append(';');
            if (--i != 0) {
                writer.append('\n');
            }
        }
        endBlock();
    }

    private void writeNullCheck(Variable field) throws IOException {
        indent().append("if (").append(field.getName()).append(" == null)");
        startBlock();
        indent().append("throw new NullPointerException(\"").append(field.getName()).append("\");");
        endBlock();
    }

    public void writeGetters() throws IOException {
        for (Variable field : fields) {
            writer.write('\n');
            indent().append("public ")
                    .append(field.getTypeName())
                    .append(' ')
                    .append(field.getTypeKind() == TypeKind.BOOLEAN ? "is" : "get")
                    .append(Character.toUpperCase(field.getName().charAt(0)))
                    .append(field.getName().substring(1))
                    .append("()");
            startBlock();
            indent().append("return this.").append(field.getName()).append(';');
            endBlock();
        }
    }

    public void writeSetters() throws IOException {
        for (Variable field : fields) {
            if (field.isMutable()) {
                writer.write('\n');
                indent().append("public void set")
                        .append(Character.toUpperCase(field.getName().charAt(0)))
                        .append(field.getName().substring(1))
                        .append('(')
                        .append(field.getTypeName())
                        .append(' ')
                        .append(field.getName())
                        .append(')');
                startBlock();
                if (field.needsNullCheck()) {
                    writeNullCheck(field);
                }
                indent().append("this.")
                        .append(field.getName())
                        .append(" = ")
                        .append(field.getName())
                        .append(';');
                endBlock();
            }
        }
    }

    public void writeToString() throws IOException {
        writer.write('\n');
        indent().append("@Override\n");
        indent().append("public String toString()");
        startBlock();
        indent().append("return \"").append(concreteName).append("{");
        int i =fields.size();
        for (Variable field : fields) {
            writer.append(field.getName())
                    .append("=\" + ");
            if (field.getTypeKind() == ARRAY) {
                writer.append("java.util.Arrays.toString(this.").append(field.getName()).append(')');
            } else {
                writer.append("this.").append(field.getName());
            }
            if (--i != 0) {
                writer.append("\n");
                indent().append(INDENTATION).append(INDENTATION).append("+ \", ");
            }
        }
        writer.append(" + \"}\";");
        endBlock();
    }

    public void writeEquals() throws IOException {
        writer.write('\n');
        indent().append("@Override\n");
        indent().append("public boolean equals(Object o)");
        startBlock();
        indent().append("if (this == o) return true;\n");
        indent().append("if (o instanceof ").append(concreteName).append(')');
        startBlock();
        indent().append(concreteName).append(" other = (").append(concreteName).append(") o;\n");
        indent().append("return ");
        int i = fields.size();
        for (Variable field : fields) {
            if (field.getTypeKind().isPrimitive()) {
                writer.append("(this.")
                        .append(field.getName())
                        .append(" == other.")
                        .append(field.getName())
                        .append(')');
            } else if (field.getTypeKind() == ARRAY) {
                writer.append("(java.util.Arrays.equals(this.")
                        .append(field.getName())
                        .append(", other.")
                        .append(field.getName())
                        .append("))");
            } else if (field.isNullable()) {
                writer.append("(this.")
                        .append(field.getName())
                        .append(" == null ? other.")
                        .append(field.getName())
                        .append(" == null : this.")
                        .append(field.getName())
                        .append(".equals(other.")
                        .append(field.getName())
                        .append("))");
            } else {
                writer.append("(this.")
                        .append(field.getName())
                        .append(".equals(other.")
                        .append(field.getName())
                        .append("))");
            }
            if (--i != 0) {
                writer.append('\n');
                indent().append(INDENTATION).append(INDENTATION).append(" && ");
            }
        }
        writer.append(';');
        endBlock();
        indent().append("return false;");
        endBlock();
    }

    public void writeHashCode() throws IOException {
        boolean privateVariableDeclared = false;
        writer.write('\n');
        indent().append("@Override\n");
        indent().append("public int hashCode()");
        startBlock();
        indent().append("int result = 17;\n");
        for (Variable field : fields) {
            Variable variable = field;
            if (field.getTypeKind() == DOUBLE) {
                variable = new LocalVariable(LONG, "l");
                indent();
                if (!privateVariableDeclared) {
                    writer.append(variable.getTypeName())
                            .append(' ');
                    privateVariableDeclared = true;
                }
                writer.append(variable.getName())
                        .append(" = Double.doubleToLongBits(")
                        .append(field.getName())
                        .append(");\n");
            }
            indent().append("result = 31 * result + ");
            appendHashCode(variable);
            writer.append(";\n");
        }
        indent().append("return result;");
        endBlock();
    }

    private void appendHashCode(Variable field) throws IOException {
        switch (field.getTypeKind()) {
            case BYTE:
            case CHAR:
            case SHORT:
            case INT:
                writer.append(field.getName());
                break;
            case LONG:
                writer.append("(int) (")
                        .append(field.getName())
                        .append(" ^ (")
                        .append(field.getName())
                        .append(" >>> 32))");
                break;
            case BOOLEAN:
                writer.append("(").append(field.getName()).append(" ? 1 : 0)");
                break;
            case FLOAT:
                writer.append("Float.floatToIntBits(").append(field.getName()).append(')');
                break;
            case DOUBLE:
                throw new IllegalArgumentException("Not calculating hashCode directly on the double type");
            default:
                if (field.isNullable()) {
                    writer.append('(').append(field.getName()).append(" == null ? 0 : ");
                }
                writer.append(field.getName()).append(".hashCode()");
                if (field.isNullable()) {
                    writer.append(')');
                }
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    private Writer indent() throws IOException {
        for (int i = 0; i < indentationLevel; i++) {
            writer.append(INDENTATION);
        }
        return writer;
    }
}
