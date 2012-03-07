package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityEnderman extends EntityMob
{
    private static boolean canCarryBlocks[];
    public boolean isAttacking;
    private int teleportDelay;
    private int field_35185_e;

    public EntityEnderman(World world)
    {
        super(world);
        isAttacking = false;
        teleportDelay = 0;
        field_35185_e = 0;
        texture = "/mob/enderman.png";
        moveSpeed = 0.2F;
        attackStrength = 7;
        setSize(0.6F, 2.9F);
        stepHeight = 1.0F;
    }

    public int getMaxHealth()
    {
        return 40;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, new Byte((byte)0));
        dataWatcher.addObject(17, new Byte((byte)0));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("carried", (short)getCarried());
        nbttagcompound.setShort("carriedData", (short)getCarryingData());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        setCarried(nbttagcompound.getShort("carried"));
        setCarryingData(nbttagcompound.getShort("carriedData"));
    }

    protected Entity findPlayerToAttack()
    {
        EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 64D);
        if (entityplayer != null)
        {
            if (shouldAttackPlayer(entityplayer))
            {
                if (field_35185_e++ == 5)
                {
                    field_35185_e = 0;
                    return entityplayer;
                }
            }
            else
            {
                field_35185_e = 0;
            }
        }
        return null;
    }

    public int getEntityBrightnessForRender(float f)
    {
        return super.getEntityBrightnessForRender(f);
    }

    public float getEntityBrightness(float f)
    {
        return super.getEntityBrightness(f);
    }

    private boolean shouldAttackPlayer(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.armorInventory[3];
        if (itemstack != null && itemstack.itemID == Block.pumpkin.blockID)
        {
            return false;
        }
        Vec3D vec3d = entityplayer.getLook(1.0F).normalize();
        Vec3D vec3d1 = Vec3D.createVector(posX - entityplayer.posX, (boundingBox.minY + (double)(height / 2.0F)) - (entityplayer.posY + (double)entityplayer.getEyeHeight()), posZ - entityplayer.posZ);
        double d = vec3d1.lengthVector();
        vec3d1 = vec3d1.normalize();
        double d1 = vec3d.dotProduct(vec3d1);
        if (d1 > 1.0D - 0.025000000000000001D / d)
        {
            return entityplayer.canEntityBeSeen(this);
        }
        else
        {
            return false;
        }
    }

    public void onLivingUpdate()
    {
        if (isWet())
        {
            attackEntityFrom(DamageSource.drown, 1);
        }
        isAttacking = entityToAttack != null;
        moveSpeed = entityToAttack == null ? 0.3F : 6.5F;
        if (!worldObj.multiplayerWorld)
        {
            if (getCarried() == 0)
            {
                if (rand.nextInt(20) == 0)
                {
                    int i = MathHelper.floor_double((posX - 2D) + rand.nextDouble() * 4D);
                    int l = MathHelper.floor_double(posY + rand.nextDouble() * 3D);
                    int j1 = MathHelper.floor_double((posZ - 2D) + rand.nextDouble() * 4D);
                    int l1 = worldObj.getBlockId(i, l, j1);
                    if (canCarryBlocks[l1])
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
            worldObj.spawnParticle("largesmoke", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0.0D, 0.0D, 0.0D);
        }

        if (worldObj.isDaytime() && !worldObj.multiplayerWorld)
        {
            float f = getEntityBrightness(1.0F);
            if (f > 0.5F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F)
            {
                entityToAttack = null;
                teleportRandomly();
            }
        }
        if (isWet())
        {
            entityToAttack = null;
            teleportRandomly();
        }
        isJumping = false;
        if (entityToAttack != null)
        {
            faceEntity(entityToAttack, 100F, 100F);
        }
        if (!worldObj.multiplayerWorld && isEntityAlive())
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
                teleportDelay = 0;
            }
        }
        super.onLivingUpdate();
    }

    protected boolean teleportRandomly()
    {
        double d = posX + (rand.nextDouble() - 0.5D) * 64D;
        double d1 = posY + (double)(rand.nextInt(64) - 32);
        double d2 = posZ + (rand.nextDouble() - 0.5D) * 64D;
        return teleportTo(d, d1, d2);
    }

    protected boolean teleportToEntity(Entity entity)
    {
        Vec3D vec3d = Vec3D.createVector(posX - entity.posX, ((boundingBox.minY + (double)(height / 2.0F)) - entity.posY) + (double)entity.getEyeHeight(), posZ - entity.posZ);
        vec3d = vec3d.normalize();
        double d = 16D;
        double d1 = (posX + (rand.nextDouble() - 0.5D) * 8D) - vec3d.xCoord * d;
        double d2 = (posY + (double)(rand.nextInt(16) - 8)) - vec3d.yCoord * d;
        double d3 = (posZ + (rand.nextDouble() - 0.5D) * 8D) - vec3d.zCoord * d;
        return teleportTo(d1, d2, d3);
    }

    protected boolean teleportTo(double d, double d1, double d2)
    {
        double d3 = posX;
        double d4 = posY;
        double d5 = posZ;
        posX = d;
        posY = d1;
        posZ = d2;
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
                if (i1 == 0 || !Block.blocksList[i1].blockMaterial.getIsSolid())
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
                if (worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.getIsAnyLiquid(boundingBox))
                {
                    flag = true;
                }
            }
        }
        if (!flag)
        {
            setPosition(d3, d4, d5);
            return false;
        }
        int l = 128;
        for (int j1 = 0; j1 < l; j1++)
        {
            double d6 = (double)j1 / ((double)l - 1.0D);
            float f = (rand.nextFloat() - 0.5F) * 0.2F;
            float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
            float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
            double d7 = d3 + (posX - d3) * d6 + (rand.nextDouble() - 0.5D) * (double)width * 2D;
            double d8 = d4 + (posY - d4) * d6 + rand.nextDouble() * (double)height;
            double d9 = d5 + (posZ - d5) * d6 + (rand.nextDouble() - 0.5D) * (double)width * 2D;
            worldObj.spawnParticle("largesmoke", d7, d8, d9, f, f1, f2);
        }

        worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
        worldObj.playSoundAtEntity(this, "mob.endermen.portal", 1.0F, 1.0F);
        return true;
    }

    protected String getLivingSound()
    {
        return "mob.endermen.idle";
    }

    protected String getHurtSound()
    {
        return "mob.endermen.hit";
    }

    protected String getDeathSound()
    {
        return "mob.endermen.death";
    }

    protected int getDropItemId()
    {
        return Item.enderPearl.shiftedIndex;
    }

    protected void dropFewItems(boolean flag, int i)
    {
        int j = getDropItemId();
        if (j > 0)
        {
            int k = rand.nextInt(2 + i);
            for (int l = 0; l < k; l++)
            {
                dropItem(j, 1);
            }
        }
    }

    public void setCarried(int i)
    {
        dataWatcher.updateObject(16, Byte.valueOf((byte)(i & 0xff)));
    }

    public int getCarried()
    {
        return dataWatcher.getWatchableObjectByte(16);
    }

    public void setCarryingData(int i)
    {
        dataWatcher.updateObject(17, Byte.valueOf((byte)(i & 0xff)));
    }

    public int getCarryingData()
    {
        return dataWatcher.getWatchableObjectByte(17);
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (damagesource instanceof EntityDamageSourceIndirect)
        {
            for (int j = 0; j < 64; j++)
            {
                if (teleportRandomly())
                {
                    return true;
                }
            }

            return false;
        }
        else
        {
            return super.attackEntityFrom(damagesource, i);
        }
    }

    static
    {
        canCarryBlocks = new boolean[256];
        canCarryBlocks[Block.grass.blockID] = true;
        canCarryBlocks[Block.dirt.blockID] = true;
        canCarryBlocks[Block.sand.blockID] = true;
        canCarryBlocks[Block.gravel.blockID] = true;
        canCarryBlocks[Block.plantYellow.blockID] = true;
        canCarryBlocks[Block.plantRed.blockID] = true;
        canCarryBlocks[Block.mushroomBrown.blockID] = true;
        canCarryBlocks[Block.mushroomRed.blockID] = true;
        canCarryBlocks[Block.tnt.blockID] = true;
        canCarryBlocks[Block.cactus.blockID] = true;
        canCarryBlocks[Block.blockClay.blockID] = true;
        canCarryBlocks[Block.pumpkin.blockID] = true;
        canCarryBlocks[Block.melon.blockID] = true;
        canCarryBlocks[Block.mycelium.blockID] = true;
    }
}
