package net.minecraft.src;

import java.util.Random;

public class EntityXPOrb extends Entity
{
    public static boolean noxp = false;

    /**
     * A constantly increasing value that RenderXPOrb uses to control the colour shifting (Green / yellow)
     */
    public int xpColor;

    /** The age of the XP orb in ticks. */
    public int xpOrbAge;
    public int field_70532_c;

    /** The health of this XP orb. */
    private int xpOrbHealth;

    /** This is how much XP this orb has. */
    private int xpValue;

    /** The closest EntityPlayer to this orb. */
    private EntityPlayer closestPlayer;

    /** Threshold color for tracking players */
    private int xpTargetColor;

    public EntityXPOrb(World par1World, double par2, double par4, double par6, int par8)
    {
        super(par1World);
        xpOrbHealth = 5;
        setSize(0.5F, 0.5F);
        yOffset = height / 2.0F;
        setPosition(par2, par4, par6);
        rotationYaw = (float)(Math.random() * 360D);
        motionX = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F;
        motionY = (float)(Math.random() * 0.20000000000000001D) * 2.0F;
        motionZ = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F;
        xpValue = par8;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityXPOrb(World par1World)
    {
        super(par1World);
        xpOrbHealth = 5;
        setSize(0.25F, 0.25F);
        yOffset = height / 2.0F;
    }

    protected void entityInit()
    {
        if(noxp){
            setDead();
        }
    }

    public int getBrightnessForRender(float par1)
    {
        float f = 0.5F;

        if (f < 0.0F)
        {
            f = 0.0F;
        }

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        int i = super.getBrightnessForRender(par1);
        int j = i & 0xff;
        int k = i >> 16 & 0xff;
        j += (int)(f * 15F * 16F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (field_70532_c > 0)
        {
            field_70532_c--;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.029999999329447746D;

        if (worldObj.getBlockMaterial(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) == Material.lava)
        {
            motionY = 0.20000000298023224D;
            motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
            motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
            playSound("random.fizz", 0.4F, 2.0F + rand.nextFloat() * 0.4F);
        }

        pushOutOfBlocks(posX, (boundingBox.minY + boundingBox.maxY) / 2D, posZ);
        double d = 8D;

        if (xpTargetColor < (xpColor - 20) + entityId % 100)
        {
            if (closestPlayer == null || closestPlayer.getDistanceSqToEntity(this) > d * d)
            {
                closestPlayer = worldObj.getClosestPlayerToEntity(this, d);
            }

            xpTargetColor = xpColor;
        }

        if (closestPlayer != null)
        {
            double d1 = (closestPlayer.posX - posX) / d;
            double d2 = ((closestPlayer.posY + (double)closestPlayer.getEyeHeight()) - posY) / d;
            double d3 = (closestPlayer.posZ - posZ) / d;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D)
            {
                d5 *= d5;
                motionX += (d1 / d4) * d5 * 0.10000000000000001D;
                motionY += (d2 / d4) * d5 * 0.10000000000000001D;
                motionZ += (d3 / d4) * d5 * 0.10000000000000001D;
            }
        }

        moveEntity(motionX, motionY, motionZ);
        float f = 0.98F;

        if (onGround)
        {
            f = 0.5880001F;
            int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));

            if (i > 0)
            {
                f = Block.blocksList[i].slipperiness * 0.98F;
            }
        }

        motionX *= f;
        motionY *= 0.98000001907348633D;
        motionZ *= f;

        if (onGround)
        {
            motionY *= -0.89999997615814209D;
        }

        xpColor++;
        xpOrbAge++;

        if (xpOrbAge >= 6000)
        {
            setDead();
        }
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return worldObj.handleMaterialAcceleration(boundingBox, Material.water, this);
    }

    /**
     * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    protected void dealFireDamage(int par1)
    {
        attackEntityFrom(DamageSource.inFire, par1);
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

        setBeenAttacked();
        xpOrbHealth -= par2;

        if (xpOrbHealth <= 0)
        {
            setDead();
        }

        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("Health", (byte)xpOrbHealth);
        par1NBTTagCompound.setShort("Age", (short)xpOrbAge);
        par1NBTTagCompound.setShort("Value", (short)xpValue);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        xpOrbHealth = par1NBTTagCompound.getShort("Health") & 0xff;
        xpOrbAge = par1NBTTagCompound.getShort("Age");
        xpValue = par1NBTTagCompound.getShort("Value");
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (worldObj.isRemote)
        {
            return;
        }

        if (field_70532_c == 0 && par1EntityPlayer.xpCooldown == 0)
        {
            par1EntityPlayer.xpCooldown = 2;
            playSound("random.orb", 0.1F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));
            par1EntityPlayer.onItemPickup(this, 1);
            par1EntityPlayer.addExperience(xpValue);
            setDead();
        }
    }

    /**
     * Returns the XP value of this XP orb.
     */
    public int getXpValue()
    {
        return xpValue;
    }

    /**
     * Returns a number from 1 to 10 based on how much XP this orb is worth. This is used by RenderXPOrb to determine
     * what texture to use.
     */
    public int getTextureByXP()
    {
        if (xpValue >= 2477)
        {
            return 10;
        }

        if (xpValue >= 1237)
        {
            return 9;
        }

        if (xpValue >= 617)
        {
            return 8;
        }

        if (xpValue >= 307)
        {
            return 7;
        }

        if (xpValue >= 149)
        {
            return 6;
        }

        if (xpValue >= 73)
        {
            return 5;
        }

        if (xpValue >= 37)
        {
            return 4;
        }

        if (xpValue >= 17)
        {
            return 3;
        }

        if (xpValue >= 7)
        {
            return 2;
        }

        return xpValue < 3 ? 0 : 1;
    }

    /**
     * Get xp split rate (Is called until the xp drop code in EntityLiving.onEntityUpdate is complete)
     */
    public static int getXPSplit(int par0)
    {
        if (par0 >= 2477)
        {
            return 2477;
        }

        if (par0 >= 1237)
        {
            return 1237;
        }

        if (par0 >= 617)
        {
            return 617;
        }

        if (par0 >= 307)
        {
            return 307;
        }

        if (par0 >= 149)
        {
            return 149;
        }

        if (par0 >= 73)
        {
            return 73;
        }

        if (par0 >= 37)
        {
            return 37;
        }

        if (par0 >= 17)
        {
            return 17;
        }

        if (par0 >= 7)
        {
            return 7;
        }

        return par0 < 3 ? 1 : 3;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }
}
