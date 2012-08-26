package net.minecraft.src;

import java.util.*;

public class EntityBoat extends Entity
{
    public static boolean waterlift = false;

    private boolean field_70279_a;
    private double field_70276_b;
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
        field_70279_a = true;
        field_70276_b = 0.070000000000000007D;
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
        return (double)height * 0.0D - 0.30000001192092896D;
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

        if ((par1DamageSource.getEntity() instanceof EntityPlayer) && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode)
        {
            setDamageTaken(100);
        }

        if (getDamageTaken() > 40)
        {
            if (riddenByEntity != null)
            {
                riddenByEntity.mountEntity(this);
            }

            dropItemWithOffset(Item.boat.shiftedIndex, 1, 0.0F);
            setDead();
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
        if (field_70279_a)
        {
            boatPosRotationIncrements = par9 + 5;
        }
        else
        {
            double d = par1 - posX;
            double d1 = par3 - posY;
            double d2 = par5 - posZ;
            double d3 = d * d + d1 * d1 + d2 * d2;

            if (d3 > 1.0D)
            {
                boatPosRotationIncrements = 3;
            }
            else
            {
                return;
            }
        }

        boatX = par1;
        boatY = par3;
        boatZ = par5;
        boatYaw = par7;
        boatPitch = par8;
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
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(boundingBox.minX, d2, boundingBox.minZ, boundingBox.maxX, d8, boundingBox.maxZ);

            if (worldObj.isAABBInMaterial(axisalignedbb, Material.water))
            {
                d += 1.0D / (double)i;
            }
        }

        double d1 = Math.sqrt(motionX * motionX + motionZ * motionZ);

        if (d1 > 0.26249999999999996D)
        {
            double d3 = Math.cos(((double)rotationYaw * Math.PI) / 180D);
            double d9 = Math.sin(((double)rotationYaw * Math.PI) / 180D);

            for (int i1 = 0; (double)i1 < 1.0D + d1 * 60D; i1++)
            {
                double d17 = rand.nextFloat() * 2.0F - 1.0F;
                double d20 = (double)(rand.nextInt(2) * 2 - 1) * 0.69999999999999996D;

                if (rand.nextBoolean())
                {
                    double d22 = (posX - d3 * d17 * 0.80000000000000004D) + d9 * d20;
                    double d24 = posZ - d9 * d17 * 0.80000000000000004D - d3 * d20;
                    worldObj.spawnParticle("splash", d22, posY - 0.125D, d24, motionX, motionY, motionZ);
                }
                else
                {
                    double d23 = posX + d3 + d9 * d17 * 0.69999999999999996D;
                    double d25 = (posZ + d9) - d3 * d17 * 0.69999999999999996D;
                    worldObj.spawnParticle("splash", d23, posY - 0.125D, d25, motionX, motionY, motionZ);
                }
            }
        }

        if (worldObj.isRemote && field_70279_a)
        {
            if (boatPosRotationIncrements > 0)
            {
                double d4 = posX + (boatX - posX) / (double)boatPosRotationIncrements;
                double d10 = posY + (boatY - posY) / (double)boatPosRotationIncrements;
                double d14 = posZ + (boatZ - posZ) / (double)boatPosRotationIncrements;
                double d18 = MathHelper.wrapAngleTo180_double(boatYaw - (double)rotationYaw);
                rotationYaw += d18 / (double)boatPosRotationIncrements;
                rotationPitch += (boatPitch - (double)rotationPitch) / (double)boatPosRotationIncrements;
                boatPosRotationIncrements--;
                setPosition(d4, d10, d14);
                setRotation(rotationYaw, rotationPitch);
            }
            else
            {
                double d5 = posX + motionX;
                double d11 = posY + motionY;
                double d15 = posZ + motionZ;
                setPosition(d5, d11, d15);

                if (onGround)
                {
                    motionX *= 0.5D;
                    motionY *= 0.5D;
                    motionZ *= 0.5D;
                }

                motionX *= 0.99000000953674316D;
                motionY *= 0.94999998807907104D;
                motionZ *= 0.99000000953674316D;
            }

            return;
        }

