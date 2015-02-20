Prototype
=========
Prototype provides a concise syntax to define POJOs (Plain Old Java Objects). It saves developers the need to write repetitive boilerplate code and writes it instead of them.

Prototype based classes are immutable and disallow `null` as field values by default. This behavior can be overriden using the provided annotations.

Prototype is required only in compile time and requires JDK 1.6 or later.

Getting started
---------------
Prototype is only required in compile time.
The generated classes do not need the Prototype library.

#### Using maven
Just add dependency to the library:
```xml
<dependency>
    <groupId>io.kimia</groupId>
    <artifactId>prototype</artifactId>
    <version>0.1</version>
    <scope>provided</scope>
</dependency>
```

#### Manually
Since Prototype has no dependencies, just add our jar to your classpath while running `javac`.

Simple Usage
------------
Here's an example of a prototype of a point's prototype. This prototype:
```java
import io.kimia.prototype.Prototype;
@Prototype class _Point { int x, y; }
```
Will produce:
```java
public final class Point {
    private final int x;
    private final int y;
    public Point(int x, int y) {...}
    public int getX() {...}
    public int getY() {...}
    @Override public int hashCode() {...}
    @Override public boolean equals(Object o) {...}
    @Override public String toString() {...}
}
```
Advanced Usage
--------------
The prototype library includes variations to the default `@Prototype` annotation:
 - `@Mutable` - generates mutability for all the fields or for selected fields.
 - `@Nullable` - allows a field to hold `null` as a value.

For more in depth explanation, see the API documentation.

License
-------
Prototype is licesnsed under MIT. [Full license here](LICENSE.txt).
