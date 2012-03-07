package net.minecraft.src;

public class EntityTNTPrimed extends Entity
{
    /** How long the fuse is */
    public int fuse;

    public EntityTNTPrimed(World par1World)
    {
        super(par1World);
        fuse = 0;
        preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
        yOffset = height / 2.0F;
    }

    public EntityTNTPrimed(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
        setPosition(par2, par4, par6);
        float f = (float)(Math.random() * Math.PI * 2D);
        motionX = -(float)Math.sin(f) * 0.02F;
        motionY = 0.2D;
        motionZ = -(float)Math.cos(f) * 0.02F;
        fuse = 80;
        prevPosX = par2;
        prevPosY = par4;
        prevPosZ = par6;
    }

    protected void entityInit()
    {
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.04D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.98D;
        motionY *= 0.98D;
        motionZ *= 0.98D;

        if (onGround)
        {
            motionX *= 0.7D;
            motionZ *= 0.7D;
            motionY *= -0.5D;
        }

        if (fuse-- <= 0)
        {
            if (!worldObj.isRemote)
            {
                setEntityDead();
                explode();
            }
            else
            {
                setEntityDead();
            }
        }
        else
        {
            worldObj.spawnParticle("smoke", posX, posY + 0.5D, posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode()
    {
        float f = 4F;
        worldObj.createExplosion(null, posX, posY, posZ, f);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setByte("Fuse", (byte)fuse);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        fuse = par1NBTTagCompound.getByte("Fuse");
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i){
        Entity entity = damagesource.getEntity();
        if(worldObj.isRemote || isDead || !(entity instanceof EntityPlayer)){
            return true;
        }
        worldObj.playSoundAtEntity(this, "step.grass", 1.0F, 1.0F);
        setEntityDead();
        dropItem(Block.tnt.blockID, 1);
        return true;
    }
}