        if (waterlift){
            double d6 = d * 2D - 1.0D;
            motionY += 0.039999999105930328D * d6;
        }else{
            if (d < 1.0D)
            {
                double d6 = d * 2D - 1.0D;
                motionY += 0.039999999105930328D * d6;
            }
            else
            {
                if (motionY < 0.0D)
                {
                    motionY /= 2D;
                }

                motionY += 0.0070000002160668373D;
            }
        }

        if (riddenByEntity != null)
        {
            motionX += riddenByEntity.motionX * field_70276_b;
            motionZ += riddenByEntity.motionZ * field_70276_b;
        }

        double d7 = Math.sqrt(motionX * motionX + motionZ * motionZ);

        if (d7 > 0.34999999999999998D)
        {
            double d12 = 0.34999999999999998D / d7;
            motionX *= d12;
            motionZ *= d12;
            d7 = 0.34999999999999998D;
        }

        if (d7 > d1 && field_70276_b < 0.34999999999999998D)
        {
            field_70276_b += (0.34999999999999998D - field_70276_b) / 35D;

            if (field_70276_b > 0.34999999999999998D)
            {
                field_70276_b = 0.34999999999999998D;
            }
        }
        else
        {
            field_70276_b -= (field_70276_b - 0.070000000000000007D) / 35D;

            if (field_70276_b < 0.070000000000000007D)
            {
                field_70276_b = 0.070000000000000007D;
            }
        }

        if (onGround)
        {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }

        moveEntity(motionX, motionY, motionZ);

        if (isCollidedHorizontally && d1 > 0.20000000000000001D)
        {
            if (!worldObj.isRemote)
            {
                setDead();

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
            motionX *= 0.99000000953674316D;
            motionY *= 0.94999998807907104D;
            motionZ *= 0.99000000953674316D;
        }

        rotationPitch = 0.0F;
        double d13 = rotationYaw;
        double d16 = prevPosX - posX;
        double d19 = prevPosZ - posZ;

        if (d16 * d16 + d19 * d19 > 0.001D)
        {
            d13 = (float)((Math.atan2(d19, d16) * 180D) / Math.PI);
        }

        double d21 = MathHelper.wrapAngleTo180_double(d13 - (double)rotationYaw);

        if (d21 > 20D)
        {
            d21 = 20D;
        }

        if (d21 < -20D)
        {
            d21 = -20D;
        }

        rotationYaw += d21;
        setRotation(rotationYaw, rotationPitch);

        if (worldObj.isRemote)
        {
            return;
        }

        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && !list.isEmpty())
        {
            Iterator iterator = list.iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                Entity entity = (Entity)iterator.next();

                if (entity != riddenByEntity && entity.canBePushed() && (entity instanceof EntityBoat))
                {
                    entity.applyEntityCollision(this);
                }
            }
            while (true);
        }

        for (int j1 = 0; j1 < 4; j1++)
        {
            int k1 = MathHelper.floor_double(posX + ((double)(j1 % 2) - 0.5D) * 0.80000000000000004D);
            int l1 = MathHelper.floor_double(posZ + ((double)(j1 / 2) - 0.5D) * 0.80000000000000004D);

            for (int i2 = 0; i2 < 2; i2++)
            {
                int j2 = MathHelper.floor_double(posY) + i2;
                int k2 = worldObj.getBlockId(k1, j2, l1);
                int l2 = worldObj.getBlockMetadata(k1, j2, l1);

                if (k2 == Block.snow.blockID)
                {
                    worldObj.setBlockWithNotify(k1, j2, l1, 0);
                    continue;
                }

                if (k2 == Block.waterlily.blockID)
                {
                    Block.waterlily.dropBlockAsItemWithChance(worldObj, k1, j2, l1, l2, 0.3F, 0);
                    worldObj.setBlockWithNotify(k1, j2, l1, 0);
                }
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
            double d = Math.cos(((double)rotationYaw * Math.PI) / 180D) * 0.40000000000000002D;
            double d1 = Math.sin(((double)rotationYaw * Math.PI) / 180D) * 0.40000000000000002D;
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

    public void func_70270_d(boolean par1)
    {
        field_70279_a = par1;
    }
}
