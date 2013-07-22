OldDays, NBXlite and SpawnHuman return old things that were removed.
NBXlite is fork of InsanityBringer's NoBiomesX.

SSPC is a fork of simo_415's Single Player Commands 3.2.2.
Everything in src/client/spc, bin/spc-* and bin-old/spc-* is licenced under GNU LGPLv3.

To set this up:

1.  Download MCP.

2.  Decompile client using MCP.

3.  Clone this repository to **src-mods** directory.

4.  Go to **src-mods/build** and run **deploy.sh** script. (Or **deploy.bat** if you use Windows)

5.  It is ready. You may now recompile client and start it.

To build a release, run **make.sh** script **in the MCP directory**. Not from src-mods/build!
Resulting zips will be in **reobf/result2**.

build/ln.exe source code can be found here: https://github.com/neosmart/ln-win