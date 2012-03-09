package net.minecraft.src;

import net.minecraft.src.nbxlite.chunkproviders.*;

public abstract class WorldProvider
{
    /** world object being used */
    public World worldObj;
    public WorldType terrainType;

    /** World chunk manager being used to generate chunks */
    public WorldChunkManager worldChunkMgr;

    /**
     * States whether the Hell world provider is used(true) or if the normal world provider is used(false)
     */
    public boolean isHellWorld;

    /**
     * A boolean that tells if a world does not have a sky. Used in calculating weather and skylight
     */
    public boolean hasNoSky;
    public float lightBrightnessTable[];

    /** 0 for normal world -1 for hell */
    public int worldType;
    private float colorsSunriseSunset[];

    public WorldProvider()
    {
        isHellWorld = false;
        hasNoSky = false;
        lightBrightnessTable = new float[16];
        worldType = 0;
        colorsSunriseSunset = new float[4];
    }

    /**
     * associate an existing world with a World provider, and setup its lightbrightness table
     */
    public final void registerWorld(World par1World)
    {
        worldObj = par1World;
        terrainType = par1World.getWorldInfo().getTerrainType();
        registerWorldChunkManager();
        generateLightBrightnessTable();
    }

    /**
     * Creates the light to brightness table
     */
    protected void generateLightBrightnessTable()
    {
        float f = 0.0F;
        if(mod_noBiomesX.ClassicLight){
            f = 0.05F;
        }

        for (int i = 0; i <= 15; i++)
        {
            float f1 = 1.0F - (float)i / 15F;
            lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3F + 1.0F)) * (1.0F - f) + f;
        }
    }

    /**
     * creates a new world chunk manager for WorldProvider
     */
    protected void registerWorldChunkManager()
    {
        if (worldObj.getWorldInfo().getTerrainType() == WorldType.field_48636_c)
        {
            worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F, 0.5F);
        }
        else
        {
            worldChunkMgr = new WorldChunkManager(worldObj);
        }
    }

    /**
     * Returns the chunk provider back for the world provider
     */
    public IChunkProvider getChunkProvider()
    {
        if (terrainType == WorldType.field_48636_c)
        {
            return new ChunkProviderFlat(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
        }
        else
        {
//             return new ChunkProviderGenerate(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
            return new ChunkProviderGenerate2(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
        }
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    public boolean canCoordinateBeSpawn(int par1, int par2)
    {
        if (mod_noBiomesX.Generator==0 && mod_noBiomesX.MapFeatures==2){
            return true;
        }
        int i = worldObj.getFirstUncoveredBlock(par1, par2);
        if (mod_noBiomesX.Generator==0 && mod_noBiomesX.MapFeatures==3){
            return i == Block.stone.blockID;
        }
        if (mod_noBiomesX.Generator==2 || mod_noBiomesX.MapTheme==1){
            return i == Block.grass.blockID;
        }
        return i == Block.sand.blockID;
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    public float calculateCelestialAngle(long par1, float par3)
    {
        int i = (int)(par1 % 24000L);
        float f = ((float)i + par3) / 24000F - 0.25F;

        if (f < 0.0F)
        {
            f++;
        }

        if (f > 1.0F)
        {
            f--;
        }

        float f1 = f;
        f = 1.0F - (float)((Math.cos((double)f * Math.PI) + 1.0D) / 2D);
        f = f1 + (f - f1) / 3F;
        return f;
    }

    public int getMoonPhase(long par1, float par3)
    {
        return (int)(par1 / 24000L) % 8;
    }

    public boolean func_48217_e()
    {
        return true;
    }

    /**
     * Returns array with sunrise/sunset colors
     */
    public float[] calcSunriseSunsetColors(float par1, float par2)
    {
        if(!mod_noBiomesX.SunriseEffect){
            return null;
        }
        float f = 0.4F;
        float f1 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) - 0.0F;
        float f2 = -0F;

        if (f1 >= f2 - f && f1 <= f2 + f)
        {
            float f3 = ((f1 - f2) / f) * 0.5F + 0.5F;
            float f4 = 1.0F - (1.0F - MathHelper.sin(f3 * (float)Math.PI)) * 0.99F;
            f4 *= f4;
            colorsSunriseSunset[0] = f3 * 0.3F + 0.7F;
            colorsSunriseSunset[1] = f3 * f3 * 0.7F + 0.2F;
            colorsSunriseSunset[2] = f3 * f3 * 0.0F + 0.2F;
            colorsSunriseSunset[3] = f4;
            return colorsSunriseSunset;
        }
        else
        {
            return null;
        }
    }

    /**
     * Return Vec3D with biome specific fog color
     */
    public Vec3D getFogColor(float par1, float par2)
    {
        float f = MathHelper.cos(par1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

        if (f < 0.0F)
        {
            f = 0.0F;
        }

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        float f1 = 0.7529412F;
        float f2 = 0.8470588F;
        float f3 = 1.0F;
        f1 *= f * 0.94F + 0.06F;
        f2 *= f * 0.94F + 0.06F;
        f3 *= f * 0.91F + 0.09F;
        return Vec3D.createVector(f1, f2, f3);
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    public boolean canRespawnHere()
    {
        return true;
    }

    public static WorldProvider getProviderForDimension(int par0)
    {
        if (par0 == -1)
        {
            return new WorldProviderHell();
        }

        if (par0 == 0)
        {
            return new WorldProviderSurface();
        }

        if (par0 == 1)
        {
            return new WorldProviderEnd();
        }
        else
        {
            return null;
        }
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
            return 160F;
        }
        if(mod_noBiomesX.LowHangingClouds)
        {
            return 108F;
        } else
        {
            return 128F;
        }
    }

    public boolean isSkyColored()
    {
        if ((mod_noBiomesX.Generator==0 && mod_noBiomesX.MapFeatures==3) || mod_noBiomesX.MapTheme!=0){
            return false;
        }
        return true;
    }

    /**
     * Gets the hard-coded portal location to use when entering this dimension
     */
    public ChunkCoordinates getEntrancePortalLocation()
    {
        return null;
    }

    public int getAverageGroundLevel()
    {
        return terrainType != WorldType.field_48636_c ? 64 : 4;
    }

    /**
     * returns true if there should be no sky, false otherwise
     */
    public boolean getWorldHasNoSky()
    {
        return terrainType != WorldType.field_48636_c && !hasNoSky;
    }

    public double func_46065_j()
    {
        return terrainType != WorldType.field_48636_c ? 0.03125D : 1.0D;
    }

    public boolean func_48218_b(int par1, int par2)
    {
        return false;
    }
}
