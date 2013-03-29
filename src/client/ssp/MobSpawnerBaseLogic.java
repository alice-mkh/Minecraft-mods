package net.minecraft.src;

import java.util.*;

public abstract class MobSpawnerBaseLogic
{
    public int field_98286_b;
    private String mobID;
    private List field_98285_e;
    private WeightedRandomMinecart field_98282_f;
    public double field_98287_c;
    public double field_98284_d;
    private int field_98283_g;
    private int field_98293_h;
    private int field_98294_i;
    private Entity field_98291_j;
    private int field_98292_k;
    private int field_98289_l;
    private int field_98290_m;

    public MobSpawnerBaseLogic()
    {
        field_98286_b = 20;
        mobID = "Pig";
        field_98285_e = null;
        field_98282_f = null;
        field_98284_d = 0.0D;
        field_98283_g = 200;
        field_98293_h = 800;
        field_98294_i = 4;
        field_98292_k = 6;
        field_98289_l = 16;
        field_98290_m = 4;
    }

    public String func_98276_e()
    {
        if (func_98269_i() == null)
        {
            if (mobID.equals("Minecart"))
            {
                mobID = "MinecartRideable";
            }

            return mobID;
        }
        else
        {
            return func_98269_i().field_98223_c;
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
        return getSpawnerWorld().getClosestPlayer((double)getSpawnerX() + 0.5D, (double)getSpawnerY() + 0.5D, (double)getSpawnerZ() + 0.5D, field_98289_l) != null;
    }

    public void updateSpawner()
    {
        if (net.minecraft.client.Minecraft.getMinecraft().enableSP){
            field_98284_d = field_98287_c;
        }
        if (!canRun())
        {
            return;
        }

        if (!net.minecraft.client.Minecraft.getMinecraft().enableSP)
        {
            double d = (float)getSpawnerX() + getSpawnerWorld().rand.nextFloat();
            double d1 = (float)getSpawnerY() + getSpawnerWorld().rand.nextFloat();
            double d2 = (float)getSpawnerZ() + getSpawnerWorld().rand.nextFloat();
            getSpawnerWorld().spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
            getSpawnerWorld().spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);

            if (field_98286_b > 0)
            {
                field_98286_b--;
            }

            field_98284_d = field_98287_c;
            field_98287_c = (field_98287_c + (double)(1000F / ((float)field_98286_b + 200F))) % 360D;
        }else{
            double d = (float)getSpawnerX() + getSpawnerWorld().rand.nextFloat();
            double d1 = (float)getSpawnerY() + getSpawnerWorld().rand.nextFloat();
            double d2 = (float)getSpawnerZ() + getSpawnerWorld().rand.nextFloat();
            getSpawnerWorld().spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
            getSpawnerWorld().spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);
            for (field_98287_c += 1000F / ((float)field_98286_b + 200F); field_98287_c > 360D;)
            {
                field_98287_c -= 360D;
                field_98284_d -= 360D;
            }
        }
        if (!getSpawnerWorld().isRemote)
        {
            if (field_98286_b == -1)
            {
                func_98273_j();
            }

            if (field_98286_b > 0)
            {
                field_98286_b--;
                return;
            }

            boolean flag = false;

            for (int i = 0; i < field_98294_i; i++)
            {
                Entity entity = EntityList.createEntityByName(func_98276_e(), getSpawnerWorld());

                if (entity == null)
                {
                    return;
                }

                int j = getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getAABBPool().getAABB(getSpawnerX(), getSpawnerY(), getSpawnerZ(), getSpawnerX() + 1, getSpawnerY() + 1, getSpawnerZ() + 1).expand(field_98290_m * 2, 4D, field_98290_m * 2)).size();

                if (j >= field_98292_k)
                {
                    func_98273_j();
                    return;
                }

                double d3 = (double)getSpawnerX() + (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand.nextDouble()) * (double)field_98290_m;
                double d4 = (getSpawnerY() + getSpawnerWorld().rand.nextInt(3)) - 1;
                double d5 = (double)getSpawnerZ() + (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand.nextDouble()) * (double)field_98290_m;
                EntityLiving entityliving = (entity instanceof EntityLiving) ? (EntityLiving)entity : null;
                entity.setLocationAndAngles(d3, d4, d5, getSpawnerWorld().rand.nextFloat() * 360F, 0.0F);

                if (entityliving != null && !entityliving.getCanSpawnHere())
                {
                    continue;
                }

                entity = func_98265_a(entity);
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
        if (func_98269_i() != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            par1Entity.addEntityID(nbttagcompound);
            NBTBase nbtbase;

            for (Iterator iterator = func_98269_i().field_98222_b.getTags().iterator(); iterator.hasNext(); nbttagcompound.setTag(nbtbase.getName(), nbtbase.copy()))
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
                Entity entity1 = EntityList.createEntityByName(nbttagcompound1.getString("id"), getSpawnerWorld());

                if (entity1 != null)
                {
                    NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                    entity1.addEntityID(nbttagcompound2);
                    NBTBase nbtbase1;

                    for (Iterator iterator1 = nbttagcompound1.getTags().iterator(); iterator1.hasNext(); nbttagcompound2.setTag(nbtbase1.getName(), nbtbase1.copy()))
                    {
                        nbtbase1 = (NBTBase)iterator1.next();
                    }

                    entity1.readFromNBT(nbttagcompound2);
                    entity1.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                    getSpawnerWorld().spawnEntityInWorld(entity1);
                    entity.mountEntity(entity1);
                }

                entity = entity1;
            }
        }
        else if ((par1Entity instanceof EntityLiving) && par1Entity.worldObj != null)
        {
            ((EntityLiving)par1Entity).initCreature();
            getSpawnerWorld().spawnEntityInWorld(par1Entity);
        }

