CriminalFaceDB

Criminal Records & Crime Management System

CriminalFaceDB is a Java Swing desktop application backed by MySQL for managing criminal records, crimes, investigation cases, and police officers. The application provides a dark-themed user interface, criminal profile management, crime and case tracking, officer management, and an analytics dashboard.

The project also includes a Face Scanner prototype interface designed as the foundation for future real-time face detection and facial-recognition functionality.

Note: The current face scanner is a demonstration prototype and does not perform real facial recognition yet.

📌 Table of Contents

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

🔎 Overview

CriminalFaceDB is a police-record management system designed for educational and demonstration purposes.

The application allows users to:

Manage detailed criminal profiles

Store criminal photographs

Record crimes associated with criminals

Track investigation cases

Manage police officer information

Monitor wanted status

View crime statistics

Analyze case statuses

Search and filter criminal records

Use a prototype face-scanner interface

The application uses Java Swing/AWT for the desktop interface and MySQL as the backend database.

The database structure follows a DAO-based architecture using JDBC for communication between the Java application and MySQL.

✨ Features

📊 Dashboard

The dashboard provides an overview of the database using live statistics.

It displays:

Total number of criminals

Total wanted criminals

Total crimes

Open cases

Solved cases

👤 Criminal Management

The criminal management module allows users to:

Add new criminal records

Edit existing criminal records

Delete criminal records

Search criminals

Filter criminals by different attributes

View detailed criminal information

Mark criminals as wanted

Upload criminal photographs

Each criminal profile can contain:

Name

Age

Gender

Address

Nationality

Height

Weight

Blood group

Wanted status

Photograph

📷 Photo Handling

The application supports uploading photographs for criminal profiles.

Uploaded photographs can be associated with individual criminal records and displayed in the criminal management interface.

The current project stores image paths under the application's image resources.

🚨 Crime Management

The crime module allows users to manage crime records associated with criminals.

Crime records include:

Crime type

Description

Date

Location

Status

Severity

Supported severity levels include:

Low

Medium

High

Critical

Users can browse and filter crime records according to different attributes.

📁 Case Management

The case management system allows investigation cases to be tracked using FIR/case numbers.

Each case can contain:

Case number

Associated criminal

Assigned police officer

Case status

Opened date

Closed date

Court date

Investigation notes

This allows criminal records to be connected with their corresponding investigations.

👮 Police Officer Management

The application includes a police officer directory.

Officer information includes:

Officer name

Badge number

Rank

Department

Police station

Contact information

Officers can also be associated with investigation cases.

📈 Crime Analytics

The analytics dashboard provides a visual representation of crime and case data.

It currently includes:

Crime Type Analysis

A bar chart displaying crimes grouped by type.

Case Status Analysis

A pie chart showing the distribution of cases according to their status.

The charts are custom-drawn using Java Swing/AWT.

🔍 Face Scanner

The application includes a dedicated Face Scanner interface.

The interface provides:

Camera controls

Scan functionality

Identification result display

Criminal information presentation

However, the current implementation is a prototype.

Actual facial recognition functionality is planned for future development.

🛠️ Tech Stack

Layer

Technology

Programming Language

Java 11+

User Interface

Java Swing / AWT

Database

MySQL 8+

Database Connectivity

JDBC

Architecture

DAO Pattern

Build System

Plain Java compilation scripts

Face Recognition

Prototype / Planned

Version Control

Git & GitHub

📂 Project Structure

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

🗄️ Database Schema

The application uses a MySQL database named:

criminal_db

Main Tables

criminals

Stores personal information about criminals.

Includes:

Criminal ID

Name

Age

Gender

Address

Nationality

Height

Weight

Blood group

Photo path

Wanted status

crimes

Stores crime information associated with criminals.

Includes:

Crime ID

Criminal ID

Crime type

Description

Date

Location

Status

Severity

police_officers

Stores information about police officers.

Includes:

Officer ID

Name

Badge number

Rank

Department

Station

Contact information

cases

Stores investigation and case information.

Includes:

Case ID

Case/FIR number

Criminal ID

Officer ID

Status

Opened date

Closed date

Court date

Notes

face_encodings

A table reserved for storing facial embeddings associated with criminal records.

Currently reserved for future facial-recognition integration.

Database Views

The project also includes database views for simplifying data analysis.

v_criminal_summary

Provides summarized criminal information along with associated crime and case counts.

v_case_details

Combines case information with criminal and police officer details.

🚀 Getting Started

Prerequisites

Before running the application, make sure you have:

JDK 11 or newer

MySQL Server 8+

MySQL Connector/J

Git

Windows, Linux, or macOS

1. Clone the Repository

git clone https://github.com/vrishabhverm2210-ship-it/Criminal-face_detection.git

Navigate into the project:

