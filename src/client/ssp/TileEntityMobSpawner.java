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

    /** The extra NBT data to add to spawned entities */
    private NBTTagCompound spawnerTags;
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
        return mobID;
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
            double d3 = (float)zCoord + worldObj.rand.nextFloat();
            worldObj.spawnParticle("smoke", d, d1, d3, 0.0D, 0.0D, 0.0D);
            worldObj.spawnParticle("flame", d, d1, d3, 0.0D, 0.0D, 0.0D);

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

            for (int i = 0; i < spawnCount; i++)
            {
                Entity entity = EntityList.createEntityByName(mobID, worldObj);

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

                double d2 = (double)xCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * (double)field_82348_s;
                double d4 = (yCoord + worldObj.rand.nextInt(3)) - 1;
                double d5 = (double)zCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * (double)field_82348_s;
                EntityLiving entityliving = (entity instanceof EntityLiving) ? (EntityLiving)entity : null;
                entity.setLocationAndAngles(d2, d4, d5, worldObj.rand.nextFloat() * 360F, 0.0F);

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

            for (Iterator iterator = spawnerTags.getTags().iterator(); iterator.hasNext(); nbttagcompound.setTag(nbtbase.getName(), nbtbase.copy()))
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

        if (par1NBTTagCompound.hasKey("SpawnData"))
        {
            spawnerTags = par1NBTTagCompound.getCompoundTag("SpawnData");
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
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("EntityId", mobID);
        par1NBTTagCompound.setShort("Delay", (short)delay);
        par1NBTTagCompound.setShort("MinSpawnDelay", (short)minSpawnDelay);
        par1NBTTagCompound.setShort("MaxSpawnDelay", (short)maxSpawnDelay);
        par1NBTTagCompound.setShort("SpawnCount", (short)spawnCount);
        par1NBTTagCompound.setShort("MaxNearbyEntities", (short)field_82350_j);
        par1NBTTagCompound.setShort("RequiredPlayerRange", (short)field_82349_r);
        par1NBTTagCompound.setShort("SpawnRange", (short)field_82348_s);

        if (spawnerTags != null)
        {
            par1NBTTagCompound.setCompoundTag("SpawnData", spawnerTags);
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
