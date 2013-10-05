package net.minecraft.src;

import java.lang.reflect.Constructor;
import java.util.*;
import net.minecraft.src.ssp.FakeWorldServer;
import net.minecraft.src.ssp.WorldSSP;

public final class SpawnerAnimals
{
    /** The 17x17 area around the player where mobs can spawn */
    private HashMap eligibleChunksForSpawning;
    private static Map classToStringMapping;

    public SpawnerAnimals()
    {
        eligibleChunksForSpawning = new HashMap();
    }

    /**
     * Given a chunk, find a random position in it.
     */
    protected static ChunkPosition getRandomSpawningPointInChunk(World par0World, int par1, int par2)
    {
        Chunk chunk = par0World.getChunkFromChunkCoords(par1, par2);
        int i = par1 * 16 + par0World.rand.nextInt(16);
        int j = par2 * 16 + par0World.rand.nextInt(16);
        int k = par0World.rand.nextInt(chunk != null ? (chunk.getTopFilledSegment() + 16) - 1 : par0World.getActualHeight());
        return new ChunkPosition(i, k, j);
    }

    /**
     * adds all chunks within the spawn radius of the players to eligibleChunksForSpawning. pars: the world,
     * hostileCreatures, passiveCreatures. returns number of eligible chunks.
     */
    public int findChunksForSpawning(WorldServer par1WorldServer, boolean par2, boolean par3, boolean par4)
    {
        if (!par2 && !par3)
        {
            return 0;
        }
        else
        {
            eligibleChunksForSpawning.clear();
            int var3;
            int var6;

            for (var3 = 0; var3 < par1WorldServer.playerEntities.size(); ++var3)
            {
                EntityPlayer var4 = (EntityPlayer)par1WorldServer.playerEntities.get(var3);
                int var5 = MathHelper.floor_double(var4.posX / 16.0D);
                var6 = MathHelper.floor_double(var4.posZ / 16.0D);
                byte var7 = 8;

                for (int var8 = -var7; var8 <= var7; ++var8)
                {
                    for (int var9 = -var7; var9 <= var7; ++var9)
                    {
                        boolean var10 = var8 == -var7 || var8 == var7 || var9 == -var7 || var9 == var7;
                        ChunkCoordIntPair var11 = new ChunkCoordIntPair(var8 + var5, var9 + var6);

                        if (!var10)
                        {
                            eligibleChunksForSpawning.put(var11, Boolean.valueOf(false));
                        }
                        else if (!eligibleChunksForSpawning.containsKey(var11))
                        {
                            eligibleChunksForSpawning.put(var11, Boolean.valueOf(true));
                        }
                    }
                }
            }

            var3 = 0;
            ChunkCoordinates var31 = par1WorldServer.getSpawnPoint();
            EnumCreatureType[] var32 = EnumCreatureType.values();
            var6 = var32.length;

            for (int var33 = 0; var33 < var6; ++var33)
            {
                EnumCreatureType var34 = var32[var33];

                if ((!var34.getPeacefulCreature() || par3) && (var34.getPeacefulCreature() || par2) && par1WorldServer.countEntities(var34.getCreatureClass()) <= var34.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256)
                {
                    Iterator var35 = eligibleChunksForSpawning.keySet().iterator();
                    label108:

                    while (var35.hasNext())
                    {
                        ChunkCoordIntPair var37 = (ChunkCoordIntPair)var35.next();

                        if (!((Boolean)eligibleChunksForSpawning.get(var37)).booleanValue())
                        {
                            ChunkPosition var36 = getRandomSpawningPointInChunk(par1WorldServer, var37.chunkXPos, var37.chunkZPos);
                            int var12 = var36.x;
                            int var13 = var36.y;
                            int var14 = var36.z;

                            if (!par1WorldServer.isBlockNormalCube(var12, var13, var14) && par1WorldServer.getBlockMaterial(var12, var13, var14) == var34.getCreatureMaterial())
                            {
                                int var15 = 0;
                                int var16 = 0;

                                while (var16 < 3)
                                {
                                    int var17 = var12;
                                    int var18 = var13;
                                    int var19 = var14;
                                    byte var20 = 6;
                                    SpawnListEntry var21 = null;
                                    EntityLivingData entitylivingdata = null;
                                    int var22 = 0;

                                    while (true)
                                    {
                                        if (var22 < 4)
                                        {
                                            label101:
                                            {
                                                var17 += par1WorldServer.rand.nextInt(var20) - par1WorldServer.rand.nextInt(var20);
                                                var18 += par1WorldServer.rand.nextInt(1) - par1WorldServer.rand.nextInt(1);
                                                var19 += par1WorldServer.rand.nextInt(var20) - par1WorldServer.rand.nextInt(var20);

                                                if (canCreatureTypeSpawnAtLocation(var34, par1WorldServer, var17, var18, var19))
                                                {
                                                    float var23 = (float)var17 + 0.5F;
                                                    float var24 = (float)var18;
                                                    float var25 = (float)var19 + 0.5F;

                                                    if (par1WorldServer.getClosestPlayer((double)var23, (double)var24, (double)var25, 24.0D) == null)
                                                    {
                                                        float var26 = var23 - (float)var31.posX;
                                                        float var27 = var24 - (float)var31.posY;
                                                        float var28 = var25 - (float)var31.posZ;
                                                        float var29 = var26 * var26 + var27 * var27 + var28 * var28;

                                                        if (var29 >= 576.0F)
                                                        {
                                                            if (var21 == null)
                                                            {
                                                                var21 = spawnRandomCreature(par1WorldServer, var34, var17, var18, var19);

                                                                if (var21 == null)
                                                                {
                                                                    break label101;
                                                                }
                                                            }

                                                            EntityLiving var38;

                                                            try
                                                            {
                                                                var38 = (EntityLiving)var21.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {par1WorldServer});
                                                            }
                                                            catch (Exception var30)
                                                            {
                                                                var30.printStackTrace();
                                                                return var3;
                                                            }

                                                            var38.setLocationAndAngles((double)var23, (double)var24, (double)var25, par1WorldServer.rand.nextFloat() * 360.0F, 0.0F);

                                                            if (var38.getCanSpawnHere())
                                                            {
                                                                ++var15;
                                                                par1WorldServer.spawnEntityInWorld(var38);
                                                                entitylivingdata = var38.onSpawnWithEgg(entitylivingdata);

                                                                if (var15 >= var38.getMaxSpawnedInChunk())
                                                                {
                                                                    continue label108;
                                                                }
                                                            }

                                                            var3 += var15;
                                                        }
                                                    }
                                                }

                                                ++var22;
                                                continue;
                                            }
                                        }

                                        ++var16;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return var3;
        }
    }

    public int performSpawningSP(WorldSSP par0WorldServer, boolean par1, boolean par2)
    {
        if (!par1 && !par2)
        {
            return 0;
        }
        else
        {
            eligibleChunksForSpawning.clear();
            int var3;
            int var6;

            for (var3 = 0; var3 < par0WorldServer.playerEntities.size(); ++var3)
            {
                EntityPlayer var4 = (EntityPlayer)par0WorldServer.playerEntities.get(var3);
                int var5 = MathHelper.floor_double(var4.posX / 16.0D);
                var6 = MathHelper.floor_double(var4.posZ / 16.0D);
                byte var7 = 8;

                for (int var8 = -var7; var8 <= var7; ++var8)
                {
                    for (int var9 = -var7; var9 <= var7; ++var9)
                    {
                        boolean var10 = var8 == -var7 || var8 == var7 || var9 == -var7 || var9 == var7;
                        ChunkCoordIntPair var11 = new ChunkCoordIntPair(var8 + var5, var9 + var6);

                        if (!var10)
                        {
                            eligibleChunksForSpawning.put(var11, Boolean.valueOf(false));
                        }
                        else if (!eligibleChunksForSpawning.containsKey(var11))
                        {
                            eligibleChunksForSpawning.put(var11, Boolean.valueOf(true));
                        }
                    }
                }
            }

            var3 = 0;
            ChunkCoordinates var31 = par0WorldServer.getSpawnPoint();
            EnumCreatureType[] var32 = EnumCreatureType.values();
            var6 = var32.length;

            for (int var33 = 0; var33 < var6; ++var33)
            {
                EnumCreatureType var34 = var32[var33];

                if ((!var34.getPeacefulCreature() || par2) && (var34.getPeacefulCreature() || par1) && par0WorldServer.countEntities(var34.getCreatureClass()) <= var34.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256)
                {
                    Iterator var35 = eligibleChunksForSpawning.keySet().iterator();
                    label108:

                    while (var35.hasNext())
                    {
                        ChunkCoordIntPair var37 = (ChunkCoordIntPair)var35.next();

                        if (!((Boolean)eligibleChunksForSpawning.get(var37)).booleanValue())
                        {
                            ChunkPosition var36 = getRandomSpawningPointInChunk(par0WorldServer, var37.chunkXPos, var37.chunkZPos);
                            int var12 = var36.x;
                            int var13 = var36.y;
                            int var14 = var36.z;

                            if (!par0WorldServer.isBlockNormalCube(var12, var13, var14) && par0WorldServer.getBlockMaterial(var12, var13, var14) == var34.getCreatureMaterial())
                            {
                                int var15 = 0;
                                int var16 = 0;

                                while (var16 < 3)
                                {
                                    int var17 = var12;
                                    int var18 = var13;
                                    int var19 = var14;
                                    byte var20 = 6;
                                    SpawnListEntry var21 = null;
                                    EntityLivingData entitylivingdata = null;
                                    int var22 = 0;

                                    while (true)
                                    {
                                        if (var22 < 4)
                                        {
                                            label101:
                                            {
                                                var17 += par0WorldServer.rand.nextInt(var20) - par0WorldServer.rand.nextInt(var20);
                                                var18 += par0WorldServer.rand.nextInt(1) - par0WorldServer.rand.nextInt(1);
                                                var19 += par0WorldServer.rand.nextInt(var20) - par0WorldServer.rand.nextInt(var20);

                                                if (canCreatureTypeSpawnAtLocation(var34, par0WorldServer, var17, var18, var19))
                                                {
                                                    float var23 = (float)var17 + 0.5F;
                                                    float var24 = (float)var18;
                                                    float var25 = (float)var19 + 0.5F;

                                                    if (par0WorldServer.getClosestPlayer((double)var23, (double)var24, (double)var25, 24.0D) == null)
                                                    {
                                                        float var26 = var23 - (float)var31.posX;
                                                        float var27 = var24 - (float)var31.posY;
                                                        float var28 = var25 - (float)var31.posZ;
                                                        float var29 = var26 * var26 + var27 * var27 + var28 * var28;

                                                        if (var29 >= 576.0F)
                                                        {
                                                            if (var21 == null)
                                                            {
                                                                var21 = spawnRandomCreature(par0WorldServer, var34, var17, var18, var19);

                                                                if (var21 == null)
                                                                {
                                                                    break label101;
                                                                }
                                                            }

                                                            EntityLiving var38;

                                                            try
                                                            {
                                                                var38 = (EntityLiving)var21.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {par0WorldServer});
                                                            }
                                                            catch (Exception var30)
                                                            {
                                                                var30.printStackTrace();
                                                                return var3;
                                                            }

                                                            var38.setLocationAndAngles((double)var23, (double)var24, (double)var25, par0WorldServer.rand.nextFloat() * 360.0F, 0.0F);

//                                                             if(!var38.allow(par0WorldServer.provider.dimensionId))
//                                                             {
//                                                                 continue;
//                                                             }
                                                            if (var38.getCanSpawnHere())
                                                            {
                                                                ++var15;
                                                                par0WorldServer.spawnEntityInWorld(var38);
                                                                entitylivingdata = var38.onSpawnWithEgg(entitylivingdata);

                                                                if (var15 >= var38.getMaxSpawnedInChunk())
                                                                {
                                                                    continue label108;
                                                                }
                                                            }

                                                            var3 += var15;
                                                        }
                                                    }
                                                }

                                                ++var22;
                                                continue;
                                            }
                                        }

                                        ++var16;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return var3;
        }
    }

    /**
     * Returns whether or not the specified creature type can spawn at the specified location.
     */
    public static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType par0EnumCreatureType, World par1World, int par2, int par3, int par4)
    {
        if (par0EnumCreatureType.getCreatureMaterial() == Material.water)
        {
            return par1World.getBlockMaterial(par2, par3, par4).isLiquid() && par1World.getBlockMaterial(par2, par3 - 1, par4).isLiquid() && !par1World.isBlockNormalCube(par2, par3 + 1, par4);
        }

        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4))
        {
            return false;
        }
        else
        {
            int i = par1World.getBlockId(par2, par3 - 1, par4);
            return i != Block.bedrock.blockID && !par1World.isBlockNormalCube(par2, par3, par4) && !par1World.getBlockMaterial(par2, par3, par4).isLiquid() && !par1World.isBlockNormalCube(par2, par3 + 1, par4);
        }
    }

    /**
     * Called during chunk generation to spawn initial creatures.
     */
    public static void performWorldGenSpawning(World par0World, BiomeGenBase par1BiomeGenBase, int par2, int par3, int par4, int par5, Random par6Random)
    {
        List list = getSpawnableList(par0World, par1BiomeGenBase, EnumCreatureType.creature);

        if (list.isEmpty())
        {
            return;
        }

        while (par6Random.nextFloat() < par1BiomeGenBase.getSpawningChance())
        {
            SpawnListEntry spawnlistentry = (SpawnListEntry)WeightedRandom.getRandomItem(par0World.rand, list);
            EntityLivingData entitylivingdata = null;
            int i = spawnlistentry.minGroupCount + par6Random.nextInt((1 + spawnlistentry.maxGroupCount) - spawnlistentry.minGroupCount);
            int j = par2 + par6Random.nextInt(par4);
            int k = par3 + par6Random.nextInt(par5);
            int l = j;
            int i1 = k;
            int j1 = 0;

            while (j1 < i)
            {
                boolean flag = false;

                for (int k1 = 0; !flag && k1 < 4; k1++)
                {
                    int l1 = par0World.getTopSolidOrLiquidBlock(j, k);

                    if (canCreatureTypeSpawnAtLocation(EnumCreatureType.creature, par0World, j, l1, k))
                    {
                        float f = (float)j + 0.5F;
                        float f1 = l1;
                        float f2 = (float)k + 0.5F;
                        EntityLiving entityliving;

                        try
                        {
                            entityliving = (EntityLiving)spawnlistentry.entityClass.getConstructor(new Class[]
                                    {
                                        net.minecraft.src.World.class
                                    }).newInstance(new Object[]
                                            {
                                                par0World
                                            });
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                            continue;
                        }

                        entityliving.setLocationAndAngles(f, f1, f2, par6Random.nextFloat() * 360F, 0.0F);
                        par0World.spawnEntityInWorld(entityliving);
                        entitylivingdata = entityliving.onSpawnWithEgg(entitylivingdata);
                        flag = true;
                    }

                    j += par6Random.nextInt(5) - par6Random.nextInt(5);

                    for (k += par6Random.nextInt(5) - par6Random.nextInt(5); j < par2 || j >= par2 + par4 || k < par3 || k >= par3 + par4; k = (i1 + par6Random.nextInt(5)) - par6Random.nextInt(5))
                    {
                        j = (l + par6Random.nextInt(5)) - par6Random.nextInt(5);
                    }
                }

                j1++;
            }
        }
    }

    private static SpawnListEntry spawnRandomCreature(World w, EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4){
        if (w instanceof FakeWorldServer){
            return null;
        }
        List list2 = w.getChunkProvider().getPossibleCreatures(par1EnumCreatureType, par2, par3, par4);
        List list = new ArrayList();
        for (Object o : list2){
            SpawnListEntry s = (SpawnListEntry)o;
            String str = (String)(classToStringMapping.get(s.entityClass));
            if (EntityLiving.allow(str, w.provider.dimensionId)){
                list.add(s);
            }
        }
        if (list == null || list.isEmpty()){
            return null;
        }
        return (SpawnListEntry)WeightedRandom.getRandomItem(w.rand, list);
    }

    private static List getSpawnableList(World w, BiomeGenBase par1BiomeGenBase, EnumCreatureType par2EnumCreatureType){
        List list2 = par1BiomeGenBase.getSpawnableList(par2EnumCreatureType);
        List list = new ArrayList();
        for (Object o : list2){
            SpawnListEntry s = (SpawnListEntry)o;
            String str = (String)(classToStringMapping.get(s.entityClass));
            if (EntityLiving.allow(str, w.provider.dimensionId)){
                list.add(s);
            }
        }
        return list;
    }

    static{
        try{
            java.lang.reflect.Field f = (EntityList.class).getDeclaredFields()[1];
            f.setAccessible(true);
            classToStringMapping = (Map)(f.get(null));
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
}
