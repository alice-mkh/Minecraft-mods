package net.minecraft.src;

import net.minecraft.src.nbxlite.chunkproviders.*;

public class WorldProviderSurface extends WorldProvider
{
    public WorldProviderSurface()
    {
        super();
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
        if (mod_noBiomesX.Generator==0 && (mod_noBiomesX.MapFeatures>=2)){
            return true;
        }
        int i = worldObj.getFirstUncoveredBlock(par1, par2);
        if (mod_noBiomesX.Generator==2 || mod_noBiomesX.MapTheme==1){
            return i == Block.grass.blockID;
        }
        return i == Block.sand.blockID;
    }

    /**
     * Returns array with sunrise/sunset colors
     */
    public float[] calcSunriseSunsetColors(float par1, float par2)
    {
        if(!mod_noBiomesX.SunriseEffect){
            return null;
        }
        return super.calcSunriseSunsetColors(par1, par2);
    }

    /**
     * the y level at which clouds are rendered.
     */
    public float getCloudHeight()
    {
        if (mod_noBiomesX.Generator==0 && mod_noBiomesX.MapFeatures==3 && mod_noBiomesX.IndevMapType==2){
            return -16F;
        }
        if(worldObj.totalSkyLight == 16)
        {
            if (mod_noBiomesX.Generator==0 && mod_noBiomesX.MapFeatures>=3){
                return mod_noBiomesX.IndevHeight+64;
            }
            return 160F;
        }
        if(mod_noBiomesX.LowHangingClouds)
        {
            if (mod_noBiomesX.Generator==0 && mod_noBiomesX.MapFeatures>=3){
                return mod_noBiomesX.IndevHeight+2;
            }
            return 108F;
        }
        return 128F;
    }

    public boolean isSkyColored()
    {
        if (mod_noBiomesX.Generator==0 && (mod_noBiomesX.MapFeatures>=3 || mod_noBiomesX.MapTheme!=0)){
            return false;
        }
        return true;
    }

    public int getAverageGroundLevel()
    {
        if (mod_noBiomesX.Generator==0 && mod_noBiomesX.MapFeatures>=3){
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
