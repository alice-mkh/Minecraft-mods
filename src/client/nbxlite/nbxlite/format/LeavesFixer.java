package net.minecraft.src.nbxlite.format;

import java.io.*;
import java.util.*;
import net.minecraft.src.*;

public class LeavesFixer{
    public static void fixAllLeaves(String par1Str, IProgressUpdate par2IProgressUpdate){
        par2IProgressUpdate.setLoadingProgress(0);
        ArrayList overworldFiles = new ArrayList();
        ArrayList netherFiles = new ArrayList();
        File overworldDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "saves"), par1Str);
        File netherDir = new File(overworldDir, "DIM-1");
        addRegionFilesToCollection(overworldDir, overworldFiles);
        if (netherDir.exists()){
            addRegionFilesToCollection(netherDir, netherFiles);
        }
        int i = overworldFiles.size() + netherFiles.size();
        processDirectory(new File(overworldDir, "region"), overworldFiles, 0, i, par2IProgressUpdate);
        processDirectory(new File(netherDir, "region"), netherFiles, overworldFiles.size(), i, par2IProgressUpdate);
    }

    private static void addRegionFilesToCollection(File par1File, Collection par2Collection){
        File file = new File(par1File, "region");
        File afile[] = file.listFiles(new LeavesFileFilter());
        if (afile != null){
            Collections.addAll(par2Collection, afile);
        }
    }

    private static void processDirectory(File par1File, Iterable par2Iterable, int par4, int par5, IProgressUpdate par6IProgressUpdate){
        int i;
        for (Iterator iterator = par2Iterable.iterator(); iterator.hasNext(); par6IProgressUpdate.setLoadingProgress(i)){
            File file = (File)iterator.next();
            processRegion(file, par4, par5, par6IProgressUpdate);
            par4++;
            i = (int)Math.round((100D * (double)par4) / (double)par5);
        }
    }

    private static void processRegion(File par2File, int par4, int par5, IProgressUpdate par6IProgressUpdate){
        try{
            RegionFile regionfile = new RegionFile(par2File);
            for (int i = 0; i < 32; i++){
                for (int j = 0; j < 32; j++){
                    if (!regionfile.isChunkSaved(i, j)){
                        continue;
                    }
                    DataInputStream datainputstream = regionfile.getChunkDataInputStream(i, j);
                    if (datainputstream == null){
                        Minecraft.getMinecraft().getLogAgent().logWarning("Failed to fetch input stream");
                    }else{
                        NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
                        datainputstream.close();
                        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Level");
                        processChunk(nbttagcompound1);
                        nbttagcompound.setCompoundTag("Level", nbttagcompound1);
                        DataOutputStream dataoutputstream = regionfile.getChunkDataOutputStream(i, j);
                        CompressedStreamTools.write(nbttagcompound, dataoutputstream);
                        dataoutputstream.close();
                    }
                }
                int k = (int)Math.round((100D * (double)(par4 * 1024)) / (double)(par5 * 1024));
                int l = (int)Math.round((100D * (double)((i + 1) * 32 + par4 * 1024)) / (double)(par5 * 1024));
                if (l > k){
                    par6IProgressUpdate.setLoadingProgress(l);
                }
            }
            regionfile.close();
        }
        catch (IOException ioexception){
            ioexception.printStackTrace();
        }
    }

    public static void processChunk(NBTTagCompound tag){
        byte[] blocks = tag.getByteArray("Blocks");
        NibbleArray data = new NibbleArray(tag.getByteArray("Data"), 7);
        for (int x = 0; x < 16; x++){
            for (int y = 0; y < 128; y++){
                for (int z = 0; z < 16; z++){
                    int id = blocks[x << 11 | z << 7 | y];
                    if (id == Block.leaves.blockID){
                        data.set(y, x, z, 0);
                    }
                }
            }
        }
        tag.setByteArray("Data", data.data);
    }

    private static class LeavesFileFilter implements FilenameFilter{
        @Override
        public boolean accept(File par1File, String par2Str){
            return par2Str.endsWith(".mcr");
        }
    }
}