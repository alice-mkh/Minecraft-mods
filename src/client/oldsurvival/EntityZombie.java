package net.minecraft.src;

import java.util.Random;

public class EntityZombie extends EntityMob
{
    public EntityZombie(World par1World)
    {
        super(par1World);
        texture = "/mob/zombie.png";
        moveSpeed = 0.23F;
        attackStrength = 4;
        func_48084_aL().func_48673_b(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIBreakDoor(this));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, net.minecraft.src.EntityPlayer.class, moveSpeed, false));
        tasks.addTask(3, new EntityAIAttackOnCollide(this, net.minecraft.src.EntityVillager.class, moveSpeed, true));
        tasks.addTask(4, new EntityAIMoveTwardsRestriction(this, moveSpeed));
        tasks.addTask(5, new EntityAIMoveThroughVillage(this, moveSpeed, false));
        tasks.addTask(6, new EntityAIWander(this, moveSpeed));
        tasks.addTask(7, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 8F));
        tasks.addTask(7, new EntityAILookIdle(this));
        field_48105_bU.addTask(1, new EntityAIHurtByTarget(this, false));
        field_48105_bU.addTask(2, new EntityAINearestAttackableTarget(this, net.minecraft.src.EntityPlayer.class, 16F, 0, true));
        field_48105_bU.addTask(2, new EntityAINearestAttackableTarget(this, net.minecraft.src.EntityVillager.class, 16F, 0, false));
    }

    public int getMaxHealth()
    {
        return 20;
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue()
    {
        return 2;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (worldObj.isDaytime() && !worldObj.isRemote)
        {
            float f = getEntityBrightness(1.0F);

            if (f > 0.5F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F)
            {
                setFire(8);
            }
        }

        super.onLivingUpdate();
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.zombie";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.zombiehurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.zombiedeath";
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        if (!mod_OldSurvivalMode.OldDrops){
            return Item.rottenFlesh.shiftedIndex;
        }else{
            return Item.feather.shiftedIndex;
        }
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected void func_48085_j_(int par1)
    {
        if (mod_OldSurvivalMode.DisableRareLoot){
            return;
        }
        switch (rand.nextInt(4))
        {
            case 0:
                dropItem(Item.swordSteel.shiftedIndex, 1);
                break;

            case 1:
                dropItem(Item.helmetSteel.shiftedIndex, 1);
                break;

            case 2:
                dropItem(Item.ingotIron.shiftedIndex, 1);
                break;

            case 3:
                dropItem(Item.shovelSteel.shiftedIndex, 1);
                break;
        }
    }
}
