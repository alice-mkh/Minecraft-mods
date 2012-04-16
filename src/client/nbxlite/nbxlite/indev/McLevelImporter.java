package net.minecraft.src.nbxlite.indev;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.mod_noBiomesX;

public class McLevelImporter{
    private NBTTagCompound about;
    private NBTTagCompound environment;
    private NBTTagCompound map;

    private byte skybrightness;
    private byte surrgroundtype;
    private byte surrwatertype;
    private int cloudheight;
    private int surrgroundheight;
    private int surrwaterheight;
    private int timeofday;

    private int height;
    private int length;
    private int width;
    private byte[] blocks;
    private byte[] data;

    public McLevelImporter(NBTTagCompound a, NBTTagCompound env, NBTTagCompound m){
        about = a;
        environment = env;
        loadEnvironment(env);
        map = m;
        loadMap(m);
        importLevel();
    }

    private void importLevel(){
        mod_noBiomesX.Generator = mod_noBiomesX.GEN_BIOMELESS;
        mod_noBiomesX.MapFeatures = mod_noBiomesX.FEATURES_INDEV;
        mod_noBiomesX.GenerateNewOres = false;
        mod_noBiomesX.IndevWorld = blocks;
        mod_noBiomesX.IndevWidthX = width;
        mod_noBiomesX.IndevWidthZ = length;
        mod_noBiomesX.IndevHeight = height;
        mod_noBiomesX.FiniteImport = true;
    }

    private void loadEnvironment(NBTTagCompound tag){
        skybrightness = tag.getByte("SkyBrightness");
        surrgroundtype = tag.getByte("SurroundingGroundType");
        surrwatertype = tag.getByte("SurroundingWaterType");
        cloudheight = tag.getShort("CloudHeight");
        surrgroundheight = tag.getShort("SurroundingGroundHeight");
        surrwaterheight = tag.getShort("SurroundingWaterHeight");
        timeofday = tag.getShort("TimeOfDay");
    }

    private void loadMap(NBTTagCompound tag){
        height = tag.getShort("Height");
        length = tag.getShort("Length");
        width = tag.getShort("Width");
        blocks = tag.getByteArray("Blocks");
        data = tag.getByteArray("Data");
    }
}