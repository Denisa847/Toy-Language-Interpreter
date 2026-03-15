# 🚀 Toy Language Interpreter

A desktop application developed in **Java using JavaFX**, designed to demonstrate concepts from **Interpreter Design, Object-Oriented Programming, Memory Management, and Concurrent Programming**.

The project implements a **custom toy programming language interpreter** that allows users to **execute programs step-by-step while visualizing runtime structures** such as the execution stack, heap memory, symbol table, and synchronization tables.

---

# ✨ Key Features

### 🧵 Multi-threading
Create new execution threads using the `fork()` statement.

Each thread runs independently with its own:

* Execution Stack
* Symbol Table

While sharing:

* Heap
* Output List
* Synchronization Tables

---

### ⚙️ Procedures
Support for procedure-like functionality using:

* `CallStmt`
* `ReturnStmt`
* a dedicated **Procedure Table (`ProcTable`)**

This allows reusable blocks of code similar to functions.

---

### 🧠 Dynamic Memory (Heap)
Programs can dynamically allocate memory using a managed heap.

Supported operations:

* `new` — allocate heap memory
* `readHeap` — read value from heap
* `writeHeap` — update heap value

---

### ♻️ Garbage Collection
The interpreter includes a **safe garbage collector** that removes unreachable heap values.

The garbage collector:

* tracks references from symbol tables
* tracks nested heap references
* removes unused heap entries
* prevents memory leaks during execution

---

### 🔍 Static Type Checker
Before execution, programs are validated using a **static type checker**.

The type checker ensures:

* variable declarations are correct
* expressions use compatible types
* statements follow language semantics

Programs that fail type checking **cannot be executed**.

---

# 🏗 Project Structure

The project follows a **strict layered architecture** for clean separation of concerns.

| Package | Responsibility |
|--------|---------------|
| **model** | Definitions for *Statements, Expressions, Types, and Values*. |
| **programState** | Core runtime structures: *ExeStack, SymTable, Heap, and Sync Tables*. |
| **controller** | Coordinates `oneStep` and `allStep` execution using an `ExecutorService`. |
| **repository** | Manages program states and logs execution history. |
| **gui** | JavaFX application and GUI components for visual execution. |
| **view** | Command-line interface and the `TypeChecker` utility. |

---

# 🧪 Example Programs

The interpreter includes multiple example programs demonstrating language features such as:

* arithmetic expressions
* heap allocation and references
* loops and conditionals
* concurrent execution using `fork`
* semaphore synchronization
* barrier synchronization
* lock-based mutual exclusion
* countdown latch synchronization
* procedures and function calls

---

# ▶️ Running the Application

## Prerequisites

* Java **11+**
* JavaFX
