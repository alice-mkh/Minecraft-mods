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

    /**
     * Saves the given World Info with the given NBTTagCompound as the Player.
     */
    public abstract void saveWorldInfoWithPlayer(WorldInfo worldinfo, NBTTagCompound nbttagcompound);

    /**
     * saves level.dat and backs up the existing one to level.dat_old
     */
    public void saveWorldInfoAndPlayer(WorldInfo par1WorldInfo, java.util.List par2List);

    /**
     * Saves the passed in world info.
     */
    public abstract void saveWorldInfo(WorldInfo worldinfo);

    /**
     * returns null if no saveHandler is relevent (eg. SMP)
     */
    public abstract IPlayerFileData getSaveHandler();

    /**
     * Called to flush all changes to disk, waiting for them to complete.
     */
    public abstract void flush();

    /**
     * Gets the file location of the given map
     */
    public abstract File getMapFileFromName(String s);

    /**
     * Returns the name of the directory where world information is saved.
     */
    public abstract String getWorldDirectoryName();
}
