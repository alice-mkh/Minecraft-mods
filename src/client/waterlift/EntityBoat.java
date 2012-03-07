package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityBoat extends Entity
{
    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;

    public EntityBoat(World par1World)
    {
        super(par1World);
        preventEntitySpawning = true;
        setSize(1.5F, 0.6F);
        yOffset = height / 2.0F;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        dataWatcher.addObject(17, new Integer(0));
        dataWatcher.addObject(18, new Integer(1));
        dataWatcher.addObject(19, new Integer(0));
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return par1Entity.boundingBox;
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return true;
    }

    public EntityBoat(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
        setPosition(par2, par4 + (double)yOffset, par6);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = par2;
        prevPosY = par4;
        prevPosZ = par6;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return (double)height * 0.0D - 0.3D;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (worldObj.isRemote || isDead)
        {
            return true;
        }

        setForwardDirection(-getForwardDirection());
        setTimeSinceHit(10);
        setDamageTaken(getDamageTaken() + par2 * 10);
        setBeenAttacked();

        if (getDamageTaken() > 40)
        {
            if (riddenByEntity != null)
            {
                riddenByEntity.mountEntity(this);
            }

            for (int i = 0; i < 3; i++)
            {
                dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
            }

            for (int j = 0; j < 2; j++)
            {
                dropItemWithOffset(Item.stick.shiftedIndex, 1, 0.0F);
            }

            setEntityDead();
        }

        return true;
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
        setForwardDirection(-getForwardDirection());
        setTimeSinceHit(10);
        setDamageTaken(getDamageTaken() * 11);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        boatX = par1;
        boatY = par3;
        boatZ = par5;
        boatYaw = par7;
        boatPitch = par8;
        boatPosRotationIncrements = par9 + 4;
        motionX = velocityX;
        motionY = velocityY;
        motionZ = velocityZ;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double par1, double par3, double par5)
    {
        velocityX = motionX = par1;
        velocityY = motionY = par3;
        velocityZ = motionZ = par5;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (getTimeSinceHit() > 0)
        {
            setTimeSinceHit(getTimeSinceHit() - 1);
        }

        if (getDamageTaken() > 0)
        {
            setDamageTaken(getDamageTaken() - 1);
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        int i = 5;
        double d = 0.0D;

        for (int j = 0; j < i; j++)
        {
            double d2 = (boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(j + 0)) / (double)i) - 0.125D;
            double d8 = (boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(j + 1)) / (double)i) - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBoxFromPool(boundingBox.minX, d2, boundingBox.minZ, boundingBox.maxX, d8, boundingBox.maxZ);

            if (worldObj.isAABBInMaterial(axisalignedbb, Material.water))
            {
                d += 1.0D / (double)i;
            }
        }

        double d1 = Math.sqrt(motionX * motionX + motionZ * motionZ);

        if (d1 > 0.15D)
        {
            double d3 = Math.cos(((double)rotationYaw * Math.PI) / 180D);
            double d9 = Math.sin(((double)rotationYaw * Math.PI) / 180D);

            for (int i1 = 0; (double)i1 < 1.0D + d1 * 60D; i1++)
            {
                double d16 = rand.nextFloat() * 2.0F - 1.0F;
                double d19 = (double)(rand.nextInt(2) * 2 - 1) * 0.7D;

                if (rand.nextBoolean())
                {
                    double d21 = (posX - d3 * d16 * 0.8D) + d9 * d19;
                    double d23 = posZ - d9 * d16 * 0.8D - d3 * d19;
                    worldObj.spawnParticle("splash", d21, posY - 0.125D, d23, motionX, motionY, motionZ);
                }
                else
                {
                    double d22 = posX + d3 + d9 * d16 * 0.7D;
                    double d24 = (posZ + d9) - d3 * d16 * 0.7D;
                    worldObj.spawnParticle("splash", d22, posY - 0.125D, d24, motionX, motionY, motionZ);
                }
            }
        }

        if (worldObj.isRemote)
        {
            if (boatPosRotationIncrements > 0)
            {
                double d4 = posX + (boatX - posX) / (double)boatPosRotationIncrements;
                double d10 = posY + (boatY - posY) / (double)boatPosRotationIncrements;
                double d13 = posZ + (boatZ - posZ) / (double)boatPosRotationIncrements;
                double d17;

                for (d17 = boatYaw - (double)rotationYaw; d17 < -180D; d17 += 360D) { }

                for (; d17 >= 180D; d17 -= 360D) { }

                rotationYaw += d17 / (double)boatPosRotationIncrements;
                rotationPitch += (boatPitch - (double)rotationPitch) / (double)boatPosRotationIncrements;
                boatPosRotationIncrements--;
                setPosition(d4, d10, d13);
                setRotation(rotationYaw, rotationPitch);
            }
            else
            {
                double d5 = posX + motionX;
                double d11 = posY + motionY;
                double d14 = posZ + motionZ;
                setPosition(d5, d11, d14);

                if (onGround)
                {
                    motionX *= 0.5D;
                    motionY *= 0.5D;
                    motionZ *= 0.5D;
                }

                motionX *= 0.99D;
                motionY *= 0.95D;
                motionZ *= 0.99D;
            }

            return;
        }

        double d6 = d * 2D - 1.0D;
        motionY += 0.04D * d6;

        if (riddenByEntity != null)
        {
            motionX += riddenByEntity.motionX * 0.2D;
            motionZ += riddenByEntity.motionZ * 0.2D;
        }

        double d7 = 0.4D;

        if (motionX < -d7)
        {
            motionX = -d7;
        }

        if (motionX > d7)
        {
            motionX = d7;
        }

        if (motionZ < -d7)
        {
            motionZ = -d7;
        }

        if (motionZ > d7)
        {
            motionZ = d7;
        }

        if (onGround)
        {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }

        moveEntity(motionX, motionY, motionZ);

        if (isCollidedHorizontally && d1 > 0.2D)
        {
            if (!worldObj.isRemote)
            {
                setEntityDead();

                for (int k = 0; k < 3; k++)
                {
                    dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
                }

                for (int l = 0; l < 2; l++)
                {
                    dropItemWithOffset(Item.stick.shiftedIndex, 1, 0.0F);
                }
            }
        }
        else
        {
            motionX *= 0.99D;
            motionY *= 0.95D;
            motionZ *= 0.99D;
        }

        rotationPitch = 0.0F;
        double d12 = rotationYaw;
        double d15 = prevPosX - posX;
        double d18 = prevPosZ - posZ;

        if (d15 * d15 + d18 * d18 > 0.001D)
        {
            d12 = (float)((Math.atan2(d18, d15) * 180D) / Math.PI);
        }

        double d20;

        for (d20 = d12 - (double)rotationYaw; d20 >= 180D; d20 -= 360D) { }

        for (; d20 < -180D; d20 += 360D) { }

        if (d20 > 20D)
        {
            d20 = 20D;
        }

        if (d20 < -20D)
        {
            d20 = -20D;
        }

        rotationYaw += d20;
        setRotation(rotationYaw, rotationPitch);
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.2D, 0.0D, 0.2D));

        if (list != null && list.size() > 0)
        {
            for (int j1 = 0; j1 < list.size(); j1++)
            {
                Entity entity = (Entity)list.get(j1);

                if (entity != riddenByEntity && entity.canBePushed() && (entity instanceof EntityBoat))
                {
                    entity.applyEntityCollision(this);
                }
            }
        }

        for (int k1 = 0; k1 < 4; k1++)
        {
            int l1 = MathHelper.floor_double(posX + ((double)(k1 % 2) - 0.5D) * 0.8D);
            int i2 = MathHelper.floor_double(posY);
            int j2 = MathHelper.floor_double(posZ + ((double)(k1 / 2) - 0.5D) * 0.8D);

            if (worldObj.getBlockId(l1, i2, j2) == Block.snow.blockID)
            {
                worldObj.setBlockWithNotify(l1, i2, j2, 0);
            }
        }

        if (riddenByEntity != null && riddenByEntity.isDead)
        {
            riddenByEntity = null;
        }
    }

    public void updateRiderPosition()
    {
        if (riddenByEntity == null)
        {
            return;
        }
        else
        {
            double d = Math.cos(((double)rotationYaw * Math.PI) / 180D) * 0.4D;
            double d1 = Math.sin(((double)rotationYaw * Math.PI) / 180D) * 0.4D;
            riddenByEntity.setPosition(posX + d, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ + d1);
            return;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        if (riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != par1EntityPlayer)
        {
            return true;
        }

        if (!worldObj.isRemote)
        {
            par1EntityPlayer.mountEntity(this);
        }

        return true;
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamageTaken(int par1)
    {
        dataWatcher.updateObject(19, Integer.valueOf(par1));
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public int getDamageTaken()
    {
        return dataWatcher.getWatchableObjectInt(19);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int par1)
    {
        dataWatcher.updateObject(17, Integer.valueOf(par1));
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit()
    {
        return dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setForwardDirection(int par1)
    {
        dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getForwardDirection()
    {
        return dataWatcher.getWatchableObjectInt(18);
    }
}
