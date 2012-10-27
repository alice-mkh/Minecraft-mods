package net.minecraft.src;

import java.util.Calendar;
import java.util.Random;

public class EntityZombie extends EntityMob
{
    public static boolean burns = true;
    public static boolean defense = true;

    public boolean helmet;
    public boolean armor;

    private int field_82234_d;

    public EntityZombie(World par1World)
    {
        super(par1World);
        field_82234_d = 0;
        texture = "/mob/zombie.png";
        moveSpeed = 0.23F;
        helmet = Math.random() < 0.20000000298023224D;
        armor = Math.random() < 0.20000000298023224D;
        getNavigator().setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIBreakDoor(this));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, net.minecraft.src.EntityPlayer.class, moveSpeed, false));
        tasks.addTask(3, new EntityAIAttackOnCollide(this, net.minecraft.src.EntityVillager.class, moveSpeed, true));
        tasks.addTask(4, new EntityAIMoveTwardsRestriction(this, moveSpeed));
        tasks.addTask(5, new EntityAIMoveThroughVillage(this, moveSpeed, false));
        tasks.addTask(6, new EntityAIWander(this, moveSpeed));
        tasks.addTask(7, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 8F));
        tasks.addTask(7, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, net.minecraft.src.EntityPlayer.class, 16F, 0, true));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, net.minecraft.src.EntityVillager.class, 16F, 0, false));
    }

    /**
     * This method returns a value to be applied directly to entity speed, this factor is less than 1 when a slowdown
     * potion effect is applied, more than 1 when a haste potion effect is applied and 2 for fleeing entities.
     */
    public float getSpeedModifier()
    {
        return super.getSpeedModifier() * (isChild() ? 1.5F : 1.0F);
    }

    protected void entityInit()
    {
        super.entityInit();
        getDataWatcher().addObject(12, Byte.valueOf((byte)0));
        getDataWatcher().addObject(13, Byte.valueOf((byte)0));
        getDataWatcher().addObject(14, Byte.valueOf((byte)0));
    }

    /**
     * Returns the texture's file path as a String.
     */
    public String getTexture()
    {
        return func_82231_m() ? "/mob/zombie_villager.png" : "/mob/zombie.png";
    }

    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() || !burns;
    }

    public int getMaxHealth()
    {
        return /*FIXME survivaltest ? 10 : */20;
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue()
    {
        int i = super.getTotalArmorValue() + (defense ? 2 : 0);

        if (i > 20)
        {
            i = 20;
        }

        return i;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return getDataWatcher().getWatchableObjectByte(12) == 1;
    }

    public void func_82227_f(boolean par1)
    {
        getDataWatcher().updateObject(12, Byte.valueOf((byte)1));
    }

    public boolean func_82231_m()
    {
        return getDataWatcher().getWatchableObjectByte(13) == 1;
    }

    public void func_82229_g(boolean par1)
    {
        getDataWatcher().updateObject(13, Byte.valueOf((byte)(par1 ? 1 : 0)));
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (worldObj.isDaytime() && !worldObj.isRemote && !isChild() && burns)
        {
            float f = getBrightness(1.0F);

            if (f > 0.5F && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)))
            {
                boolean flag = true;
                ItemStack itemstack = getCurrentItemOrArmor(4);

                if (itemstack != null)
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + rand.nextInt(2));

                        if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
                        {
                            renderBrokenItemStack(itemstack);
                            func_70062_b(4, null);
                        }
                    }

                    flag = false;
                }

                if (flag)
                {
                    setFire(8);
                }
            }
        }

        super.onLivingUpdate();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (!worldObj.isRemote && func_82230_o())
        {
            int i = func_82233_q();
            field_82234_d -= i;

            if (field_82234_d <= 0)
            {
                func_82232_p();
            }
        }

        super.onUpdate();
    }

    public int func_82193_c(Entity par1Entity)
    {
        ItemStack itemstack = getHeldItem();
        int i = 4;

        if (itemstack != null)
        {
            i += itemstack.getDamageVsEntity(this);
        }

        return i;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.zombie.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.zombie.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.zombie.death";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        worldObj.playSoundAtEntity(this, "mob.zombie.step", 0.15F, 1.0F);
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return Item.rottenFlesh.shiftedIndex;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected void dropRareDrop(int par1)
    {
        switch (rand.nextInt(3))
        {
            case 0:
                dropItem(Item.ingotIron.shiftedIndex, 1);
                break;
            case 1:
                dropItem(Item.field_82797_bK.shiftedIndex, 1);
                break;
            case 2:
                dropItem(Item.field_82794_bL.shiftedIndex, 1);
                break;
        }
    }

    protected void func_82164_bB()
    {
        super.func_82164_bB();

        if (rand.nextFloat() < (worldObj.difficultySetting != 3 ? 0.01F : 0.05F))
        {
            int i = rand.nextInt(3);

            if (i == 0)
            {
                func_70062_b(0, new ItemStack(Item.swordSteel));
            }
            else
            {
                func_70062_b(0, new ItemStack(Item.shovelSteel));
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);

        if (isChild())
        {
            par1NBTTagCompound.setBoolean("IsBaby", true);
        }

        if (func_82231_m())
        {
            par1NBTTagCompound.setBoolean("IsVillager", true);
        }

        par1NBTTagCompound.setInteger("ConversionTime", func_82230_o() ? field_82234_d : -1);
        par1NBTTagCompound.setBoolean("Helmet", helmet);
        par1NBTTagCompound.setBoolean("Armor", armor);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.getBoolean("IsBaby"))
        {
            func_82227_f(true);
        }

        if (par1NBTTagCompound.getBoolean("IsVillager"))
        {
            func_82229_g(true);
        }

        if (par1NBTTagCompound.hasKey("ConversionTime") && par1NBTTagCompound.getInteger("ConversionTime") > -1)
        {
            func_82228_a(par1NBTTagCompound.getInteger("ConversionTime"));
        }
        helmet = par1NBTTagCompound.getBoolean("Helmet");
        armor = par1NBTTagCompound.getBoolean("Armor");
    }

    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLiving par1EntityLiving)
    {
        super.onKillEntity(par1EntityLiving);

        if (worldObj.difficultySetting >= 2 && (par1EntityLiving instanceof EntityVillager))
        {
            if (worldObj.difficultySetting == 2 && rand.nextBoolean())
            {
                return;
            }

            EntityZombie entityzombie = new EntityZombie(worldObj);
            entityzombie.func_82149_j(par1EntityLiving);
            worldObj.setEntityDead(par1EntityLiving);
            entityzombie.func_82163_bD();
            entityzombie.func_82229_g(true);

            if (par1EntityLiving.isChild())
            {
                entityzombie.func_82227_f(true);
            }

            worldObj.spawnEntityInWorld(entityzombie);
            worldObj.playAuxSFXAtEntity(null, 1016, (int)posX, (int)posY, (int)posZ, 0);
        }
    }

    public void func_82163_bD()
    {
        field_82172_bs = rand.nextFloat() < field_82181_as[worldObj.difficultySetting];

        if (worldObj.rand.nextFloat() < 0.05F)
        {
            func_82229_g(true);
        }

        func_82164_bB();
        func_82162_bC();

        if (getCurrentItemOrArmor(4) == null)
        {
            Calendar calendar = worldObj.func_83015_S();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && rand.nextFloat() < 0.25F)
            {
                func_70062_b(4, new ItemStack(rand.nextFloat() >= 0.1F ? Block.pumpkin : Block.pumpkinLantern));
                field_82174_bp[4] = 0.0F;
            }
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.getCurrentEquippedItem();

        if (itemstack != null && itemstack.getItem() == Item.appleGold && itemstack.getItemDamage() == 0 && func_82231_m() && isPotionActive(Potion.weakness))
        {
            if (!par1EntityPlayer.capabilities.isCreativeMode)
            {
                itemstack.stackSize--;
            }

            if (itemstack.stackSize <= 0)
            {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
            }

            if (!worldObj.isRemote)
            {
                func_82228_a(rand.nextInt(2401) + 3600);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void func_82228_a(int par1)
    {
        field_82234_d = par1;
        getDataWatcher().updateObject(14, Byte.valueOf((byte)1));
        func_82170_o(Potion.weakness.id);
        addPotionEffect(new PotionEffect(Potion.damageBoost.id, par1, Math.min(worldObj.difficultySetting - 1, 0)));
        worldObj.setEntityState(this, (byte)16);
    }

    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 16)
        {
            worldObj.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "mob.zombie.remedy", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    public boolean func_82230_o()
    {
        return getDataWatcher().getWatchableObjectByte(14) == 1;
    }

    protected void func_82232_p()
    {
        EntityVillager entityvillager = new EntityVillager(worldObj);
        entityvillager.func_82149_j(this);
        entityvillager.func_82163_bD();
        entityvillager.func_82187_q();

        if (isChild())
        {
            entityvillager.setGrowingAge(-24000);
        }

        worldObj.setEntityDead(this);
        worldObj.spawnEntityInWorld(entityvillager);
        entityvillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
        worldObj.playAuxSFXAtEntity(null, 1017, (int)posX, (int)posY, (int)posZ, 0);
    }

    protected int func_82233_q()
    {
        int i = 1;

        if (rand.nextFloat() < 0.01F)
        {
            int j = 0;

            for (int k = (int)posX - 4; k < (int)posX + 4 && j < 14; k++)
            {
                for (int l = (int)posY - 4; l < (int)posY + 4 && j < 14; l++)
                {
                    for (int i1 = (int)posZ - 4; i1 < (int)posZ + 4 && j < 14; i1++)
                    {
                        int j1 = worldObj.getBlockId(k, l, i1);

                        if (j1 != Block.fenceIron.blockID && j1 != Block.bed.blockID)
                        {
                            continue;
                        }

                        if (rand.nextFloat() < 0.3F)
                        {
                            i++;
                        }

                        j++;
                    }
                }
            }
        }

        return i;
    }
}
