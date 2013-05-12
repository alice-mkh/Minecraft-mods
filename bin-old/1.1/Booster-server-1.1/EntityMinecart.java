package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityMinecart extends Entity
    implements IInventory
{
    private ItemStack cargoItems[];
    private int fuel;
    private boolean field_469_aj;
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
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double field_9159_ar;
    private double minecartPitch;

    public EntityMinecart(World world)
    {
        super(world);
        cargoItems = new ItemStack[36];
        fuel = 0;
        field_469_aj = false;
        preventEntitySpawning = true;
        setSize(0.98F, 0.7F);
        yOffset = height / 2.0F;
    }

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

    public AxisAlignedBB getCollisionBox(Entity entity)
    {
        return entity.boundingBox;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    public boolean canBePushed()
    {
        return true;
    }

    public EntityMinecart(World world, double d, double d1, double d2,
            int i)
    {
        this(world);
        setPosition(d, d1 + (double)yOffset, d2);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = d;
        prevPosY = d1;
        prevPosZ = d2;
        minecartType = i;
    }

    public double getMountedYOffset()
    {
        return (double)height * 0.0D - 0.30000001192092896D;
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (worldObj.singleplayerWorld || isDead)
        {
            return true;
        }
        func_41016_d(-func_41021_q());
        func_41014_b(10);
        setBeenAttacked();
        func_41018_e_(func_41020_o() + i * 10);
        if (func_41020_o() > 40)
        {
            if (riddenByEntity != null)
            {
                riddenByEntity.mountEntity(this);
            }
            setEntityDead();
            dropItemWithOffset(Item.minecartEmpty.shiftedIndex, 1, 0.0F);
            if (minecartType == 1)
            {
                EntityMinecart entityminecart = this;
                label0:
                for (int j = 0; j < entityminecart.getSizeInventory(); j++)
                {
                    ItemStack itemstack = entityminecart.getStackInSlot(j);
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
                        int k = rand.nextInt(21) + 10;
                        if (k > itemstack.stackSize)
                        {
                            k = itemstack.stackSize;
                        }
                        itemstack.stackSize -= k;
                        EntityItem entityitem = new EntityItem(worldObj, posX + (double)f, posY + (double)f1, posZ + (double)f2, new ItemStack(itemstack.itemID, k, itemstack.getItemDamage()));
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

    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    public void setEntityDead()
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
                float f3 = 0.05F;
                entityitem.motionX = (float)rand.nextGaussian() * f3;
                entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)rand.nextGaussian() * f3;
                worldObj.spawnEntityInWorld(entityitem);
            }
            while (true);
        }

        super.setEntityDead();
    }

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
        if (isMinecartPowered() && rand.nextInt(4) == 0)
        {
            worldObj.spawnParticle("largesmoke", posX, posY + 0.80000000000000004D, posZ, 0.0D, 0.0D, 0.0D);
        }
        if (worldObj.singleplayerWorld)
        {
            if (turnProgress > 0)
            {
                double d = posX + (minecartX - posX) / (double)turnProgress;
                double d1 = posY + (minecartY - posY) / (double)turnProgress;
                double d3 = posZ + (minecartZ - posZ) / (double)turnProgress;
                double d5;
                for (d5 = field_9159_ar - (double)rotationYaw; d5 < -180D; d5 += 360D) { }
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
            if (((BlockRail)Block.blocksList[l]).getIsPowered())
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
                    double d32 = 0.040000000000000001D;
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
            rotationYaw = (float)((Math.atan2(d7, d6) * 180D) / 3.1415926535897931D);
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

    public Vec3D func_182_g(double d, double d1, double d2)
    {
        int i = MathHelper.floor_double(d);
        int j = MathHelper.floor_double(d1);
        int k = MathHelper.floor_double(d2);
        if (BlockRail.isRailBlockAt(worldObj, i, j - 1, k))
        {
            j--;
        }
        int l = worldObj.getBlockId(i, j, k);
        if (BlockRail.isRailBlock(l))
        {
            int i1 = worldObj.getBlockMetadata(i, j, k);
            d1 = j;
            if (((BlockRail)Block.blocksList[l]).getIsPowered())
            {
                i1 &= 7;
            }
            if (i1 >= 2 && i1 <= 5)
            {
                d1 = j + 1;
            }
            int ai[][] = field_468_ak[i1];
            double d3 = 0.0D;
            double d4 = (double)i + 0.5D + (double)ai[0][0] * 0.5D;
            double d5 = (double)j + 0.5D + (double)ai[0][1] * 0.5D;
            double d6 = (double)k + 0.5D + (double)ai[0][2] * 0.5D;
            double d7 = (double)i + 0.5D + (double)ai[1][0] * 0.5D;
            double d8 = (double)j + 0.5D + (double)ai[1][1] * 0.5D;
            double d9 = (double)k + 0.5D + (double)ai[1][2] * 0.5D;
            double d10 = d7 - d4;
            double d11 = (d8 - d5) * 2D;
            double d12 = d9 - d6;
            if (d10 == 0.0D)
            {
                d = (double)i + 0.5D;
                d3 = d2 - (double)k;
            }
            else if (d12 == 0.0D)
            {
                d2 = (double)k + 0.5D;
                d3 = d - (double)i;
            }
            else
            {
                double d13 = d - d4;
                double d14 = d2 - d6;
                double d15 = (d13 * d10 + d14 * d12) * 2D;
                d3 = d15;
            }
            d = d4 + d10 * d3;
            d1 = d5 + d11 * d3;
            d2 = d6 + d12 * d3;
            if (d11 < 0.0D)
            {
                d1++;
            }
            if (d11 > 0.0D)
            {
                d1 += 0.5D;
            }
            return Vec3D.createVector(d, d1, d2);
        }
        else
        {
            return null;
        }
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger("Type", minecartType);
        if (minecartType == 2)
        {
            nbttagcompound.setDouble("PushX", pushX);
            nbttagcompound.setDouble("PushZ", pushZ);
            nbttagcompound.setShort("Fuel", (short)fuel);
        }
        else if (minecartType == 1)
        {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 0; i < cargoItems.length; i++)
            {
                if (cargoItems[i] != null)
                {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    nbttagcompound1.setByte("Slot", (byte)i);
                    cargoItems[i].writeToNBT(nbttagcompound1);
                    nbttaglist.setTag(nbttagcompound1);
                }
            }

            nbttagcompound.setTag("Items", nbttaglist);
        }
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        minecartType = nbttagcompound.getInteger("Type");
        if (minecartType == 2)
        {
            pushX = nbttagcompound.getDouble("PushX");
            pushZ = nbttagcompound.getDouble("PushZ");
            fuel = nbttagcompound.getShort("Fuel");
        }
        else if (minecartType == 1)
        {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
            cargoItems = new ItemStack[getSizeInventory()];
            for (int i = 0; i < nbttaglist.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
                int j = nbttagcompound1.getByte("Slot") & 0xff;
                if (j >= 0 && j < cargoItems.length)
                {
                    cargoItems[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
                }
            }
        }
    }

    public void applyEntityCollision(Entity entity)
    {
        if (worldObj.singleplayerWorld)
        {
            return;
        }
        if (entity == riddenByEntity)
        {
            return;
        }
        if ((entity instanceof EntityLiving) && !(entity instanceof EntityPlayer) && minecartType == 0 && motionX * motionX + motionZ * motionZ > 0.01D && riddenByEntity == null && entity.ridingEntity == null)
        {
            entity.mountEntity(this);
        }
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;
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
            if (entity instanceof EntityMinecart)
            {
                double d4 = entity.motionX + motionX;
                double d5 = entity.motionZ + motionZ;
                if (((EntityMinecart)entity).minecartType == 2 && minecartType != 2)
                {
                    motionX *= 0.20000000298023224D;
                    motionZ *= 0.20000000298023224D;
                    addVelocity(entity.motionX - d, 0.0D, entity.motionZ - d1);
                    entity.motionX *= 0.94999998807907104D;
                    entity.motionZ *= 0.94999998807907104D;
                }
                else if (((EntityMinecart)entity).minecartType != 2 && minecartType == 2)
                {
                    entity.motionX *= 0.20000000298023224D;
                    entity.motionZ *= 0.20000000298023224D;
                    entity.addVelocity(motionX + d, 0.0D, motionZ + d1);
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
                    entity.motionX *= 0.20000000298023224D;
                    entity.motionZ *= 0.20000000298023224D;
                    entity.addVelocity(d4 + d, 50.0D, d5 + d1);
                }
            }
            else
            {
                addVelocity(-d, 0.0D, -d1);
                entity.addVelocity(d / 4D, 0.0D, d1 / 4D);
            }
        }
    }

    public int getSizeInventory()
    {
        return 27;
    }

    public ItemStack getStackInSlot(int i)
    {
        return cargoItems[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if (cargoItems[i] != null)
        {
            if (cargoItems[i].stackSize <= j)
            {
                ItemStack itemstack = cargoItems[i];
                cargoItems[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = cargoItems[i].splitStack(j);
            if (cargoItems[i].stackSize == 0)
            {
                cargoItems[i] = null;
            }
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        cargoItems[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    public String getInvName()
    {
        return "Minecart";
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void onInventoryChanged()
    {
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        if (minecartType == 0)
        {
            if (riddenByEntity != null && (riddenByEntity instanceof EntityPlayer) && riddenByEntity != entityplayer)
            {
                return true;
            }
            if (!worldObj.singleplayerWorld)
            {
                entityplayer.mountEntity(this);
            }
        }
        else if (minecartType == 1)
        {
            if (!worldObj.singleplayerWorld)
            {
                entityplayer.displayGUIChest(this);
            }
        }
        else if (minecartType == 2)
        {
            ItemStack itemstack = entityplayer.inventory.getCurrentItem();
            if (itemstack != null && itemstack.itemID == Item.coal.shiftedIndex)
            {
                if (--itemstack.stackSize == 0)
                {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                }
                fuel += 3600;
            }
            pushX = posX - entityplayer.posX;
            pushZ = posZ - entityplayer.posZ;
        }
        return true;
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (isDead)
        {
            return false;
        }
        return entityplayer.getDistanceSqToEntity(this) <= 64D;
    }

    protected boolean isMinecartPowered()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    protected void setMinecartPowered(boolean flag)
    {
        if (flag)
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

    public void func_41018_e_(int i)
    {
        dataWatcher.updateObject(19, Integer.valueOf(i));
    }

    public int func_41020_o()
    {
        return dataWatcher.getWatchableObjectInt(19);
    }

    public void func_41014_b(int i)
    {
        dataWatcher.updateObject(17, Integer.valueOf(i));
    }

    public int func_41019_p()
    {
        return dataWatcher.getWatchableObjectInt(17);
    }

    public void func_41016_d(int i)
    {
        dataWatcher.updateObject(18, Integer.valueOf(i));
    }

    public int func_41021_q()
    {
        return dataWatcher.getWatchableObjectInt(18);
    }
}
