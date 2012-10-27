package net.minecraft.src.nbxlite;

import java.io.PrintStream;
import net.minecraft.src.*;

public class MetadataChunkBlock
{

    public MetadataChunkBlock(EnumSkyBlock enumskyblock, int i, int j, int k, int l, int i1, int j1)
    {
        blockEnum = enumskyblock;
        minX = i;
        minY = j;
        minZ = k;
        maxX = l;
        maxY = i1;
        maxZ = j1;
    }

    public void updateChunkLighting(World world)
    {
        int i = (maxX - minX) + 1;
        int j = (maxY - minY) + 1;
        int k = (maxZ - minZ) + 1;
        int l = i * j * k;
        if(l > 16 * 16 * 256)
        {
            System.out.println("Light too large, skipping!");
            return;
        }
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;
        boolean flag1 = false;
        for(int k1 = minX; k1 <= maxX; k1++)
        {
            for(int l1 = minZ; l1 <= maxZ; l1++)
            {
                int i2 = k1 >> 4;
                int j2 = l1 >> 4;
                boolean flag2 = false;
                if(flag && i2 == i1 && j2 == j1)
                {
                    flag2 = flag1;
                } else
                {
                    flag2 = world.doChunksNearChunkExist(k1, 0, l1, 1);
                    if(flag2)
                    {
                        Chunk chunk = world.getChunkFromChunkCoords(k1 >> 4, l1 >> 4);
                        if(chunk.isEmpty())
                        {
                            flag2 = false;
                        }
                    }
                    flag1 = flag2;
                    i1 = i2;
                    j1 = j2;
                }
                if(!flag2)
                {
                    continue;
                }
                if(minY < 0)
                {
                    minY = 0;
                }
                if(maxY >= 256)
                {
                    maxY = 255;
                }
                for(int k2 = minY; k2 <= maxY; k2++)
                {
                    int l2 = world.getSavedLightValue(blockEnum, k1, k2, l1);
                    int i3 = 0;
                    int j3 = world.getBlockId(k1, k2, l1);
                    int k3 = Block.lightOpacity[j3];
                    if(k3 == 0)
                    {
                        k3 = 1;
                    }
                    int l3 = 0;
                    if(blockEnum == EnumSkyBlock.Sky && !world.provider.hasNoSky)
                    {
                        if(world.canExistingBlockSeeTheSky(k1, k2, l1))
                        {
                            l3 = 15;
                        }
                    } else
                    if(blockEnum == EnumSkyBlock.Block)
                    {
                        l3 = Block.lightValue[j3];
                    }
                    if(k3 >= 15 && l3 == 0)
                    {
                        i3 = 0;
                    } else
                    {
                        int i4 = world.getSavedLightValue(blockEnum, k1 - 1, k2, l1);
                        int k4 = world.getSavedLightValue(blockEnum, k1 + 1, k2, l1);
                        int l4 = world.getSavedLightValue(blockEnum, k1, k2 - 1, l1);
                        int i5 = world.getSavedLightValue(blockEnum, k1, k2 + 1, l1);
                        int j5 = world.getSavedLightValue(blockEnum, k1, k2, l1 - 1);
                        int k5 = world.getSavedLightValue(blockEnum, k1, k2, l1 + 1);
                        if ((ODNBXlite.SurrWaterType==Block.waterStill.blockID||ODNBXlite.SurrWaterType==Block.waterMoving.blockID) && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV){
                            if (isBounds(world, k1-1, k2, l1)){
                                i4 = ODNBXlite.getLightInBounds(blockEnum, k2);
                            }
                            if (isBounds(world, k1+1, k2, l1)){
                                k4 = ODNBXlite.getLightInBounds(blockEnum, k2);
                            }
                            if (isBounds(world, k1, k2-1, l1)){
                                l4 = ODNBXlite.getLightInBounds(blockEnum, k2-1);
                            }
                            if (isBounds(world, k1, k2+1, l1)){
                                i5 = ODNBXlite.getLightInBounds(blockEnum, k2+1);
                            }
                            if (isBounds(world, k1, k2, l1-1)){
                                j5 = ODNBXlite.getLightInBounds(blockEnum, k2);
                            }
                            if (isBounds(world, k1, k2, l1+1)){
                                k5 = ODNBXlite.getLightInBounds(blockEnum, k2);
                            }
                        }
                        i3 = i4;
                        if(k4 > i3)
                        {
                            i3 = k4;
                        }
                        if(l4 > i3)
                        {
                            i3 = l4;
                        }
                        if(i5 > i3)
                        {
                            i3 = i5;
                        }
                        if(j5 > i3)
                        {
                            i3 = j5;
                        }
                        if(k5 > i3)
                        {
                            i3 = k5;
                        }
                        i3 -= k3;
                        if(i3 < 0)
                        {
                            i3 = 0;
                        }
                        if(l3 > i3)
                        {
                            i3 = l3;
                        }
                    }
                    if(l2 == i3)
                    {
                        continue;
                    }
                    world.setLightValue(blockEnum, k1, k2, l1, i3);
                    int j4 = i3 - 1;
                    if(j4 < 0)
                    {
                        j4 = 0;
                    }
                    world.neighborLightPropagationChanged(blockEnum, k1 - 1, k2, l1, j4);
                    world.neighborLightPropagationChanged(blockEnum, k1, k2 - 1, l1, j4);
                    world.neighborLightPropagationChanged(blockEnum, k1, k2, l1 - 1, j4);
                    if(k1 + 1 >= maxX)
                    {
                        world.neighborLightPropagationChanged(blockEnum, k1 + 1, k2, l1, j4);
                    }
                    if(k2 + 1 >= maxY)
                    {
                        world.neighborLightPropagationChanged(blockEnum, k1, k2 + 1, l1, j4);
                    }
                    if(l1 + 1 >= maxZ)
                    {
                        world.neighborLightPropagationChanged(blockEnum, k1, k2, l1 + 1, j4);
                    }
                }

            }

        }

    }

