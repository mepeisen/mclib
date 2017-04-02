@echo off

set /a COUNTA=3500

setlocal enableextensions disabledelayedexpansion

:LOOP
copy models\blockstates.json src\main\resources\assets\mclib\blockstates\custom-%COUNTA%.json

rem copy models\items.json src\main\resources\assets\mclib\models\item\custom-%COUNTA%.json
rem set "search=COUNT"
rem set "replace=%COUNTA%"
rem set "textFile=src\main\resources\assets\mclib\models\item\custom-%COUNTA%.json"
rem for /f "delims=" %%i in ('type "%textFile%" ^& break ^> "%textFile%" ') do (
rem         set "line=%%i"
rem         setlocal enabledelayedexpansion
rem         >>"%textFile%" echo(!line:%search%=%replace%!
rem         endlocal
rem     )

set /a COUNTA+=1
if %COUNTA% lss 4000 goto LOOP
