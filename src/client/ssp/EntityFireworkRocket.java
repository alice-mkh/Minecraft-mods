package net.minecraft.src;

import java.util.Random;

public class EntityFireworkRocket extends Entity
{
    /** The age of the firework in ticks. */
    private int fireworkAge;

    /**
     * The lifetime of the firework in ticks. When the age reaches the lifetime the firework explodes.
     */
    private int lifetime;

    public EntityFireworkRocket(World par1World)
    {
        super(par1World);
        setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
        dataWatcher.addObjectByDataType(8, 5);
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double par1)
    {
        return par1 < 4096D;
    }

    public EntityFireworkRocket(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack)
    {
        super(par1World);
        fireworkAge = 0;
        setSize(0.25F, 0.25F);
        setPosition(par2, par4, par6);
        yOffset = 0.0F;
        int i = 1;

        if (par8ItemStack != null && par8ItemStack.hasTagCompound())
        {
            dataWatcher.updateObject(8, par8ItemStack);
            NBTTagCompound nbttagcompound = par8ItemStack.getTagCompound();
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");

            if (nbttagcompound1 != null)
            {
                i += nbttagcompound1.getByte("Flight");
            }
        }

        motionX = rand.nextGaussian() * 0.001D;
        motionZ = rand.nextGaussian() * 0.001D;
        motionY = 0.050000000000000003D;
        lifetime = 10 * i + rand.nextInt(6) + rand.nextInt(7);
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
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        super.onUpdate();
        motionX *= 1.1499999999999999D;
        motionZ *= 1.1499999999999999D;
        motionY += 0.040000000000000001D;
        moveEntity(motionX, motionY, motionZ);
        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);

        for (rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / Math.PI); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }

        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }

        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }

        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;

        if (fireworkAge == 0)
        {
            worldObj.playSoundAtEntity(this, "fireworks.launch", 3F, 1.0F);
        }

        fireworkAge++;

        if ((worldObj.isRemote || Minecraft.getMinecraft().enableSP) && fireworkAge % 2 < 2)
        {
            worldObj.spawnParticle("fireworksSpark", posX, posY - 0.29999999999999999D, posZ, rand.nextGaussian() * 0.050000000000000003D, -motionY * 0.5D, rand.nextGaussian() * 0.050000000000000003D);
        }

        if (!worldObj.isRemote && fireworkAge > lifetime)
        {
            if (Minecraft.getMinecraft().enableSP){
                handleHealthUpdate((byte)17);
            }else{
                worldObj.setEntityState(this, (byte)17);
            }
            setDead();
        }
    }

    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 17 && (worldObj.isRemote || Minecraft.getMinecraft().enableSP))
        {
            ItemStack itemstack = dataWatcher.getWatchableObjectItemStack(8);
            NBTTagCompound nbttagcompound = null;

            if (itemstack != null && itemstack.hasTagCompound())
            {
                nbttagcompound = itemstack.getTagCompound().getCompoundTag("Fireworks");
            }

            worldObj.func_92088_a(posX, posY, posZ, motionX, motionY, motionZ, nbttagcompound);
        }

        super.handleHealthUpdate(par1);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("Life", fireworkAge);
        par1NBTTagCompound.setInteger("LifeTime", lifetime);
        ItemStack itemstack = dataWatcher.getWatchableObjectItemStack(8);

        if (itemstack != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            itemstack.writeToNBT(nbttagcompound);
            par1NBTTagCompound.setCompoundTag("FireworksItem", nbttagcompound);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        fireworkAge = par1NBTTagCompound.getInteger("Life");
        lifetime = par1NBTTagCompound.getInteger("LifeTime");
        NBTTagCompound nbttagcompound = par1NBTTagCompound.getCompoundTag("FireworksItem");

        if (nbttagcompound != null)
        {
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack != null)
            {
                dataWatcher.updateObject(8, itemstack);
            }
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float par1)
    {
        return super.getBrightness(par1);
    }

    public int getBrightnessForRender(float par1)
    {
        return super.getBrightnessForRender(par1);
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }
}
