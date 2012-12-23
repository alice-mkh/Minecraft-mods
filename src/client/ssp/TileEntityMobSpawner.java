package net.minecraft.src;

import java.util.*;

public class TileEntityMobSpawner extends TileEntity
{
    /** The stored delay before a new spawn. */
    public int delay;

    /**
     * The string ID of the mobs being spawned from this spawner. Defaults to pig, apparently.
     */
    private String mobID;
    private List field_92060_e;

    /** The extra NBT data to add to spawned entities */
    private TileEntityMobSpawnerSpawnData spawnerTags;
    public double yaw;
    public double yaw2;
    private int minSpawnDelay;
    private int maxSpawnDelay;
    private int spawnCount;
    private Entity spawnedMob;
    private int field_82350_j;
    private int field_82349_r;
    private int field_82348_s;

    public TileEntityMobSpawner()
    {
        delay = -1;
        field_92060_e = null;
        spawnerTags = null;
        yaw2 = 0.0D;
        minSpawnDelay = 200;
        maxSpawnDelay = 800;
        spawnCount = 4;
        field_82350_j = 6;
        field_82349_r = 16;
        field_82348_s = 4;
        mobID = "Pig";
        delay = 20;
    }

    public String getMobID()
    {
        if (spawnerTags == null)
        {
            return mobID;
        }
        else
        {
            return spawnerTags.field_92084_c;
        }
    }

    public void setMobID(String par1Str)
    {
        mobID = par1Str;
    }

    /**
     * Returns true if there is a player in range (using World.getClosestPlayer)
     */
    public boolean anyPlayerInRange()
    {
        return worldObj.getClosestPlayer((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, field_82349_r) != null;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (net.minecraft.client.Minecraft.getMinecraft().enableSP){
            yaw2 = yaw;
        }
        if (!anyPlayerInRange())
        {
            return;
        }

        if (!net.minecraft.client.Minecraft.getMinecraft().enableSP)
        {
            double d = (float)xCoord + worldObj.rand.nextFloat();
            double d1 = (float)yCoord + worldObj.rand.nextFloat();
            double d2 = (float)zCoord + worldObj.rand.nextFloat();
            worldObj.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
            worldObj.spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);

            if (delay > 0)
            {
                delay--;
            }

            yaw2 = yaw;
            yaw = (yaw + (double)(1000F / ((float)delay + 200F))) % 360D;
        }else{
            double d = (float)xCoord + worldObj.rand.nextFloat();
            double d1 = (float)yCoord + worldObj.rand.nextFloat();
            double d2 = (float)zCoord + worldObj.rand.nextFloat();
            worldObj.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
            worldObj.spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);

            for (yaw += 1000F / ((float)delay + 200F); yaw > 360D;)
            {
                yaw -= 360D;
                yaw2 -= 360D;
            }
        }
        if (!worldObj.isRemote)
        {
            if (delay == -1)
            {
                updateDelay();
            }

            if (delay > 0)
            {
                delay--;
                return;
            }

            boolean flag = false;

            for (int i = 0; i < spawnCount; i++)
            {
                Entity entity = EntityList.createEntityByName(getMobID(), worldObj);

                if (entity == null)
                {
                    return;
                }

                int j = worldObj.getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(field_82348_s * 2, 4D, field_82348_s * 2)).size();

                if (j >= field_82350_j)
                {
                    updateDelay();
                    return;
                }

                if (entity == null)
                {
                    continue;
                }

                double d3 = (double)xCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * (double)field_82348_s;
                double d4 = (yCoord + worldObj.rand.nextInt(3)) - 1;
                double d5 = (double)zCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * (double)field_82348_s;
                EntityLiving entityliving = (entity instanceof EntityLiving) ? (EntityLiving)entity : null;
                entity.setLocationAndAngles(d3, d4, d5, worldObj.rand.nextFloat() * 360F, 0.0F);

                if (entityliving != null && !entityliving.getCanSpawnHere())
                {
                    continue;
                }

                writeNBTTagsTo(entity);
                worldObj.spawnEntityInWorld(entity);
                worldObj.playAuxSFX(2004, xCoord, yCoord, zCoord, 0);

                if (entityliving != null)
                {
                    entityliving.spawnExplosionParticle();
                }

                flag = true;
            }

