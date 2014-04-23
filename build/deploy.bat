@echo off

cd ..
cd ..

:SETUP
set SRC=%cd%\src\minecraft\net\minecraft\src\
set CL=%cd%\src\minecraft\net\minecraft\client\
set CS=%cd%\src\minecraft\net\minecraft\server\
set MODSSRC=%cd%\src-mods\src\client\
set MODSCL=%cd%\src-mods\src\client-client\
set MODSCS=%cd%\src-mods\src\client-server\
set CONF=%cd%\src-mods\build\conf_deploy\
cls
Echo.
Echo. WARNING : The Original Minecraft Source will not be saved.
Echo.
Echo.Press enter to deploy the repository
pause >NUL
cls

:DEL
Echo.
Echo. * Deleting old files
Echo.
for /f %%a in (%CONF%\deploy_src.txt) do del %SRC%\%%a
for /f %%a in (%CONF%\deploy_server.txt) do del %CS%\%%a
for /f %%a in (%CONF%\deploy_client.txt) do del %CL%\%%a

:LINK
Echo.
Echo. * Linking src files
Echo.
fsutil hardlink create %CL%\ClientBrandRetriever.java %MODSCL%\ssp\ClientBrandRetriever.java
fsutil hardlink create %CS%\MinecraftServer.java %MODSCS%\ssp\MinecraftServer.java
fsutil hardlink create %SRC%\Minecraft.java %MODSCL%\ssp\Minecraft.java
fsutil hardlink create %cd%\jars\assets\char.png %cd%\src-mods\resources\char.png
%cd%\src-mods\build\ln.exe %cd%\src-mods\src\client\ %cd%\src\minecraft\net\minecraft\src\mods\
%cd%\src-mods\build\ln.exe %cd%\src-mods\resources\olddays\ %cd%\jars\assets\olddays\
%cd%\src-mods\build\ln.exe %cd%\src-mods\resources\textures\ %cd%\jars\assets\textures\
%cd%\src-mods\build\wget.exe http://dev.bukkit.org/media/files/715/447/worldedit-5.5.7.jar -cO jars/WorldEdit.jar
fsutil hardlink create %cd%\jars\WorldEdit.jar %cd%\lib\WorldEdit.jar
pause
cls
