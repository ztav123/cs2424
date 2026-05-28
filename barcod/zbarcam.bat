@set PATH=%PATH%;C:\Users\lamth\OneDrive\Desktop\pos-system\cs2424\barcod
@echo This is the zbarcam output window.
@echo Hold a bar code in front of the camera (make sure it's in focus!)
@echo and decoded results will appear below.
@echo.
@echo Initializing camera, please wait...
@echo.
@py -3.10 barcode_scanner.py
@if errorlevel 1 pause
