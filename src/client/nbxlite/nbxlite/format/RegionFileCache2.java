package net.minecraft.src.nbxlite.format;

import java.io.*;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import net.minecraft.src.RegionFile;

public class RegionFileCache2
{
    /** A map containing Files as keys and RegionFiles as values */
    private static final Map regionsByFilename = new HashMap();

    private RegionFileCache2()
    {
    }

    public static synchronized RegionFile createOrLoadRegionFile(File par0File, int par1, int par2)
    {
        File file = new File(par0File, "region");
        File file1 = new File(file, (new StringBuilder()).append("r.").append(par1 >> 5).append(".").append(par2 >> 5).append(".mcr").toString());
        Reference reference = (Reference)regionsByFilename.get(file1);

        if (reference != null)
        {
            RegionFile regionfile = (RegionFile)reference.get();

            if (regionfile != null)
            {
                return regionfile;
            }
        }

        if (!file.exists())
        {
            file.mkdirs();
        }

        if (regionsByFilename.size() >= 256)
        {
            clearRegionFileReferences();
        }

        RegionFile regionfile1 = new RegionFile(file1);
        regionsByFilename.put(file1, new SoftReference(regionfile1));
        return regionfile1;
    }

    /**
     * Saves the current Chunk Map Cache
     */
    public static synchronized void clearRegionFileReferences()
    {
        Iterator iterator = regionsByFilename.values().iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Reference reference = (Reference)iterator.next();

            try
            {
                RegionFile regionfile = (RegionFile)reference.get();

                if (regionfile != null)
                {
                    regionfile.close();
                }
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        }
        while (true);

        regionsByFilename.clear();
    }

    /**
     * Returns an input stream for the specified chunk. Args: worldDir, chunkX, chunkZ
     */
    public static DataInputStream getChunkInputStream(File par0File, int par1, int par2)
    {
        RegionFile regionfile = createOrLoadRegionFile(par0File, par1, par2);
        return regionfile.getChunkDataInputStream(par1 & 0x1f, par2 & 0x1f);
    }

    /**
     * Returns an output stream for the specified chunk. Args: worldDir, chunkX, chunkZ
     */
    public static DataOutputStream getChunkOutputStream(File par0File, int par1, int par2)
    {
        RegionFile regionfile = createOrLoadRegionFile(par0File, par1, par2);
        return regionfile.getChunkDataOutputStream(par1 & 0x1f, par2 & 0x1f);
    }
}
