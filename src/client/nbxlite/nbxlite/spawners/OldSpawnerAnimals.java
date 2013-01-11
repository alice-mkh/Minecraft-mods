package net.minecraft.src.nbxlite.spawners;

import java.lang.reflect.Constructor;
import java.util.*;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.oldbiomes.OldBiomeGenBase;

public class OldSpawnerAnimals
{

    private int maxSpawns;
    private Class spawnBaseClass;
    private List spawnSubclasses;
    private Set nearbyChunkSet;

    public OldSpawnerAnimals(int i, EnumCreatureType type)
    {
        nearbyChunkSet = new HashSet();
        maxSpawns = i;
        spawnBaseClass = type.getCreatureClass();
        spawnSubclasses = OldBiomeGenBase.notABiome.getSpawnableList(type);
    }

    public void func_1150_a(World world)
    {
        int i = world.countEntities2(spawnBaseClass);
        if(i < maxSpawns)
        {
            for(int j = 0; j < 3; j++)
            {
                if (spawnBaseClass.isAssignableFrom(EntityWaterMob.class)){
                    func_1149_a_water(world, 1, null);
                }else{
                    func_1149_a(world, 1, null);
                }
            }

        }
    }

    private boolean isBounds(int x, int z){
        if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS){
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

    protected ChunkPosition func_1151_a(World world, int i, int j)
    {
        int k = i + world.rand.nextInt(16);
        int l = world.rand.nextInt(getWorldHeight(world, i, j));
        int i1 = j + world.rand.nextInt(16);
        return new ChunkPosition(k, l, i1);
    }

    private int func_1149_a(World world, int i, IProgressUpdate iprogressupdate)
    {
        nearbyChunkSet.clear();
        for(int j = 0; j < world.playerEntities.size(); j++)
        {
            EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(j);
            int l = MathHelper.floor_double(entityplayer.posX / 16D);
            int i1 = MathHelper.floor_double(entityplayer.posZ / 16D);
            byte byte0 = 4;
            for(int k1 = -byte0; k1 <= byte0; k1++)
            {
                for(int i2 = -byte0; i2 <= byte0; i2++)
                {
                    nearbyChunkSet.add(new ChunkCoordIntPair(k1 + l, i2 + i1));
                }

            }

        }

        int k = 0;
        Iterator iterator = nearbyChunkSet.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator.next();
            if(world.rand.nextInt(10) == 0)
            {
                int j1 = world.rand.nextInt(spawnSubclasses.size());
                ChunkPosition chunkposition = func_1151_a(world, chunkcoordintpair.chunkXPos * 16, chunkcoordintpair.chunkZPos * 16);
                int l1 = chunkposition.x;
                int j2 = chunkposition.y;
                int k2 = chunkposition.z;
                if(world.isBlockOpaqueCube(l1, j2, k2))
                {
                    return 0;
                }
                if(world.getBlockMaterial(l1, j2, k2) != Material.air)
                {
                    return 0;
                }
                int l2 = 0;
                while(l2 < 3) 
                {
                    int i3 = l1;
                    int j3 = j2;
                    int k3 = k2;
                    byte byte1 = 6;
                    for(int l3 = 0; l3 < 2; l3++)
                    {
                        i3 += world.rand.nextInt(byte1) - world.rand.nextInt(byte1);
                        j3 += world.rand.nextInt(1) - world.rand.nextInt(1);
                        k3 += world.rand.nextInt(byte1) - world.rand.nextInt(byte1);
                        if(!world.isBlockOpaqueCube(i3, j3 - 1, k3) || world.isBlockOpaqueCube(i3, j3, k3) || world.getBlockMaterial(i3, j3, k3).isLiquid() || world.isBlockOpaqueCube(i3, j3 + 1, k3))
                        {
                            continue;
                        }
                        float f = (float)i3 + 0.5F;
                        float f1 = j3;
                        float f2 = (float)k3 + 0.5F;
                        if(world.getClosestPlayer(f, f1, f2, 24D) != null)
                        {
                            continue;
                        }
                        float f3 = f - (float)world.getWorldInfo().getSpawnX();
                        float f4 = f1 - (float)world.getWorldInfo().getSpawnY();
                        float f5 = f2 - (float)world.getWorldInfo().getSpawnZ();
                        float f6 = f3 * f3 + f4 * f4 + f5 * f5;
                        if(f6 < 576F)
                        {
                            continue;
                        }
                        EntityLiving entityliving;
                        try
                        {
                            entityliving = (EntityLiving)((SpawnListEntryBeta)spawnSubclasses.get(j1)).entityClass.getConstructor(new Class[] {
                                net.minecraft.src.World.class
                            }).newInstance(new Object[] {
                                world
                            });
                        }
                        catch(Exception exception)
                        {
                            exception.printStackTrace();
                            return k;
                        }
                        if ((entityliving instanceof EntityWolf || entityliving instanceof EntityOcelot || entityliving instanceof EntityEnderman) && world.rand.nextInt(10) != 0){
                            continue;
                        }
                        entityliving.setLocationAndAngles(f, f1, f2, world.rand.nextFloat() * 360F, 0.0F);
                        if(!entityliving.getCanSpawnHere())
                        {
                            continue;
                        }
                        if(isBounds(i3, k3)){
                            continue;
                        }
                        if(entityliving instanceof EntitySlime && entityliving.posY > 16D && ODNBXlite.RestrictSlimes)
                        {
                            continue;
                        }
                        k++;
                        world.spawnEntityInWorld(entityliving);
                        entityliving.initCreature();
                    }

                    l2++;
                }
            }
        } while(true);
        return k;
    }

