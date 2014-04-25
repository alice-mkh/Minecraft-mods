OldDays, NBXlite and SpawnHuman return old things that were removed.
NBXlite is fork of InsanityBringer's NoBiomesX.

SSPC is a fork of simo_415's Single Player Commands 3.2.2.
Everything in src/client/spc, bin/spc-* and bin-old/spc-* is licenced under GNU LGPLv3.

To set this up:

1.  Download MCP.

2.  Decompile client using MCP.

3.  Clone this repository to **src-mods** directory.

4.  Go to **src-mods/build** and run **deploy.sh** script. (Or **deploy.bat** if you use Windows)
    * In Windows, Symlinks cannot be done without any administrator privileges, that meens you need to be an administrator ; the script will run automatically as administrator, or it will ask you your credentials.

5.  It is ready. You may now recompile client and start it.

To build a release, go to **src-mods/build** and run **make.sh** script. (Or **make.bat** if you use Windows)
Resulting zips will be in **reobf/result2**.
