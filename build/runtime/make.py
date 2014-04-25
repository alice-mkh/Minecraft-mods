import os, shutil, linecache, zipfile

currentpath = os.getcwd()
RESULT_DIR  = "./reobf/result/"
RESULT_DIR2 = "./reobf/result2/"
additional  = "./reobf/additional/"
reobf       = currentpath + "/reobf/minecraft/"
conf_mv     = currentpath + '/src-mods/build/conf_make/make_mv_%s.txt'
conf_cp     = currentpath + '/src-mods/build/conf_make/make_cp_%s.txt'
conf_add    = currentpath + '/src-mods/build/conf_make/make_add_%s.txt'
conf_unused = currentpath + '/src-mods/build/conf_make/make_unused.txt'
conf_mods   = currentpath + '/src-mods/build/conf_make/make_mods_list.txt'
srcdir      = [
				"src-mods/README.md",
				"src-mods/src/client/nbxlite/", 
				"src-mods/src/client/olddays/", 
				"src-mods/src/client/spawnhuman/", 
				"src-mods/src/client/ssp/", 
				"src-mods/src/client-client/", 
				"src-mods/src/client-server/", 
				"src-mods/src/server/", 
				"src-mods/resources/", 
				"src-mods/build/"
			  ]

# Find a class in /reobf/minecraft/ that contains the provided string in the first two lines
def find(classname):
	if os.path.exists(reobf + classname + ".class") == True:
		print("Find: " + classname + ".class" + " --> " + classname + ".java")
		return reobf + classname + ".class"
	for root, dirs, files in os.walk(reobf):
		for file in files:
			lines = linecache.getlines(reobf + file, 2)
			if any(classname + ".java" in s for s in lines):
				print("Find: " + file + " --> " + classname + ".java")
				return reobf + file
	print("Not Found " + classname)
	return

# Universal copy function that automatically detect the type of the source and create automatically the folders for it
def copy(src, dst):
	if os.path.isdir(src) == True:
		shutil.copytree(src, dst)
		return
	if not os.path.exists(os.path.dirname(dst)):
		os.makedirs(os.path.dirname(dst))
	shutil.copy(src, dst)

# Universal move function that automatically detect the type of the source and create automatically the folders for it
def move(src, dst):
	if os.path.isdir(src) == True:
		shutil.move(src, dst)
		return
	if not os.path.exists(os.path.dirname(dst)):
		os.makedirs(os.path.dirname(dst))
	shutil.move(src, dst)

# Universal zip fonction that automatically detect the type of the source and write them into a zip file without the /src-mods/ folder
def zipsrc(srcs, zip):
	src_zip = zipfile.ZipFile(zip, "w" )
	for src in srcs:
		if os.path.isdir(src):
			for dir_, _, files in os.walk(src):
				for fileName in files:
					src_zip.write(dir_ + "/" + fileName, dir_.replace("src-mods", "") + "/" + fileName, zipfile.ZIP_DEFLATED)
		else:
			src_zip.write(src, src.replace("src-mods/", ""), zipfile.ZIP_DEFLATED )
	
# Detects the os and launches mcp scripts to get the latest deobfuscated code
if os.name is "nt":
	os.system("runtime\\bin\\python\\python_mcp.exe runtime\\recompile.py")
	os.system("runtime\\bin\\python\\python_mcp.exe runtime\\reobfuscate.py")
else:
	os.system("python runtime/recompile.py")
	os.system("python runtime/reobfuscate.py")

# If the Result folder doesn't exist, create it
if os.path.isdir(RESULT_DIR + "/client/") == False:
	os.makedirs(os.path.dirname(RESULT_DIR + "/client/"))
	
# Place the unused files specified in the corresponding config file into the /unused/ folder
for Unfile in [line.strip() for line in open(conf_unused)]:
	if os.path.isdir(reobf + Unfile) == True:
		move(reobf + Unfile, RESULT_DIR + "/client/unused/" + Unfile)
	else:
		move(reobf + Unfile + ".class", RESULT_DIR + "/client/unused/")

# Read the mod list config file and move, copy or add the files specified in their configs
for mod in [line.strip() for line in open(conf_mods)]:
	if os.path.exists(conf_mv % (mod)):
		mv = [line.strip() for line in open(conf_mv % (mod))]
		for mvfilename in mv :
			if os.path.isdir(reobf + mvfilename) == True:
				move(reobf + mvfilename, RESULT_DIR + "/client/" + mod + "/" + mvfilename)
			else: 
				if mvfilename == "other":
					for root, dirs, files in os.walk(reobf):
						for file in files:
							move(reobf + file, RESULT_DIR + "/client/" + mod + "/")
				else:
					file = find(mvfilename)
					if file is not None:
						move(file, RESULT_DIR + "/client/" + mod + "/")
	if os.path.exists(conf_cp % (mod)):
		cp = [line.strip() for line in open(conf_cp % (mod))]
		for cpfilename in cp :
			if os.path.isdir(reobf + cpfilename) == True:
				copy(reobf + cpfilename, RESULT_DIR + "/client/" + mod + "/" + cpfilename)
			else:
				file = find(cpfilename)
				if file is not None:
					copy(file, RESULT_DIR + "/client/" + mod + "/")
	if os.path.exists(conf_add % (mod)):
		add = [line.strip() for line in open(conf_add % (mod))]
		for addfilename in add :
			copy(additional + addfilename, RESULT_DIR + "/client/" + mod + "/assets/minecraft/" + addfilename)

# Add WorldEdit into the mods files
print("Adding worldedit...")
copy(currentpath + "/lib/WorldEdit.jar", RESULT_DIR + "/client/spc/WorldEdit.jar")

# If the Result2 folder doesn't exist, create it
if os.path.isdir(RESULT_DIR2 + "/client/") == False:
	os.makedirs(os.path.dirname(RESULT_DIR2 + "/client/"))

# Zip the compiled mods files
for mod in [line.strip() for line in open(conf_mods)]:
	print("Packaging " + mod + ".zip ...")
	mod_zip = zipfile.ZipFile(RESULT_DIR2 + "/client/" + mod + ".zip", "w" )
	for dir_, _, files in os.walk(RESULT_DIR + "/client/" + mod + "/"):
		for fileName in files:
			filepath = (dir_ + "/" + fileName).replace(RESULT_DIR + "/client/" + mod, "")
			mod_zip.write(dir_ + "/" + fileName, filepath, zipfile.ZIP_DEFLATED)
	
# Zip the sources files specified in the header of this script
print("Packaging source code ...")
zipsrc(srcdir, RESULT_DIR2 + "/client/src.zip")

print("Packaging spc source code ...")
zipsrc("src-mods/src/client/spc/", RESULT_DIR2 + "/client/spc-src.zip")