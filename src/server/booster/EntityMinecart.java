package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityMinecart extends Entity implements IInventory
{
    private ItemStack cargoItems[];
    private int fuel;
    private boolean field_469_aj;

    /** The type of minecart, 2 for powered, 1 for storage. */
    public int minecartType;
    public double pushX;
    public double pushZ;
    private static final int field_468_ak[][][] =
    {
        {
            {
                0, 0, -1
            }, {
                0, 0, 1
            }
        }, {
            {
                -1, 0, 0
            }, {
                1, 0, 0
            }
        }, {
            {
                -1, -1, 0
            }, {
                1, 0, 0
            }
        }, {
            {
                -1, 0, 0
            }, {
                1, -1, 0
            }
        }, {
            {
                0, 0, -1
            }, {
                0, -1, 1
            }
        }, {
            {
                0, -1, -1
            }, {
                0, 0, 1
            }
        }, {
            {
                0, 0, 1
            }, {
                1, 0, 0
            }
        }, {
            {
                0, 0, 1
            }, {
                -1, 0, 0
            }
        }, {
            {
                0, 0, -1
            }, {
                -1, 0, 0
            }
        }, {
            {
                0, 0, -1
            }, {
                1, 0, 0
            }
        }
    };

    /** appears to be the progress of the turn */
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;

    public EntityMinecart(World par1World)
    {
        super(par1World);
        cargoItems = new ItemStack[36];
        fuel = 0;
        field_469_aj = false;
        preventEntitySpawning = true;
        setSize(0.98F, 0.7F);
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
        dataWatcher.addObject(16, new Byte((byte)0));
        dataWatcher.addObject(17, new Integer(0));
        dataWatcher.addObject(18, new Integer(1));
        dataWatcher.addObject(19, new Integer(0));
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
        return null;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return true;
    }

    public EntityMinecart(World par1World, double par2, double par4, double par6, int par8)
    {
        this(par1World);
        setPosition(par2, par4 + (double)yOffset, par6);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = par2;
        prevPosY = par4;
        prevPosZ = par6;
        minecartType = par8;
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
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (worldObj.isRemote || isDead)
        {
            return true;
        }

        func_41016_d(-func_41021_q());
        func_41014_b(10);
        setBeenAttacked();
        func_41018_e_(func_41020_o() + par2 * 10);

        if (func_41020_o() > 40)
        {
            if (riddenByEntity != null)
            {
                riddenByEntity.mountEntity(this);
            }

            setDead();
            dropItemWithOffset(Item.minecartEmpty.shiftedIndex, 1, 0.0F);

            if (minecartType == 1)
            {
                EntityMinecart entityminecart = this;
                label0:

                for (int i = 0; i < entityminecart.getSizeInventory(); i++)
                {
                    ItemStack itemstack = entityminecart.getStackInSlot(i);

                    if (itemstack == null)
                    {
                        continue;
                    }

                    float f = rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = rand.nextFloat() * 0.8F + 0.1F;

                    do
                    {
                        if (itemstack.stackSize <= 0)
                        {
                            continue label0;
                        }

                        int j = rand.nextInt(21) + 10;

                        if (j > itemstack.stackSize)
                        {
                            j = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j;
                        EntityItem entityitem = new EntityItem(worldObj, posX + (double)f, posY + (double)f1, posZ + (double)f2, new ItemStack(itemstack.itemID, j, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float)rand.nextGaussian() * f3;
                        entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float)rand.nextGaussian() * f3;
                        worldObj.spawnEntityInWorld(entityitem);
                    }
                    while (true);
                }

                dropItemWithOffset(Block.chest.blockID, 1, 0.0F);
            }
            else if (minecartType == 2)
            {
                dropItemWithOffset(Block.stoneOvenIdle.blockID, 1, 0.0F);
            }
        }

        return true;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        label0:

        for (int i = 0; i < getSizeInventory(); i++)
        {
            ItemStack itemstack = getStackInSlot(i);

            if (itemstack == null)
            {
                continue;
            }

            float f = rand.nextFloat() * 0.8F + 0.1F;
            float f1 = rand.nextFloat() * 0.8F + 0.1F;
            float f2 = rand.nextFloat() * 0.8F + 0.1F;

            do
            {
                if (itemstack.stackSize <= 0)
                {
                    continue label0;
                }

                int j = rand.nextInt(21) + 10;

                if (j > itemstack.stackSize)
                {
                    j = itemstack.stackSize;
                }

                itemstack.stackSize -= j;
                EntityItem entityitem = new EntityItem(worldObj, posX + (double)f, posY + (double)f1, posZ + (double)f2, new ItemStack(itemstack.itemID, j, itemstack.getItemDamage()));

                if (itemstack.hasTagCompound())
                {
                    entityitem.item.setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                }

                float f3 = 0.05F;
                entityitem.motionX = (float)rand.nextGaussian() * f3;
                entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)rand.nextGaussian() * f3;
                worldObj.spawnEntityInWorld(entityitem);
            }
            while (true);
        }

        super.setDead();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (func_41019_p() > 0)
        {
            func_41014_b(func_41019_p() - 1);
        }

        if (func_41020_o() > 0)
        {
            func_41018_e_(func_41020_o() - 1);
        }

        if (posY < -64D)
        {
            kill();
        }

        if (isMinecartPowered() && rand.nextInt(4) == 0)
        {
            worldObj.spawnParticle("largesmoke", posX, posY + 0.80000000000000004D, posZ, 0.0D, 0.0D, 0.0D);
        }

        if (worldObj.isRemote)
        {
            if (turnProgress > 0)
            {
                double d = posX + (minecartX - posX) / (double)turnProgress;
                double d1 = posY + (minecartY - posY) / (double)turnProgress;
                double d3 = posZ + (minecartZ - posZ) / (double)turnProgress;
                double d5;

                for (d5 = minecartYaw - (double)rotationYaw; d5 < -180D; d5 += 360D) { }

                for (; d5 >= 180D; d5 -= 360D) { }

                rotationYaw += d5 / (double)turnProgress;
                rotationPitch += (minecartPitch - (double)rotationPitch) / (double)turnProgress;
                turnProgress--;
                setPosition(d, d1, d3);
                setRotation(rotationYaw, rotationPitch);
            }
            else
            {
                setPosition(posX, posY, posZ);
                setRotation(rotationYaw, rotationPitch);
            }

            return;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.039999999105930328D;
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posY);
        int k = MathHelper.floor_double(posZ);

        if (BlockRail.isRailBlockAt(worldObj, i, j - 1, k))
        {
            j--;
        }

        double d2 = 0.40000000000000002D;
        double d4 = 0.0078125D;
        int l = worldObj.getBlockId(i, j, k);

        if (BlockRail.isRailBlock(l))
        {
            Vec3D vec3d = func_182_g(posX, posY, posZ);
            int i1 = worldObj.getBlockMetadata(i, j, k);
            posY = j;
            boolean flag = false;
            boolean flag1 = false;

            if (l == Block.railPowered.blockID)
            {
                flag = (i1 & 8) != 0;
                flag1 = !flag;
            }

            if (((BlockRail)Block.blocksList[l]).isPowered())
            {
                i1 &= 7;
            }

            if (i1 >= 2 && i1 <= 5)
            {
                posY = j + 1;
            }

            if (i1 == 2)
            {
                motionX -= d4;
            }

            if (i1 == 3)
            {
                motionX += d4;
            }

            if (i1 == 4)
            {
                motionZ += d4;
            }

            if (i1 == 5)
            {
                motionZ -= d4;
            }

            int ai[][] = field_468_ak[i1];
            double d9 = ai[1][0] - ai[0][0];
            double d10 = ai[1][2] - ai[0][2];
            double d11 = Math.sqrt(d9 * d9 + d10 * d10);
            double d12 = motionX * d9 + motionZ * d10;

            if (d12 < 0.0D)
            {
                d9 = -d9;
                d10 = -d10;
            }

            double d13 = Math.sqrt(motionX * motionX + motionZ * motionZ);
            motionX = (d13 * d9) / d11;
            motionZ = (d13 * d10) / d11;

            if (flag1)
            {
                double d16 = Math.sqrt(motionX * motionX + motionZ * motionZ);

                if (d16 < 0.029999999999999999D)
                {
                    motionX *= 0.0D;
                    motionY *= 0.0D;
                    motionZ *= 0.0D;
                }
                else
                {
                    motionX *= 0.5D;
                    motionY *= 0.0D;
                    motionZ *= 0.5D;
                }
            }

            double d17 = 0.0D;
            double d18 = (double)i + 0.5D + (double)ai[0][0] * 0.5D;
            double d19 = (double)k + 0.5D + (double)ai[0][2] * 0.5D;
            double d20 = (double)i + 0.5D + (double)ai[1][0] * 0.5D;
            double d21 = (double)k + 0.5D + (double)ai[1][2] * 0.5D;
            d9 = d20 - d18;
            d10 = d21 - d19;

            if (d9 == 0.0D)
            {
                posX = (double)i + 0.5D;
                d17 = posZ - (double)k;
            }
            else if (d10 == 0.0D)
            {
                posZ = (double)k + 0.5D;
                d17 = posX - (double)i;
            }
            else
            {
                double d22 = posX - d18;
                double d24 = posZ - d19;
                double d26 = (d22 * d9 + d24 * d10) * 2D;
                d17 = d26;
            }

            posX = d18 + d9 * d17;
            posZ = d19 + d10 * d17;
            setPosition(posX, posY + (double)yOffset, posZ);
            double d23 = motionX;
            double d25 = motionZ;

            if (riddenByEntity != null)
            {
                d23 *= 0.75D;
                d25 *= 0.75D;
            }

            if (d23 < -d2)
            {
                d23 = -d2;
            }

            if (d23 > d2)
            {
                d23 = d2;
            }

            if (d25 < -d2)
            {
                d25 = -d2;
            }

            if (d25 > d2)
            {
                d25 = d2;
            }

            moveEntity(d23, 0.0D, d25);

            if (ai[0][1] != 0 && MathHelper.floor_double(posX) - i == ai[0][0] && MathHelper.floor_double(posZ) - k == ai[0][2])
            {
                setPosition(posX, posY + (double)ai[0][1], posZ);
            }
            else if (ai[1][1] != 0 && MathHelper.floor_double(posX) - i == ai[1][0] && MathHelper.floor_double(posZ) - k == ai[1][2])
            {
                setPosition(posX, posY + (double)ai[1][1], posZ);
            }

            if (riddenByEntity != null)
            {
                motionX *= 0.99699997901916504D;
                motionY *= 0.0D;
                motionZ *= 0.99699997901916504D;
            }
            else
            {
                if (minecartType == 2)
                {
                    double d27 = MathHelper.sqrt_double(pushX * pushX + pushZ * pushZ);

                    if (d27 > 0.01D)
                    {
                        pushX /= d27;
                        pushZ /= d27;
                        double d29 = 0.040000000000000001D;
                        motionX *= 0.80000001192092896D;
                        motionY *= 0.0D;
                        motionZ *= 0.80000001192092896D;
                        motionX += pushX * d29;
                        motionZ += pushZ * d29;
                    }
                    else
                    {
                        motionX *= 0.89999997615814209D;
                        motionY *= 0.0D;
                        motionZ *= 0.89999997615814209D;
                    }
                }

                motionX *= 0.95999997854232788D;
                motionY *= 0.0D;
                motionZ *= 0.95999997854232788D;
            }

            Vec3D vec3d1 = func_182_g(posX, posY, posZ);

            if (vec3d1 != null && vec3d != null)
            {
                double d28 = (vec3d.yCoord - vec3d1.yCoord) * 0.050000000000000003D;
                double d14 = Math.sqrt(motionX * motionX + motionZ * motionZ);

                if (d14 > 0.0D)
                {
                    motionX = (motionX / d14) * (d14 + d28);
                    motionZ = (motionZ / d14) * (d14 + d28);
                }

                setPosition(posX, vec3d1.yCoord, posZ);
            }

            int k1 = MathHelper.floor_double(posX);
            int l1 = MathHelper.floor_double(posZ);

            if (k1 != i || l1 != k)
            {
                double d15 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                motionX = d15 * (double)(k1 - i);
                motionZ = d15 * (double)(l1 - k);
            }

            if (minecartType == 2)
            {
                double d30 = MathHelper.sqrt_double(pushX * pushX + pushZ * pushZ);

                if (d30 > 0.01D && motionX * motionX + motionZ * motionZ > 0.001D)
                {
                    pushX /= d30;
                    pushZ /= d30;

                    if (pushX * motionX + pushZ * motionZ < 0.0D)
                    {
                        pushX = 0.0D;
                        pushZ = 0.0D;
                    }
                    else
                    {
                        pushX = motionX;
                        pushZ = motionZ;
                    }
                }
            }

            if (flag)
            {
                double d31 = Math.sqrt(motionX * motionX + motionZ * motionZ);

                if (d31 > 0.01D)
                {
                    double d32 = 0.04D;
                    motionX += (motionX / d31) * d32;
                    motionZ += (motionZ / d31) * d32;
                }
                else if (i1 == 1)
                {
                    if (worldObj.isBlockNormalCube(i - 1, j, k))
                    {
                        motionX = 0.02D;
                    }
                    else if (worldObj.isBlockNormalCube(i + 1, j, k))
                    {
                        motionX = -0.02D;
                    }
                }
                else if (i1 == 0)
                {
                    if (worldObj.isBlockNormalCube(i, j, k - 1))
                    {
                        motionZ = 0.02D;
                    }
                    else if (worldObj.isBlockNormalCube(i, j, k + 1))
                    {
                        motionZ = -0.02D;
                    }
                }
            }
        }
        else
        {
            if (motionX < -d2)
            {
                motionX = -d2;
            }

            if (motionX > d2)
            {
                motionX = d2;
            }

            if (motionZ < -d2)
            {
                motionZ = -d2;
            }

            if (motionZ > d2)
            {
                motionZ = d2;
            }

            if (onGround)
            {
                motionX *= 0.5D;
                motionY *= 0.5D;
                motionZ *= 0.5D;
            }

            moveEntity(motionX, motionY, motionZ);

            if (!onGround)
            {
                motionX *= 0.94999998807907104D;
                motionY *= 0.94999998807907104D;
                motionZ *= 0.94999998807907104D;
            }
        }

        rotationPitch = 0.0F;
        double d6 = prevPosX - posX;
        double d7 = prevPosZ - posZ;

        if (d6 * d6 + d7 * d7 > 0.001D)
        {
            rotationYaw = (float)((Math.atan2(d7, d6) * 180D) / Math.PI);

            if (field_469_aj)
            {
                rotationYaw += 180F;
            }
        }

        double d8;

        for (d8 = rotationYaw - prevRotationYaw; d8 >= 180D; d8 -= 360D) { }

        for (; d8 < -180D; d8 += 360D) { }

        if (d8 < -170D || d8 >= 170D)
        {
            rotationYaw += 180F;
            field_469_aj = !field_469_aj;
        }

        setRotation(rotationYaw, rotationPitch);
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && list.size() > 0)
        {
            for (int j1 = 0; j1 < list.size(); j1++)
            {
                Entity entity = (Entity)list.get(j1);

                if (entity != riddenByEntity && entity.canBePushed() && (entity instanceof EntityMinecart))
                {
                    entity.applyEntityCollision(this);
                }
            }
        }

        if (riddenByEntity != null && riddenByEntity.isDead)
        {
            if (riddenByEntity.ridingEntity == this)
            {
                riddenByEntity.ridingEntity = null;
            }

            riddenByEntity = null;
        }

        if (fuel > 0)
        {
            fuel--;
        }

        if (fuel <= 0)
        {
            pushX = pushZ = 0.0D;
        }

        setMinecartPowered(fuel > 0);
    }

    public Vec3D func_182_g(double par1, double par3, double par5)
    {
        int i = MathHelper.floor_double(par1);
        int j = MathHelper.floor_double(par3);
        int k = MathHelper.floor_double(par5);

        if (BlockRail.isRailBlockAt(worldObj, i, j - 1, k))
        {
            j--;
        }

        int l = worldObj.getBlockId(i, j, k);

        if (BlockRail.isRailBlock(l))
        {
            int i1 = worldObj.getBlockMetadata(i, j, k);
            par3 = j;

            if (((BlockRail)Block.blocksList[l]).isPowered())
            {
                i1 &= 7;
            }

            if (i1 >= 2 && i1 <= 5)
            {
                par3 = j + 1;
            }

            int ai[][] = field_468_ak[i1];
            double d = 0.0D;
            double d1 = (double)i + 0.5D + (double)ai[0][0] * 0.5D;
            double d2 = (double)j + 0.5D + (double)ai[0][1] * 0.5D;
            double d3 = (double)k + 0.5D + (double)ai[0][2] * 0.5D;
            double d4 = (double)i + 0.5D + (double)ai[1][0] * 0.5D;
            double d5 = (double)j + 0.5D + (double)ai[1][1] * 0.5D;
            double d6 = (double)k + 0.5D + (double)ai[1][2] * 0.5D;
            double d7 = d4 - d1;
            double d8 = (d5 - d2) * 2D;
            double d9 = d6 - d3;

            if (d7 == 0.0D)
            {
                par1 = (double)i + 0.5D;
                d = par5 - (double)k;
            }
            else if (d9 == 0.0D)
            {
                par5 = (double)k + 0.5D;
                d = par1 - (double)i;
            }
            else
            {
                double d10 = par1 - d1;
                double d11 = par5 - d3;
                double d12 = (d10 * d7 + d11 * d9) * 2D;
                d = d12;
            }

            par1 = d1 + d7 * d;
            par3 = d2 + d8 * d;
            par5 = d3 + d9 * d;

            if (d8 < 0.0D)
            {
                par3++;
            }

            if (d8 > 0.0D)
            {
                par3 += 0.5D;
            }

            return Vec3D.createVector(par1, par3, par5);
        }
        else
        {
            return null;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("Type", minecartType);

        if (minecartType == 2)
        {
            par1NBTTagCompound.setDouble("PushX", pushX);
            par1NBTTagCompound.setDouble("PushZ", pushZ);
            par1NBTTagCompound.setShort("Fuel", (short)fuel);
        }
        else if (minecartType == 1)
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 0; i < cargoItems.length; i++)
            {
                if (cargoItems[i] != null)
                {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("Slot", (byte)i);
                    cargoItems[i].writeToNBT(nbttagcompound);
                    nbttaglist.appendTag(nbttagcompound);
                }
            }

            par1NBTTagCompound.setTag("Items", nbttaglist);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        minecartType = par1NBTTagCompound.getInteger("Type");

        if (minecartType == 2)
        {
            pushX = par1NBTTagCompound.getDouble("PushX");
            pushZ = par1NBTTagCompound.getDouble("PushZ");
            fuel = par1NBTTagCompound.getShort("Fuel");
        }
        else if (minecartType == 1)
        {
            NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
            cargoItems = new ItemStack[getSizeInventory()];

            for (int i = 0; i < nbttaglist.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
                int j = nbttagcompound.getByte("Slot") & 0xff;

                if (j >= 0 && j < cargoItems.length)
                {
                    cargoItems[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
                }
            }
        }
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity par1Entity)
    {
        if (worldObj.isRemote)
        {
            return;
        }

        if (par1Entity == riddenByEntity)
        {
            return;
        }

        if ((par1Entity instanceof EntityLiving) && !(par1Entity instanceof EntityPlayer) && !(par1Entity instanceof EntityIronGolem) && minecartType == 0 && motionX * motionX + motionZ * motionZ > 0.01D && riddenByEntity == null && par1Entity.ridingEntity == null)
        {
            par1Entity.mountEntity(this);
        }

        double d = par1Entity.posX - posX;
        double d1 = par1Entity.posZ - posZ;
        double d2 = d * d + d1 * d1;

        if (d2 >= 9.9999997473787516E-005D)
        {
            d2 = MathHelper.sqrt_double(d2);
            d /= d2;
            d1 /= d2;
            double d3 = 1.0D / d2;

            if (d3 > 1.0D)
            {
                d3 = 1.0D;
            }

            d *= d3;
            d1 *= d3;
            d *= 0.10000000149011612D;
            d1 *= 0.10000000149011612D;
            d *= 1.0F - entityCollisionReduction;
            d1 *= 1.0F - entityCollisionReduction;
            d *= 0.5D;
            d1 *= 0.5D;

            if (par1Entity instanceof EntityMinecart)
            {
                double d4 = par1Entity.motionX + motionX;
                double d5 = par1Entity.motionZ + motionZ;

                if (((EntityMinecart)par1Entity).minecartType == 2 && minecartType != 2)
                {
                    motionX *= 0.20000000298023224D;
                    motionZ *= 0.20000000298023224D;
                    addVelocity(par1Entity.motionX - d, 0.0D, par1Entity.motionZ - d1);
                    par1Entity.motionX *= 0.94999998807907104D;
                    par1Entity.motionZ *= 0.94999998807907104D;
                }
                else if (((EntityMinecart)par1Entity).minecartType != 2 && minecartType == 2)
                {
                    par1Entity.motionX *= 0.20000000298023224D;
                    par1Entity.motionZ *= 0.20000000298023224D;
                    par1Entity.addVelocity(motionX + d, 0.0D, motionZ + d1);
                    motionX *= 0.94999998807907104D;
                    motionZ *= 0.94999998807907104D;
                }
                else
                {
                    d4 /= 2D;
                    d5 /= 2D;
                    motionX *= 0.20000000298023224D;
                    motionZ *= 0.20000000298023224D;
                    addVelocity(d4 - d, 0.0D, d5 - d1);
                    par1Entity.motionX *= 0.20000000298023224D;
                    par1Entity.motionZ *= 0.20000000298023224D;
                    par1Entity.addVelocity(d4 + d, 0.0D, d5 + d1);
                }
            }
            else
            {
                addVelocity(-d, 0.0D, -d1);
                par1Entity.addVelocity(d / 4D, 0.0D, d1 / 4D);
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 27;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return cargoItems[par1];
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (cargoItems[par1] != null)
        {
            if (cargoItems[par1].stackSize <= par2)
            {
                ItemStack itemstack = cargoItems[par1];
                cargoItems[par1] = null;
                return itemstack;
            }

            ItemStack itemstack1 = cargoItems[par1].splitStack(par2);

            if (cargoItems[par1].stackSize == 0)
            {
                cargoItems[par1] = null;
            }

            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (cargoItems[par1] != null)
        {
            ItemStack itemstack = cargoItems[par1];
            cargoItems[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        cargoItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > getInventoryStackLimit())
        {
            par2ItemStack.stackSize = getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "container.minecart";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        if (minecartType == 0)
        {
            if (riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != par1EntityPlayer)
            {
                return true;
            }

            if (!worldObj.isRemote)
            {
                par1EntityPlayer.mountEntity(this);
            }
        }
        else if (minecartType == 1)
        {
            if (!worldObj.isRemote)
            {
                par1EntityPlayer.displayGUIChest(this);
            }
        }
        else if (minecartType == 2)
        {
            ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

            if (itemstack != null && itemstack.itemID == Item.coal.shiftedIndex)
            {
                if (--itemstack.stackSize == 0)
                {
                    par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                }

                fuel += 3600;
            }

            pushX = posX - par1EntityPlayer.posX;
            pushZ = posZ - par1EntityPlayer.posZ;
        }

        return true;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        if (isDead)
        {
            return false;
        }

        return par1EntityPlayer.getDistanceSqToEntity(this) <= 64D;
    }

    /**
     * Is this minecart powered (Fuel > 0)
     */
    protected boolean isMinecartPowered()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    /**
     * Set if this minecart is powered (Fuel > 0)
     */
    protected void setMinecartPowered(boolean par1)
    {
        if (par1)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(dataWatcher.getWatchableObjectByte(16) | 1)));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(dataWatcher.getWatchableObjectByte(16) & -2)));
        }
    }

    public void openChest()
    {
    }

    public void closeChest()
    {
    }

    public void func_41018_e_(int par1)
    {
        dataWatcher.updateObject(19, Integer.valueOf(par1));
    }

    public int func_41020_o()
    {
        return dataWatcher.getWatchableObjectInt(19);
    }

    public void func_41014_b(int par1)
    {
        dataWatcher.updateObject(17, Integer.valueOf(par1));
    }

    public int func_41019_p()
    {
        return dataWatcher.getWatchableObjectInt(17);
    }

    public void func_41016_d(int par1)
    {
        dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    public int func_41021_q()
    {
        return dataWatcher.getWatchableObjectInt(18);
    }
}
