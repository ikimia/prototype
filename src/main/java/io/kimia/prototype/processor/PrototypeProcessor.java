package io.kimia.prototype.processor;

import io.kimia.prototype.Prototype;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * <p>
 * Processes {@link Prototype} annotations.
 * </p>
 *
 * <p>
 * Creates a new type for each detected prototype.
 * </p>
 *
 * @see Prototype
 */
@SupportedAnnotationTypes("io.kimia.prototype.Prototype")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class PrototypeProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            Elements elementUtils = processingEnv.getElementUtils();
            for (Element element : roundEnv.getElementsAnnotatedWith(Prototype.class)) {
                TypeElement typeElement = (TypeElement) element;
                String violatedPreconditionMessage = checkPreconditions(element);
                if (violatedPreconditionMessage != null) {
                    error(violatedPreconditionMessage, typeElement);
                    continue;
                }
                Name packageName = elementUtils.getPackageOf(typeElement).getQualifiedName();
                Name typeName = typeElement.getSimpleName();
                CharSequence concreteTypeName = typeName.subSequence(1, typeName.length());
                ConcreteTypeWriter writer = null;
                try {
                    writer = new ConcreteTypeWriter(processingEnv.getFiler(), typeElement, packageName, concreteTypeName);
                    writer.writePackageDeclaration();
                    writer.writeGeneratedAnnotation(PrototypeProcessor.class);
                    writer.startClassDefinition();
                    writer.writeMembers();
                    writer.writeConstructor();
                    writer.writeGetters();
                    writer.writeSetters();
                    writer.writeHashCode();
                    writer.writeEquals();
                    writer.writeToString();
                    writer.endBlock();
                    writer.close();
                } catch (Exception e) {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException ex) {
                            // Nothing to do
                        }
                    }
                    error(e.getMessage(), typeElement);
                }
            }
        }
        return false;
    }

    private String checkPreconditions(Element element) {
        if (!(element.getSimpleName().charAt(0) == '_')) {
            return "Types annotated with @Prototype must start with \"_\"";
        }
        if (countFields(element.getEnclosedElements()) == 0) {
            return "Types annotated with @Prototype must contain fields";
        }
        return null;
    }

    private void error(String message, TypeElement element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private static int countFields(List<? extends Element> elements) {
        int count = 0;
        for (Element element : elements) {
            if (element.getKind() == ElementKind.FIELD
                    && !element.getModifiers().contains(Modifier.STATIC)) {
                count++;
            }
        }
        return count;
    }
}