    private int func_1149_a_water(World world, int i, IProgressUpdate iprogressupdate)
    {
        nearbyChunkSet.clear();
        for(int j = 0; j < world.playerEntities.size(); j++)
        {
            EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(j);
            int l = MathHelper.floor_double(entityplayer.posX / 16D);
            int i1 = MathHelper.floor_double(entityplayer.posZ / 16D);
            byte byte0 = 4;
            for(int k1 = -byte0; k1 <= byte0; k1++)
            {
                for(int i2 = -byte0; i2 <= byte0; i2++)
                {
                    nearbyChunkSet.add(new ChunkCoordIntPair(k1 + l, i2 + i1));
                }

            }

        }

        int k = 0;
        Iterator iterator = nearbyChunkSet.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator.next();
            if(world.rand.nextInt(10) == 0)
            {
                int j1 = world.rand.nextInt(spawnSubclasses.size());
                ChunkPosition chunkposition = func_1151_a(world, chunkcoordintpair.chunkXPos * 16, chunkcoordintpair.chunkZPos * 16);
                int l1 = chunkposition.x;
                int j2 = chunkposition.y;
                int k2 = chunkposition.z;
                if(!world.getBlockMaterial(l1, j2, k2).isLiquid())
                {
                    return 0;
                }
                int l2 = 0;
                while(l2 < 3) 
                {
                    int i3 = l1;
                    int j3 = j2;
                    int k3 = k2;
                    byte byte1 = 6;
                    for(int l3 = 0; l3 < 2; l3++)
                    {
                        i3 += world.rand.nextInt(byte1) - world.rand.nextInt(byte1);
                        j3 += world.rand.nextInt(1) - world.rand.nextInt(1);
                        k3 += world.rand.nextInt(byte1) - world.rand.nextInt(byte1);
                        if(!world.getBlockMaterial(i3, j3, k3).isLiquid())
                        {
                            continue;
                        }
                        float f = (float)i3 + 0.5F;
                        float f1 = j3;
                        float f2 = (float)k3 + 0.5F;
                        if(world.getClosestPlayer(f, f1, f2, 24D) != null)
                        {
                            continue;
                        }
                        float f3 = f - (float)world.getWorldInfo().getSpawnX();
                        float f4 = f1 - (float)world.getWorldInfo().getSpawnY();
                        float f5 = f2 - (float)world.getWorldInfo().getSpawnZ();
                        float f6 = f3 * f3 + f4 * f4 + f5 * f5;
                        if(f6 < 576F)
                        {
                            continue;
                        }
                        EntityLiving entityliving;
                        try
                        {
                            entityliving = (EntityLiving)((SpawnListEntryBeta)spawnSubclasses.get(j1)).entityClass.getConstructor(new Class[] {
                                net.minecraft.src.World.class
                            }).newInstance(new Object[] {
                                world
                            });
                        }
                        catch(Exception exception)
                        {
                            exception.printStackTrace();
                            return k;
                        }
                        entityliving.setLocationAndAngles(f, f1, f2, world.rand.nextFloat() * 360F, 0.0F);
                        if(!entityliving.getCanSpawnHere())
                        {
                            continue;
                        }
                        if(isBounds(i3, k3)){
                            continue;
                        }
                        k++;
                        world.spawnEntityInWorld(entityliving);
                    }

                    l2++;
                }
            }
        } while(true);
        return k;
    }

    public static int getWorldHeight(World w, int i, int j){
        Chunk c = w.getChunkFromBlockCoords(i, j);
        return Math.max(c.getTopFilledSegment() - 16, 128);
    }
}
