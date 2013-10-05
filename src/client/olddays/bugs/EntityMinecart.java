package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public abstract class EntityMinecart extends Entity
{
    public static boolean boosters = false;

    private boolean isInReverse;
    private final IUpdatePlayerListBox field_82344_g;
    private String entityName;
    private static final int matrix[][][] =
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
    private double velocityX;
    private double velocityY;
    private double velocityZ;

    public EntityMinecart(World par1World)
    {
        super(par1World);
        preventEntitySpawning = true;
        setSize(0.98F, 0.7F);
        yOffset = height / 2.0F;
        field_82344_g = par1World == null ? null : par1World.getMinecartSoundUpdater(this);
    }

    /**
     * Creates a new minecart of the specified type in the specified location in the given world. par0World - world to
     * create the minecart in, double par1,par3,par5 represent x,y,z respectively. int par7 specifies the type: 1 for
     * MinecartChest, 2 for MinecartFurnace, 3 for MinecartTNT, 4 for MinecartMobSpawner, 5 for MinecartHopper and 0 for
     * a standard empty minecart
     */
    public static EntityMinecart createMinecart(World par0World, double par1, double par3, double par5, int par7)
    {
        switch (par7)
        {
            case 1:
                return new EntityMinecartChest(par0World, par1, par3, par5);
            case 2:
                return new EntityMinecartFurnace(par0World, par1, par3, par5);
            case 3:
                return new EntityMinecartTNT(par0World, par1, par3, par5);
            case 4:
                return new EntityMinecartMobSpawner(par0World, par1, par3, par5);
            case 5:
                return new EntityMinecartHopper(par0World, par1, par3, par5);
        }

        return new EntityMinecartEmpty(par0World, par1, par3, par5);
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
        dataWatcher.addObject(17, new Integer(0));
        dataWatcher.addObject(18, new Integer(1));
        dataWatcher.addObject(19, new Float(0.0F));
        dataWatcher.addObject(20, new Integer(0));
        dataWatcher.addObject(21, new Integer(6));
        dataWatcher.addObject(22, Byte.valueOf((byte)0));
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        if (par1Entity.canBePushed())
        {
            return par1Entity.boundingBox;
        }
        else
        {
            return null;
        }
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

    public EntityMinecart(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
        setPosition(par2, par4, par6);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = par2;
        prevPosY = par4;
        prevPosZ = par6;
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
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (worldObj.isRemote || isDead)
        {
            return true;
        }

        if (isEntityInvulnerable())
        {
            return false;
        }

        setRollingDirection(-getRollingDirection());
        setRollingAmplitude(10);
        setBeenAttacked();
        setDamage(getDamage() + par2 * 10F);
        boolean flag = (par1DamageSource.getEntity() instanceof EntityPlayer) && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode;

        if (flag || getDamage() > 40F)
        {
            if (riddenByEntity != null)
            {
                riddenByEntity.mountEntity(this);
            }

            if (!flag || isInvNameLocalized())
            {
                killMinecart(par1DamageSource);
            }
            else
            {
                setDead();
            }
        }

        return true;
    }

    public void killMinecart(DamageSource par1DamageSource)
    {
        setDead();
        ItemStack itemstack = new ItemStack(Item.minecartEmpty, 1);

        if (entityName != null)
        {
            itemstack.setItemName(entityName);
        }

        entityDropItem(itemstack, 0.0F);
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
        setRollingDirection(-getRollingDirection());
        setRollingAmplitude(10);
        setDamage(getDamage() + getDamage() * 10F);
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
        super.setDead();

        if (field_82344_g != null)
        {
            field_82344_g.update();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (field_82344_g != null)
        {
            field_82344_g.update();
        }

        if (getRollingAmplitude() > 0)
        {
            setRollingAmplitude(getRollingAmplitude() - 1);
        }

        if (getDamage() > 0.0F)
        {
            setDamage(getDamage() - 1.0F);
        }

        if (posY < -64D)
        {
            kill();
        }

        if (!worldObj.isRemote && (worldObj instanceof WorldServer))
        {
            worldObj.theProfiler.startSection("portal");
            MinecraftServer minecraftserver = ((WorldServer)worldObj).getMinecraftServer();
            int j = getMaxInPortalTime();

            if (inPortal)
            {
                if (minecraftserver.getAllowNether())
                {
                    if (ridingEntity == null && portalCounter++ >= j)
                    {
                        portalCounter = j;
                        timeUntilPortal = getPortalCooldown();
                        byte byte0;

                        if (worldObj.provider.dimensionId == -1)
                        {
                            byte0 = 0;
                        }
                        else
                        {
                            byte0 = -1;
                        }

                        travelToDimension(byte0);
                    }

                    inPortal = false;
                }
            }
            else
            {
                if (portalCounter > 0)
                {
                    portalCounter -= 4;
                }

                if (portalCounter < 0)
                {
                    portalCounter = 0;
                }
            }

            if (timeUntilPortal > 0)
            {
                timeUntilPortal--;
            }

            worldObj.theProfiler.endSection();
        }

        if (worldObj.isRemote)
        {
            if (turnProgress > 0)
            {
                double d = posX + (minecartX - posX) / (double)turnProgress;
                double d1 = posY + (minecartY - posY) / (double)turnProgress;
                double d3 = posZ + (minecartZ - posZ) / (double)turnProgress;
                double d5 = MathHelper.wrapAngleTo180_double(minecartYaw - (double)rotationYaw);
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
        int k = MathHelper.floor_double(posY);
        int l = MathHelper.floor_double(posZ);

        if (BlockRailBase.isRailBlockAt(worldObj, i, k - 1, l))
        {
            k--;
        }

        double d2 = 0.40000000000000002D;
        double d4 = 0.0078125D;
        int i1 = worldObj.getBlockId(i, k, l);

        if (BlockRailBase.isRailBlock(i1))
        {
            int j1 = worldObj.getBlockMetadata(i, k, l);
            updateOnTrack(i, k, l, d2, d4, i1, j1);

            if (i1 == Block.railActivator.blockID)
            {
                onActivatorRailPass(i, k, l, (j1 & 8) != 0);
            }
        }
        else
        {
            func_94088_b(d2);
        }

        doBlockCollisions();
        rotationPitch = 0.0F;
        double d6 = prevPosX - posX;
        double d7 = prevPosZ - posZ;

        if (d6 * d6 + d7 * d7 > 0.001D)
        {
            rotationYaw = (float)((Math.atan2(d7, d6) * 180D) / Math.PI);

            if (isInReverse)
            {
                rotationYaw += 180F;
            }
        }

        double d8 = MathHelper.wrapAngleTo180_float(rotationYaw - prevRotationYaw);

        if (d8 < -170D || d8 >= 170D)
        {
            rotationYaw += 180F;
            isInReverse = !isInReverse;
        }

        setRotation(rotationYaw, rotationPitch);
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && !list.isEmpty())
        {
            for (int k1 = 0; k1 < list.size(); k1++)
            {
                Entity entity = (Entity)list.get(k1);

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
    }

    /**
     * Called every tick the minecart is on an activator rail.
     */
    public void onActivatorRailPass(int i, int j, int k, boolean flag)
    {
    }

    protected void func_94088_b(double par1)
    {
        if (motionX < -par1)
        {
            motionX = -par1;
        }

        if (motionX > par1)
        {
            motionX = par1;
        }

        if (motionZ < -par1)
        {
            motionZ = -par1;
        }

        if (motionZ > par1)
        {
            motionZ = par1;
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

    protected void updateOnTrack(int par1, int par2, int par3, double par4, double par6, int par8, int par9)
    {
        fallDistance = 0.0F;
        Vec3 vec3 = func_70489_a(posX, posY, posZ);
        posY = par2;
        boolean flag = false;
        boolean flag1 = false;

        if (par8 == Block.railPowered.blockID)
        {
            flag = (par9 & 8) != 0;
            flag1 = !flag;
        }

        if (((BlockRailBase)Block.blocksList[par8]).isPowered())
        {
            par9 &= 7;
        }

        if (par9 >= 2 && par9 <= 5)
        {
            posY = par2 + 1;
        }

        if (par9 == 2)
        {
            motionX -= par6;
        }

        if (par9 == 3)
        {
            motionX += par6;
        }

        if (par9 == 4)
        {
            motionZ += par6;
        }

        if (par9 == 5)
        {
            motionZ -= par6;
        }

        int ai[][] = matrix[par9];
        double d = ai[1][0] - ai[0][0];
        double d1 = ai[1][2] - ai[0][2];
        double d2 = Math.sqrt(d * d + d1 * d1);
        double d3 = motionX * d + motionZ * d1;

        if (d3 < 0.0D)
        {
            d = -d;
            d1 = -d1;
        }

        double d4 = Math.sqrt(motionX * motionX + motionZ * motionZ);

        if (!boosters && d4 > 2D)
        {
            d4 = 2D;
        }

        motionX = (d4 * d) / d2;
        motionZ = (d4 * d1) / d2;

        if (riddenByEntity != null && (riddenByEntity instanceof EntityLivingBase))
        {
            double d7 = ((EntityLivingBase)riddenByEntity).moveForward;

            if (d7 > 0.0D)
            {
                double d10 = -Math.sin((riddenByEntity.rotationYaw * (float)Math.PI) / 180F);
                double d12 = Math.cos((riddenByEntity.rotationYaw * (float)Math.PI) / 180F);
                double d14 = motionX * motionX + motionZ * motionZ;

                if (d14 < 0.01D)
                {
                    motionX += d10 * 0.10000000000000001D;
                    motionZ += d12 * 0.10000000000000001D;
                    flag1 = false;
                }
            }
        }

        if (flag1)
        {
            double d8 = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (d8 < 0.029999999999999999D)
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

        double d9 = 0.0D;
        double d11 = (double)par1 + 0.5D + (double)ai[0][0] * 0.5D;
        double d13 = (double)par3 + 0.5D + (double)ai[0][2] * 0.5D;
        double d15 = (double)par1 + 0.5D + (double)ai[1][0] * 0.5D;
        double d16 = (double)par3 + 0.5D + (double)ai[1][2] * 0.5D;
        d = d15 - d11;
        d1 = d16 - d13;

        if (d == 0.0D)
        {
            posX = (double)par1 + 0.5D;
            d9 = posZ - (double)par3;
        }
        else if (d1 == 0.0D)
        {
            posZ = (double)par3 + 0.5D;
            d9 = posX - (double)par1;
        }
        else
        {
            double d17 = posX - d11;
            double d19 = posZ - d13;
            d9 = (d17 * d + d19 * d1) * 2D;
        }

        posX = d11 + d * d9;
        posZ = d13 + d1 * d9;
        setPosition(posX, posY + (double)yOffset, posZ);
        double d18 = motionX;
        double d20 = motionZ;

        if (riddenByEntity != null)
        {
            d18 *= 0.75D;
            d20 *= 0.75D;
        }

        if (d18 < -par4)
        {
            d18 = -par4;
        }

        if (d18 > par4)
        {
            d18 = par4;
        }

        if (d20 < -par4)
        {
            d20 = -par4;
        }

        if (d20 > par4)
        {
            d20 = par4;
        }

        moveEntity(d18, 0.0D, d20);

        if (ai[0][1] != 0 && MathHelper.floor_double(posX) - par1 == ai[0][0] && MathHelper.floor_double(posZ) - par3 == ai[0][2])
        {
            setPosition(posX, posY + (double)ai[0][1], posZ);
        }
        else if (ai[1][1] != 0 && MathHelper.floor_double(posX) - par1 == ai[1][0] && MathHelper.floor_double(posZ) - par3 == ai[1][2])
        {
            setPosition(posX, posY + (double)ai[1][1], posZ);
        }

        applyDrag();
        Vec3 vec3_1 = func_70489_a(posX, posY, posZ);

        if (vec3_1 != null && vec3 != null)
        {
            double d21 = (vec3.yCoord - vec3_1.yCoord) * 0.050000000000000003D;
            double d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (d5 > 0.0D)
            {
                motionX = (motionX / d5) * (d5 + d21);
                motionZ = (motionZ / d5) * (d5 + d21);
            }

            setPosition(posX, vec3_1.yCoord, posZ);
        }

        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posZ);

        if (i != par1 || j != par3)
        {
            double d6 = Math.sqrt(motionX * motionX + motionZ * motionZ);
            motionX = d6 * (double)(i - par1);
            motionZ = d6 * (double)(j - par3);
        }

        if (flag)
        {
            double d22 = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (d22 > 0.01D)
            {
                double d23 = 0.059999999999999998D;
                if (boosters){
                    d23 = 0.04D;
                }
                motionX += (motionX / d22) * d23;
                motionZ += (motionZ / d22) * d23;
            }
            else if (par9 == 1)
            {
                if (worldObj.isBlockNormalCube(par1 - 1, par2, par3))
                {
                    motionX = 0.02D;
                }
                else if (worldObj.isBlockNormalCube(par1 + 1, par2, par3))
                {
                    motionX = -0.02D;
                }
            }
            else if (par9 == 0)
            {
                if (worldObj.isBlockNormalCube(par1, par2, par3 - 1))
                {
                    motionZ = 0.02D;
                }
                else if (worldObj.isBlockNormalCube(par1, par2, par3 + 1))
                {
                    motionZ = -0.02D;
                }
            }
        }
    }

    protected void applyDrag()
    {
        if (riddenByEntity != null)
        {
            motionX *= 0.99699997901916504D;
            motionY *= 0.0D;
            motionZ *= 0.99699997901916504D;
        }
        else
        {
            motionX *= 0.95999997854232788D;
            motionY *= 0.0D;
            motionZ *= 0.95999997854232788D;
        }
    }

    public Vec3 func_70495_a(double par1, double par3, double par5, double par7)
    {
        int i = MathHelper.floor_double(par1);
        int j = MathHelper.floor_double(par3);
        int k = MathHelper.floor_double(par5);

        if (BlockRailBase.isRailBlockAt(worldObj, i, j - 1, k))
        {
            j--;
        }

        int l = worldObj.getBlockId(i, j, k);

        if (BlockRailBase.isRailBlock(l))
        {
            int i1 = worldObj.getBlockMetadata(i, j, k);

            if (((BlockRailBase)Block.blocksList[l]).isPowered())
            {
                i1 &= 7;
            }

            par3 = j;

            if (i1 >= 2 && i1 <= 5)
            {
                par3 = j + 1;
            }

            int ai[][] = matrix[i1];
            double d = ai[1][0] - ai[0][0];
            double d1 = ai[1][2] - ai[0][2];
            double d2 = Math.sqrt(d * d + d1 * d1);
            d /= d2;
            d1 /= d2;
            par1 += d * par7;
            par5 += d1 * par7;

            if (ai[0][1] != 0 && MathHelper.floor_double(par1) - i == ai[0][0] && MathHelper.floor_double(par5) - k == ai[0][2])
            {
                par3 += ai[0][1];
            }
            else if (ai[1][1] != 0 && MathHelper.floor_double(par1) - i == ai[1][0] && MathHelper.floor_double(par5) - k == ai[1][2])
            {
                par3 += ai[1][1];
            }

            return func_70489_a(par1, par3, par5);
        }
        else
        {
            return null;
        }
    }

    public Vec3 func_70489_a(double par1, double par3, double par5)
    {
        int i = MathHelper.floor_double(par1);
        int j = MathHelper.floor_double(par3);
        int k = MathHelper.floor_double(par5);

        if (BlockRailBase.isRailBlockAt(worldObj, i, j - 1, k))
        {
            j--;
        }

        int l = worldObj.getBlockId(i, j, k);

        if (BlockRailBase.isRailBlock(l))
        {
            int i1 = worldObj.getBlockMetadata(i, j, k);
            par3 = j;

            if (((BlockRailBase)Block.blocksList[l]).isPowered())
            {
                i1 &= 7;
            }

            if (i1 >= 2 && i1 <= 5)
            {
                par3 = j + 1;
            }

            int ai[][] = matrix[i1];
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
                d = (d10 * d7 + d11 * d9) * 2D;
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

            return worldObj.getWorldVec3Pool().getVecFromPool(par1, par3, par5);
        }
        else
        {
            return null;
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (par1NBTTagCompound.getBoolean("CustomDisplayTile"))
        {
            setDisplayTile(par1NBTTagCompound.getInteger("DisplayTile"));
            setDisplayTileData(par1NBTTagCompound.getInteger("DisplayData"));
            setDisplayTileOffset(par1NBTTagCompound.getInteger("DisplayOffset"));
        }

        if (par1NBTTagCompound.hasKey("CustomName") && par1NBTTagCompound.getString("CustomName").length() > 0)
        {
            entityName = par1NBTTagCompound.getString("CustomName");
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (hasDisplayTile())
        {
            par1NBTTagCompound.setBoolean("CustomDisplayTile", true);
            par1NBTTagCompound.setInteger("DisplayTile", getDisplayTile() != null ? getDisplayTile().blockID : 0);
            par1NBTTagCompound.setInteger("DisplayData", getDisplayTileData());
            par1NBTTagCompound.setInteger("DisplayOffset", getDisplayTileOffset());
        }

        if (entityName != null && entityName.length() > 0)
        {
            par1NBTTagCompound.setString("CustomName", entityName);
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
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

        if ((par1Entity instanceof EntityLivingBase) && !(par1Entity instanceof EntityPlayer) && !(par1Entity instanceof EntityIronGolem) && getMinecartType() == 0 && motionX * motionX + motionZ * motionZ > 0.01D && riddenByEntity == null && par1Entity.ridingEntity == null)
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
                if (!boosters){
                    double d4 = par1Entity.posX - posX;
                    double d5 = par1Entity.posZ - posZ;
                    Vec3 vec3 = worldObj.getWorldVec3Pool().getVecFromPool(d4, 0.0D, d5).normalize();
                    Vec3 vec3_1 = worldObj.getWorldVec3Pool().getVecFromPool(MathHelper.cos((rotationYaw * (float)Math.PI) / 180F), 0.0D, MathHelper.sin((rotationYaw * (float)Math.PI) / 180F)).normalize();
                    double d6 = Math.abs(vec3.dotProduct(vec3_1));

                    if (d6 < 0.80000001192092896D)
                    {
                        return;
                    }
                }
                double d7 = par1Entity.motionX + motionX;
                double d8 = par1Entity.motionZ + motionZ;

                if (((EntityMinecart)par1Entity).getMinecartType() == 2 && getMinecartType() != 2)
                {
                    motionX *= 0.20000000298023224D;
                    motionZ *= 0.20000000298023224D;
                    addVelocity(par1Entity.motionX - d, 0.0D, par1Entity.motionZ - d1);
                    par1Entity.motionX *= 0.94999998807907104D;
                    par1Entity.motionZ *= 0.94999998807907104D;
                }
                else if (((EntityMinecart)par1Entity).getMinecartType() != 2 && getMinecartType() == 2)
                {
                    par1Entity.motionX *= 0.20000000298023224D;
                    par1Entity.motionZ *= 0.20000000298023224D;
                    par1Entity.addVelocity(motionX + d, 0.0D, motionZ + d1);
                    motionX *= 0.94999998807907104D;
                    motionZ *= 0.94999998807907104D;
                }
                else
                {
                    d7 /= 2D;
                    d8 /= 2D;
                    motionX *= 0.20000000298023224D;
                    motionZ *= 0.20000000298023224D;
                    addVelocity(d7 - d, 0.0D, d8 - d1);
                    par1Entity.motionX *= 0.20000000298023224D;
                    par1Entity.motionZ *= 0.20000000298023224D;
                    par1Entity.addVelocity(d7 + d, 0.0D, d8 + d1);
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
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        minecartX = par1;
        minecartY = par3;
        minecartZ = par5;
        minecartYaw = par7;
        minecartPitch = par8;
        turnProgress = par9 + 2;
        motionX = velocityX;
        motionY = velocityY;
        motionZ = velocityZ;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double par1, double par3, double par5)
    {
        velocityX = motionX = par1;
        velocityY = motionY = par3;
        velocityZ = motionZ = par5;
    }

    /**
     * Sets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public void setDamage(float par1)
    {
        dataWatcher.updateObject(19, Float.valueOf(par1));
    }

    /**
     * Gets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public float getDamage()
    {
        return dataWatcher.getWatchableObjectFloat(19);
    }

    /**
     * Sets the rolling amplitude the cart rolls while being attacked.
     */
    public void setRollingAmplitude(int par1)
    {
        dataWatcher.updateObject(17, Integer.valueOf(par1));
    }

    /**
     * Gets the rolling amplitude the cart rolls while being attacked.
     */
    public int getRollingAmplitude()
    {
        return dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Sets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public void setRollingDirection(int par1)
    {
        dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    /**
     * Gets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public int getRollingDirection()
    {
        return dataWatcher.getWatchableObjectInt(18);
    }

    public abstract int getMinecartType();

    public Block getDisplayTile()
    {
        if (!hasDisplayTile())
        {
            return getDefaultDisplayTile();
        }
        else
        {
            int i = getDataWatcher().getWatchableObjectInt(20) & 0xffff;
            return i <= 0 || i >= Block.blocksList.length ? null : Block.blocksList[i];
        }
    }

    public Block getDefaultDisplayTile()
    {
        return null;
    }

    public int getDisplayTileData()
    {
        if (!hasDisplayTile())
        {
            return getDefaultDisplayTileData();
        }
        else
        {
            return getDataWatcher().getWatchableObjectInt(20) >> 16;
        }
    }

    public int getDefaultDisplayTileData()
    {
        return 0;
    }

    public int getDisplayTileOffset()
    {
        if (!hasDisplayTile())
        {
            return getDefaultDisplayTileOffset();
        }
        else
        {
            return getDataWatcher().getWatchableObjectInt(21);
        }
    }

    public int getDefaultDisplayTileOffset()
    {
        return 6;
    }

    public void setDisplayTile(int par1)
    {
        getDataWatcher().updateObject(20, Integer.valueOf(par1 & 0xffff | getDisplayTileData() << 16));
        setHasDisplayTile(true);
    }

    public void setDisplayTileData(int par1)
    {
        Block block = getDisplayTile();
        int i = block != null ? block.blockID : 0;
        getDataWatcher().updateObject(20, Integer.valueOf(i & 0xffff | par1 << 16));
        setHasDisplayTile(true);
    }

    public void setDisplayTileOffset(int par1)
    {
        getDataWatcher().updateObject(21, Integer.valueOf(par1));
        setHasDisplayTile(true);
    }

    public boolean hasDisplayTile()
    {
        return getDataWatcher().getWatchableObjectByte(22) == 1;
    }

    public void setHasDisplayTile(boolean par1)
    {
        getDataWatcher().updateObject(22, Byte.valueOf((byte)(par1 ? 1 : 0)));
    }

    /**
     * Sets the minecart's name.
     */
    public void setMinecartName(String par1Str)
    {
        entityName = par1Str;
    }

    /**
     * Gets the username of the entity.
     */
    public String getEntityName()
    {
        if (entityName != null)
        {
            return entityName;
        }
        else
        {
            return super.getEntityName();
        }
    }

    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
    public boolean isInvNameLocalized()
    {
        return entityName != null;
    }

    public String func_95999_t()
    {
        return entityName;
    }
}
