package io.kimia.prototype.tools;

import java.io.*;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.tools.*;

public class InMemoryCompiler {

    public static ClassLoader compile(DiagnosticListener<JavaFileObject> diagnostics, String name, String source) {
        InMemoryClassLoader classLoader = new InMemoryClassLoader();
        ToolProvider
                .getSystemJavaCompiler()
                .getTask(null, new InMemoryJavaFileManager(classLoader, diagnostics), diagnostics, null, null,
                        Collections.singleton(
                                new InMemoryJavaFileObject(name, JavaFileObject.Kind.SOURCE).withContent(source)
                        )
                ).call();
        return classLoader;
    }

    private static class InMemoryClassLoader extends ClassLoader {
        private final Map<String, InMemoryJavaFileObject> fileObjects = new HashMap<String, InMemoryJavaFileObject>();

        public void register(String className, InMemoryJavaFileObject javaFileObject) {
            fileObjects.put(className, javaFileObject);
        }

        @Override
        protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (fileObjects.containsKey(name)) {
                byte[] bytes = fileObjects.get(name).getBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }
            return super.loadClass(name, resolve);
        }
    }

    private static class InMemoryJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        private final InMemoryClassLoader classLoader;

        public InMemoryJavaFileManager(InMemoryClassLoader classLoader, DiagnosticListener<JavaFileObject> diagnostics) {
            super(ToolProvider.getSystemJavaCompiler().getStandardFileManager(diagnostics, null, null));
            this.classLoader = classLoader;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(
                Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {

            if ((location == StandardLocation.CLASS_OUTPUT && kind == JavaFileObject.Kind.CLASS)
                    || (location == StandardLocation.SOURCE_OUTPUT && kind == JavaFileObject.Kind.SOURCE)) {

                InMemoryJavaFileObject fileObject = new InMemoryJavaFileObject(className.replace('.', '/'), kind);
                classLoader.register(className, fileObject);
                return fileObject;
            } else {
                throw new UnsupportedOperationException("Supporting only source code and bytecode processing");
            }
        }
    }

    private static class InMemoryJavaFileObject extends SimpleJavaFileObject {
        private final ByteArrayOutputStream os = new ByteArrayOutputStream();

        public InMemoryJavaFileObject(String className, Kind kind) {
            super(URI.create("mem:///" + className + kind.extension), kind);
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return os;
        }

        @Override
        public InputStream openInputStream() throws IOException {
            return new ByteArrayInputStream(os.toByteArray());
        }

        @Override
        public CharSequence getCharContent(boolean b) throws IOException {
            return os.toString();
        }

        public byte[] getBytes() {
            return os.toByteArray();
        }

        public InMemoryJavaFileObject withContent(String charContent) {
            try {
                os.write(charContent.getBytes());
                return this;
            } catch (IOException e) {
                throw new RuntimeException("Impossible IOException", e);
            }
        }
    }
}