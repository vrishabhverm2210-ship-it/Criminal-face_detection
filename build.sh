#!/bin/bash
# ============================================================
#  CriminalFaceDB — Linux/Mac Build Script
# ============================================================

SRC="src"
BIN="bin"
LIB="lib/mysql-connector-j.jar"

mkdir -p $BIN

echo "[1/3] Compiling..."
javac -cp $LIB -d $BIN \
  $SRC/criminaldb/model/Criminal.java \
  $SRC/criminaldb/db/DBConnection.java \
  $SRC/criminaldb/db/CriminalDAO.java \
  $SRC/criminaldb/db/CaseDAO.java \
  $SRC/criminaldb/utils/UITheme.java \
  $SRC/criminaldb/ui/DashboardPanel.java \
  $SRC/criminaldb/ui/CriminalFormDialog.java \
  $SRC/criminaldb/ui/CriminalPanel.java \
  $SRC/criminaldb/ui/CrimePanel.java \
  $SRC/criminaldb/ui/AnalyticsPanel.java \
  $SRC/criminaldb/ui/FaceRecPanel.java \
  $SRC/criminaldb/ui/MainFrame.java

if [ $? -ne 0 ]; then echo "[ERROR] Failed"; exit 1; fi

echo "[2/3] Success! Running..."
java -cp "$BIN:$LIB" criminaldb.ui.MainFrame
