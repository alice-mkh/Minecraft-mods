package net.minecraft.src;

import net.minecraft.src.nbxlite.chunkproviders.*;
import net.minecraft.src.nbxlite.oldbiomes.*;

public class WorldProviderSurface extends WorldProvider
{
    public WorldProviderSurface()
    {
        super();
    }

    /**
     * creates a new world chunk manager for WorldProvider
     */
    protected void registerWorldChunkManager()
    {
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_OLDBIOMES && mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_SKY){
            worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F, 0.0F, OldBiomeGenBase.sky);
        }else{
            super.registerWorldChunkManager();
        }
    }

    /**
     * Creates the light to brightness table
     */
    protected void generateLightBrightnessTable()
    {
        float f = 0.0F;
        if(mod_noBiomesX.ClassicLight)
        {
            f = 0.05F;
        }

        for (int i = 0; i <= 15; i++)
        {
            float f1 = 1.0F - (float)i / 15F;
            lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3F + 1.0F)) * (1.0F - f) + f;
        }
    }

    /**
     * Returns the chunk provider back for the world provider
     */
    public IChunkProvider getChunkProvider()
    {
        if (terrainType == WorldType.FLAT)
        {
            return new ChunkProviderFlat(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
        }
        else
        {
            return new ChunkProviderGenerate2(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
        }
//         return terrainType.getChunkGenerator(worldObj);
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    public boolean canCoordinateBeSpawn(int par1, int par2)
    {
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS && mod_noBiomesX.MapFeatures>=mod_noBiomesX.FEATURES_INFDEV0420){
            return true;
        }
        int i = worldObj.getFirstUncoveredBlock(par1, par2);
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_OLDBIOMES && mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_SKY){
            return i == 0 ? false : Block.blocksList[i].blockMaterial.isSolid();
        }
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_NEWBIOMES || mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_HELL){
            return i == Block.grass.blockID;
        }
        return i == Block.sand.blockID;
    }

    /**
     * Returns array with sunrise/sunset colors
     */
    public float[] calcSunriseSunsetColors(float par1, float par2)
    {
        if(!mod_noBiomesX.SunriseEffect || (mod_noBiomesX.Generator==mod_noBiomesX.GEN_OLDBIOMES && mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_SKY)){
            return null;
        }
        return super.calcSunriseSunsetColors(par1, par2);
    }

    /**
     * the y level at which clouds are rendered.
     */
    public float getCloudHeight()
    {
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS && mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INDEV && mod_noBiomesX.IndevMapType==mod_noBiomesX.TYPE_FLOATING){
            return -16F;
        }
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_OLDBIOMES && mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_SKY){
            return 8F;
        }
        if(worldObj.totalSkyLight == 16)
        {
            if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS && (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INDEV || mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_CLASSIC)){
                return mod_noBiomesX.IndevHeight+64;
            }
            return 160F;
        }
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS && (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INFDEV0227 || mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INFDEV0420 || mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INFDEV0608)){
            return 120F;
        }
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS && (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INDEV || mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_CLASSIC)){
            return mod_noBiomesX.IndevHeight+2;
        }
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_NEWBIOMES){
            return 128F;
        }
        return 108F;
    }

    public boolean isSkyColored()
    {
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS && mod_noBiomesX.MapTheme!=mod_noBiomesX.THEME_NORMAL){
            return false;
        }
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS && mod_noBiomesX.MapFeatures>mod_noBiomesX.FEATURES_ALPHA11201){
            return false;
        }
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_OLDBIOMES && mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_SKY){
            return false;
        }
        return true;
    }

    public int getAverageGroundLevel()
    {
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS && (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INDEV || mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_CLASSIC)){
            return mod_noBiomesX.IndevHeight - 32;
        }
        return terrainType.getSeaLevel(worldObj);
    }

//FORGE COMPATIBILITY
    public String getSaveFolder(){
        return "FIXME";
    }
    public String getWelcomeMessage(){
        return "FIXME";
    }
    public String getDepartMessage(){
        return "FIXME";
    }
    public double getMovementFactor(){
        return 1.0D;
    }
}
