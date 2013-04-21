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
    @Override
    protected void registerWorldChunkManager()
    {
        if (dimensionId==0 && ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_SKY){
            worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.betaSky, 0.5F, 0.0F, OldBiomeGenBase.sky);
        }else{
            super.registerWorldChunkManager();
        }
    }

    /**
     * Returns the chunk provider back for the world provider
     */
    @Override
    public IChunkProvider createChunkGenerator()
    {
        if (terrainType == WorldType.DEFAULT || terrainType == WorldType.DEFAULT_1_1 || terrainType == WorldType.LARGE_BIOMES){
            return new ChunkProviderGenerate2(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
        }
        return super.createChunkGenerator();
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    @Override
    public boolean canCoordinateBeSpawn(int par1, int par2)
    {
        if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES){
            return super.canCoordinateBeSpawn(par1, par2);
        }
        int i = worldObj.getFirstUncoveredBlock(par1, par2);
        if (ODNBXlite.MapTheme==ODNBXlite.THEME_HELL){
            return i == Block.grass.blockID;
        }
        if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures>=ODNBXlite.FEATURES_INFDEV0420){
            return true;
        }
        if (ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_SKY){
            return i == 0 ? false : Block.blocksList[i].blockMaterial.isSolid();
        }
        return i == Block.sand.blockID;
    }

    /**
     * the y level at which clouds are rendered.
     */
    @Override
    public float getCloudHeight()
    {
        return ODNBXlite.CloudHeight;
    }

    /**
     * returns true if this dimension is supposed to display void particles and pull in the far plane based on the
     * user's Y offset.
     */
    @Override
    public boolean getWorldHasVoidParticles()
    {
        return super.getWorldHasVoidParticles() && ODNBXlite.VoidFog==0;
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    @Override
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

    @Override
    public boolean isSkyColored()
    {
        if (ODNBXlite.VoidFog>2){
            return false;
        }
        return true;
    }

    @Override
    public int getAverageGroundLevel()
    {
        if (ODNBXlite.isFinite()){
            return ODNBXlite.IndevHeight - 32;
        }
        return super.getAverageGroundLevel();
    }

    /**
     * Creates the light to brightness table
     */
    @Override
    protected void generateLightBrightnessTable()
    {
        float f = ODNBXlite.ClassicLight > 0 ? 0.05F : 0.0F;

        for (int i = 0; i <= 15; i++)
        {
            float f1 = 1.0F - (float)i / 15F;
            lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3F + 1.0F)) * (1.0F - f) + f;
        }
    }

    /**
     * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
     */
    @Override
    public String getDimensionName()
    {
        return "Overworld";
    }
}
