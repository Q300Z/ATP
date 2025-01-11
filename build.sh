#!/bin/sh

# Définir les variables de répertoire
SRC_DIR="src"
BUILD_DIR="build"
OUTPUT_JAR="AndroidDeviceManager.jar"
MANIFEST_FILE="MANIFEST.MF"
JAVA_DIR="$HOME/.jdks/temurin-1.8.0_432/bin"

# Créer le répertoire de compilation s'il n'existe pas
mkdir -p "$BUILD_DIR"

# Compiler le projet Java
echo "Compilation du projet Java..."
"$JAVA_DIR"/javac -source 1.8 -target 1.8 -d "$BUILD_DIR" -sourcepath "$SRC_DIR" "$SRC_DIR"/*.java

# Vérifier si la compilation a réussi
if [ $? -ne 0 ]; then
  echo "Erreur de compilation!"
  exit 1
fi

# Créer le fichier JAR avec le MANIFEST
echo "Création du fichier JAR avec manifest..."
"$JAVA_DIR"/jar cfm "$OUTPUT_JAR" "$MANIFEST_FILE" -C "$BUILD_DIR" .

# Vérifier si le fichier JAR a été créé
if [ ! -f "$OUTPUT_JAR" ]; then
  echo "Erreur lors de la création du fichier JAR!"
  exit 1
fi

echo "Compilation terminée avec succès ! Le fichier JAR est : $OUTPUT_JAR"
exit 0