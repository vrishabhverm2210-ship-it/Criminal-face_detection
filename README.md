CriminalFaceDB
Criminal Face Recognition & Crime Database System — a Java Swing desktop application backed by MySQL for managing criminal records, crimes, cases and police officers, with a face-scanner module and a crime analytics dashboard.

Java MySQL UI Status

Table of Contents
Overview
Features
Tech Stack
Project Structure
Database Schema
Getting Started
Usage Guide
Face Recognition Status
Roadmap
Responsible Use
Contributing
Author
Overview
CriminalFaceDB is a police-records management system with a dark-themed desktop interface. It stores detailed criminal profiles (with photos), links them to crimes and investigation cases handled by police officers, and visualizes crime statistics. A Face Scanner screen provides the interface for identifying a person against the database.

The project ships with sample data themed around Punjab Police (Ludhiana, Amritsar, Patiala, Chandigarh, Jalandhar) so it can be demonstrated immediately after setup.

Features
Dashboard — live stat cards: total criminals, wanted count, total crimes, open cases, solved cases.
Criminal management — add, edit, delete and search criminals by name, address or blood group; each profile stores age, gender, address, nationality, height, weight, blood group and wanted status.
Photo handling — upload a photo per criminal (stored in resources/images/) and preview it in the table view.
Crime records — view and filter crimes by type, status and severity (Low / Medium / High / Critical).
Case management — track FIR-numbered cases with assigned officer, status, opened/closed dates, court date and notes.
Officer directory — police officers with badge number, rank, department and station.
Analytics — custom-drawn bar chart (crimes by type) and pie chart (case status breakdown).
Face Scanner screen — camera controls, scan action and an identification result card (see Face Recognition Status).
Tech Stack
Layer	Technology
Language	Java 11+
UI	Java Swing / AWT (custom dark theme in UITheme)
Database	MySQL 8+
DB access	JDBC via mysql-connector-j, DAO pattern
Build	Plain javac scripts (build.sh, build.bat)
Project Structure
CriminalFaceDB/
├── build.sh                  # Compile & run (Linux / macOS)
├── build.bat                 # Compile & run (Windows)
├── lib/
│   └── README.txt            # Put mysql-connector-j.jar here
├── sql/
│   └── schema.sql            # Tables, views and sample data
└── src/criminaldb/
    ├── model/
    │   └── Criminal.java
    ├── db/
    │   ├── DBConnection.java # JDBC connection settings
    │   ├── CriminalDAO.java  # Criminal CRUD, search, statistics
    │   └── CaseDAO.java      # Crimes, cases, officers
    ├── utils/
    │   └── UITheme.java      # Colors, fonts, button styling
    └── ui/
        ├── MainFrame.java    # Entry point, sidebar navigation
        ├── DashboardPanel.java
        ├── CriminalPanel.java
        ├── CriminalFormDialog.java
        ├── CrimePanel.java   # Also contains Cases and Officers panels
        ├── AnalyticsPanel.java
        └── FaceRecPanel.java
Database Schema
Database name: criminal_db

Table	Purpose
criminals	Personal details, photo path, wanted flag
crimes	Crime type, description, date, location, status, severity (FK → criminals)
police_officers	Badge number, rank, department, station, contact
cases	Case number, linked criminal and officer, status, dates, notes
face_encodings	Reserved for face embeddings (LONGBLOB) per criminal
Two views are included: v_criminal_summary (crime and case counts per criminal) and v_case_details (cases joined with criminal and officer names).

Getting Started
Prerequisites
JDK 11 or newer
MySQL Server 8+
MySQL Connector/J (.jar)
1. Clone the repository
git clone https://github.com/tranumgrover/CriminalFaceDB.git
cd CriminalFaceDB
2. Create the database
mysql -u root -p < sql/schema.sql
This creates criminal_db, all tables, views and the sample data.

3. Add the JDBC driver
Download Connector/J and place it in lib/ named exactly mysql-connector-j.jar (the build scripts expect this filename).

4. Configure the connection
Edit src/criminaldb/db/DBConnection.java if your MySQL settings differ:

private static final String DB_URL  = "jdbc:mysql://localhost:3306/criminal_db?useSSL=false&serverTimezone=UTC";
private static final String DB_USER = "root";
private static final String DB_PASS = "";   // set your password
5. Build and run
# Linux / macOS
chmod +x build.sh
./build.sh

# Windows
build.bat
The scripts compile everything into bin/ and launch criminaldb.ui.MainFrame.

Usage Guide
Dashboard — overview of the system at a glance.
Criminals — click Add to create a record, select a row to edit/delete it, use the search box to filter, and Upload Photo to attach an image.
Crimes / Cases / Officers — browse records and add new crimes or cases.
Analytics — view crime-type and case-status charts.
Face Scanner — start the camera and click Scan Face to run an identification.
Face Recognition Status
Important: the face scanner is currently a demonstration prototype, not a working recognition engine.

Start Camera shows a placeholder; there is no live webcam feed yet.
Scan Face does not compare facial features. After a short delay it returns a random wanted criminal from the database to demonstrate the result card.
Capture Frame in the Criminals screen saves a screen capture as a placeholder, not a webcam image.
The face_encodings table exists in the schema but is not yet used.
Integration paths are documented in the header comment of FaceRecPanel.java:

OpenCV (Java) — webcam capture with VideoCapture, detection with CascadeClassifier, comparison against stored encodings.
Python bridge — a face_match.py script using the face_recognition library, called from Java and returning the matched criminal_id.
DeepFace — DeepFace.find() against the stored image folder.
Roadmap
 Real webcam capture and face detection (OpenCV)
 Real face matching using stored embeddings in face_encodings
 Confidence score with a configurable match threshold
 Login and role-based access for officers
 Edit/delete for crimes and cases
 Move DB credentials to a config file or environment variables
 Export reports (PDF / CSV)
 Maven or Gradle build
Responsible Use
Facial recognition can produce false matches and carries privacy and bias risks. This project is for educational and demonstration purposes only. All sample records are fictional. Any real-world use would require accurate models, human verification, proper authorization and legal compliance, and a match must never be the sole basis for accusing anyone.

Contributing
Contributions are welcome:

Fork the repository
Create a branch: git checkout -b feature/your-feature
Commit your changes and push the branch
Open a pull request
Author
Rishabh Verma
