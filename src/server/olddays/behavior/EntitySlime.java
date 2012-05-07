package net.minecraft.src;

import java.util.Random;

public class EntitySlime extends EntityLiving implements IMob
{
    public static boolean allow = true;
    public static int slimeSpawn = 3;

    public float field_40122_a;
    public float field_401_a;
    public float field_400_b;

    /** ticks until this slime jumps again */
    private int slimeJumpDelay;

    public EntitySlime(World par1World)
    {
        super(par1World);
        slimeJumpDelay = 0;
        texture = "/mob/slime.png";
        int i = 1 << rand.nextInt(3);
        yOffset = 0.0F;
        slimeJumpDelay = rand.nextInt(20) + 10;
        setSlimeSize(i);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, new Byte((byte)1));
    }

    public void setSlimeSize(int par1)
    {
        dataWatcher.updateObject(16, new Byte((byte)par1));
        setSize(0.6F * (float)par1, 0.6F * (float)par1);
        setPosition(posX, posY, posZ);
        setEntityHealth(getMaxHealth());
        experienceValue = par1;
    }

    public int getMaxHealth()
    {
        int i = getSlimeSize();
        return i * i;
    }

    /**
     * Returns the size of the slime.
     */
    public int getSlimeSize()
    {
        return dataWatcher.getWatchableObjectByte(16);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Size", getSlimeSize() - 1);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        setSlimeSize(par1NBTTagCompound.getInteger("Size") + 1);
    }

    /**
     * Returns the name of a particle effect that may be randomly created by EntitySlime.onUpdate()
     */
    protected String getSlimeParticle()
    {
        return "slime";
    }

    protected String func_40118_E()
    {
        return "mob.slime";
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (!worldObj.isRemote && worldObj.difficultySetting == 0 && getSlimeSize() > 0)
        {
            isDead = true;
        }

        field_401_a = field_401_a + (field_40122_a - field_401_a) * 0.5F;
        field_400_b = field_401_a;
        boolean flag = onGround;
        super.onUpdate();

        if (onGround && !flag)
        {
            int i = getSlimeSize();

            for (int j = 0; j < i * 8; j++)
            {
                float f = rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = rand.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
                worldObj.spawnParticle(getSlimeParticle(), posX + (double)f2, boundingBox.minY, posZ + (double)f3, 0.0D, 0.0D, 0.0D);
            }

            if (func_40121_G())
            {
                worldObj.playSoundAtEntity(this, func_40118_E(), getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            field_40122_a = -0.5F;
        }

        func_40116_B();
    }

    protected void updateEntityActionState()
    {
        despawnEntity();
        EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 16D);

        if (entityplayer != null)
        {
            faceEntity(entityplayer, 10F, 20F);
        }

        if (onGround && slimeJumpDelay-- <= 0)
        {
            slimeJumpDelay = func_40115_A();

            if (entityplayer != null)
            {
                slimeJumpDelay /= 3;
            }

            isJumping = true;

            if (func_40117_I())
            {
                worldObj.playSoundAtEntity(this, func_40118_E(), getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }

            field_40122_a = 1.0F;
            moveStrafing = 1.0F - rand.nextFloat() * 2.0F;
            moveForward = 1 * getSlimeSize();
        }
        else
        {
            isJumping = false;

            if (onGround)
            {
                moveStrafing = moveForward = 0.0F;
            }
        }
    }

    protected void func_40116_B()
    {
        field_40122_a = field_40122_a * 0.6F;
    }

    protected int func_40115_A()
    {
        return rand.nextInt(20) + 10;
    }

    protected EntitySlime createInstance()
    {
        return new EntitySlime(worldObj);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        int i = getSlimeSize();

        if (!worldObj.isRemote && i > 1 && getHealth() <= 0)
        {
            int j = 2 + rand.nextInt(3);

            for (int k = 0; k < j; k++)
            {
                float f = (((float)(k % 2) - 0.5F) * (float)i) / 4F;
                float f1 = (((float)(k / 2) - 0.5F) * (float)i) / 4F;
                EntitySlime entityslime = createInstance();
                entityslime.setSlimeSize(i / 2);
                entityslime.setLocationAndAngles(posX + (double)f, posY + 0.5D, posZ + (double)f1, rand.nextFloat() * 360F, 0.0F);
                worldObj.spawnEntityInWorld(entityslime);
            }
        }

        super.setDead();
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (func_40119_C())
        {
            int i = getSlimeSize();

            if (canEntityBeSeen(par1EntityPlayer) && (double)getDistanceToEntity(par1EntityPlayer) < 0.59999999999999998D * (double)i && par1EntityPlayer.attackEntityFrom(DamageSource.causeMobDamage(this), func_40113_D()))
            {
                worldObj.playSoundAtEntity(this, "mob.slimeattack", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    protected boolean func_40119_C()
    {
        return getSlimeSize() > 1;
    }

    protected int func_40113_D()
    {
        return getSlimeSize();
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.slime";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.slime";
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        if (getSlimeSize() == 1)
        {
            return Item.slimeBall.shiftedIndex;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        if (!allow || slimeSpawn==0){
            return false;
        }
        if (slimeSpawn==1){
            return posY < 32D;
        }
        if (slimeSpawn==2){
            Chunk chunk = worldObj.getChunkFromBlockCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));
            return (getSlimeSize() == 1 || worldObj.difficultySetting > 0) && rand.nextInt(10) == 0 && chunk.getRandomWithSeed(0x3ad8025fL).nextInt(10) == 0 && posY < 16D;
        }
        Chunk chunk = worldObj.getChunkFromBlockCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));

        if ((getSlimeSize() == 1 || worldObj.difficultySetting > 0) && rand.nextInt(10) == 0 && chunk.getRandomWithSeed(0x3ad8025fL).nextInt(10) == 0 && posY < 40D)
        {
            return super.getCanSpawnHere();
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F * (float)getSlimeSize();
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 0;
    }

    protected boolean func_40117_I()
    {
        return getSlimeSize() > 1;
    }

    protected boolean func_40121_G()
    {
        return getSlimeSize() > 2;
    }
}
