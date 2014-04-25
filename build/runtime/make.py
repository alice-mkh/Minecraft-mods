import os, shutil, linecache, zipfile
currentpath = os.getcwd()

RESULT_DIR = "./result"
RESULT_DIR2 = "./result2"
reobf = currentpath + "/reobf/minecraft/"

def find(classname):
	found = "null"
	for root, dirs, files in os.walk(reobf):
		if os.path.exists(reobf + classname + ".class") == True:
			found = reobf + classname + ".class"
			break
		else:
			for file in files:
				line = linecache.getline(reobf + file, 1)
				if line.find(classname + ".java") is not -1:
					print("Find: " + file + " --> " + classname + ".java")
					found = reobf + file
					break
				else:
					line = linecache.getline(reobf + file, 2)
					if line.find(classname + ".java") is not -1:
						print("Find: " + file + " --> " + classname + ".java")
						found = reobf + file
						break
	if found is "null":
		print("Not Found " + classname)
	return found

def copy(src, dst):
	if os.path.isdir(src) == True:
		shutil.copytree(src, dst)
	else:
		if not os.path.exists(os.path.dirname(dst)):
			os.makedirs(os.path.dirname(dst))
		shutil.copy(src, dst)
	
def move(src, dst):
	if os.path.isdir(src) == True:
		shutil.move(src, dst)
	else:
		if not os.path.exists(os.path.dirname(dst)):
			os.makedirs(os.path.dirname(dst))
		shutil.move(src, dst)

if os.name is "nt":
	os.system("runtime\\bin\\python\\python_mcp.exe runtime\\recompile.py")
	os.system("runtime\\bin\\python\\python_mcp.exe runtime\\reobfuscate.py")
else:
	os.system("python runtime\\recompile.py")
	os.system("python runtime\\reobfuscate.py")

for Unfile in [line.strip() for line in open('src-mods/build/conf_make/make_unused.txt')]:
	if os.path.isdir(reobf + Unfile) == True:
		move(reobf + Unfile, RESULT_DIR + "/client/unused/" + Unfile)
	else:
		move(reobf + Unfile + ".class", RESULT_DIR + "/client/unused/")


for mod in [line.strip() for line in open('src-mods/build/conf_make/make_mods_list.txt')]:
	if os.path.exists('src-mods/build/conf_make/make_mv_' + mod + '.txt'):
		mv = [line.strip() for line in open('src-mods/build/conf_make/make_mv_' + mod + '.txt')]
		for mvfilename in mv :
			if os.path.isdir(reobf + mvfilename) == True:
				move(reobf + mvfilename, RESULT_DIR + "/client/" + mod + "/" + mvfilename)
			else: 
				if mvfilename == "other":
					for root, dirs, files in os.walk(currentpath + "/reobf/minecraft"):
						for file in files:
							move(reobf + file, RESULT_DIR + "/client/" + mod + "/")
				else:
					file = find(mvfilename)
					if file is not "null":
						move(file, RESULT_DIR + "/client/" + mod + "/")
	if os.path.exists('src-mods/build/conf_make/make_cp_' + mod + '.txt'):
		cp = [line.strip() for line in open('src-mods/build/conf_make/make_cp_' + mod + '.txt')]
		for cpfilename in cp :
			if os.path.isdir(reobf + cpfilename) == True:
				copy(reobf + cpfilename, RESULT_DIR + "/client/" + mod + "/" + cpfilename)
			else:
				file = find(cpfilename)
				if file is not "null":
					copy(file, RESULT_DIR + "/client/" + mod + "/")
	if os.path.exists('src-mods/build/conf_make/make_add_' + mod + '.txt'):
		add = [line.strip() for line in open('src-mods/build/conf_make/make_add_' + mod + '.txt')]
		for addfilename in add :
			copy("src-mods/resources/" + addfilename, RESULT_DIR + "/client/" + mod + "/assets/minecraft/" + addfilename)
		
print("Adding worldedit...")
copy(currentpath + "/lib/worldedit.jar", RESULT_DIR + "/client/spc/worldedit.jar")

if os.path.isdir(RESULT_DIR2 + "/Client/") == False:
	os.makedirs(os.path.dirname(RESULT_DIR2 + "/Client/"))

for mod in [line.strip() for line in open('src-mods/build/conf_make/make_mods_list.txt')]:
	print("Packaging " + mod + ".zip ...")
	zf = zipfile.ZipFile("%s.zip" % (RESULT_DIR2 + "/Client/" + mod), "w")
	abs_src = os.path.abspath(RESULT_DIR + "/Client/" + mod)
	for dirname, subdirs, files in os.walk(RESULT_DIR + "/Client/" + mod):
		for filename in files:
			absname = os.path.abspath(os.path.join(dirname, filename))
			arcname = absname[len(abs_src) + 1:]
			zf.write(absname, arcname)
	zf.close()
	
print("Packaging source code ...")
src_zip = zipfile.ZipFile(RESULT_DIR2 + "/Client/src.zip", "w" )
src_zip.write("src-mods/Readme.md", "Readme.md", zipfile.ZIP_DEFLATED )
srcdir = ["src-mods/src/client/nbxlite/", "src-mods/src/client/olddays/", "src-mods/src/client/spawnhuman/", "src-mods/src/client/ssp/", "src-mods/src/client-client/", "src-mods/src/client-server/", "src-mods/src/server/", "src-mods/resources/", "src-mods/build/"]
for dir in srcdir:
	for dir_, _, files in os.walk(dir):
		for fileName in files:
			src_zip.write(dir_ + "/" + fileName, dir_.replace("src-mods/", "") + "/" + fileName, zipfile.ZIP_DEFLATED)

print("Packaging spc source code ...")
spc_src_zip = zipfile.ZipFile(RESULT_DIR2 + "/Client/spc-src.zip", "w" )
for dir_, _, files in os.walk("src-mods/src/client/spc/"):
	for fileName in files:
		spc_src_zip.write(dir_ + "/" + fileName, dir_.replace("src-mods/", "") + "/" + fileName, zipfile.ZIP_DEFLATED)