package net.minecraft.src;

public class WorldProviderHell extends WorldProvider
{
    public WorldProviderHell()
    {
    }

    public void registerWorldChunkManager()
    {
        worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 1.0F, 0.0F);
        isHellWorld = true;
        hasNoSky = true;
        worldType = -1;
    }

    /**
     * Creates the light to brightness table
     */
    protected void generateLightBrightnessTable()
    {
        float f = 0.1F;

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
        return new ChunkProviderHell(worldObj, worldObj.getSeed());
    }

    public boolean func_48567_d()
    {
        return false;
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    public boolean canCoordinateBeSpawn(int par1, int par2)
    {
        int k = worldObj.getFirstUncoveredBlock(par1, par2);
        if(k == Block.bedrock.blockID)
        {
            return false;
        }
        if(k == 0)
        {
            return false;
        }
        return Block.opaqueCubeLookup[k];
    }

    public float calculateCelestialAngle(long par1, float par3)
    {
        return 0.5F;
    }

    public boolean canRespawnHere()
    {
        return false;
    }
}
