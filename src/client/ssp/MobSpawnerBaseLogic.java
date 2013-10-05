package net.minecraft.src;

import java.util.*;

public abstract class MobSpawnerBaseLogic
{
    /** The delay to spawn. */
    public int spawnDelay;
    private String mobID;

    /** List of minecart to spawn. */
    private List minecartToSpawn;
    private WeightedRandomMinecart randomMinecart;
    public double field_98287_c;
    public double field_98284_d;
    private int minSpawnDelay;
    private int maxSpawnDelay;

    /** A counter for spawn tries. */
    private int spawnCount;
    private Entity field_98291_j;
    private int maxNearbyEntities;

    /** The distance from which a player activates the spawner. */
    private int activatingRangeFromPlayer;

    /** The range coefficient for spawning entities around. */
    private int spawnRange;

    public MobSpawnerBaseLogic()
    {
        spawnDelay = 20;
        mobID = "Pig";
        minSpawnDelay = 200;
        maxSpawnDelay = 800;
        spawnCount = 4;
        maxNearbyEntities = 6;
        activatingRangeFromPlayer = 16;
        spawnRange = 4;
    }

    /**
     * Gets the entity name that should be spawned.
     */
    public String getEntityNameToSpawn()
    {
        if (getRandomMinecart() == null)
        {
            if (mobID.equals("Minecart"))
            {
                mobID = "MinecartRideable";
            }

            return mobID;
        }
        else
        {
            return getRandomMinecart().minecartName;
        }
    }

    public void setMobID(String par1Str)
    {
        mobID = par1Str;
    }

    /**
     * Returns true if there's a player close enough to this mob spawner to activate it.
     */
    public boolean canRun()
    {
        return getSpawnerWorld().getClosestPlayer((double)getSpawnerX() + 0.5D, (double)getSpawnerY() + 0.5D, (double)getSpawnerZ() + 0.5D, activatingRangeFromPlayer) != null;
    }

