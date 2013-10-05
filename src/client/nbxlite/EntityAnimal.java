package net.minecraft.src;

import java.util.List;
import java.util.Random;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals
{
    public static boolean despawn = false;

    private int inLove;

    /**
     * This is representation of a counter for reproduction progress. (Note that this is different from the inLove which
     * represent being in Love-Mode)
     */
    private int breeding;
    public boolean breeded = false;

    public EntityAnimal(World par1World)
    {
        super(par1World);
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
        if (getGrowingAge() != 0)
        {
            inLove = 0;
        }

        super.updateAITick();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (getGrowingAge() != 0)
        {
            inLove = 0;
        }

        if (inLove > 0)
        {
            inLove--;
            String s = "heart";

            if (inLove % 10 == 0)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle(s, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
        }
        else
        {
            breeding = 0;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity par1Entity, float par2)
    {
        if (par1Entity instanceof EntityPlayer)
        {
            if (par2 < 3F)
            {
                double d = par1Entity.posX - posX;
                double d1 = par1Entity.posZ - posZ;
                rotationYaw = (float)((Math.atan2(d1, d) * 180D) / Math.PI) - 90F;
                hasAttacked = true;
            }

            EntityPlayer entityplayer = (EntityPlayer)par1Entity;

            if (entityplayer.getCurrentEquippedItem() == null || !isBreedingItem(entityplayer.getCurrentEquippedItem()))
            {
                entityToAttack = null;
            }
        }
        else if (par1Entity instanceof EntityAnimal)
        {
            EntityAnimal entityanimal = (EntityAnimal)par1Entity;

            if (getGrowingAge() > 0 && entityanimal.getGrowingAge() < 0)
            {
                if ((double)par2 < 2.5D)
                {
                    hasAttacked = true;
                }
            }
            else if (inLove > 0 && entityanimal.inLove > 0)
            {
                if (entityanimal.entityToAttack == null)
                {
                    entityanimal.entityToAttack = this;
                }

                if (entityanimal.entityToAttack == this && (double)par2 < 3.5D)
                {
                    entityanimal.inLove++;
                    inLove++;
                    breeding++;

                    if (breeding % 4 == 0)
                    {
                        worldObj.spawnParticle("heart", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, 0.0D, 0.0D, 0.0D);
                    }

                    if (breeding == 60)
                    {
                        procreate((EntityAnimal)par1Entity);
                    }
                }
                else
                {
                    breeding = 0;
                }
            }
            else
            {
                breeding = 0;
                entityToAttack = null;
            }
        }
    }

    /**
     * Creates a baby animal according to the animal type of the target at the actual position and spawns 'love'
     * particles.
     */
    private void procreate(EntityAnimal par1EntityAnimal)
    {
        EntityAgeable entityageable = createChild(par1EntityAnimal);

        if (entityageable != null)
        {
            setGrowingAge(6000);
            par1EntityAnimal.setGrowingAge(6000);
            inLove = 0;
            breeding = 0;
            entityToAttack = null;
            par1EntityAnimal.entityToAttack = null;
            par1EntityAnimal.breeding = 0;
            par1EntityAnimal.inLove = 0;
            breeded = true;
            if (entityageable instanceof EntityAnimal){
                ((EntityAnimal)entityageable).breeded = true;
            }
            par1EntityAnimal.breeded = true;
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);

            for (int i = 0; i < 7; i++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle("heart", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }

            worldObj.spawnEntityInWorld(entityageable);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (isEntityInvulnerable())
        {
            return false;
        }

        fleeingTick = 60;

        if (!isAIEnabled())
        {
            AttributeInstance attributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);

            if (attributeinstance.getModifier(field_110179_h) == null)
            {
                attributeinstance.applyModifier(field_110181_i);
            }
        }

        entityToAttack = null;
        inLove = 0;
        return super.attackEntityFrom(par1DamageSource, par2);
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float getBlockPathWeight(int par1, int par2, int par3)
    {
        if (worldObj.getBlockId(par1, par2 - 1, par3) == Block.grass.blockID)
        {
            return 10F;
        }
        else
        {
            return worldObj.getLightBrightness(par1, par2, par3) - 0.5F;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("InLove", inLove);
        par1NBTTagCompound.setBoolean("Breeded", breeded);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        inLove = par1NBTTagCompound.getInteger("InLove");
        breeded = par1NBTTagCompound.getBoolean("Breeded");
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        if (fleeingTick > 0)
        {
            return null;
        }

        float f = 8F;

        if (inLove > 0)
        {
            List list = worldObj.getEntitiesWithinAABB(getClass(), boundingBox.expand(f, f, f));

            for (int i = 0; i < list.size(); i++)
            {
                EntityAnimal entityanimal = (EntityAnimal)list.get(i);

                if (entityanimal != this && entityanimal.inLove > 0)
                {
                    return entityanimal;
                }
            }
        }
        else if (getGrowingAge() == 0)
        {
            List list1 = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityPlayer.class, boundingBox.expand(f, f, f));

            for (int j = 0; j < list1.size(); j++)
            {
                EntityPlayer entityplayer = (EntityPlayer)list1.get(j);

                if (entityplayer.getCurrentEquippedItem() != null && isBreedingItem(entityplayer.getCurrentEquippedItem()))
                {
                    return entityplayer;
                }
            }
        }
        else if (getGrowingAge() > 0)
        {
            List list2 = worldObj.getEntitiesWithinAABB(getClass(), boundingBox.expand(f, f, f));

            for (int k = 0; k < list2.size(); k++)
            {
                EntityAnimal entityanimal1 = (EntityAnimal)list2.get(k);

                if (entityanimal1 != this && entityanimal1.getGrowingAge() < 0)
                {
                    return entityanimal1;
                }
            }
        }

        return null;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
        return worldObj.getBlockId(i, j - 1, k) == Block.grass.blockID && worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return 120;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        if (this instanceof EntityHorse){
            return despawn && !breeded && !getLeashed() && riddenByEntity == null && ((EntityHorse)this).getOwnerName().isEmpty();
        }
        return despawn && !breeded && !getLeashed();
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer par1EntityPlayer)
    {
        return 1 + worldObj.rand.nextInt(3);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack.itemID == Item.wheat.itemID;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (itemstack != null && isBreedingItem(itemstack) && getGrowingAge() == 0 && inLove <= 0)
        {
            if (!par1EntityPlayer.capabilities.isCreativeMode)
            {
                itemstack.stackSize--;

                if (itemstack.stackSize <= 0)
                {
                    par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                }
            }

            func_110196_bT();
            return true;
        }
        else
        {
            return super.interact(par1EntityPlayer);
        }
    }

    public void func_110196_bT()
    {
        inLove = 600;
        entityToAttack = null;
        worldObj.setEntityState(this, (byte)18);
    }

    /**
     * Returns if the entity is currently in 'love mode'.
     */
    public boolean isInLove()
    {
        return inLove > 0;
    }

    public void resetInLove()
    {
        inLove = 0;
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal par1EntityAnimal)
    {
        if (par1EntityAnimal == this)
        {
            return false;
        }

        if (par1EntityAnimal.getClass() != getClass())
        {
            return false;
        }
        else
        {
            return isInLove() && par1EntityAnimal.isInLove();
        }
    }

    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 18)
        {
            for (int i = 0; i < 7; i++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle("heart", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }
}
