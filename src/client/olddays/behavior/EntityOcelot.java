package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityOcelot extends EntityTameable
{
    public static boolean allow = true;

    /**
     * The tempt AI task for this mob, used to prevent taming while it is fleeing.
     */
    private EntityAITempt aiTempt;

    public EntityOcelot(World par1World)
    {
        super(par1World);
        texture = "/mob/ozelot.png";
        setSize(0.6F, 0.8F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, aiSit);
        tasks.addTask(3, aiTempt = new EntityAITempt(this, 0.18F, Item.fishRaw.shiftedIndex, true));
        tasks.addTask(4, new EntityAIAvoidEntity(this, net.minecraft.src.EntityPlayer.class, 16F, 0.23F, 0.4F));
        tasks.addTask(5, new EntityAIFollowOwner(this, 0.3F, 10F, 5F));
        tasks.addTask(6, new EntityAIOcelotSit(this, 0.4F));
        tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
        tasks.addTask(8, new EntityAIOcelotAttack(this));
        tasks.addTask(9, new EntityAIMate(this, 0.23F));
        tasks.addTask(10, new EntityAIWander(this, 0.23F));
        tasks.addTask(11, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 10F));
        targetTasks.addTask(1, new EntityAITargetNonTamed(this, net.minecraft.src.EntityChicken.class, 14F, 750, false));
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(18, Byte.valueOf((byte)0));
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    public void updateAITick()
    {
        if (!getMoveHelper().func_48186_a())
        {
            setSneaking(false);
            setSprinting(false);
        }
        else
        {
            float f = getMoveHelper().getSpeed();

            if (f == 0.18F)
            {
                setSneaking(true);
                setSprinting(false);
            }
            else if (f == 0.4F)
            {
                setSneaking(false);
                setSprinting(true);
            }
            else
            {
                setSneaking(false);
                setSprinting(false);
            }
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return !isTamed();
    }

    /**
     * Returns the texture's file path as a String.
     */
    public String getTexture()
    {
        switch (func_48148_ad())
        {
            case 0:
                return "/mob/ozelot.png";

            case 1:
                return "/mob/cat_black.png";

            case 2:
                return "/mob/cat_red.png";

            case 3:
                return "/mob/cat_siamese.png";
        }

        return super.getTexture();
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    public int getMaxHealth()
    {
        return 10;
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float f)
    {
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("CatType", func_48148_ad());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        func_48147_c(par1NBTTagCompound.getInteger("CatType"));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        if (isTamed())
        {
            if (isInLove())
            {
                return "mob.cat.purr";
            }

            if (rand.nextInt(4) == 0)
            {
                return "mob.cat.purreow";
            }
            else
            {
                return "mob.cat.meow";
            }
        }
        else
        {
            return "";
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.cat.hitt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.cat.hitt";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return Item.leather.shiftedIndex;
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        aiSit.func_48407_a(false);
        return super.attackEntityFrom(par1DamageSource, par2);
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean flag, int i)
    {
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (!isTamed())
        {
            if (aiTempt.func_48270_h() && itemstack != null && itemstack.itemID == Item.fishRaw.shiftedIndex && par1EntityPlayer.getDistanceSqToEntity(this) < 9D)
            {
                itemstack.stackSize--;

                if (itemstack.stackSize <= 0)
                {
                    par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                }

                if (!worldObj.isRemote)
                {
                    if (rand.nextInt(3) == 0)
                    {
                        setTamed(true);
                        func_48147_c(1 + worldObj.rand.nextInt(3));
                        setOwner(par1EntityPlayer.username);
                        func_48142_a(true);
                        aiSit.func_48407_a(true);
                        worldObj.setEntityState(this, (byte)7);
                    }
                    else
                    {
                        func_48142_a(false);
                        worldObj.setEntityState(this, (byte)6);
                    }
                }
            }

            return true;
        }

        if (par1EntityPlayer.username.equalsIgnoreCase(getOwnerName()) && !worldObj.isRemote && !isWheat(itemstack))
        {
            aiSit.func_48407_a(!isSitting());
        }

        return super.interact(par1EntityPlayer);
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public EntityAnimal spawnBabyAnimal(EntityAnimal par1EntityAnimal)
    {
        EntityOcelot entityocelot = new EntityOcelot(worldObj);

        if (isTamed())
        {
            entityocelot.setOwner(getOwnerName());
            entityocelot.setTamed(true);
            entityocelot.func_48147_c(func_48148_ad());
        }

        return entityocelot;
    }

    /**
     * Checks if the parameter is an wheat item.
     */
    public boolean isWheat(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && par1ItemStack.itemID == Item.fishRaw.shiftedIndex;
    }

    public boolean func_48135_b(EntityAnimal par1EntityAnimal)
    {
        if (par1EntityAnimal == this)
        {
            return false;
        }

        if (!isTamed())
        {
            return false;
        }

        if (!(par1EntityAnimal instanceof EntityOcelot))
        {
            return false;
        }

        EntityOcelot entityocelot = (EntityOcelot)par1EntityAnimal;

        if (!entityocelot.isTamed())
        {
            return false;
        }
        else
        {
            return isInLove() && entityocelot.isInLove();
        }
    }

    public int func_48148_ad()
    {
        return dataWatcher.getWatchableObjectByte(18);
    }

    public void func_48147_c(int par1)
    {
        dataWatcher.updateObject(18, Byte.valueOf((byte)par1));
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        if (!allow){
            return false;
        }
        if (worldObj.rand.nextInt(3) == 0)
        {
            return false;
        }

        if (worldObj.checkIfAABBIsClear(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.isAnyLiquid(boundingBox))
        {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(boundingBox.minY);
            int k = MathHelper.floor_double(posZ);

            if (j < 63)
            {
                return false;
            }

            int l = worldObj.getBlockId(i, j - 1, k);

            if (l == Block.grass.blockID || l == Block.leaves.blockID)
            {
                return true;
            }
        }

        return false;
    }
}
