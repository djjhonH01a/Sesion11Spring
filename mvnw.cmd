@REM Maven Wrapper - Windows
@REM https://maven.apache.org/wrapper/

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_PSMODULEPRESENT__=
@SET __MVNW_SCRIPT__=%~f0

@IF NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir

SET "MAVEN_PROJECTBASEDIR=%~dp0"
IF NOT "%MAVEN_PROJECTBASEDIR:~-1%"=="" SET "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%\"

:findBaseDir
IF EXIST "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar" goto endDetectBaseDir
SET "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%.."
IF NOT "%MAVEN_PROJECTBASEDIR:~-1%"=="" SET "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%\"
IF EXIST "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar" goto endDetectBaseDir
SET "MAVEN_PROJECTBASEDIR=%CD%"
IF EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" goto endDetectBaseDir
SET "MAVEN_PROJECTBASEDIR=%CD%"

:endDetectBaseDir

SET "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
SET "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

IF NOT EXIST "%WRAPPER_JAR%" (
  echo Error: maven-wrapper.jar no encontrado en %WRAPPER_JAR%
  exit /B 1
)

"%JAVA_HOME%\bin\java.exe" -version >NUL 2>&1
IF %ERRORLEVEL% EQU 0 (
  SET "JAVACMD=%JAVA_HOME%\bin\java.exe"
) ELSE (
  SET "JAVACMD=java"
)

"%JAVACMD%" ^
  -classpath "%WRAPPER_JAR%" ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  %WRAPPER_LAUNCHER% %*
IF ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%
exit /B %ERROR_CODE%
