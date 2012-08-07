package net.minecraft.src;

import java.io.File;

public interface ISaveHandler
{
    /**
     * Loads and returns the world info
     */
    public abstract WorldInfo loadWorldInfo();

    /**
     * Checks the session lock to prevent save collisions
     */
    public abstract void checkSessionLock() throws MinecraftException;

    /**
     * Returns the chunk loader with the provided world provider
     */
    public abstract IChunkLoader getChunkLoader(WorldProvider worldprovider);

    public abstract void func_75755_a(WorldInfo worldinfo, NBTTagCompound nbttagcompound);

    /**
     * saves level.dat and backs up the existing one to level.dat_old
     */
    public void saveWorldInfoAndPlayer(WorldInfo par1WorldInfo, java.util.List par2List);

    /**
     * Saves the passed in world info.
     */
    public abstract void saveWorldInfo(WorldInfo worldinfo);

    public abstract IPlayerFileData func_75756_e();

    public abstract void func_75759_a();

    /**
     * Gets the file location of the given map
     */
    public abstract File getMapFileFromName(String s);

    /**
     * Returns the name of the directory where world information is saved
     */
    public abstract String getSaveDirectoryName();
}
