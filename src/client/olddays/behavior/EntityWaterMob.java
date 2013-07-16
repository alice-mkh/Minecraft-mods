package net.minecraft.src;

import java.util.Random;

public abstract class EntityWaterMob extends EntityCreature implements IAnimals
{
    public static boolean squidsNeedWater = true;

    public EntityWaterMob(World par1World)
    {
        super(par1World);
    }

    public boolean canBreatheUnderwater()
    {
        return true;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return worldObj.checkNoEntityCollision(boundingBox);
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
        return true;
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer par1EntityPlayer)
    {
        return 1 + worldObj.rand.nextInt(3);
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        int i = getAir();
        super.onEntityUpdate();

        if (isEntityAlive() && !isInWater() && squidsNeedWater)
        {
            setAir(--i);

            if (getAir() == -20)
            {
                setAir(0);
                attackEntityFrom(DamageSource.drown, 2.0F);
            }
        }
        else
        {
            setAir(300);
        }
    }
}