            if (flag)
            {
                updateDelay();
            }
        }

        super.updateEntity();
    }

    public void writeNBTTagsTo(Entity par1Entity)
    {
        if (spawnerTags != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            par1Entity.addEntityID(nbttagcompound);
            NBTBase nbtbase;

            for (Iterator iterator = spawnerTags.field_92083_b.getTags().iterator(); iterator.hasNext(); nbttagcompound.setTag(nbtbase.getName(), nbtbase.copy()))
            {
                nbtbase = (NBTBase)iterator.next();
            }

            par1Entity.readFromNBT(nbttagcompound);
        }
        else if ((par1Entity instanceof EntityLiving) && par1Entity.worldObj != null)
        {
            ((EntityLiving)par1Entity).initCreature();
        }
    }

    /**
     * Sets the delay before a new spawn (base delay of 200 + random number up to 600).
     */
    private void updateDelay()
    {
        if (maxSpawnDelay <= minSpawnDelay)
        {
            delay = minSpawnDelay;
        }
        else
        {
            delay = minSpawnDelay + worldObj.rand.nextInt(maxSpawnDelay - minSpawnDelay);
        }

        if (field_92060_e != null && field_92060_e.size() > 0)
        {
            spawnerTags = (TileEntityMobSpawnerSpawnData)WeightedRandom.getRandomItem(worldObj.rand, field_92060_e);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType().blockID, 1, 0);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        mobID = par1NBTTagCompound.getString("EntityId");
        delay = par1NBTTagCompound.getShort("Delay");

        if (par1NBTTagCompound.hasKey("SpawnPotentials"))
        {
            field_92060_e = new ArrayList();
            NBTTagList nbttaglist = par1NBTTagCompound.getTagList("SpawnPotentials");

            for (int i = 0; i < nbttaglist.tagCount(); i++)
            {
                field_92060_e.add(new TileEntityMobSpawnerSpawnData(this, (NBTTagCompound)nbttaglist.tagAt(i)));
            }
        }
        else
        {
            field_92060_e = null;
        }

        if (par1NBTTagCompound.hasKey("SpawnData"))
        {
            spawnerTags = new TileEntityMobSpawnerSpawnData(this, par1NBTTagCompound.getCompoundTag("SpawnData"), mobID);
        }
        else
        {
            spawnerTags = null;
        }

        if (par1NBTTagCompound.hasKey("MinSpawnDelay"))
        {
            minSpawnDelay = par1NBTTagCompound.getShort("MinSpawnDelay");
            maxSpawnDelay = par1NBTTagCompound.getShort("MaxSpawnDelay");
            spawnCount = par1NBTTagCompound.getShort("SpawnCount");
        }

        if (par1NBTTagCompound.hasKey("MaxNearbyEntities"))
        {
            field_82350_j = par1NBTTagCompound.getShort("MaxNearbyEntities");
            field_82349_r = par1NBTTagCompound.getShort("RequiredPlayerRange");
        }

        if (par1NBTTagCompound.hasKey("SpawnRange"))
        {
            field_82348_s = par1NBTTagCompound.getShort("SpawnRange");
        }

        if (worldObj != null && worldObj.isRemote)
        {
            spawnedMob = null;
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("EntityId", getMobID());
        par1NBTTagCompound.setShort("Delay", (short)delay);
        par1NBTTagCompound.setShort("MinSpawnDelay", (short)minSpawnDelay);
        par1NBTTagCompound.setShort("MaxSpawnDelay", (short)maxSpawnDelay);
        par1NBTTagCompound.setShort("SpawnCount", (short)spawnCount);
        par1NBTTagCompound.setShort("MaxNearbyEntities", (short)field_82350_j);
        par1NBTTagCompound.setShort("RequiredPlayerRange", (short)field_82349_r);
        par1NBTTagCompound.setShort("SpawnRange", (short)field_82348_s);

        if (spawnerTags != null)
        {
            par1NBTTagCompound.setCompoundTag("SpawnData", (NBTTagCompound)spawnerTags.field_92083_b.copy());
        }

        if (spawnerTags != null || field_92060_e != null && field_92060_e.size() > 0)
        {
            NBTTagList nbttaglist = new NBTTagList();

            if (field_92060_e != null && field_92060_e.size() > 0)
            {
                TileEntityMobSpawnerSpawnData tileentitymobspawnerspawndata;

                for (Iterator iterator = field_92060_e.iterator(); iterator.hasNext(); nbttaglist.appendTag(tileentitymobspawnerspawndata.func_92081_a()))
                {
                    tileentitymobspawnerspawndata = (TileEntityMobSpawnerSpawnData)iterator.next();
                }
            }
            else
            {
                nbttaglist.appendTag(spawnerTags.func_92081_a());
            }

            par1NBTTagCompound.setTag("SpawnPotentials", nbttaglist);
        }
    }

    /**
     * will create the entity from the internalID the first time it is accessed
     */
    public Entity getMobEntity()
    {
        if (spawnedMob == null)
        {
            Entity entity = EntityList.createEntityByName(getMobID(), null);
            writeNBTTagsTo(entity);
            spawnedMob = entity;
        }

        return spawnedMob;
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        nbttagcompound.removeTag("SpawnPotentials");
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbttagcompound);
    }

    /**
     * Called when a client event is received with the event number and argument, see World.sendClientEvent
     */
    public void receiveClientEvent(int par1, int par2)
    {
        if (par1 == 1 && worldObj.isRemote)
        {
            delay = minSpawnDelay;
        }
    }
}
