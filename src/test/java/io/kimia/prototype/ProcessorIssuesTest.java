package io.kimia.prototype;

import static org.testng.Assert.assertEquals;

import io.kimia.prototype.tools.InMemoryCompiler;
import org.testng.annotations.Test;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * Tests errors and warnings issued by the annotation processor.
 */
public class ProcessorIssuesTest {

    @Test
    public void testWrongPrefix() {
        compileAndAssertError("IllegalPrototype", "int x;", "Types annotated with @Prototype must start with \"_\"");
    }

    @Test
    public void testEmptyPrototype() {
        compileAndAssertError("_Empty", "", "Types annotated with @Prototype must contain fields");
    }

    private static void compileAndAssertError(String name, String source, String message) {
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        InMemoryCompiler.compile(diagnostics,
                name, "@io.kimia.prototype.Prototype class " + name + "{" + source + "}");
        Diagnostic<? extends JavaFileObject> diagnostic = diagnostics.getDiagnostics().get(0);
        assertEquals(diagnostic.getKind(), Diagnostic.Kind.ERROR);
        assertEquals(diagnostic.getMessage(null), "mem:///" + name + ".java:1: " + message);
    }
}
