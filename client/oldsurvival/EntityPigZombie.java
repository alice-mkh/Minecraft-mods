package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityPigZombie extends EntityZombie
{
    /** Above zero if this PigZombie is Angry. */
    private int angerLevel;

    /** A random delay until this PigZombie next makes a sound. */
    private int randomSoundDelay;

    /** The ItemStack that any PigZombie holds (a gold sword, in fact). */
    private static final ItemStack defaultHeldItem;

    public EntityPigZombie(World par1World)
    {
        super(par1World);
        angerLevel = 0;
        randomSoundDelay = 0;
        texture = "/mob/pigzombie.png";
        moveSpeed = 0.5F;
        attackStrength = 5;
        isImmuneToFire = true;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        moveSpeed = entityToAttack == null ? 0.5F : 0.95F;

        if (randomSoundDelay > 0 && --randomSoundDelay == 0)
        {
            worldObj.playSoundAtEntity(this, "mob.zombiepig.zpigangry", getSoundVolume() * 2.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        super.onUpdate();
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return worldObj.difficultySetting > 0 && worldObj.checkIfAABBIsClear(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.isAnyLiquid(boundingBox);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("Anger", (short)angerLevel);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        angerLevel = par1NBTTagCompound.getShort("Anger");
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        if (angerLevel == 0)
        {
            return null;
        }
        else
        {
            return super.findPlayerToAttack();
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        Entity entity = par1DamageSource.getEntity();

        if (entity instanceof EntityPlayer)
        {
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(32D, 32D, 32D));

            for (int i = 0; i < list.size(); i++)
            {
                Entity entity1 = (Entity)list.get(i);

                if (entity1 instanceof EntityPigZombie)
                {
                    EntityPigZombie entitypigzombie = (EntityPigZombie)entity1;
                    entitypigzombie.becomeAngryAt(entity);
                }
            }

            becomeAngryAt(entity);
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    /**
     * Causes this PigZombie to become angry at the supplied Entity (which will be a player).
     */
//FOR FORGE COMPATIBILITY
//     private void becomeAngryAt(Entity par1Entity)
    public void becomeAngryAt(Entity par1Entity)
    {
        entityToAttack = par1Entity;
        angerLevel = 400 + rand.nextInt(400);
        randomSoundDelay = rand.nextInt(40);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.zombiepig.zpig";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.zombiepig.zpighurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.zombiepig.zpigdeath";
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int i = rand.nextInt(2 + par2);

        if (!mod_OldSurvivalMode.OldDrops){
            for (int j = 0; j < i; j++)
            {
                dropItem(Item.rottenFlesh.shiftedIndex, 1);
            }

            i = rand.nextInt(2 + par2);

            for (int k = 0; k < i; k++)
            {
                dropItem(Item.goldNugget.shiftedIndex, 1);
            }
        }else{
            int k = rand.nextInt(3);
            if (par2 > 0){
                k += rand.nextInt(par2 + 1);
            }
            for (int l = 0; l < k; l++){
                dropItem(Item.porkCooked.shiftedIndex, 1);
            }
        }
    }

    protected void func_48085_j_(int par1)
    {
        if (mod_OldSurvivalMode.DisableRareLoot){
            return;
        }
        if (par1 > 0)
        {
            ItemStack itemstack = new ItemStack(Item.swordGold);
            EnchantmentHelper.func_48441_a(rand, itemstack, 5);
            entityDropItem(itemstack, 0.0F);
        }
        else
        {
            int i = rand.nextInt(3);

            if (i == 0)
            {
                dropItem(Item.ingotGold.shiftedIndex, 1);
            }
            else if (i == 1)
            {
                dropItem(Item.swordGold.shiftedIndex, 1);
            }
            else if (i == 2)
            {
                dropItem(Item.helmetGold.shiftedIndex, 1);
            }
        }
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        if (!mod_OldSurvivalMode.OldDrops){
            return Item.rottenFlesh.shiftedIndex;
        }else{
            return Item.porkCooked.shiftedIndex;
        }
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return defaultHeldItem;
    }

    static
    {
        defaultHeldItem = new ItemStack(Item.swordGold, 1);
    }
}
