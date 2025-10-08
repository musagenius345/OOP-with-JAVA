# VU OOP Java Coursework
## Object-Oriented Programming with Java

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![NetBeans](https://img.shields.io/badge/IDE-NetBeans-blue.svg)](https://netbeans.apache.org/)
[![License](https://img.shields.io/badge/License-Academic-green.svg)](LICENSE)

---
GROUP MEMBERS
1. Musa Badru VU-BCS-2507-0707-EVE
2. BUSINGE Emmanuel Carlos VU-BIT-2411-0676-DAY
3. Dani Joseph VU-BIT-2411-0677-DAY
4. Jurua Arnold VU-BCS-2407-2095-DAY
5. Ahmed mukhtar muse VU-BIT-2411-1144-DAY
6. Lagu Emmanuel Onama Legge VU-DIT-2411-0349-DAY
---

## 📋 Repository Overview

This repository contains complete solutions for the Object-Oriented Programming Java coursework, consisting of two major programming assignments:

1. **Question 1 (15 Marks):** Projectile Motion Simulator - Console Application
2. **Question 2 (25 Marks):** SALSA Exhibition Registration System - GUI + Database Application

---

## Repository Structure

```
VU-OOP-Java-Coursework/
│
├── README.md                           # This file - Main overview
│
├── Question1-ProjectileMotion/
│   ├── src/
│   ├── README.md                       #Detailed Q1 documentation
│
├── Question2-SALSARegistration/
│   ├── src/
│   ├── database/
│   │   └── VUE_Exhibition.accdb
│   ├── screenshots/
│   └── README.md                       # Detailed Q2 documentation
│
├── .gitignore                          # Git ignore file
└── LICENSE                             # Academic use license
```

---

## Project Summary

### Question 1: Projectile Motion Simulator (15 Marks)

**Description:** A console-based Java application that calculates projectile motion physics.

**Key Features:**
- Calculates time of flight, maximum height, and horizontal range
- Input validation for velocity and angle
- Accurate physics formulas implementation
- Formatted output with additional trajectory information

**Technologies:**
- Java (Console Application)
- Java Math Library
- Scanner for user input

**[View Full Documentation →](Question1-ProjectileMotion/README.md)**

---

### Question 2: SALSA Registration System (25 Marks)

**Description:** A full-featured desktop application for managing dance festival registrations with database integration.

**Key Features:**
- Complete CRUD operations (Create, Read, Update, Delete)
- Java Swing GUI with professional design
- MS Access database integration via UCanAccess
- Image upload and display functionality
- Comprehensive input validation
- SQL injection prevention with PreparedStatements

**Technologies:**
- Java Swing (GUI)
- JDBC with MS Access
- UCanAccess JDBC Driver
- JFileChooser (File Handling)
- Regular Expressions (Validation)

**[View Full Documentation →](Question2-SALSARegistration/README.md)**

---

## Quick Start Guide

### Prerequisites

- **Java JDK 8 or higher** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **NetBeans IDE 12+** - [Download](https://netbeans.apache.org/download/)
- **Microsoft Access** - For database viewing
- **Git** - [Download](https://git-scm.com/downloads)

### Clone Repository

```bash
git clone https://github.com/musagenius345/OOP-with-JAVA.git
cd VU-OOP-Java-Coursework
```

### Running Question 1

```bash
cd Question1-ProjectileMotion/src
javac ProjectileMotionSimulator.java
java ProjectileMotionSimulator
```

### Running Question 2

**Using NetBeans:**
1. Open NetBeans IDE
2. File → Open Project
3. Select `Question2-SALSARegistration`
4. Right-click project → Run (F6)

**Using Command Line:**
```bash
cd Question2-SALSARegistration/src
javac -cp ".;../lib/*" SALSARegistrationSystem.java
java -cp ".;../lib/*" SALSARegistrationSystem
```

---

## Technologies Used

### Programming Languages
- **Java 8+** - Primary programming language

### Frameworks & Libraries
- **Java Swing** - GUI framework
- **JDBC** - Database connectivity
- **UCanAccess 5.0.1** - MS Access JDBC driver
- **Apache Commons Lang** - Utility functions
- **HSQLDB** - Embedded database engine
- **Jackcess** - MS Access file parsing

### Development Tools
- **NetBeans IDE** - Integrated Development Environment
- **Microsoft Access** - Database management
- **Git & GitHub** - Version control

### Design Patterns
- **MVC (Model-View-Controller)** - Separation of concerns
- **Singleton** - Database connection management
- **Factory Pattern** - GUI component creation

---

## 📚 Documentation

### Available Documentation

1. **Main README** (this file) - Repository overview
2. **Question 1 README** - Detailed projectile motion documentation
3. **Question 2 README** - Detailed SALSA system documentation
4. **Setup Guide PDF** - Complete installation instructions
5. **Word Document** - summarised coursework with:
   - Github link
   - Screenshots

### JavaDoc

Generate JavaDoc documentation:

```bash
# For Question 1
cd Question1-ProjectileMotion/src
javadoc -d ../docs ProjectileMotionSimulator.java

# For Question 2
cd Question2-SALSARegistration/src
javadoc -d ../docs -cp "../lib/*" SALSARegistrationSystem.java
```

---

## Testing

### Test Coverage

**Question 1:**
- ✅ Valid input scenarios (various velocities and angles)
- ✅ Edge cases (0°, 90°, 45° angles)
- ✅ Input validation (negative values, invalid ranges)
- ✅ Exception handling (non-numeric input)

**Question 2:**
- ✅ Registration with valid data
- ✅ Search existing records
- ✅ Update participant information
- ✅ Delete records with confirmation
- ✅ Image upload and display
- ✅ Email format validation
- ✅ Contact number validation
- ✅ Empty field detection
- ✅ Duplicate ID prevention
- ✅ Database connectivity
- ✅ Exception handling for all operations

### Running Tests

```bash
# Manual testing steps provided in individual README files
# Automated tests can be added using JUnit
```

---

## Security Features

### Question 2 Security Implementations:

1. **SQL Injection Prevention**
   - All queries use PreparedStatements
   - No string concatenation in SQL

2. **Input Validation**
   - Email regex validation
   - Contact number format checking
   - Empty field detection

3. **Exception Handling**
   - Graceful error recovery
   - User-friendly error messages
   - Application stability maintained

4. **Data Sanitization**
   - Input trimming (removes whitespace)
   - Type checking before database operations

---

## 📊 Features Comparison

| Feature | Question 1 | Question 2 |
|---------|-----------|-----------|
| User Interface | Console | GUI (Swing) |
| Data Storage | None | MS Access DB |
| Input Method | Scanner | Text Fields |
| Output Method | Console | GUI + Database |
| Validation | Basic | Comprehensive |
| CRUD Operations | N/A | Full CRUD |
| Image Handling | N/A | Upload & Display |
| Error Handling | Try-Catch | Multi-layer |
| Complexity | Low | High |
| Lines of Code | ~100 | ~500+ |

---

## 🚧 Known Issues & Limitations

### Question 1:
- Console-only interface (no GUI)
- No data persistence
- Limited input validation options

### Question 2:
- Single-user application (no concurrent access)
- Image paths stored as strings (not binary data)
- Database must be on local filesystem
- No data export functionality
- Limited search options (ID only)

### Future Enhancements:
- Add JUnit tests
- Implement multi-user support
- Add export to PDF/Excel
- Create web-based version
- Add advanced search filters
- Implement data analytics dashboard

---


### If you're using this for learning:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -m 'Add YourFeature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

---

## 🙏 Acknowledgments

- **Course Instructor** - For guidance and project requirements
- **Oracle** - For Java SDK and documentation
- **Apache Software Foundation** - For Commons libraries
- **UCanAccess Team** - For the JDBC driver
- **NetBeans Community** - For the excellent IDE
- **Stack Overflow Community** - For troubleshooting assistance


---

## 🔗 Useful Links

### Documentation:
- [Java SE Documentation](https://docs.oracle.com/javase/8/docs/)
- [Java Swing Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)
- [UCanAccess Documentation](http://ucanaccess.sourceforge.net/site.html)

### Learning Resources:
- [Oracle Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- [Effective Java](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Design Patterns](https://java-design-patterns.com/)

### Tools:
- [NetBeans IDE](https://netbeans.apache.org/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Eclipse IDE](https://www.eclipse.org/)

---


<div align="center">

### ⭐ Star this repository if you found it helpful!

**Made with ❤️ by Musa Badru**

</div>
