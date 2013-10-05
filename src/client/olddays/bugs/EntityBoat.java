package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityBoat extends Entity
{
    public static boolean waterlift = false;
    public static boolean oldbreaking = false;

    private boolean field_70279_a;
    private double speedMultiplier;
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
        speedMultiplier = 0.070000000000000007D;
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
        dataWatcher.addObject(19, new Float(0.0F));
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
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (isEntityInvulnerable())
        {
            return false;
        }

        if (worldObj.isRemote || isDead)
        {
            return true;
        }

        setForwardDirection(-getForwardDirection());
        setTimeSinceHit(10);
        setDamageTaken(getDamageTaken() + par2 * 10F);
        setBeenAttacked();
        boolean flag = (par1DamageSource.getEntity() instanceof EntityPlayer) && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode;

        if (flag || getDamageTaken() > 40F)
        {
            if (riddenByEntity != null)
            {
                riddenByEntity.mountEntity(this);
            }

            if (!flag)
            {
                if (oldbreaking){
                    for (int i = 0; i < 3; i++)
                    {
                        dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
                    }

                    for (int j = 0; j < 2; j++)
                    {
                        dropItemWithOffset(Item.stick.itemID, 1, 0.0F);
                    }
                }else{
                    dropItemWithOffset(Item.boat.itemID, 1, 0.0F);
                }
            }

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
        setDamageTaken(getDamageTaken() * 11F);
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

        if (getDamageTaken() > 0.0F)
        {
            setDamageTaken(getDamageTaken() - 1.0F);
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        int i = 5;
        double d = 0.0D;

        for (int j = 0; j < i; j++)
        {
            double d2 = (boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(j + 0)) / (double)i) - 0.125D;
            double d9 = (boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) * (double)(j + 1)) / (double)i) - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(boundingBox.minX, d2, boundingBox.minZ, boundingBox.maxX, d9, boundingBox.maxZ);

            if (worldObj.isAABBInMaterial(axisalignedbb, Material.water))
            {
                d += 1.0D / (double)i;
            }
        }

        double d1 = Math.sqrt(motionX * motionX + motionZ * motionZ);

        if (d1 > 0.26249999999999996D)
        {
            double d3 = Math.cos(((double)rotationYaw * Math.PI) / 180D);
            double d10 = Math.sin(((double)rotationYaw * Math.PI) / 180D);

            for (int i1 = 0; (double)i1 < 1.0D + d1 * 60D; i1++)
            {
                double d20 = rand.nextFloat() * 2.0F - 1.0F;
                double d23 = (double)(rand.nextInt(2) * 2 - 1) * 0.69999999999999996D;

                if (rand.nextBoolean())
                {
                    double d25 = (posX - d3 * d20 * 0.80000000000000004D) + d10 * d23;
                    double d27 = posZ - d10 * d20 * 0.80000000000000004D - d3 * d23;
                    worldObj.spawnParticle("splash", d25, posY - 0.125D, d27, motionX, motionY, motionZ);
                }
                else
                {
                    double d26 = posX + d3 + d10 * d20 * 0.69999999999999996D;
                    double d28 = (posZ + d10) - d3 * d20 * 0.69999999999999996D;
                    worldObj.spawnParticle("splash", d26, posY - 0.125D, d28, motionX, motionY, motionZ);
                }
            }
        }

        if (worldObj.isRemote && field_70279_a)
        {
            if (boatPosRotationIncrements > 0)
            {
                double d4 = posX + (boatX - posX) / (double)boatPosRotationIncrements;
                double d11 = posY + (boatY - posY) / (double)boatPosRotationIncrements;
                double d16 = posZ + (boatZ - posZ) / (double)boatPosRotationIncrements;
                double d21 = MathHelper.wrapAngleTo180_double(boatYaw - (double)rotationYaw);
                rotationYaw += d21 / (double)boatPosRotationIncrements;
                rotationPitch += (boatPitch - (double)rotationPitch) / (double)boatPosRotationIncrements;
                boatPosRotationIncrements--;
                setPosition(d4, d11, d16);
                setRotation(rotationYaw, rotationPitch);
            }
            else
            {
                double d5 = posX + motionX;
                double d12 = posY + motionY;
                double d17 = posZ + motionZ;
                setPosition(d5, d12, d17);

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

        if (riddenByEntity != null && (riddenByEntity instanceof EntityLivingBase))
        {
            double d7 = ((EntityLivingBase)riddenByEntity).moveForward;

            if (d7 > 0.0D)
            {
                double d13 = -Math.sin((riddenByEntity.rotationYaw * (float)Math.PI) / 180F);
                double d18 = Math.cos((riddenByEntity.rotationYaw * (float)Math.PI) / 180F);
                motionX += d13 * speedMultiplier * 0.05000000074505806D;
                motionZ += d18 * speedMultiplier * 0.05000000074505806D;
            }
        }

        double d8 = Math.sqrt(motionX * motionX + motionZ * motionZ);

        if (d8 > 0.34999999999999998D)
        {
            double d14 = 0.34999999999999998D / d8;
            motionX *= d14;
            motionZ *= d14;
            d8 = 0.34999999999999998D;
        }

        if (d8 > d1 && speedMultiplier < 0.34999999999999998D)
        {
            speedMultiplier += (0.34999999999999998D - speedMultiplier) / 35D;

            if (speedMultiplier > 0.34999999999999998D)
            {
                speedMultiplier = 0.34999999999999998D;
            }
        }
        else
        {
            speedMultiplier -= (speedMultiplier - 0.070000000000000007D) / 35D;

            if (speedMultiplier < 0.070000000000000007D)
            {
                speedMultiplier = 0.070000000000000007D;
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
            if (!worldObj.isRemote && !isDead)
            {
                setDead();

                for (int k = 0; k < 3; k++)
                {
                    dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
                }

                for (int l = 0; l < 2; l++)
                {
                    dropItemWithOffset(Item.stick.itemID, 1, 0.0F);
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
        double d15 = rotationYaw;
        double d19 = prevPosX - posX;
        double d22 = prevPosZ - posZ;

        if (d19 * d19 + d22 * d22 > 0.001D)
        {
            d15 = (float)((Math.atan2(d22, d19) * 180D) / Math.PI);
        }

        double d24 = MathHelper.wrapAngleTo180_double(d15 - (double)rotationYaw);

        if (d24 > 20D)
        {
            d24 = 20D;
        }

        if (d24 < -20D)
        {
            d24 = -20D;
        }

        rotationYaw += d24;
        setRotation(rotationYaw, rotationPitch);

        if (worldObj.isRemote)
        {
            return;
        }

        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && !list.isEmpty())
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
            int l1 = MathHelper.floor_double(posX + ((double)(k1 % 2) - 0.5D) * 0.80000000000000004D);
            int i2 = MathHelper.floor_double(posZ + ((double)(k1 / 2) - 0.5D) * 0.80000000000000004D);

            for (int j2 = 0; j2 < 2; j2++)
            {
                int k2 = MathHelper.floor_double(posY) + j2;
                int l2 = worldObj.getBlockId(l1, k2, i2);

                if (l2 == Block.snow.blockID)
                {
                    worldObj.setBlockToAir(l1, k2, i2);
                    continue;
                }

                if (l2 == Block.waterlily.blockID)
                {
                    worldObj.destroyBlock(l1, k2, i2, true);
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
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer par1EntityPlayer)
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
    public void setDamageTaken(float par1)
    {
        dataWatcher.updateObject(19, Float.valueOf(par1));
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamageTaken()
    {
        return dataWatcher.getWatchableObjectFloat(19);
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
