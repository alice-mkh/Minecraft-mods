import os, urllib
path = os.getcwd()

def symlink(source, link_name):
    os_symlink = getattr(os, "symlink", None)
    if callable(os_symlink):
        os_symlink(source, link_name)
    else:
        import ctypes
        csl = ctypes.windll.kernel32.CreateSymbolicLinkW
        csl.argtypes = (ctypes.c_wchar_p, ctypes.c_wchar_p, ctypes.c_uint32)
        csl.restype = ctypes.c_ubyte
        flags = 1 if os.path.isdir(source) else 0
        if csl(link_name, source, flags) == 0:
            raise ctypes.WinError()

print("Gathering overwritten files list...")	
delsrc = [line.strip() for line in open( 'src-mods/build/conf_deploy/deploy_src.txt' )]
delserver = [line.strip() for line in open('src-mods/build/conf_deploy/deploy_server.txt')]
delclient = [line.strip() for line in open('src-mods/build/conf_deploy/deploy_client.txt')]

print("Deleting files...")
for filename in delsrc :
    os.remove( 'src/minecraft/net/minecraft/src/' + filename )
for filename in delserver :
    os.remove( 'src/minecraft/net/minecraft/server/' + filename )
for filename in delclient :
    os.remove( 'src/minecraft/net/minecraft/client/' + filename )

print("Downloading worldedit...")	
urllib.urlretrieve ('http://dev.bukkit.org/media/files/715/447/worldedit-5.5.7.jar', 'lib/WorldEdit.jar')

print("Linking files and folder to the minecraft source directory...")
symlink( path + '/src-mods/src/client', path + '/src/minecraft/net/minecraft/src/mods')
symlink( path + '/src-mods/resources/olddays', path + '/jars/assets/olddays')
symlink( path + '/src-mods/resources/textures', path + '/jars/assets/textures')
symlink( path + '/src-mods/src/client-client/ssp/ClientBrandRetriever.java' , path + '/src/minecraft/net/minecraft/client/ClientBrandRetriever.java')
symlink( path + '/src-mods/src/client-server/ssp/MinecraftServer.java', path + '/src/minecraft/net/minecraft/server/MinecraftServer.java')
symlink( path + '/src-mods/src/client-client/ssp/Minecraft.java', path + '/src/minecraft/net/minecraft/src/Minecraft.java')
symlink( path + '/src-mods/resources/char.png', path + '/jars/assets/char.png')
symlink( path + '/lib/WorldEdit.jar', path + '/jars/WorldEdit.jar')