    public void updateSpawner()
    {
        if (Minecraft.getMinecraft().enableSP){
            field_98284_d = field_98287_c;
        }
        if (!canRun())
        {
            return;
        }

        if (!Minecraft.getMinecraft().enableSP)
        {
            double d = (float)getSpawnerX() + getSpawnerWorld().rand.nextFloat();
            double d1 = (float)getSpawnerY() + getSpawnerWorld().rand.nextFloat();
            double d2 = (float)getSpawnerZ() + getSpawnerWorld().rand.nextFloat();
            getSpawnerWorld().spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
            getSpawnerWorld().spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);

            if (spawnDelay > 0)
            {
                spawnDelay--;
            }

            field_98284_d = field_98287_c;
            field_98287_c = (field_98287_c + (double)(1000F / ((float)spawnDelay + 200F))) % 360D;
        }else{
            double d = (float)getSpawnerX() + getSpawnerWorld().rand.nextFloat();
            double d1 = (float)getSpawnerY() + getSpawnerWorld().rand.nextFloat();
            double d2 = (float)getSpawnerZ() + getSpawnerWorld().rand.nextFloat();
            getSpawnerWorld().spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
            getSpawnerWorld().spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);
            for (field_98287_c += 1000F / ((float)spawnDelay + 200F); field_98287_c > 360D;)
            {
                field_98287_c -= 360D;
                field_98284_d -= 360D;
            }
        }
        if (!getSpawnerWorld().isRemote)
        {
            if (spawnDelay == -1)
            {
                func_98273_j();
            }

            if (spawnDelay > 0)
            {
                spawnDelay--;
                return;
            }

            boolean flag = false;

            for (int i = 0; i < spawnCount; i++)
            {
                Entity entity = EntityList.createEntityByName(getEntityNameToSpawn(), getSpawnerWorld());

                if (entity == null)
                {
                    return;
                }

                int j = getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getAABBPool().getAABB(getSpawnerX(), getSpawnerY(), getSpawnerZ(), getSpawnerX() + 1, getSpawnerY() + 1, getSpawnerZ() + 1).expand(spawnRange * 2, 4D, spawnRange * 2)).size();

                if (j >= maxNearbyEntities)
                {
                    func_98273_j();
                    return;
                }

                double d3 = (double)getSpawnerX() + (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand.nextDouble()) * (double)spawnRange;
                double d4 = (getSpawnerY() + getSpawnerWorld().rand.nextInt(3)) - 1;
                double d5 = (double)getSpawnerZ() + (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand.nextDouble()) * (double)spawnRange;
                EntityLiving entityliving = (entity instanceof EntityLiving) ? (EntityLiving)entity : null;
                entity.setLocationAndAngles(d3, d4, d5, getSpawnerWorld().rand.nextFloat() * 360F, 0.0F);

                if (entityliving != null && !entityliving.getCanSpawnHere())
                {
                    continue;
                }

                func_98265_a(entity);
                getSpawnerWorld().playAuxSFX(2004, getSpawnerX(), getSpawnerY(), getSpawnerZ(), 0);

                if (entityliving != null)
                {
                    entityliving.spawnExplosionParticle();
                }

                flag = true;
            }

            if (flag)
            {
                func_98273_j();
            }
        }
    }

    public Entity func_98265_a(Entity par1Entity)
    {
        if (getRandomMinecart() != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            par1Entity.writeToNBTOptional(nbttagcompound);
            NBTBase nbtbase;

            for (Iterator iterator = getRandomMinecart().field_98222_b.getTags().iterator(); iterator.hasNext(); nbttagcompound.setTag(nbtbase.getName(), nbtbase.copy()))
            {
                nbtbase = (NBTBase)iterator.next();
            }

            par1Entity.readFromNBT(nbttagcompound);

            if (par1Entity.worldObj != null)
            {
                par1Entity.worldObj.spawnEntityInWorld(par1Entity);
            }

            Entity entity = par1Entity;
            NBTTagCompound nbttagcompound1;

            for (; nbttagcompound.hasKey("Riding"); nbttagcompound = nbttagcompound1)
            {
                nbttagcompound1 = nbttagcompound.getCompoundTag("Riding");
                Entity entity1 = EntityList.createEntityByName(nbttagcompound1.getString("id"), par1Entity.worldObj);

                if (entity1 != null)
                {
                    NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                    entity1.writeToNBTOptional(nbttagcompound2);
                    NBTBase nbtbase1;

                    for (Iterator iterator1 = nbttagcompound1.getTags().iterator(); iterator1.hasNext(); nbttagcompound2.setTag(nbtbase1.getName(), nbtbase1.copy()))
                    {
                        nbtbase1 = (NBTBase)iterator1.next();
                    }

                    entity1.readFromNBT(nbttagcompound2);
                    entity1.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);

                    if (par1Entity.worldObj != null)
                    {
                        par1Entity.worldObj.spawnEntityInWorld(entity1);
                    }

                    entity.mountEntity(entity1);
                }

                entity = entity1;
            }
        }
        else if ((par1Entity instanceof EntityLivingBase) && par1Entity.worldObj != null)
        {
            ((EntityLiving)par1Entity).onSpawnWithEgg(null);
            getSpawnerWorld().spawnEntityInWorld(par1Entity);
        }

        return par1Entity;
    }

    private void func_98273_j()
    {
        if (maxSpawnDelay <= minSpawnDelay)
        {
            spawnDelay = minSpawnDelay;
        }
        else
        {
            spawnDelay = minSpawnDelay + getSpawnerWorld().rand.nextInt(maxSpawnDelay - minSpawnDelay);
        }

        if (minecartToSpawn != null && minecartToSpawn.size() > 0)
        {
            setRandomMinecart((WeightedRandomMinecart)WeightedRandom.getRandomItem(getSpawnerWorld().rand, minecartToSpawn));
        }

        func_98267_a(1);
    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        mobID = par1NBTTagCompound.getString("EntityId");
        spawnDelay = par1NBTTagCompound.getShort("Delay");

        if (par1NBTTagCompound.hasKey("SpawnPotentials"))
        {
            minecartToSpawn = new ArrayList();
            NBTTagList nbttaglist = par1NBTTagCompound.getTagList("SpawnPotentials");

            for (int i = 0; i < nbttaglist.tagCount(); i++)
            {
                minecartToSpawn.add(new WeightedRandomMinecart(this, (NBTTagCompound)nbttaglist.tagAt(i)));
            }
        }
        else
        {
            minecartToSpawn = null;
        }

        if (par1NBTTagCompound.hasKey("SpawnData"))
        {
            setRandomMinecart(new WeightedRandomMinecart(this, par1NBTTagCompound.getCompoundTag("SpawnData"), mobID));
        }
        else
        {
            setRandomMinecart(null);
        }

        if (par1NBTTagCompound.hasKey("MinSpawnDelay"))
        {
            minSpawnDelay = par1NBTTagCompound.getShort("MinSpawnDelay");
            maxSpawnDelay = par1NBTTagCompound.getShort("MaxSpawnDelay");
            spawnCount = par1NBTTagCompound.getShort("SpawnCount");
        }

        if (par1NBTTagCompound.hasKey("MaxNearbyEntities"))
        {
            maxNearbyEntities = par1NBTTagCompound.getShort("MaxNearbyEntities");
            activatingRangeFromPlayer = par1NBTTagCompound.getShort("RequiredPlayerRange");
        }

        if (par1NBTTagCompound.hasKey("SpawnRange"))
        {
            spawnRange = par1NBTTagCompound.getShort("SpawnRange");
        }

        if (getSpawnerWorld() != null && getSpawnerWorld().isRemote)
        {
            field_98291_j = null;
        }
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setString("EntityId", getEntityNameToSpawn());
        par1NBTTagCompound.setShort("Delay", (short)spawnDelay);
        par1NBTTagCompound.setShort("MinSpawnDelay", (short)minSpawnDelay);
        par1NBTTagCompound.setShort("MaxSpawnDelay", (short)maxSpawnDelay);
        par1NBTTagCompound.setShort("SpawnCount", (short)spawnCount);
        par1NBTTagCompound.setShort("MaxNearbyEntities", (short)maxNearbyEntities);
        par1NBTTagCompound.setShort("RequiredPlayerRange", (short)activatingRangeFromPlayer);
        par1NBTTagCompound.setShort("SpawnRange", (short)spawnRange);

        if (getRandomMinecart() != null)
        {
            par1NBTTagCompound.setCompoundTag("SpawnData", (NBTTagCompound)getRandomMinecart().field_98222_b.copy());
        }

        if (getRandomMinecart() != null || minecartToSpawn != null && minecartToSpawn.size() > 0)
        {
            NBTTagList nbttaglist = new NBTTagList();

            if (minecartToSpawn != null && minecartToSpawn.size() > 0)
            {
                WeightedRandomMinecart weightedrandomminecart;

                for (Iterator iterator = minecartToSpawn.iterator(); iterator.hasNext(); nbttaglist.appendTag(weightedrandomminecart.func_98220_a()))
                {
                    weightedrandomminecart = (WeightedRandomMinecart)iterator.next();
                }
            }
            else
            {
                nbttaglist.appendTag(getRandomMinecart().func_98220_a());
            }

            par1NBTTagCompound.setTag("SpawnPotentials", nbttaglist);
        }
    }

    public Entity func_98281_h()
    {
        if (field_98291_j == null)
        {
            Entity entity = EntityList.createEntityByName(getEntityNameToSpawn(), null);
            entity = func_98265_a(entity);
            field_98291_j = entity;
        }

        return field_98291_j;
    }

    /**
     * Sets the delay to minDelay if parameter given is 1, else return false.
     */
    public boolean setDelayToMin(int par1)
    {
        if (par1 == 1 && getSpawnerWorld().isRemote)
        {
            spawnDelay = minSpawnDelay;
            return true;
        }
        else
        {
            return false;
        }
    }

    public WeightedRandomMinecart getRandomMinecart()
    {
        return randomMinecart;
    }

    public void setRandomMinecart(WeightedRandomMinecart par1WeightedRandomMinecart)
    {
        randomMinecart = par1WeightedRandomMinecart;
    }

    public abstract void func_98267_a(int i);

    public abstract World getSpawnerWorld();

    public abstract int getSpawnerX();

    public abstract int getSpawnerY();

    public abstract int getSpawnerZ();
}
