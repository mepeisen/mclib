@echo off

set /a COUNTA=4500

setlocal enableextensions disabledelayedexpansion

:LOOP
copy models\blockstates.json src\main\resources\assets\mclib\blockstates\custom-%COUNTA%.json

copy models\items.json src\main\resources\assets\mclib\models\item\custom-%COUNTA%.json
set "search=COUNT"
set "replace=%COUNTA%"
set "textFile=src\main\resources\assets\mclib\models\item\custom-%COUNTA%.json"
for /f "delims=" %%i in ('type "%textFile%" ^& break ^> "%textFile%" ') do (
        set "line=%%i"
        setlocal enabledelayedexpansion
        >>"%textFile%" echo(!line:%search%=%replace%!
        endlocal
    )
set /a COUNTA+=1
if %COUNTA% lss 6001 goto LOOP
