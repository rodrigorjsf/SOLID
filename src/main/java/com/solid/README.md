# Single Responsibility Principle (SRP)

## Overview

The **Single Responsibility Principle** is the first principle of SOLID. It states that:

> **A class should have one, and only one, reason to change.**

In other words, a class should have only one primary responsibility or reason for modification. This makes code more maintainable, testable, and flexible.

## Why SRP Matters

### Problems Without SRP

When a class has multiple responsibilities:

- **Difficult to Test**: You need to mock or set up multiple dependencies
- **Hard to Maintain**: Changes in one responsibility affect the entire class
- **Tight Coupling**: Different concerns are tangled together
- **Code Reuse**: Hard to reuse parts of the class independently
- **Unclear Intent**: The class purpose becomes ambiguous

### Benefits of SRP

- **Easier Testing**: Each class can be tested in isolation
- **Better Maintainability**: Changes to one responsibility don't affect others
- **Loose Coupling**: Responsibilities are separated and independent
- **Code Reuse**: Classes can be used independently in different contexts
- **Clear Intent**: Each class has a clear, single purpose

## Visualizing SRP

### ❌ Violating SRP - Bad Design

```mermaid
graph TB
    subgraph Course["Course Class (VIOLATION)"]
        A["Data Representation<br/>- id<br/>- name<br/>- category<br/>- description"]
        B["Database Operations<br/>- saveToDatabase()<br/>- loadFromDatabase()<br/>- validateData()"]
    end

    style Course fill:#ff6b6b,stroke:#c92a2a,color:#fff
    style A fill:#ff9999,stroke:#c92a2a
    style B fill:#ff9999,stroke:#c92a2a
```

**Problem**: The `Course` class has **two responsibilities**:
1. Representing course data
2. Managing database persistence

This violates SRP because the class has two reasons to change:
- When domain logic changes (data model)
- When database operations change (persistence strategy)

### ✅ Following SRP - Good Design

```mermaid
graph TB
    subgraph Course["Course Class<br/>(Data Model)"]
        A["Responsibility:<br/>Represent Course Data<br/>- id<br/>- name<br/>- category<br/>- description"]
    end

    subgraph Repository["CourseRepository Class<br/>(Persistence)"]
        B["Responsibility:<br/>Manage Data Persistence<br/>- save()<br/>- findById()<br/>- update()<br/>- delete()"]
    end

    Course -->|"depends on"| Repository

    style Course fill:#51cf66,stroke:#2f9e44,color:#fff
    style Repository fill:#51cf66,stroke:#2f9e44,color:#fff
    style A fill:#69db7c,stroke:#2f9e44
    style B fill:#69db7c,stroke:#2f9e44
```

**Solution**: Separate concerns into different classes:
1. **Course**: Only represents course data
2. **CourseRepository**: Only handles database persistence

Each class has **one responsibility** and **one reason to change**.

## Class Diagram Comparison

### Bad Implementation Structure

```mermaid
classDiagram
    class Course {
        -id: int
        -name: String
        -category: Category
        -description: String
        +saveToDatabase()
        +loadFromDatabase(int)
        +validateData()
        +getId()
        +getName()
    }

    Course --> Category

    class Category {
        -id: int
        -name: String
        +getId()
        +getName()
    }
```

**Issues**:
- `Course` mixed with database logic
- Hard to test without database
- Tightly coupled to database implementation

### Good Implementation Structure

```mermaid
classDiagram
    class Course {
        -id: int
        -name: String
        -category: Category
        -description: String
        +getId()
        +getName()
        +getCategory()
        +getDescription()
    }

    class CourseRepository {
        -pdo: PDO
        +save(Course): void
        +findById(int): Course
        +update(Course): void
        +delete(int): void
    }

    class PDO {
        -connection: Connection
        +getConnection(): Connection
    }

    Course --> Category
    CourseRepository --> Course
    CourseRepository --> PDO

    class Category {
        -id: int
        -name: String
        +getId()
        +getName()
    }
```

**Advantages**:
- `Course` focuses only on data representation
- `CourseRepository` handles all persistence concerns
- Easy to test (can mock repository)
- Can easily switch database implementations

## Dependency Flow

### Bad Design - Circular Dependencies

```mermaid
graph LR
    A["Course<br/>(Data + DB)"] -->|"depends on"| B["Database"]
    B -->|"tightly couples"| A
    C["Test"] -->|"must mock DB"| A
```

### Good Design - Clean Separation

```mermaid
graph LR
    A["Course<br/>(Data Only)"] -->|"is used by"| B["CourseRepository<br/>(Persistence)"]
    B -->|"manages"| C["Database"]
    D["Test"] -->|"can mock"| B
    style A fill:#51cf66,stroke:#2f9e44,color:#fff
    style B fill:#4dabf7,stroke:#1971c2,color:#fff
    style C fill:#ffd43b,stroke:#f08c00,color:#000
    style D fill:#da77f2,stroke:#9c36b5,color:#fff
```

## Testing Impact

### Testing Bad Design

```mermaid
graph TB
    Test["Unit Test"]
    Test -->|"needs to set up"| DB["Database Connection"]
    Test -->|"needs to configure"| Tables["Tables & Schema"]
    Test -->|"has to mock/isolate"| Course["Course Persistence Methods"]
    DB --> Complexity["High Complexity"]
    Tables --> Complexity
    Course --> Complexity

    style Complexity fill:#ff6b6b,stroke:#c92a2a,color:#fff
```

### Testing Good Design

```mermaid
graph TB
    Test["Unit Test"]
    Test -->|"tests data logic"| Course["Course Class"]
    Test -->|"mocks"| Repo["CourseRepository"]
    Course --> Simple["Simple & Clear"]
    Repo --> Simple

    style Simple fill:#51cf66,stroke:#2f9e44,color:#fff
```

## Real-World Example

### Scenario: Database Migration

**Without SRP** (Bad):
```
Database changes → Must modify Course class → Affects business logic → Need to retest everything
```

**With SRP** (Good):
```
Database changes → Only modify CourseRepository → Course unchanged → Only repository tests affected
```

## Key Takeaways

| Aspect | Without SRP | With SRP |
|--------|------------|----------|
| **Responsibilities per class** | Multiple | One |
| **Reasons to change** | Multiple | One |
| **Testability** | Difficult | Easy |
| **Maintainability** | Hard | Easy |
| **Coupling** | Tight | Loose |
| **Code Reuse** | Limited | High |
| **Clarity** | Ambiguous | Clear |

## In This Repository

This package contains both implementations:

- **`bad/`**: `Course` class violates SRP with database methods
- **`good/`**: `Course` and `CourseRepository` follow SRP with separated concerns

Run both examples to see the difference:
```bash
# Bad implementation
mvn exec:java -Dexec.mainClass="com.solid.srp.bad.BadSRP"

# Good implementation
mvn exec:java -Dexec.mainClass="com.solid.srp.good.GoodSRP"
```

## Further Reading

- **SOLID Principles**: Foundation for maintainable OOP design
- **Separation of Concerns**: Core concept behind SRP
- **Repository Pattern**: Common pattern for applying SRP with persistence
- **Dependency Injection**: Technique for loosely coupling responsibilities
