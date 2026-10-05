# CriminalFaceDB

### Criminal Records & Crime Management System

CriminalFaceDB is a Java Swing desktop application backed by MySQL for managing criminal records, crimes, investigation cases, and police officers. The application provides a dark-themed user interface, criminal profile management, crime and case tracking, officer management, and an analytics dashboard.

The project also includes a Face Scanner prototype interface designed as the foundation for future real-time face detection and facial-recognition functionality.

> **Note:** The current face scanner is a demonstration prototype and does not perform real facial recognition yet.

---

## 📌 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#️-tech-stack)
- [Project Structure](#-project-structure)
- [Database Schema](#️-database-schema)
- [Getting Started](#-getting-started)
- [Usage Guide](#-usage-guide)
- [Face Recognition Status](#-face-recognition-status)
- [Roadmap](#️-roadmap)
- [Responsible Use](#️-responsible-use)
- [Contributing](#-contributing)
- [Author](#-author)

---

## 🔎 Overview

CriminalFaceDB is a police-record management system designed for educational and demonstration purposes.

The application allows users to:

- Manage detailed criminal profiles
- Store criminal photographs
- Record crimes associated with criminals
- Track investigation cases
- Manage police officer information
- Monitor wanted status
- View crime statistics
- Analyze case statuses
- Search and filter records
- Use a prototype face-scanner interface

The application uses Java Swing/AWT for the desktop interface and MySQL as the backend database. It follows a DAO-based architecture, using JDBC for communication between the Java application and MySQL.

---

## ✨ Features

### 📊 Dashboard

The dashboard provides an overview of the database using live statistics. It displays:

- Total number of criminals
- Total wanted criminals
- Total crimes
- Open cases
- Solved cases

### 👤 Criminal Management

The criminal management module allows users to:

- Add new criminal records
- Edit existing criminal records
- Delete criminal records
- Search criminals by name, address, or blood group
- View detailed criminal information
- Mark criminals as wanted
- Upload criminal photographs

Each criminal profile can contain: name, age, gender, address, nationality, height, weight, blood group, wanted status, and photograph.

### 📷 Photo Handling

The application supports uploading photographs for criminal profiles. Uploaded photographs are associated with individual criminal records, saved under `resources/images/`, and displayed in the criminal management interface.

### 🚨 Crime Management

The crime module allows users to manage crime records associated with criminals. Crime records include:

- Crime type
- Description
- Date
- Location
- Status
- Severity (Low, Medium, High, Critical)

Users can browse and filter crime records according to different attributes.

### 📁 Case Management

The case management system tracks investigation cases using FIR/case numbers. Each case can contain:

- Case number
- Associated criminal
- Assigned police officer
- Case status
- Opened date
- Closed date
- Court date
- Investigation notes

### 👮 Police Officer Management

The application includes a police officer directory with officer name, badge number, rank, department, police station, and contact information. Officers can also be associated with investigation cases.

### 📈 Crime Analytics

The analytics dashboard provides a visual representation of crime and case data:

- **Crime Type Analysis** — a bar chart displaying crimes grouped by type.
- **Case Status Analysis** — a pie chart showing the distribution of cases by status.

The charts are custom-drawn using Java Swing/AWT.

### 🔍 Face Scanner

The application includes a dedicated Face Scanner interface that provides camera controls, scan functionality, identification result display, and criminal information presentation.

However, the current implementation is a prototype. Actual facial recognition is planned for future development.

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
| Programming Language | Java 11+ |
| User Interface | Java Swing / AWT |
| Database | MySQL 8+ |
| Database Connectivity | JDBC |
| Architecture | DAO Pattern |
| Build System | Plain Java compilation scripts |
| Face Recognition | Prototype / Planned |
| Version Control | Git & GitHub |

---

## 📂 Project Structure

```
CriminalFaceDB/
│
├── build.sh
├── build.bat
│
├── lib/
│   └── README.txt
│
├── sql/
│   └── schema.sql
│
└── src/
    └── criminaldb/
        │
        ├── model/
        │   └── Criminal.java
        │
        ├── db/
        │   ├── DBConnection.java
        │   ├── CriminalDAO.java
        │   └── CaseDAO.java
        │
        ├── utils/
        │   └── UITheme.java
        │
        └── ui/
            ├── MainFrame.java
            ├── DashboardPanel.java
            ├── CriminalPanel.java
            ├── CriminalFormDialog.java
            ├── CrimePanel.java
            ├── AnalyticsPanel.java
            ├── FaceRecPanel.java
            └── StubPanels.java
```

---

## 🗄️ Database Schema

The application uses a MySQL database named `criminal_db`.

### Main Tables

**`criminals`** — personal information about criminals: criminal ID, name, age, gender, address, nationality, height, weight, blood group, photo path, wanted status.

**`crimes`** — crimes associated with criminals: crime ID, criminal ID, crime type, description, date, location, status, severity.

**`police_officers`** — police officer information: officer ID, name, badge number, rank, department, station, contact information.

**`cases`** — investigation and case information: case ID, case/FIR number, criminal ID, officer ID, status, opened date, closed date, court date, notes.

**`face_encodings`** — reserved for storing facial embeddings associated with criminal records. Currently reserved for future facial-recognition integration.

### Database Views

- **`v_criminal_summary`** — summarized criminal information with associated crime and case counts.
- **`v_case_details`** — case information combined with criminal and police officer details.

---

## 🚀 Getting Started

### Prerequisites

- JDK 11 or newer
- MySQL Server 8+
- MySQL Connector/J
- Git
- Windows, Linux, or macOS

### 1. Clone the Repository

```bash
git clone https://github.com/vrishabhverm2210-ship-it/Criminal-face_detection.git
cd Criminal-face_detection
```

### 2. Create the Database

Start your MySQL server and execute:

```bash
mysql -u root -p < sql/schema.sql
```

This will create the `criminal_db` database, all required tables, the database views, and insert sample data.

### 3. Add MySQL Connector/J

Download MySQL Connector/J and place the `.jar` file inside `lib/`. Rename it to:

```
mysql-connector-j.jar
```

The build scripts expect this exact filename.

### ⚙️ Configure Database Connection

Open `src/criminaldb/db/DBConnection.java` and update the credentials if required:

```java
private static final String DB_URL =
    "jdbc:mysql://localhost:3306/criminal_db?useSSL=false&serverTimezone=UTC";

private static final String DB_USER = "root";

private static final String DB_PASS = "";
```

If your MySQL installation has a password, replace the empty password with yours:

```java
private static final String DB_PASS = "your_password";
```

> For production applications, database credentials should not be hard-coded. A future version will move credentials to environment variables or a configuration file.

### ▶️ Build and Run

**Windows**

```bash
build.bat
```

**Linux / macOS**

```bash
chmod +x build.sh
./build.sh
```

The script compiles the Java source files and launches `criminaldb.ui.MainFrame`.

---

## 📖 Usage Guide

**Dashboard** — an overview of the entire system: total criminals, wanted criminals, total crimes, open cases, and solved cases.

**Criminals** — add, edit, or delete a criminal, search records, upload a photograph, view details, and manage wanted status.

**Crimes** — view and filter crime records, add crime information, and track crime severity and status.

**Cases** — manage FIR/case numbers, assigned officers, associated criminals, case status, investigation dates, court dates, and notes.

**Officers** — view police personnel associated with investigation cases.

**Analytics** — graphical insights into crime types and case statuses, making it easier to understand trends within the database.

---

## 🤖 Face Recognition Status

**Current Status: Prototype**

The Face Scanner module is currently a demonstration interface and does not perform actual facial recognition.

### Current functionality

The interface provides camera controls, a scan button, an identification result interface, and criminal result display.

However:

- There is no live webcam feed yet.
- Facial features are not currently extracted.
- Facial embeddings are not currently generated.
- The system does not perform real face-to-face matching (the scan returns a random wanted record for demonstration).
- The `face_encodings` table is reserved for future use.

The current implementation demonstrates the user interface and prepares the architecture for future face-recognition integration.

### 🔮 Planned Face Recognition Integration

**Option 1 — OpenCV**

Use OpenCV with Java for webcam capture, face detection, image preprocessing, and facial feature extraction. Possible components: `VideoCapture`, `CascadeClassifier`, face detection, face embedding, face matching.

**Option 2 — Python Face Recognition Bridge**

A Python-based face-recognition service could be integrated with the Java application:

```
Java Swing Application
        │
        ▼
Capture Image
        │
        ▼
Python Face Recognition Service
        │
        ▼
Generate Face Embedding
        │
        ▼
Compare With Stored Embeddings
        │
        ▼
Return Criminal ID
        │
        ▼
Java Application
```

**Option 3 — DeepFace**

DeepFace could be integrated for face detection, face verification, face embedding generation, and face database searching.

---

## 🗺️ Roadmap

- [ ] Real-time webcam capture
- [ ] Real face detection using OpenCV
- [ ] Facial embedding generation
- [ ] Face matching against stored embeddings
- [ ] Configurable confidence threshold
- [ ] Confidence score display
- [ ] Login system
- [ ] Role-based access control
- [ ] Complete CRUD for crimes and cases
- [ ] Environment-based database configuration
- [ ] PDF report generation
- [ ] CSV report export
- [ ] Maven or Gradle build system
- [ ] Improved database security
- [ ] Advanced analytics dashboard
- [ ] Improved error handling and validation

---

## ⚠️ Responsible Use

Facial recognition technology can produce false matches and may introduce privacy, security, and bias-related risks.

This project is developed for educational and demonstration purposes. All sample criminal records included in the project are fictional.

Any real-world implementation would require:

- Proper authorization
- Appropriate security controls
- Human verification
- Accurate and validated models
- Privacy protections
- Compliance with applicable laws and regulations

A facial-recognition match should never be treated as the sole basis for accusing or taking action against an individual.

---

## 🤝 Contributing

Contributions and improvements are welcome.

1. **Fork the repository** on GitHub.
2. **Clone your fork**
   ```bash
   git clone https://github.com/your-username/Criminal-face_detection.git
   ```
3. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature
   ```
4. **Make your changes** — implement and test them.
5. **Commit your changes**
   ```bash
   git add .
   git commit -m "Add your feature"
   ```
6. **Push your branch**
   ```bash
   git push origin feature/your-feature
   ```
7. **Open a pull request** on GitHub describing your changes.

---

## 👨‍💻 Author

**Rishabh Verma**

B.Tech — Computer Science & Engineering

- GitHub: [vrishabhverm2210-ship-it](https://github.com/vrishabhverm2210-ship-it)
- Repository: [Criminal-face_detection](https://github.com/vrishabhverm2210-ship-it/Criminal-face_detection)
