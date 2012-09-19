package net.minecraft.src.nbxlite.format;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RegionFile;
import net.minecraft.src.SaveFormatOld;
import net.minecraft.src.SaveFormatComparator;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.ODNBXlite;

public class SaveConverterMcRegion extends SaveFormatOld
{
    public SaveConverterMcRegion(File file)
    {
        super(file);
    }

    public String getFormatName()
    {
        return "Scaevolus' McRegion";
    }

    public File getSaveDirectory(){
        return savesDirectory;
    }

    public void deleteWorldDirectory(String par1Str)
    {
        File file = new File(savesDirectory, par1Str);

        if (file.getName().endsWith(".mclevel"))
        {
            System.out.println((new StringBuilder()).append("Deleting ").append(file).toString());
            file.delete();
        }
        else
        {
            super.deleteWorldDirectory(par1Str);
        }
    }

    public List getSaveList()
    {
        ArrayList arraylist = new ArrayList();
        File afile[] = savesDirectory.listFiles();
        File afile1[] = afile;
        int i = afile1.length;
        for (int j = 0; j < i; j++)
        {
            File file = afile1[j];
            if (!file.isDirectory() && !file.getName().endsWith(".mclevel"))
            {
                continue;
            }
            String s = file.getName();
            WorldInfo worldinfo = getWorldInfo(s);
            int format = 0; //0 is level.dat, 1 is mclevel
            if (worldinfo == null)
            {
                if (file.isDirectory()){
                    continue;
                }
                format = 1;
            }
            boolean flag = true;
            String s1 = null;
            if (format==0){
                flag = worldinfo.getSaveVersion() != 19133;
                s1 = worldinfo.getWorldName();
            }
            if (s1 == null || MathHelper.stringNullOrLengthZero(s1))
            {
                s1 = s;
                if (format==1){
                    s1 = s.replace(".mclevel","");
                }
            }
            long l = 0L;
            if (format==0){
                arraylist.add(new SaveFormatComparator(s, s1, worldinfo.getLastTimePlayed(), l, worldinfo.getGameType(), flag, worldinfo.isHardcoreModeEnabled(), worldinfo.areCommandsAllowed()));
            }else{
                arraylist.add(new SaveFormatComparator(s, s1, 0L, l, EnumGameType.SURVIVAL, true, true, true));
            }
        }
        return arraylist;
    }

    public void flushCache()
    {
        RegionFileCache2.clearRegionFileReferences();
    }

    public ISaveHandler getSaveLoader(String s, boolean flag)
    {
        return new SaveOldDir(savesDirectory, s, flag);
    }

    public boolean isOldMapFormat(String s)
    {
        WorldInfo worldinfo = getWorldInfo(s);
        return worldinfo != null && worldinfo.getSaveVersion() == 0;
    }

    public boolean convertMapFormat(String s, IProgressUpdate iprogressupdate)
    {
        iprogressupdate.setLoadingProgress(0);
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        ArrayList arraylist3 = new ArrayList();
        ArrayList arraylist4 = new ArrayList();
        ArrayList arraylist5 = new ArrayList();
        File file = new File(savesDirectory, s);
        File file1 = new File(file, "DIM-1");
        File file2 = new File(file, "DIM1");
        System.out.println("Scanning folders...");
        addFilesToCollection(file, arraylist, arraylist1);
        if (file1.exists())
        {
            addFilesToCollection(file1, arraylist2, arraylist3);
        }
        if (file2.exists())
        {
            addFilesToCollection(file2, arraylist4, arraylist5);
        }
        int i = arraylist.size() + arraylist2.size() + arraylist4.size() + arraylist1.size() + arraylist3.size() + arraylist5.size();
        System.out.println((new StringBuilder()).append("Total conversion count is ").append(i).toString());
        convertFile(file, arraylist, 0, i, iprogressupdate);
        convertFile(file1, arraylist2, arraylist.size(), i, iprogressupdate);
        convertFile(file2, arraylist4, arraylist.size() + arraylist2.size(), i, iprogressupdate);
        WorldInfo worldinfo = getWorldInfo(s);
        worldinfo.setSaveVersion(19132);
        worldinfo.setCommandsAllowed();
        ISaveHandler isavehandler = getSaveLoader(s, false);
        isavehandler.saveWorldInfo(worldinfo);
        cleanFiles(arraylist1, arraylist.size() + arraylist2.size(), i, iprogressupdate);
        if (file1.exists())
        {
            cleanFiles(arraylist3, arraylist.size() + arraylist2.size() + arraylist1.size(), i, iprogressupdate);
        }
        return true;
    }

    private void addFilesToCollection(File file, ArrayList arraylist, ArrayList arraylist1)
    {
        ChunkFolderPattern chunkfolderpattern = new ChunkFolderPattern(null);
        ChunkFilePattern chunkfilepattern = new ChunkFilePattern(null);
        File afile[] = file.listFiles(chunkfolderpattern);
        File afile1[] = afile;
        int i = afile1.length;
        for (int j = 0; j < i; j++)
        {
            File file1 = afile1[j];
            arraylist1.add(file1);
            File afile2[] = file1.listFiles(chunkfolderpattern);
            File afile3[] = afile2;
            int k = afile3.length;
            for (int l = 0; l < k; l++)
            {
                File file2 = afile3[l];
                File afile4[] = file2.listFiles(chunkfilepattern);
                File afile5[] = afile4;
                int i1 = afile5.length;
                for (int j1 = 0; j1 < i1; j1++)
                {
                    File file3 = afile5[j1];
                    arraylist.add(new ChunkFile(file3));
                }
            }
        }
    }

    private void convertFile(File file, ArrayList arraylist, int i, int j, IProgressUpdate iprogressupdate)
    {
        Collections.sort(arraylist);
        byte abyte0[] = new byte[4096];
        int i1;
        for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); iprogressupdate.setLoadingProgress(i1))
        {
            ChunkFile chunkfile = (ChunkFile)iterator.next();
            int k = chunkfile.getXChunk();
            int l = chunkfile.getYChunk();
            RegionFile regionfile = RegionFileCache2.createOrLoadRegionFile(file, k, l);
            if (!regionfile.isChunkSaved(k & 0x1f, l & 0x1f))
            {
                try
                {
                    DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(chunkfile.getChunkFile()))));
                    DataOutputStream dataoutputstream = regionfile.getChunkDataOutputStream(k & 0x1f, l & 0x1f);
                    for (int j1 = 0; (j1 = datainputstream.read(abyte0)) != -1;)
                    {
                        dataoutputstream.write(abyte0, 0, j1);
                    }

                    dataoutputstream.close();
                    datainputstream.close();
                }
                catch (IOException ioexception)
                {
                    ioexception.printStackTrace();
                }
            }
            i++;
            i1 = (int)Math.round((100D * (double)i) / (double)j);
        }

        RegionFileCache2.clearRegionFileReferences();
    }

    private void cleanFiles(ArrayList arraylist, int i, int j, IProgressUpdate iprogressupdate)
    {
        int k;
        for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); iprogressupdate.setLoadingProgress(k))
        {
            File file = (File)iterator.next();
            File afile[] = file.listFiles();
            deleteFiles(afile);
            file.delete();
            i++;
            k = (int)Math.round((100D * (double)i) / (double)j);
        }
    }
}
