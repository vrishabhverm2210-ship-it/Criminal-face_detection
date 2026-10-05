-- ============================================================
--  CRIMINAL FACE RECOGNITION & CRIME DATABASE SYSTEM
--  Database Schema + Dummy Data
-- ============================================================

CREATE DATABASE IF NOT EXISTS criminal_db;
USE criminal_db;

-- ============================================================
-- TABLE 1: criminals
-- ============================================================
CREATE TABLE IF NOT EXISTS criminals (
    criminal_id   INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    age           INT,
    gender        ENUM('Male','Female','Other'),
    address       VARCHAR(255),
    nationality   VARCHAR(50) DEFAULT 'Indian',
    height_cm     INT,
    weight_kg     INT,
    blood_group   VARCHAR(5),
    photo_path    VARCHAR(500),
    is_wanted     BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLE 2: crimes
-- ============================================================
CREATE TABLE IF NOT EXISTS crimes (
    crime_id      INT AUTO_INCREMENT PRIMARY KEY,
    criminal_id   INT NOT NULL,
    crime_type    VARCHAR(100) NOT NULL,
    description   TEXT,
    date_occurred DATE,
    location      VARCHAR(200),
    status        ENUM('Open','Under Investigation','Closed','Wanted') DEFAULT 'Open',
    severity      ENUM('Low','Medium','High','Critical') DEFAULT 'Medium',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (criminal_id) REFERENCES criminals(criminal_id) ON DELETE CASCADE
);

-- ============================================================
-- TABLE 3: police_officers
-- ============================================================
CREATE TABLE IF NOT EXISTS police_officers (
    officer_id    INT AUTO_INCREMENT PRIMARY KEY,
    badge_number  VARCHAR(20) UNIQUE NOT NULL,
    name          VARCHAR(100) NOT NULL,
    rank          ENUM('Constable','Sub-Inspector','Inspector','DSP','SP','SSP','DIG','IG','DGP') DEFAULT 'Inspector',
    department    VARCHAR(100),
    station       VARCHAR(100),
    phone         VARCHAR(15),
    email         VARCHAR(100),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLE 4: cases
-- ============================================================
CREATE TABLE IF NOT EXISTS cases (
    case_id       INT AUTO_INCREMENT PRIMARY KEY,
    case_number   VARCHAR(30) UNIQUE NOT NULL,
    criminal_id   INT NOT NULL,
    officer_id    INT NOT NULL,
    case_title    VARCHAR(200),
    case_status   ENUM('Open','Under Investigation','Solved','Closed','Cold') DEFAULT 'Open',
    date_opened   DATE,
    date_closed   DATE,
    court_date    DATE,
    notes         TEXT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (criminal_id) REFERENCES criminals(criminal_id) ON DELETE CASCADE,
    FOREIGN KEY (officer_id)  REFERENCES police_officers(officer_id) ON DELETE RESTRICT
);

-- ============================================================
-- TABLE 5: face_encodings (for face recognition)
-- ============================================================
CREATE TABLE IF NOT EXISTS face_encodings (
    encoding_id   INT AUTO_INCREMENT PRIMARY KEY,
    criminal_id   INT NOT NULL,
    encoding_data LONGBLOB,
    added_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (criminal_id) REFERENCES criminals(criminal_id) ON DELETE CASCADE
);

-- ============================================================
-- DUMMY DATA: Police Officers
-- ============================================================
INSERT INTO police_officers (badge_number, name, rank, department, station, phone, email) VALUES
('PB001', 'Rajveer Singh',      'Inspector',     'CID',          'Civil Lines, Ludhiana',    '9876500001', 'rajveer@punjabpolice.gov.in'),
('PB002', 'Harpreet Kaur',      'Sub-Inspector', 'Special Branch','Model Town, Ludhiana',    '9876500002', 'harpreet@punjabpolice.gov.in'),
('PB003', 'Gurpreet Sharma',    'DSP',           'Anti-Terror',   'Police Lines, Amritsar',  '9876500003', 'gurpreet@punjabpolice.gov.in'),
('PB004', 'Mandeep Brar',       'Inspector',     'Narcotics',     'Sadar, Patiala',          '9876500004', 'mandeep@punjabpolice.gov.in'),
('PB005', 'Sukhwinder Gill',    'SP',            'CID',           'Sector 17, Chandigarh',   '9876500005', 'sukhwinder@punjabpolice.gov.in');

-- ============================================================
-- DUMMY DATA: Criminals
-- ============================================================
INSERT INTO criminals (name, age, gender, address, nationality, height_cm, weight_kg, blood_group, is_wanted) VALUES
('Balwinder "Balli" Thakur',   34, 'Male',   '12 Guru Nanak Street, Ludhiana',       'Indian', 172, 74, 'B+',  TRUE),
('Satnam Singh Dhillon',        28, 'Male',   '45 Model Town Ext, Amritsar',          'Indian', 168, 70, 'O+',  TRUE),
('Priya Verma',                 31, 'Female', 'House 7, Sector 32, Chandigarh',       'Indian', 160, 55, 'A+',  FALSE),
('Kuldeep "Kala" Randhawa',     42, 'Male',   'Village Machike, Ludhiana',            'Indian', 175, 85, 'AB+', TRUE),
('Ravinder Singh Bhatia',       37, 'Male',   '88 BRS Nagar, Ludhiana',               'Indian', 178, 82, 'B-',  TRUE),
('Manpreet "Manny" Sidhu',      26, 'Male',   '3 Ranjit Nagar, Patiala',             'Indian', 165, 67, 'O-',  FALSE),
('Deepika Nanda',               29, 'Female', 'Flat 4B, Dugri Phase 1, Ludhiana',    'Indian', 155, 52, 'A-',  TRUE),
('Amarjit "Ajju" Bhullar',      45, 'Male',   'Old Octroi Post, Jalandhar',          'Indian', 170, 90, 'O+',  TRUE),
('Gurjant Singh Pamma',         33, 'Male',   'Village Sahnewal, Ludhiana',           'Indian', 173, 78, 'B+',  FALSE),
('Ranjit Lal Vohra',            50, 'Male',   '22 Sarabha Nagar, Ludhiana',           'Indian', 167, 88, 'A+',  TRUE);

-- ============================================================
-- DUMMY DATA: Crimes
-- ============================================================
INSERT INTO crimes (criminal_id, crime_type, description, date_occurred, location, status, severity) VALUES
(1,  'Drug Trafficking',    'Caught with 5kg heroin near Amritsar highway',          '2024-03-12', 'NH1, Amritsar Bypass',          'Under Investigation', 'Critical'),
(1,  'Gang Activity',       'Leader of organized drug network in Ludhiana zone',      '2023-11-20', 'Model Town, Ludhiana',          'Open',                'High'),
(2,  'Robbery',             'Armed robbery at Punjab National Bank',                  '2024-01-15', 'GT Road, Amritsar',             'Wanted',              'Critical'),
(2,  'Vehicle Theft',       'Stole 3 motorcycles in residential area',                '2023-09-08', 'Green Park Colony, Amritsar',   'Closed',              'Medium'),
(3,  'Cybercrime',          'Online fraud — duped 40 victims of ₹18 lakhs',          '2024-02-28', 'Sector 32, Chandigarh',         'Under Investigation', 'High'),
(4,  'Murder',              'Accused in contract killing of rival gang leader',       '2023-06-04', 'Focal Point, Ludhiana',         'Under Investigation', 'Critical'),
(4,  'Extortion',           'Extorted ₹50 lakhs from local businessman',             '2023-12-19', 'Gill Road, Ludhiana',           'Open',                'High'),
(5,  'Arms Smuggling',      'Seized with 4 illegal firearms and ammunition',         '2024-04-01', 'Ferozepur Road, Ludhiana',      'Under Investigation', 'Critical'),
(6,  'Theft',               'Shoplifting spree across 12 stores',                    '2023-10-11', 'Adalat Bazaar, Patiala',        'Closed',              'Low'),
(7,  'Drug Peddling',       'Selling narcotics near school zones',                   '2024-03-25', 'Dugri Road, Ludhiana',          'Wanted',              'High'),
(8,  'Kidnapping',          'Abducted businessman for ransom of ₹2 crore',          '2024-01-30', 'Jalandhar Bypass',              'Under Investigation', 'Critical'),
(9,  'Fraud',               'Property fraud — forged documents worth ₹80 lakhs',    '2023-08-15', 'Sahnewal, Ludhiana',            'Closed',              'Medium'),
(10, 'Drug Trafficking',    'International drug smuggling network operator',         '2024-02-10', 'Ludhiana Railway Station',      'Open',                'Critical'),
(10, 'Money Laundering',    'Laundered ₹3 crore through shell businesses',          '2023-07-22', 'Sarabha Nagar, Ludhiana',       'Under Investigation', 'High');

-- ============================================================
-- DUMMY DATA: Cases
-- ============================================================
INSERT INTO cases (case_number, criminal_id, officer_id, case_title, case_status, date_opened, court_date, notes) VALUES
('FIR/LDH/2024/0312', 1,  1, 'Balli Thakur Drug Network Investigation',    'Under Investigation', '2024-03-12', '2024-08-15', 'Linked to cross-border smuggling'),
('FIR/AMR/2024/0115', 2,  2, 'Punjab National Bank Armed Robbery',          'Open',                '2024-01-15', '2024-09-20', 'CCTV footage retrieved'),
('FIR/CHD/2024/0228', 3,  3, 'Chandigarh Online Fraud Ring',                'Solved',              '2024-02-28', '2024-07-10', 'Accused arrested, chargesheet filed'),
('FIR/LDH/2023/0604', 4,  1, 'Focal Point Murder — Randhawa',               'Under Investigation', '2023-06-04', '2024-10-05', 'Key witness protection active'),
('FIR/LDH/2024/0401', 5,  4, 'Illegal Arms Seizure — Bhatia',               'Under Investigation', '2024-04-01', '2024-11-12', 'Forensics sent to CFSL'),
('FIR/JAL/2024/0130', 8,  5, 'Jalandhar Kidnapping — Bhullar Gang',         'Under Investigation', '2024-01-30', '2024-09-30', 'Victim recovered, accused absconding'),
('FIR/LDH/2024/0325', 7,  2, 'Drug Peddling Near Schools — Deepika Nanda',  'Open',                '2024-03-25', NULL,         'Ongoing surveillance'),
('FIR/LDH/2024/0210', 10, 1, 'International Drug Trafficking — Vohra',      'Open',                '2024-02-10', '2024-12-01', 'NCB and Punjab Police joint operation');

-- Useful views
CREATE OR REPLACE VIEW v_criminal_summary AS
SELECT 
    c.criminal_id, c.name, c.age, c.gender, c.address, c.is_wanted,
    COUNT(DISTINCT cr.crime_id) AS total_crimes,
    COUNT(DISTINCT ca.case_id) AS total_cases
FROM criminals c
LEFT JOIN crimes cr ON c.criminal_id = cr.criminal_id
LEFT JOIN cases ca  ON c.criminal_id = ca.criminal_id
GROUP BY c.criminal_id;

CREATE OR REPLACE VIEW v_case_details AS
SELECT 
    ca.case_id, ca.case_number, ca.case_title, ca.case_status,
    cr.name AS criminal_name, cr.is_wanted,
    po.name AS officer_name, po.rank,
    ca.date_opened, ca.court_date
FROM cases ca
JOIN criminals cr      ON ca.criminal_id = cr.criminal_id
JOIN police_officers po ON ca.officer_id  = po.officer_id;
