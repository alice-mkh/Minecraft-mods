package net.minecraft.src;

import java.util.Random;

public class EntitySlime extends EntityLiving implements IMob
{
    public static int slimeSpawn = 5;

    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;

    /** the time between each jump of the slime */
    private int slimeJumpDelay;

    public EntitySlime(World par1World)
    {
        super(par1World);
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

    protected void setSlimeSize(int par1)
    {
        dataWatcher.updateObject(16, new Byte((byte)par1));
        setSize(0.6F * (float)par1, 0.6F * (float)par1);
        setPosition(posX, posY, posZ);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(par1 * par1);
        setHealth(getMaxHealth());
        experienceValue = par1;
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

    /**
     * Returns the name of the sound played when the slime jumps.
     */
    protected String getJumpSound()
    {
        return (new StringBuilder()).append("mob.slime.").append(getSlimeSize() <= 1 ? "small" : "big").toString();
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

        squishFactor += (squishAmount - squishFactor) * 0.5F;
        prevSquishFactor = squishFactor;
        boolean flag = onGround;
        super.onUpdate();

        if (onGround && !flag)
        {
            int i = getSlimeSize();

            for (int k = 0; k < i * 8; k++)
            {
                float f = rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = rand.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
                worldObj.spawnParticle(getSlimeParticle(), posX + (double)f2, boundingBox.minY, posZ + (double)f3, 0.0D, 0.0D, 0.0D);
            }

            if (makesSoundOnLand())
            {
                playSound(getJumpSound(), getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            squishAmount = -0.5F;
        }
        else if (!onGround && flag)
        {
            squishAmount = 1.0F;
        }

        alterSquishAmount();

        if (worldObj.isRemote)
        {
            int j = getSlimeSize();
            setSize(0.6F * (float)j, 0.6F * (float)j);
        }
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
            slimeJumpDelay = getJumpDelay();

            if (entityplayer != null)
            {
                slimeJumpDelay /= 3;
            }

            isJumping = true;

            if (makesSoundOnJump())
            {
                playSound(getJumpSound(), getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }

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

    protected void alterSquishAmount()
    {
        squishAmount *= 0.6F;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    protected int getJumpDelay()
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

        if (!worldObj.isRemote && i > 1 && getHealth() <= 0.0F)
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
        if (canDamagePlayer())
        {
            int i = getSlimeSize();

            if (canEntityBeSeen(par1EntityPlayer) && getDistanceSqToEntity(par1EntityPlayer) < 0.59999999999999998D * (double)i * (0.59999999999999998D * (double)i) && par1EntityPlayer.attackEntityFrom(DamageSource.causeMobDamage(this), getAttackStrength()))
            {
                playSound("mob.attack", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean canDamagePlayer()
    {
        return getSlimeSize() > 1;
    }

    /**
     * Gets the amount of damage dealt to the player when "attacked" by the slime.
     */
    protected int getAttackStrength()
    {
        return getSlimeSize();
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return (new StringBuilder()).append("mob.slime.").append(getSlimeSize() <= 1 ? "small" : "big").toString();
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return (new StringBuilder()).append("mob.slime.").append(getSlimeSize() <= 1 ? "small" : "big").toString();
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        if (getSlimeSize() == 1)
        {
            return Item.slimeBall.itemID;
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
        if (slimeSpawn==0){
            return false;
        }
        if (slimeSpawn==1){
            return posY < 32D;
        }

        Chunk chunk = worldObj.getChunkFromBlockCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));

        if (slimeSpawn==2){
            return (getSlimeSize() == 1 || worldObj.difficultySetting > 0) && rand.nextInt(10) == 0 && chunk.getRandomWithSeed(0x3ad8025fL).nextInt(10) == 0 && posY < 16D;
        }

        if (slimeSpawn > 3 && worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && rand.nextInt(4) != 1)
        {
            return false;
        }

        if (slimeSpawn==3 || slimeSpawn==4)
        {
            if ((getSlimeSize() == 1 || worldObj.difficultySetting > 0) && rand.nextInt(10) == 0 && chunk.getRandomWithSeed(0x3ad8025fL).nextInt(10) == 0 && posY < 40D)
            {
                return super.getCanSpawnHere();
            }
            return false;
        }

        if (slimeSpawn > 4 && (getSlimeSize() == 1 || worldObj.difficultySetting > 0))
        {
            BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));

            if (biomegenbase == BiomeGenBase.swampland && posY > 50D && posY < 70D && (slimeSpawn > 5 || (rand.nextFloat() < 0.5F && rand.nextFloat() < worldObj.getCurrentMoonPhaseFactor())) && worldObj.getBlockLightValue(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) <= rand.nextInt(8))
            {
                return super.getCanSpawnHere();
            }

            if (rand.nextInt(10) == 0 && chunk.getRandomWithSeed(0x3ad8025fL).nextInt(10) == 0 && posY < 40D)
            {
                return super.getCanSpawnHere();
            }
        }
        return false;
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

    /**
     * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
     */
    protected boolean makesSoundOnJump()
    {
        return getSlimeSize() > 0;
    }

    /**
     * Returns true if the slime makes a sound when it lands after a jump (based upon the slime's size)
     */
    protected boolean makesSoundOnLand()
    {
        return getSlimeSize() > 2;
    }
}
