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
        motionY = 0.20000000298023224D;
        motionZ = -(float)Math.cos(f) * 0.02F;
        fuse = 80;
        prevPosX = par2;
        prevPosY = par4;
        prevPosZ = par6;
    }

    protected void entityInit()
    {
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
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
        motionY -= 0.039999999105930328D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.98000001907348633D;
        motionY *= 0.98000001907348633D;
        motionZ *= 0.98000001907348633D;

        if (onGround)
        {
            motionX *= 0.69999998807907104D;
            motionZ *= 0.69999998807907104D;
            motionY *= -0.5D;
        }

        if (fuse-- <= 0)
        {
            if (!worldObj.isRemote)
            {
                setDead();
                explode();
            }
            else
            {
                setDead();
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

    public float getShadowSize()
    {
        return 0.0F;
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i){
        if (!mod_WTFActions.ExtinguishTNT){
            return false;
        }
        Entity entity = damagesource.getEntity();
        if(worldObj.isRemote || isDead || !(entity instanceof EntityPlayer)){
            return true;
        }
        worldObj.playSoundAtEntity(this, "step.grass", 1.0F, 1.0F);
        setDead();
        dropItem(Block.tnt.blockID, 1);
        return true;
    }
}
