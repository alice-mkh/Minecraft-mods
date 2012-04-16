package net.minecraft.src.nbxlite.indev;

import java.io.File;
import java.io.FileInputStream;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagShort;
import net.minecraft.src.mod_noBiomesX;

public class McLevelImporter{
    private long createdon;
    private String author;
    private String name;
    private byte skybrightness;
    private byte surrgroundtype;
    private byte surrwatertype;
    private int cloudheight;
    private int surrgroundheight;
    private int surrwaterheight;
    private int timeofday;
    private int cloudcolor;
    private int fogcolor;
    private int skycolor;
    private int height;
    private int length;
    private int width;
    private byte[] blocks;
    private byte[] data;
    private short spawnx;
    private short spawny;
    private short spawnz;

    public McLevelImporter(File f){
        try{
            NBTTagCompound a = CompressedStreamTools.readCompressed(new FileInputStream(f)).getCompoundTag("About");
            NBTTagCompound env = CompressedStreamTools.readCompressed(new FileInputStream(f)).getCompoundTag("Environment");
            NBTTagCompound map = CompressedStreamTools.readCompressed(new FileInputStream(f)).getCompoundTag("Map");
            loadAbout(a);
            loadEnvironment(env);
            loadMap(map);
        }catch(Exception ex){
            System.out.println("Failed: "+ex);
        }
        importLevel();
    }

    private void importLevel(){
        mod_noBiomesX.IndevWorld = blocks;
        mod_noBiomesX.IndevWidthX = width;
        mod_noBiomesX.IndevWidthZ = length;
        mod_noBiomesX.IndevHeight = height;
        mod_noBiomesX.IndevSpawnX = spawnx / 32;
        mod_noBiomesX.IndevSpawnY = spawny / 32;
        mod_noBiomesX.IndevSpawnZ = spawnz / 32;
        mod_noBiomesX.Generator = mod_noBiomesX.GEN_BIOMELESS;
        mod_noBiomesX.MapFeatures = mod_noBiomesX.FEATURES_INDEV;
        mod_noBiomesX.GenerateNewOres = false;
        mod_noBiomesX.FiniteImport = true;
    }

    private void loadAbout(NBTTagCompound tag){
        createdon = tag.getLong("CreatedOn");
        author = tag.getString("Author");
        name = tag.getString("Name");
    }

    private void loadEnvironment(NBTTagCompound tag){
        skybrightness = tag.getByte("SkyBrightness");
        surrgroundtype = tag.getByte("SurroundingGroundType");
        surrwatertype = tag.getByte("SurroundingWaterType");
        cloudheight = tag.getShort("CloudHeight");
        surrgroundheight = tag.getShort("SurroundingGroundHeight");
        surrwaterheight = tag.getShort("SurroundingWaterHeight");
        timeofday = tag.getShort("TimeOfDay");
        cloudcolor = tag.getInteger("CloudColor");
        fogcolor = tag.getInteger("FogColor");
        skycolor = tag.getInteger("SkyColor");
    }

    private void loadMap(NBTTagCompound tag){
        height = tag.getShort("Height");
        length = tag.getShort("Length");
        width = tag.getShort("Width");
        blocks = tag.getByteArray("Blocks");
        data = tag.getByteArray("Data");
        NBTTagList spawn = tag.getTagList("Spawn");
        spawnx = ((NBTTagShort)spawn.tagAt(0)).data;
        spawny = ((NBTTagShort)spawn.tagAt(1)).data;
        spawnz = ((NBTTagShort)spawn.tagAt(2)).data;
    }
}