        return par1Entity;
    }

    private void func_98273_j()
    {
        if (field_98293_h <= field_98283_g)
        {
            field_98286_b = field_98283_g;
        }
        else
        {
            field_98286_b = field_98283_g + getSpawnerWorld().rand.nextInt(field_98293_h - field_98283_g);
        }

        if (field_98285_e != null && field_98285_e.size() > 0)
        {
            func_98277_a((WeightedRandomMinecart)WeightedRandom.getRandomItem(getSpawnerWorld().rand, field_98285_e));
        }

        func_98267_a(1);
    }

    public void func_98270_a(NBTTagCompound par1NBTTagCompound)
    {
        mobID = par1NBTTagCompound.getString("EntityId");
        field_98286_b = par1NBTTagCompound.getShort("Delay");

        if (par1NBTTagCompound.hasKey("SpawnPotentials"))
        {
            field_98285_e = new ArrayList();
            NBTTagList nbttaglist = par1NBTTagCompound.getTagList("SpawnPotentials");

            for (int i = 0; i < nbttaglist.tagCount(); i++)
            {
                field_98285_e.add(new WeightedRandomMinecart(this, (NBTTagCompound)nbttaglist.tagAt(i)));
            }
        }
        else
        {
            field_98285_e = null;
        }

        if (par1NBTTagCompound.hasKey("SpawnData"))
        {
            func_98277_a(new WeightedRandomMinecart(this, par1NBTTagCompound.getCompoundTag("SpawnData"), mobID));
        }
        else
        {
            func_98277_a(null);
        }

        if (par1NBTTagCompound.hasKey("MinSpawnDelay"))
        {
            field_98283_g = par1NBTTagCompound.getShort("MinSpawnDelay");
            field_98293_h = par1NBTTagCompound.getShort("MaxSpawnDelay");
            field_98294_i = par1NBTTagCompound.getShort("SpawnCount");
        }

        if (par1NBTTagCompound.hasKey("MaxNearbyEntities"))
        {
            field_98292_k = par1NBTTagCompound.getShort("MaxNearbyEntities");
            field_98289_l = par1NBTTagCompound.getShort("RequiredPlayerRange");
        }

        if (par1NBTTagCompound.hasKey("SpawnRange"))
        {
            field_98290_m = par1NBTTagCompound.getShort("SpawnRange");
        }

        if (getSpawnerWorld() != null && getSpawnerWorld().isRemote)
        {
            field_98291_j = null;
        }
    }

    public void func_98280_b(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setString("EntityId", func_98276_e());
        par1NBTTagCompound.setShort("Delay", (short)field_98286_b);
        par1NBTTagCompound.setShort("MinSpawnDelay", (short)field_98283_g);
        par1NBTTagCompound.setShort("MaxSpawnDelay", (short)field_98293_h);
        par1NBTTagCompound.setShort("SpawnCount", (short)field_98294_i);
        par1NBTTagCompound.setShort("MaxNearbyEntities", (short)field_98292_k);
        par1NBTTagCompound.setShort("RequiredPlayerRange", (short)field_98289_l);
        par1NBTTagCompound.setShort("SpawnRange", (short)field_98290_m);

        if (func_98269_i() != null)
        {
            par1NBTTagCompound.setCompoundTag("SpawnData", (NBTTagCompound)func_98269_i().field_98222_b.copy());
        }

        if (func_98269_i() != null || field_98285_e != null && field_98285_e.size() > 0)
        {
            NBTTagList nbttaglist = new NBTTagList();

            if (field_98285_e != null && field_98285_e.size() > 0)
            {
                WeightedRandomMinecart weightedrandomminecart;

                for (Iterator iterator = field_98285_e.iterator(); iterator.hasNext(); nbttaglist.appendTag(weightedrandomminecart.func_98220_a()))
                {
                    weightedrandomminecart = (WeightedRandomMinecart)iterator.next();
                }
            }
            else
            {
                nbttaglist.appendTag(func_98269_i().func_98220_a());
            }

            par1NBTTagCompound.setTag("SpawnPotentials", nbttaglist);
        }
    }

    public Entity func_98281_h()
    {
        if (field_98291_j == null)
        {
            Entity entity = EntityList.createEntityByName(func_98276_e(), null);
            entity = func_98265_a(entity);
            field_98291_j = entity;
        }

        return field_98291_j;
    }

    public boolean func_98268_b(int par1)
    {
        if (par1 == 1 && getSpawnerWorld().isRemote)
        {
            field_98286_b = field_98283_g;
            return true;
        }
        else
        {
            return false;
        }
    }

    public WeightedRandomMinecart func_98269_i()
    {
        return field_98282_f;
    }

    public void func_98277_a(WeightedRandomMinecart par1WeightedRandomMinecart)
    {
        field_98282_f = par1WeightedRandomMinecart;
    }

    public abstract void func_98267_a(int i);

    public abstract World getSpawnerWorld();

    public abstract int getSpawnerX();

    public abstract int getSpawnerY();

    public abstract int getSpawnerZ();
}
