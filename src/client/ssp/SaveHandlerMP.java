package net.minecraft.src;

import java.io.File;

public class SaveHandlerMP implements ISaveHandler
{
    public SaveHandlerMP()
    {
    }

    /**
     * Loads and returns the world info
     */
    public WorldInfo loadWorldInfo()
    {
        return null;
    }

    /**
     * Checks the session lock to prevent save collisions
     */
    public void checkSessionLock() throws MinecraftException
    {
    }

    /**
     * Returns the chunk loader with the provided world provider
     */
    public IChunkLoader getChunkLoader(WorldProvider par1WorldProvider)
    {
        return null;
    }

    public void func_75755_a(WorldInfo worldinfo, NBTTagCompound nbttagcompound)
    {
    }

    /**
     * Saves the passed in world info.
     */
    public void saveWorldInfoAndPlayer(WorldInfo par1WorldInfo, java.util.List par2List)
    {
    }

    /**
     * Saves the passed in world info.
     */
    public void saveWorldInfo(WorldInfo worldinfo)
    {
    }

    public IPlayerFileData func_75756_e()
    {
        return null;
    }

    public void func_75759_a()
    {
    }

    /**
     * Gets the file location of the given map
     */
    public File getMapFileFromName(String par1Str)
    {
        return null;
    }

    /**
     * Returns the name of the directory where world information is saved
     */
    public String getSaveDirectoryName()
    {
        return "none";
    }
}
