package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityArrow extends Entity implements IProjectile
{
    public static boolean olddamage = false;

    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    private int inData;
    private boolean inGround;

    /** 1 if the player can pick up the arrow */
    public int canBePickedUp;

    /** Seems to be some sort of timer for animating an arrow. */
    public int arrowShake;

    /** The owner of this arrow. */
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage;

    /** The amount of knockback an arrow applies when it hits a mob. */
    private int knockbackStrength;

    public EntityArrow(World par1World)
    {
        super(par1World);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        damage = 2D;
        renderDistanceWeight = 10D;
        setSize(0.5F, 0.5F);
    }

    public EntityArrow(World par1World, double par2, double par4, double par6)
    {
        super(par1World);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        damage = 2D;
        renderDistanceWeight = 10D;
        setSize(0.5F, 0.5F);
        setPosition(par2, par4, par6);
        yOffset = 0.0F;
    }

    public EntityArrow(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par4, float par5)
    {
        super(par1World);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        damage = 2D;
        renderDistanceWeight = 10D;
        shootingEntity = par2EntityLivingBase;

        if (par2EntityLivingBase instanceof EntityPlayer)
        {
            canBePickedUp = 1;
        }

        posY = (par2EntityLivingBase.posY + (double)par2EntityLivingBase.getEyeHeight()) - 0.10000000149011612D;
        double d = par3EntityLivingBase.posX - par2EntityLivingBase.posX;
        double d1 = (par3EntityLivingBase.boundingBox.minY + (double)(par3EntityLivingBase.height / 3F)) - posY;
        double d2 = par3EntityLivingBase.posZ - par2EntityLivingBase.posZ;
        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);

        if (d3 < 9.9999999999999995E-008D)
        {
            return;
        }
        else
        {
            float f = (float)((Math.atan2(d2, d) * 180D) / Math.PI) - 90F;
            float f1 = (float)(-((Math.atan2(d1, d3) * 180D) / Math.PI));
            double d4 = d / d3;
            double d5 = d2 / d3;
            setLocationAndAngles(par2EntityLivingBase.posX + d4, posY, par2EntityLivingBase.posZ + d5, f, f1);
            yOffset = 0.0F;
            float f2 = (float)d3 * 0.2F;
            setThrowableHeading(d, d1 + (double)f2, d2, par4, par5);
            return;
        }
    }

    public EntityArrow(World par1World, EntityLivingBase par2EntityLivingBase, float par3)
    {
        super(par1World);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        damage = 2D;
        renderDistanceWeight = 10D;
        shootingEntity = par2EntityLivingBase;

        if (par2EntityLivingBase instanceof EntityPlayer)
        {
            canBePickedUp = 1;
        }

        setSize(0.5F, 0.5F);
        setLocationAndAngles(par2EntityLivingBase.posX, par2EntityLivingBase.posY + (double)par2EntityLivingBase.getEyeHeight(), par2EntityLivingBase.posZ, par2EntityLivingBase.rotationYaw, par2EntityLivingBase.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionY = -MathHelper.sin((rotationPitch / 180F) * (float)Math.PI);
        setThrowableHeading(motionX, motionY, motionZ, par3 * 1.5F, 1.0F);
    }

    protected void entityInit()
    {
        dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
    {
        float f = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= f;
        par3 /= f;
        par5 /= f;
        par1 += rand.nextGaussian() * (double)(rand.nextBoolean() ? -1 : 1) * 0.0074999998323619366D * (double)par8;
        par3 += rand.nextGaussian() * (double)(rand.nextBoolean() ? -1 : 1) * 0.0074999998323619366D * (double)par8;
        par5 += rand.nextGaussian() * (double)(rand.nextBoolean() ? -1 : 1) * 0.0074999998323619366D * (double)par8;
        par1 *= par7;
        par3 *= par7;
        par5 *= par7;
        motionX = par1;
        motionY = par3;
        motionZ = par5;
        float f1 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(par1, par5) * 180D) / Math.PI);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(par3, f1) * 180D) / Math.PI);
        ticksInGround = 0;
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        setPosition(par1, par3, par5);
        setRotation(par7, par8);
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double par1, double par3, double par5)
    {
        motionX = par1;
        motionY = par3;
        motionZ = par5;

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(par1, par5) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(par3, f) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch;
            prevRotationYaw = rotationYaw;
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            ticksInGround = 0;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / Math.PI);
        }

        int i = worldObj.getBlockId(xTile, yTile, zTile);

        if (i > 0)
        {
            Block.blocksList[i].setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
            AxisAlignedBB axisalignedbb = Block.blocksList[i].getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);

            if (axisalignedbb != null && axisalignedbb.isVecInside(worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ)))
            {
                inGround = true;
            }
        }

        if (arrowShake > 0)
        {
            arrowShake--;
        }

        if (inGround)
        {
            int j = worldObj.getBlockId(xTile, yTile, zTile);
            int k = worldObj.getBlockMetadata(xTile, yTile, zTile);

            if (j != inTile || k != inData)
            {
                inGround = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionY *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                ticksInGround = 0;
                ticksInAir = 0;
                return;
            }

            ticksInGround++;

            if (ticksInGround == 1200)
            {
                setDead();
            }

            return;
        }

        ticksInAir++;
        Vec3 vec3 = worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
        Vec3 vec3_1 = worldObj.getWorldVec3Pool().getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
        MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks_do_do(vec3, vec3_1, false, true);
        vec3 = worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
        vec3_1 = worldObj.getWorldVec3Pool().getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);

        if (movingobjectposition != null)
        {
            vec3_1 = worldObj.getWorldVec3Pool().getVecFromPool(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int l = 0; l < list.size(); l++)
        {
            Entity entity1 = (Entity)list.get(l);

            if (!entity1.canBeCollidedWith() || entity1 == shootingEntity && ticksInAir < 5)
            {
                continue;
            }

            float f5 = 0.3F;
            AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f5, f5, f5);
            MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3, vec3_1);

            if (movingobjectposition1 == null)
            {
                continue;
            }

            double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

            if (d1 < d || d == 0.0D)
            {
                entity = entity1;
                d = d1;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null && movingobjectposition.entityHit != null && (movingobjectposition.entityHit instanceof EntityPlayer))
        {
            EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;

            if (entityplayer.capabilities.disableDamage || (shootingEntity instanceof EntityPlayer) && !((EntityPlayer)shootingEntity).canAttackPlayer(entityplayer))
            {
                movingobjectposition = null;
            }
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.entityHit != null)
            {
                float f1 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                int j1 = MathHelper.ceiling_double_int((double)f1 * damage);

                if (getIsCritical())
                {
                    j1 += rand.nextInt(j1 / 2 + 2);
                }
                if (olddamage){
                    j1 = 4;
                }

                DamageSource damagesource = null;

                if (shootingEntity == null)
                {
                    damagesource = DamageSource.causeArrowDamage(this, this);
                }
                else
                {
                    damagesource = DamageSource.causeArrowDamage(this, shootingEntity);
                }

                if (isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman))
                {
                    movingobjectposition.entityHit.setFire(5);
                }

                if (movingobjectposition.entityHit.attackEntityFrom(damagesource, j1))
                {
                    if (movingobjectposition.entityHit instanceof EntityLivingBase)
                    {
                        EntityLivingBase entitylivingbase = (EntityLivingBase)movingobjectposition.entityHit;

                        if (!worldObj.isRemote)
                        {
                            entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                        }

                        if (knockbackStrength > 0)
                        {
                            float f7 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

                            if (f7 > 0.0F)
                            {
                                movingobjectposition.entityHit.addVelocity((motionX * (double)knockbackStrength * 0.60000002384185791D) / (double)f7, 0.10000000000000001D, (motionZ * (double)knockbackStrength * 0.60000002384185791D) / (double)f7);
                            }
                        }

                        if (shootingEntity != null)
                        {
                            EnchantmentThorns.func_92096_a(shootingEntity, entitylivingbase, rand);
                        }

                        if (shootingEntity != null && movingobjectposition.entityHit != shootingEntity && (movingobjectposition.entityHit instanceof EntityPlayer) && (shootingEntity instanceof EntityPlayerMP))
                        {
                            ((EntityPlayerMP)shootingEntity).playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(6, 0));
                        }
                    }

                    playSound("random.bowhit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));

                    if (!(movingobjectposition.entityHit instanceof EntityEnderman))
                    {
                        setDead();
                    }
                }
                else
                {
                    motionX *= -0.10000000149011612D;
                    motionY *= -0.10000000149011612D;
                    motionZ *= -0.10000000149011612D;
                    rotationYaw += 180F;
                    prevRotationYaw += 180F;
                    ticksInAir = 0;
                }
            }
            else
            {
                xTile = movingobjectposition.blockX;
                yTile = movingobjectposition.blockY;
                zTile = movingobjectposition.blockZ;
                inTile = worldObj.getBlockId(xTile, yTile, zTile);
                inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
                motionX = (float)(movingobjectposition.hitVec.xCoord - posX);
                motionY = (float)(movingobjectposition.hitVec.yCoord - posY);
                motionZ = (float)(movingobjectposition.hitVec.zCoord - posZ);
                float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                posX -= (motionX / (double)f2) * 0.05000000074505806D;
                posY -= (motionY / (double)f2) * 0.05000000074505806D;
                posZ -= (motionZ / (double)f2) * 0.05000000074505806D;
                playSound("random.bowhit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
                inGround = true;
                arrowShake = 7;
                setIsCritical(false);

                if (inTile != 0)
                {
                    Block.blocksList[inTile].onEntityCollidedWithBlock(worldObj, xTile, yTile, zTile, this);
                }
            }
        }

        if (getIsCritical())
        {
            for (int i1 = 0; i1 < 4; i1++)
            {
                worldObj.spawnParticle("crit", posX + (motionX * (double)i1) / 4D, posY + (motionY * (double)i1) / 4D, posZ + (motionZ * (double)i1) / 4D, -motionX, -motionY + 0.20000000000000001D, -motionZ);
            }
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);

        for (rotationPitch = (float)((Math.atan2(motionY, f3) * 180D) / Math.PI); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }

        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }

        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }

        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        float f4 = 0.99F;
        float f6 = 0.05F;

        if (isInWater())
        {
            for (int k1 = 0; k1 < 4; k1++)
            {
                float f8 = 0.25F;
                worldObj.spawnParticle("bubble", posX - motionX * (double)f8, posY - motionY * (double)f8, posZ - motionZ * (double)f8, motionX, motionY, motionZ);
            }

            f4 = 0.8F;
        }

        motionX *= f4;
        motionY *= f4;
        motionZ *= f4;
        motionY -= f6;
        setPosition(posX, posY, posZ);
        doBlockCollisions();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("xTile", (short)xTile);
        par1NBTTagCompound.setShort("yTile", (short)yTile);
        par1NBTTagCompound.setShort("zTile", (short)zTile);
        par1NBTTagCompound.setByte("inTile", (byte)inTile);
        par1NBTTagCompound.setByte("inData", (byte)inData);
        par1NBTTagCompound.setByte("shake", (byte)arrowShake);
        par1NBTTagCompound.setByte("inGround", (byte)(inGround ? 1 : 0));
        par1NBTTagCompound.setByte("pickup", (byte)canBePickedUp);
        par1NBTTagCompound.setDouble("damage", damage);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        xTile = par1NBTTagCompound.getShort("xTile");
        yTile = par1NBTTagCompound.getShort("yTile");
        zTile = par1NBTTagCompound.getShort("zTile");
        inTile = par1NBTTagCompound.getByte("inTile") & 0xff;
        inData = par1NBTTagCompound.getByte("inData") & 0xff;
        arrowShake = par1NBTTagCompound.getByte("shake") & 0xff;
        inGround = par1NBTTagCompound.getByte("inGround") == 1;

        if (par1NBTTagCompound.hasKey("damage"))
        {
            damage = par1NBTTagCompound.getDouble("damage");
        }

        if (par1NBTTagCompound.hasKey("pickup"))
        {
            canBePickedUp = par1NBTTagCompound.getByte("pickup");
        }
        else if (par1NBTTagCompound.hasKey("player"))
        {
            canBePickedUp = par1NBTTagCompound.getBoolean("player") ? 1 : 0;
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (worldObj.isRemote || !inGround || arrowShake > 0)
        {
            return;
        }

        boolean flag = canBePickedUp == 1 || canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;

        if (canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1)))
        {
            flag = false;
        }

        if (flag)
        {
            playSound("random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            par1EntityPlayer.onItemPickup(this, 1);
            setDead();
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public void setDamage(double par1)
    {
        damage = par1;
    }

    public double getDamage()
    {
        return damage;
    }

    /**
     * Sets the amount of knockback the arrow applies when it hits a mob.
     */
    public void setKnockbackStrength(int par1)
    {
        knockbackStrength = par1;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public void setIsCritical(boolean par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 1)));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -2)));
        }
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public boolean getIsCritical()
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        return (byte0 & 1) != 0;
    }
}