cd Criminal-face_detection

2. Create the Database

Start your MySQL server and execute:

mysql -u root -p < sql/schema.sql

This will:

Create the criminal_db database

Create all required tables

Create database views

Insert sample data

3. Add MySQL Connector/J

Download MySQL Connector/J and place the .jar file inside:

lib/

Rename the file to:

mysql-connector-j.jar

The build scripts expect this exact filename.

⚙️ Configure Database Connection

Open:

src/criminaldb/db/DBConnection.java

Update the database credentials if required.

Example:

private static final String DB_URL =
    "jdbc:mysql://localhost:3306/criminal_db?useSSL=false&serverTimezone=UTC";

private static final String DB_USER = "root";

private static final String DB_PASS = "";

If your MySQL installation has a password, replace the empty password with your MySQL password.

Example

private static final String DB_PASS = "your_password";

For production applications, database credentials should not be hard-coded. A future version will move credentials to environment variables or a configuration file.

▶️ Build and Run

Windows

Run:

build.bat

The script compiles the Java source files and launches:

criminaldb.ui.MainFrame

Linux / macOS

Make the build script executable:

chmod +x build.sh

Then run:

./build.sh

📖 Usage Guide

Dashboard

The dashboard provides an overview of the entire system.

You can view:

Total criminals

Wanted criminals

Total crimes

Open cases

Solved cases

Criminals

Navigate to the Criminals section.

You can:

Add a criminal

Edit a criminal

Delete a criminal

Search criminals

Upload a photograph

View criminal details

Manage wanted status

Crimes

The Crimes section allows you to:

View crime records

Filter crimes

Add crime information

View crime severity

Track crime status

Cases

The Cases section allows you to manage:

FIR/case numbers

Assigned officers

Associated criminals

Case status

Investigation dates

Court dates

Notes

Officers

The Officers section provides information about police personnel associated with investigation cases.

Analytics

The Analytics section provides graphical insights into:

Crime types

Case statuses

This makes it easier to understand crime and investigation trends within the database.

🤖 Face Recognition Status

Current Status: Prototype

The Face Scanner module is currently a demonstration interface and does not perform actual facial recognition.

Current functionality

The interface currently provides:

Camera controls

Scan button

Identification result interface

Criminal result display

However:

There is no live webcam feed yet.

Facial features are not currently extracted.

Facial embeddings are not currently generated.

The system does not perform real face-to-face matching.

The face_encodings database table is currently reserved for future use.

The current implementation is intended to demonstrate the user interface and prepare the architecture for future face-recognition integration.

🔮 Planned Face Recognition Integration

Several possible approaches can be used in future versions.

Option 1 — OpenCV

Use OpenCV with Java for:

Webcam capture

Face detection

Image preprocessing

Facial feature extraction

Possible components:

VideoCapture
CascadeClassifier
Face Detection
Face Embedding
Face Matching

Option 2 — Python Face Recognition Bridge

A Python-based face-recognition service could be integrated with the Java application.

Possible architecture:

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

Option 3 — DeepFace

DeepFace could be integrated for:

Face detection

Face verification

Face embedding generation

Face database searching

🗺️ Roadmap

The following improvements are planned:

Real-time webcam capture

Real face detection using OpenCV

Facial embedding generation

Face matching against stored embeddings

Configurable confidence threshold

Confidence score display

Login system

Role-based access control

Complete CRUD for crimes and cases

Environment-based database configuration

PDF report generation

CSV report export

Maven or Gradle build system

Improved database security

Advanced analytics dashboard

Improved error handling and validation

⚠️ Responsible Use

Facial recognition technology can produce false matches and may introduce privacy, security, and bias-related risks.

This project is developed for educational and demonstration purposes.

All sample criminal records included in the project are fictional.

Any real-world implementation would require:

Proper authorization

Appropriate security controls

Human verification

Accurate and validated models

Privacy protections

Compliance with applicable laws and regulations

A facial-recognition match should never be treated as the sole basis for accusing or taking action against an individual.

🤝 Contributing

Contributions and improvements are welcome.

1. Fork the Repository

Fork the project on GitHub.

2. Clone Your Fork

git clone https://github.com/your-username/Criminal-face_detection.git

3. Create a Feature Branch

git checkout -b feature/your-feature

4. Make Your Changes

Implement and test your changes.

5. Commit Your Changes

git add .
git commit -m "Add your feature"

6. Push Your Branch

git push origin feature/your-feature

7. Open a Pull Request

Create a pull request on GitHub describing your changes.

👨‍💻 Author

Rishabh Verma

B.Tech — Computer Science & Engineering

GitHub:

https://github.com/vrishabhverm2210-ship-it

Repository:

https://github.com/vrishabhverm2210-ship-it/Criminal-face_detection
