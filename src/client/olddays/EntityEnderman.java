package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityEnderman extends EntityMob
{
    public static boolean smoke = false;
    public static boolean oldPicking = false;
    public static boolean oldhealth = false;

    private static boolean carriableBlocks[];
    private static boolean carriableBlocksOld[];

    /**
     * Counter to delay the teleportation of an enderman towards the currently attacked target
     */
    private int teleportDelay;
    private int field_70826_g;

    public EntityEnderman(World par1World)
    {
        super(par1World);
        teleportDelay = 0;
        field_70826_g = 0;
        texture = "/mob/enderman.png";
        moveSpeed = 0.2F;
        setSize(0.6F, 2.9F);
        stepHeight = 1.0F;
    }

    public int getMaxHealth()
    {
        return oldhealth ? 20 : 40;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, new Byte((byte)0));
        dataWatcher.addObject(17, new Byte((byte)0));
        dataWatcher.addObject(18, new Byte((byte)0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("carried", (short)getCarried());
        par1NBTTagCompound.setShort("carriedData", (short)getCarryingData());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        setCarried(par1NBTTagCompound.getShort("carried"));
        setCarryingData(par1NBTTagCompound.getShort("carriedData"));
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 64D);

        if (entityplayer != null)
        {
            if (shouldAttackPlayer(entityplayer))
            {
                if (field_70826_g == 0)
                {
                    worldObj.playSoundAtEntity(entityplayer, "mob.endermen.stare", 1.0F, 1.0F);
                }

                if (field_70826_g++ == 5)
                {
                    field_70826_g = 0;
                    func_70819_e(true);
                    return entityplayer;
                }
            }
            else
            {
                field_70826_g = 0;
            }
        }

        return null;
    }

    /**
     * Checks to see if this enderman should be attacking this player
     */
    private boolean shouldAttackPlayer(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.armorInventory[3];

        if (itemstack != null && itemstack.itemID == Block.pumpkin.blockID)
        {
            return false;
        }

        Vec3 vec3 = par1EntityPlayer.getLook(1.0F).normalize();
        Vec3 vec3_1 = worldObj.getWorldVec3Pool().getVecFromPool(posX - par1EntityPlayer.posX, (boundingBox.minY + (double)(height / 2.0F)) - (par1EntityPlayer.posY + (double)par1EntityPlayer.getEyeHeight()), posZ - par1EntityPlayer.posZ);
        double d = vec3_1.lengthVector();
        vec3_1 = vec3_1.normalize();
        double d1 = vec3.dotProduct(vec3_1);

        if (d1 > 1.0D - 0.025000000000000001D / d)
        {
            return par1EntityPlayer.canEntityBeSeen(this);
        }
        else
        {
            return false;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (isWet())
        {
            attackEntityFrom(DamageSource.drown, 1);
        }

        moveSpeed = entityToAttack == null ? 0.3F : 6.5F;

        if (!worldObj.isRemote && worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
        {
            if (getCarried() == 0)
            {
                if (rand.nextInt(20) == 0)
                {
                    int i = MathHelper.floor_double((posX - 2D) + rand.nextDouble() * 4D);
                    int l = MathHelper.floor_double(posY + rand.nextDouble() * 3D);
                    int j1 = MathHelper.floor_double((posZ - 2D) + rand.nextDouble() * 4D);
                    int l1 = worldObj.getBlockId(i, l, j1);

                    if ((!oldPicking && carriableBlocks[l1]) || (oldPicking && carriableBlocksOld[l1]))
                    {
                        setCarried(worldObj.getBlockId(i, l, j1));
                        setCarryingData(worldObj.getBlockMetadata(i, l, j1));
                        worldObj.setBlockWithNotify(i, l, j1, 0);
                    }
                }
            }
            else if (rand.nextInt(2000) == 0)
            {
                int j = MathHelper.floor_double((posX - 1.0D) + rand.nextDouble() * 2D);
                int i1 = MathHelper.floor_double(posY + rand.nextDouble() * 2D);
                int k1 = MathHelper.floor_double((posZ - 1.0D) + rand.nextDouble() * 2D);
                int i2 = worldObj.getBlockId(j, i1, k1);
                int j2 = worldObj.getBlockId(j, i1 - 1, k1);

                if (i2 == 0 && j2 > 0 && Block.blocksList[j2].renderAsNormalBlock())
                {
                    worldObj.setBlockAndMetadataWithNotify(j, i1, k1, getCarried(), getCarryingData());
                    setCarried(0);
                }
            }
        }

        for (int k = 0; k < 2; k++)
        {
            if (smoke){
                worldObj.spawnParticle("largesmoke", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0.0D, 0.0D, 0.0D);
            }else{
                worldObj.spawnParticle("portal", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double)width, (rand.nextDouble() - 0.5D) * 2D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2D);
            }
        }

        if (worldObj.isDaytime() && !worldObj.isRemote)
        {
            float f = getBrightness(1.0F);

            if (f > 0.5F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F)
            {
                entityToAttack = null;
                func_70819_e(false);
                teleportRandomly();
            }
        }

        if (isWet())
        {
            entityToAttack = null;
            func_70819_e(false);
            teleportRandomly();
        }

        isJumping = false;

        if (entityToAttack != null)
        {
            faceEntity(entityToAttack, 100F, 100F);
        }

        if (!worldObj.isRemote && isEntityAlive())
        {
            if (entityToAttack != null)
            {
                if ((entityToAttack instanceof EntityPlayer) && shouldAttackPlayer((EntityPlayer)entityToAttack))
                {
                    moveStrafing = moveForward = 0.0F;
                    moveSpeed = 0.0F;

                    if (entityToAttack.getDistanceSqToEntity(this) < 16D)
                    {
                        teleportRandomly();
                    }

                    teleportDelay = 0;
                }
                else if (entityToAttack.getDistanceSqToEntity(this) > 256D && teleportDelay++ >= 30 && teleportToEntity(entityToAttack))
                {
                    teleportDelay = 0;
                }
            }
            else
            {
                func_70819_e(false);
                teleportDelay = 0;
            }
        }

        super.onLivingUpdate();
    }

    /**
     * Teleport the enderman to a random nearby position
     */
    protected boolean teleportRandomly()
    {
        double d = posX + (rand.nextDouble() - 0.5D) * 64D;
        double d1 = posY + (double)(rand.nextInt(64) - 32);
        double d2 = posZ + (rand.nextDouble() - 0.5D) * 64D;
        return teleportTo(d, d1, d2);
    }

    /**
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(Entity par1Entity)
    {
        Vec3 vec3 = worldObj.getWorldVec3Pool().getVecFromPool(posX - par1Entity.posX, ((boundingBox.minY + (double)(height / 2.0F)) - par1Entity.posY) + (double)par1Entity.getEyeHeight(), posZ - par1Entity.posZ);
        vec3 = vec3.normalize();
        double d = 16D;
        double d1 = (posX + (rand.nextDouble() - 0.5D) * 8D) - vec3.xCoord * d;
        double d2 = (posY + (double)(rand.nextInt(16) - 8)) - vec3.yCoord * d;
        double d3 = (posZ + (rand.nextDouble() - 0.5D) * 8D) - vec3.zCoord * d;
        return teleportTo(d1, d2, d3);
    }

    /**
     * Teleport the enderman
     */
    protected boolean teleportTo(double par1, double par3, double par5)
    {
        double d = posX;
        double d1 = posY;
        double d2 = posZ;
        posX = par1;
        posY = par3;
        posZ = par5;
        boolean flag = false;
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posY);
        int k = MathHelper.floor_double(posZ);

        if (worldObj.blockExists(i, j, k))
        {
            boolean flag1;

            for (flag1 = false; !flag1 && j > 0;)
            {
                int i1 = worldObj.getBlockId(i, j - 1, k);

                if (i1 == 0 || !Block.blocksList[i1].blockMaterial.blocksMovement())
                {
                    posY--;
                    j--;
                }
                else
                {
                    flag1 = true;
                }
            }

            if (flag1)
            {
                setPosition(posX, posY, posZ);

                if (worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox))
                {
                    flag = true;
                }
            }
        }

        if (flag)
        {
            int l = 128;

            for (int j1 = 0; j1 < l; j1++)
            {
                double d3 = (double)j1 / ((double)l - 1.0D);
                float f = (rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
                double d4 = d + (posX - d) * d3 + (rand.nextDouble() - 0.5D) * (double)width * 2D;
                double d5 = d1 + (posY - d1) * d3 + rand.nextDouble() * (double)height;
                double d6 = d2 + (posZ - d2) * d3 + (rand.nextDouble() - 0.5D) * (double)width * 2D;
                if (smoke){
                    worldObj.spawnParticle("largesmoke", d4, d5, d6, f, f1, f2);
                }else{
                    worldObj.spawnParticle("portal", d4, d5, d6, f, f1, f2);
                }
            }

            worldObj.playSoundEffect(d, d1, d2, "mob.endermen.portal", 1.0F, 1.0F);
            func_85030_a("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
        else
        {
            setPosition(d, d1, d2);
            return false;
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return func_70823_r() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.endermen.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.endermen.death";
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return Item.enderPearl.shiftedIndex;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int i = getDropItemId();

        if (i > 0)
        {
            int j = rand.nextInt(2 + par2);

            for (int k = 0; k < j; k++)
            {
                dropItem(i, 1);
            }
        }
    }

    /**
     * Set the id of the block an enderman carries
     */
    public void setCarried(int par1)
    {
        dataWatcher.updateObject(16, Byte.valueOf((byte)(par1 & 0xff)));
    }

    /**
     * Get the id of the block an enderman carries
     */
    public int getCarried()
    {
        return dataWatcher.getWatchableObjectByte(16);
    }

    /**
     * Set the metadata of the block an enderman carries
     */
    public void setCarryingData(int par1)
    {
        dataWatcher.updateObject(17, Byte.valueOf((byte)(par1 & 0xff)));
    }

    /**
     * Get the metadata of the block an enderman carries
     */
    public int getCarryingData()
    {
        return dataWatcher.getWatchableObjectByte(17);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (func_85032_ar())
        {
            return false;
        }

        if (par1DamageSource instanceof EntityDamageSourceIndirect)
        {
            for (int i = 0; i < 64; i++)
            {
                if (teleportRandomly())
                {
                    return true;
                }
            }

            return false;
        }

        if (par1DamageSource.getEntity() instanceof EntityPlayer)
        {
            func_70819_e(true);
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    public boolean func_70823_r()
    {
        return dataWatcher.getWatchableObjectByte(18) > 0;
    }

    public void func_70819_e(boolean par1)
    {
        dataWatcher.updateObject(18, Byte.valueOf((byte)(par1 ? 1 : 0)));
    }

    /**
     * Returns the amount of damage a mob should deal.
     */
    public int getAttackStrength(Entity par1Entity)
    {
        return 7;
    }

    static
    {
        carriableBlocks = new boolean[256];
        carriableBlocks[Block.grass.blockID] = true;
        carriableBlocks[Block.dirt.blockID] = true;
        carriableBlocks[Block.sand.blockID] = true;
        carriableBlocks[Block.gravel.blockID] = true;
        carriableBlocks[Block.plantYellow.blockID] = true;
        carriableBlocks[Block.plantRed.blockID] = true;
        carriableBlocks[Block.mushroomBrown.blockID] = true;
        carriableBlocks[Block.mushroomRed.blockID] = true;
        carriableBlocks[Block.tnt.blockID] = true;
        carriableBlocks[Block.cactus.blockID] = true;
        carriableBlocks[Block.blockClay.blockID] = true;
        carriableBlocks[Block.pumpkin.blockID] = true;
        carriableBlocks[Block.melon.blockID] = true;
        carriableBlocks[Block.mycelium.blockID] = true;
        carriableBlocksOld = new boolean[256];
        carriableBlocksOld[Block.stone.blockID] = true;
        carriableBlocksOld[Block.grass.blockID] = true;
        carriableBlocksOld[Block.dirt.blockID] = true;
        carriableBlocksOld[Block.cobblestone.blockID] = true;
        carriableBlocksOld[Block.planks.blockID] = true;
        carriableBlocksOld[Block.sand.blockID] = true;
        carriableBlocksOld[Block.gravel.blockID] = true;
        carriableBlocksOld[Block.oreGold.blockID] = true;
        carriableBlocksOld[Block.oreIron.blockID] = true;
        carriableBlocksOld[Block.oreCoal.blockID] = true;
        carriableBlocksOld[Block.wood.blockID] = true;
        carriableBlocksOld[Block.leaves.blockID] = true;
        carriableBlocksOld[Block.sponge.blockID] = true;
        carriableBlocksOld[Block.glass.blockID] = true;
        carriableBlocksOld[Block.oreLapis.blockID] = true;
        carriableBlocksOld[Block.blockLapis.blockID] = true;
        carriableBlocksOld[Block.sandStone.blockID] = true;
        carriableBlocksOld[Block.cloth.blockID] = true;
        carriableBlocksOld[Block.plantYellow.blockID] = true;
        carriableBlocksOld[Block.plantRed.blockID] = true;
        carriableBlocksOld[Block.mushroomBrown.blockID] = true;
        carriableBlocksOld[Block.mushroomRed.blockID] = true;
        carriableBlocksOld[Block.blockGold.blockID] = true;
        carriableBlocksOld[Block.blockSteel.blockID] = true;
        carriableBlocksOld[Block.brick.blockID] = true;
        carriableBlocksOld[Block.tnt.blockID] = true;
        carriableBlocksOld[Block.bookShelf.blockID] = true;
        carriableBlocksOld[Block.cobblestoneMossy.blockID] = true;
        carriableBlocksOld[Block.oreDiamond.blockID] = true;
        carriableBlocksOld[Block.blockDiamond.blockID] = true;
        carriableBlocksOld[Block.oreEmerald.blockID] = true;
        carriableBlocksOld[Block.blockEmerald.blockID] = true;
        carriableBlocksOld[Block.workbench.blockID] = true;
        carriableBlocksOld[Block.oreRedstone.blockID] = true;
        carriableBlocksOld[Block.oreRedstoneGlowing.blockID] = true;
        carriableBlocksOld[Block.ice.blockID] = true;
        carriableBlocksOld[Block.cactus.blockID] = true;
        carriableBlocksOld[Block.blockClay.blockID] = true;
        carriableBlocksOld[Block.pumpkin.blockID] = true;
        carriableBlocksOld[Block.netherrack.blockID] = true;
        carriableBlocksOld[Block.slowSand.blockID] = true;
        carriableBlocksOld[Block.glowStone.blockID] = true;
        carriableBlocksOld[Block.pumpkinLantern.blockID] = true;
        carriableBlocksOld[Block.stoneBrick.blockID] = true;
        carriableBlocksOld[Block.mushroomCapBrown.blockID] = true;
        carriableBlocksOld[Block.mushroomCapRed.blockID] = true;
        carriableBlocksOld[Block.melon.blockID] = true;
        carriableBlocksOld[Block.mycelium.blockID] = true;
    }
}
