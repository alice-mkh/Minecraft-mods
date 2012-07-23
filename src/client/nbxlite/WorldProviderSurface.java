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
        if (worldType==0 && ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_SKY){
            worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.betaSky, 0.5F, 0.0F, OldBiomeGenBase.sky);
        }else{
            super.registerWorldChunkManager();
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
        if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures>=ODNBXlite.FEATURES_INFDEV0420){
            return true;
        }
        int i = worldObj.getFirstUncoveredBlock(par1, par2);
        if (ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_SKY){
            return i == 0 ? false : Block.blocksList[i].blockMaterial.isSolid();
        }
        if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES || ODNBXlite.MapTheme==ODNBXlite.THEME_HELL){
            return i == Block.grass.blockID;
        }
        return i == Block.sand.blockID;
    }

    /**
     * the y level at which clouds are rendered.
     */
    public float getCloudHeight()
    {
        return ODNBXlite.CloudHeight;
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    public float calculateCelestialAngle(long par1, float par3)
    {
        if (ODNBXlite.DayNight==0){
            return 0F;
        }
        if (ODNBXlite.DayNight==1){
            return (par1 + par3) / 24000F - 0.15F;
        }
        return super.calculateCelestialAngle(par1, par3);
    }

    public boolean isSkyColored()
    {
        if (ODNBXlite.VoidFog>2){
            return false;
        }
        return true;
    }

    public int getAverageGroundLevel()
    {
        if (ODNBXlite.isFinite()){
            return ODNBXlite.IndevHeight - 32;
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