    public boolean func_866_a(int i, int j, int k, int l, int i1, int j1)
    {
        if(i >= minX && j >= minY && k >= minZ && l <= maxX && i1 <= maxY && j1 <= maxZ)
        {
            return true;
        }
        int k1 = 1;
        if(i >= minX - k1 && j >= minY - k1 && k >= minZ - k1 && l <= maxX + k1 && i1 <= maxY + k1 && j1 <= maxZ + k1)
        {
            int l1 = maxX - minX;
            int i2 = maxY - minY;
            int j2 = maxZ - minZ;
            if(i > minX)
            {
                i = minX;
            }
            if(j > minY)
            {
                j = minY;
            }
            if(k > minZ)
            {
                k = minZ;
            }
            if(l < maxX)
            {
                l = maxX;
            }
            if(i1 < maxY)
            {
                i1 = maxY;
            }
            if(j1 < maxZ)
            {
                j1 = maxZ;
            }
            int k2 = l - i;
            int l2 = i1 - j;
            int i3 = j1 - k;
            int j3 = l1 * i2 * j2;
            int k3 = k2 * l2 * i3;
            if(k3 - j3 <= 2)
            {
                minX = i;
                minY = j;
                minZ = k;
                maxX = l;
                maxY = i1;
                maxZ = j1;
                return true;
            }
        }
        return false;
    }

    private boolean isBounds(World w, int x, int y, int z){
        if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && (w.provider==null || w.provider.dimensionId==0)){
            if (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV){
                if(x<=0 || x>=ODNBXlite.IndevWidthX-1 || z<=0 || z>=ODNBXlite.IndevWidthZ-1){
                    return true;
                }
            }
            if (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_CLASSIC){
                if(x<0 || x>=ODNBXlite.IndevWidthX || z<0 || z>=ODNBXlite.IndevWidthZ){
                    return true;
                }
            }
        }
        return false;
    }

    public final EnumSkyBlock blockEnum;
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
}
