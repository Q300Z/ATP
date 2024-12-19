@echo off

:: Définir les variables de répertoire
set SRC_DIR=src
set BUILD_DIR=build
set OUTPUT_JAR=AndroidDeviceManager.jar
set MANIFEST_FILE=MANIFEST.MF

:: Créer le répertoire de compilation s'il n'existe pas
if not exist "%BUILD_DIR%" (
    mkdir "%BUILD_DIR%"
)

:: Compiler le projet Java
echo Compilation du projet Java...
javac -source 1.8 -target 1.8 -d "%BUILD_DIR%" -sourcepath "%SRC_DIR%" "%SRC_DIR%\*.java"

:: Vérifier si la compilation a réussi
if %ERRORLEVEL% neq 0 (
    echo Erreur de compilation!
    exit /b 1
)

:: Créer le fichier JAR avec le manifeste
echo Création du fichier JAR avec manifest...
jar cfm "%OUTPUT_JAR%" "%MANIFEST_FILE%" -C "%BUILD_DIR%" .

:: Vérifier si le fichier JAR a été créé
if not exist "%OUTPUT_JAR%" (
    echo Erreur lors de la création du fichier JAR!
    exit /b 1
)

echo Compilation terminée avec succès ! Le fichier JAR est : %OUTPUT_JAR%
exit /b 0