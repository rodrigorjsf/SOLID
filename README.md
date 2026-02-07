# SOLID Principles Study Repository

This is a learning repository dedicated to studying and implementing the **SOLID principles** of object-oriented development in Java.

## About This Project

This repository contains practical examples and implementations of SOLID principles, which are fundamental concepts for writing clean, maintainable, and scalable object-oriented code. Each principle is explored through real-world examples and code patterns.

## What is SOLID?

SOLID is an acronym that represents five design principles that help developers create more maintainable, flexible, and scalable software:

### **S - Single Responsibility Principle (SRP)**
A class should have only one reason to change. In other words, a class should have only one job or responsibility. This makes classes more focused, easier to test, and simpler to maintain.

### **O - Open/Closed Principle (OCP)**
Software entities (classes, modules, functions) should be open for extension but closed for modification. This means you should be able to add new functionality without changing existing code.

### **L - Liskov Substitution Principle (LSP)**
Subtypes must be substitutable for their base types without breaking the application. Objects of a superclass should be replaceable with objects of its subclasses without breaking the code.

### **I - Interface Segregation Principle (ISP)**
Clients should not be forced to depend on interfaces they do not use. It's better to have multiple specific interfaces than one large general-purpose interface.

### **D - Dependency Inversion Principle (DIP)**
High-level modules should not depend on low-level modules; both should depend on abstractions. Depend on interfaces or abstract classes rather than concrete implementations.

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── solid/
│               ├── srp/        # Single Responsibility Principle examples
│               ├── ocp/        # Open/Closed Principle examples
│               ├── lsp/        # Liskov Substitution Principle examples
│               ├── isp/        # Interface Segregation Principle examples
│               └── dip/        # Dependency Inversion Principle examples
```

## Technologies

- **Language:** Java 25
- **Build Tool:** Maven

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 25 or higher
- Maven 3.6+

### Building the Project

```bash
mvn clean compile
```

### Running Examples

```bash
mvn exec:java -Dexec.mainClass="com.solid.srp.Main"
```

## Learning Path

This repository is organized to progressively teach SOLID principles:
1. Start with **SRP** to understand the concept of single responsibility
2. Move to **OCP** to learn how to design for extension
3. Study **LSP** to ensure proper inheritance hierarchies
4. Learn **ISP** to design focused interfaces
5. Master **DIP** to create loosely coupled systems

## Contributing

This is a study repository. Feel free to fork, modify, and experiment with the code as part of your learning journey.

## Resources

For more information about SOLID principles:
- [SOLID Wikipedia](https://en.wikipedia.org/wiki/SOLID)
- [Clean Code by Robert C. Martin](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)
- [Design Patterns](https://refactoring.guru/design-patterns)

## License

This project is open source and available for educational purposes.
