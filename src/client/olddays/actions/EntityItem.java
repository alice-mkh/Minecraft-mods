package net.minecraft.src;

import java.util.*;

public class EntityItem extends Entity
{
    public static boolean smeltOnFire = false;

    /** The item stack of this EntityItem. */
    public ItemStack item;

    /**
     * The age of this EntityItem (used to animate it up and down as well as expire it)
     */
    public int age;
    public int delayBeforeCanPickup;

    /** The health of this EntityItem. (For example, damage for tools) */
    private int health;

    /** The EntityItem's random initial float height. */
    public float hoverStart;

    public EntityItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack)
    {
        super(par1World);
        age = 0;
        health = 5;
        hoverStart = (float)(Math.random() * Math.PI * 2D);
        setSize(0.25F, 0.25F);
        yOffset = height / 2.0F;
        setPosition(par2, par4, par6);
        item = par8ItemStack;
        rotationYaw = (float)(Math.random() * 360D);
        motionX = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
        motionY = 0.20000000298023224D;
        motionZ = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityItem(World par1World)
    {
        super(par1World);
        age = 0;
        health = 5;
        hoverStart = (float)(Math.random() * Math.PI * 2D);
        setSize(0.25F, 0.25F);
        yOffset = height / 2.0F;
    }

    protected void entityInit()
    {
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (delayBeforeCanPickup > 0)
        {
            delayBeforeCanPickup--;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= 0.039999999105930328D;
        pushOutOfBlocks(posX, (boundingBox.minY + boundingBox.maxY) / 2D, posZ);
        moveEntity(motionX, motionY, motionZ);
        boolean flag = (int)prevPosX != (int)posX || (int)prevPosY != (int)posY || (int)prevPosZ != (int)posZ;

        if (flag)
        {
            if (worldObj.getBlockMaterial(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) == Material.lava)
            {
                motionY = 0.20000000298023224D;
                motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
                motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
                worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + rand.nextFloat() * 0.4F);
            }

            if (!worldObj.isRemote)
            {
                EntityItem entityitem;

                for (Iterator iterator = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class, boundingBox.expand(0.5D, 0.0D, 0.5D)).iterator(); iterator.hasNext(); func_70289_a(entityitem))
                {
                    entityitem = (EntityItem)iterator.next();
                }
            }
        }

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
            motionY *= -0.5D;
        }

        age++;

        if (age >= 6000)
        {
            setDead();
        }
    }

    public boolean func_70289_a(EntityItem par1EntityItem)
    {
        if (par1EntityItem == this)
        {
            return false;
        }

        if (!par1EntityItem.isEntityAlive() || !isEntityAlive())
        {
            return false;
        }

        if (par1EntityItem.item.getItem() != item.getItem())
        {
            return false;
        }

        if (par1EntityItem.item.getItem().getHasSubtypes() && par1EntityItem.item.getItemDamage() != item.getItemDamage())
        {
            return false;
        }

        if (par1EntityItem.item.stackSize < item.stackSize)
        {
            return par1EntityItem.func_70289_a(this);
        }

        if (par1EntityItem.item.stackSize + item.stackSize > par1EntityItem.item.getMaxStackSize())
        {
            return false;
        }
        else
        {
            par1EntityItem.item.stackSize += item.stackSize;
            par1EntityItem.delayBeforeCanPickup = Math.max(par1EntityItem.delayBeforeCanPickup, delayBeforeCanPickup);
            par1EntityItem.age = Math.min(par1EntityItem.age, age);
            setDead();
            return true;
        }
    }

    public void func_70288_d()
    {
        age = 4800;
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
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        setBeenAttacked();
        if ((par1DamageSource == DamageSource.inFire || par1DamageSource == DamageSource.onFire || par1DamageSource == DamageSource.lava) && smeltOnFire){
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(item.getItem().shiftedIndex);
            if (item != itemstack && itemstack != null){
                item.itemID = itemstack.itemID;
                item.setItemDamage(itemstack.getItemDamage());
                int j = item.stackSize;
                for (int i = 0; i < j; i++){
                    item.stackSize+=(new Random()).nextInt(2);
                }
            }
        }else{
            health -= par2;
            if (health <= 0)
            {
                setDead();
            }
        }

        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("Health", (byte)health);
        par1NBTTagCompound.setShort("Age", (short)age);

        if (item != null)
        {
            par1NBTTagCompound.setCompoundTag("Item", item.writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        health = par1NBTTagCompound.getShort("Health") & 0xff;
        age = par1NBTTagCompound.getShort("Age");
        NBTTagCompound nbttagcompound = par1NBTTagCompound.getCompoundTag("Item");
        item = ItemStack.loadItemStackFromNBT(nbttagcompound);

        if (item == null)
        {
            setDead();
        }
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

        int i = item.stackSize;

        if (delayBeforeCanPickup == 0 && par1EntityPlayer.inventory.addItemStackToInventory(item))
        {
            if (item.itemID == Block.wood.blockID)
            {
                par1EntityPlayer.triggerAchievement(AchievementList.mineWood);
            }

            if (item.itemID == Item.leather.shiftedIndex)
            {
                par1EntityPlayer.triggerAchievement(AchievementList.killCow);
            }

            if (item.itemID == Item.field_77702_n.shiftedIndex)
            {
                par1EntityPlayer.triggerAchievement(AchievementList.diamonds);
            }

            if (item.itemID == Item.blazeRod.shiftedIndex)
            {
                par1EntityPlayer.triggerAchievement(AchievementList.blazeRod);
            }

            worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            par1EntityPlayer.onItemPickup(this, i);

            if (item.stackSize <= 0)
            {
                setDead();
            }
        }
    }

    public String func_70023_ak()
    {
        return StatCollector.translateToLocal((new StringBuilder()).append("item.").append(item.func_77977_a()).toString());
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }
}
