package net.minecraft.src.ssp;

import java.util.Random;
import net.minecraft.src.Achievement;
import net.minecraft.src.AchievementList;
import net.minecraft.src.AttributeInstance;
import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityCrit2FX;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityHorse;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMinecartHopper;
import net.minecraft.src.EntityPickupFX;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiBrewingStand;
import net.minecraft.src.GuiChest;
import net.minecraft.src.GuiCommandBlock;
import net.minecraft.src.GuiCrafting;
import net.minecraft.src.GuiDispenser;
import net.minecraft.src.GuiEditSign;
import net.minecraft.src.GuiEnchantment;
import net.minecraft.src.GuiFurnace;
import net.minecraft.src.GuiHopper;
import net.minecraft.src.GuiMerchant;
import net.minecraft.src.GuiScreenBook;
import net.minecraft.src.GuiScreenHorseInventory;
import net.minecraft.src.GuiWinGame;
import net.minecraft.src.I18n;
import net.minecraft.src.IInventory;
import net.minecraft.src.IMerchant;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Minecraft;
import net.minecraft.src.MouseFilter;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.Session;
import net.minecraft.src.SharedMonsterAttributes;
import net.minecraft.src.StatBase;
import net.minecraft.src.StatList;
import net.minecraft.src.StringUtils;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityBrewingStand;
import net.minecraft.src.TileEntityCommandBlock;
import net.minecraft.src.TileEntityDispenser;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.TileEntityHopper;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.World;

public class EntityPlayerSP2 extends EntityClientPlayerMP
{
    public static int armor = 3;
    public static int combat = 3;
    public static boolean sprint = true;
    public static int startitems = 0;
    public static boolean alertWolves = false;

    private MouseFilter field_71162_ch;
    private MouseFilter field_71160_ci;
    private MouseFilter field_71161_cj;

    public EntityPlayerSP2(Minecraft par1Minecraft, World par2World, Session par3Session, int par4)
    {
        super(par1Minecraft, par2World, par3Session, new NetClientHandlerSP(par1Minecraft));
        sprintToggleTimer = 0;
        sprintingTicksLeft = 0;
        field_71162_ch = new MouseFilter();
        field_71160_ci = new MouseFilter();
        field_71161_cj = new MouseFilter();
        mc = par1Minecraft;
        dimension = par4;
    }

    @Override
    public void incrementStat(StatBase par1StatBase, int par2)
    {
    }

    /**
     * Sends a chat message from the player. Args: chatMessage
     */
    @Override
    public void sendChatMessage(String par1Str)
    {
        if (par1Str.startsWith("/"))
        {
            mc.getIntegratedServer().getCommandManager().executeCommand(this, par1Str);
        }
        else
        {
            mc.ingameGUI.getChatGUI().printChatMessage((new StringBuilder()).append("<").append(username).append("> ").append(par1Str).toString());
        }
    }

    @Override
    public void respawnPlayer()
    {
        mc.respawn(false, 0, false);
    }

    @Override
    public void travelToDimension(int par1)
    {
        if (worldObj.isRemote)
        {
            return;
        }

        if (dimension == 1 && par1 == 1)
        {
            triggerAchievement(AchievementList.theEnd2);
            mc.displayGuiScreen(new GuiWinGame());
        }
        else
        {
            triggerAchievement(AchievementList.theEnd);
            mc.sndManager.playSoundFX("portal.travel", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            mc.usePortal(1);
        }
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
    }

    @Override
    public void updateEntityActionState()
    {
        super.updateEntityActionState();
        moveStrafing = movementInput.moveStrafe;
        moveForward = movementInput.moveForward;
        isJumping = movementInput.jump;
        prevRenderArmYaw = renderArmYaw;
        prevRenderArmPitch = renderArmPitch;
        renderArmPitch += (double)(rotationPitch - renderArmPitch) * 0.5D;
        renderArmYaw += (double)(rotationYaw - renderArmYaw) * 0.5D;
    }

    /**
     * Returns whether the entity is in a local (client) world
     */
    @Override
    public boolean isClientWorld()
    {
        return true;
    }

    /**
     * Gets the player's field of view multiplier. (ex. when flying)
     */
    @Override
    public float getFOVMultiplier()
    {
        float f = 1.0F;

        if (capabilities.isFlying)
        {
            f *= 1.1F;
        }

        AttributeInstance attributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        f = (float)((double)f * ((attributeinstance.getAttributeValue() / (double)capabilities.getWalkSpeed() + 1.0D) / 2D));

        if (isUsingItem() && getItemInUse().itemID == Item.bow.itemID)
        {
            int i = getItemInUseDuration();
            float f1 = (float)i / 20F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }
            else
            {
                f1 *= f1;
            }

            f *= 1.0F - f1 * 0.15F;
        }

        return f;
    }

    /**
     * sets current screen to null (used on escape buttons of GUIs)
     */
    @Override
    public void closeScreen()
    {
        openContainer = inventoryContainer;
        mc.displayGuiScreen(null);
    }

    /**
     * Displays the GUI for editing a sign. Args: tileEntitySign
     */
    @Override
    public void displayGUIEditSign(TileEntity par1TileEntity)
    {
        if (par1TileEntity instanceof TileEntitySign)
        {
            mc.displayGuiScreen(new GuiEditSign((TileEntitySign)par1TileEntity));
        }
        else if (par1TileEntity instanceof TileEntityCommandBlock)
        {
            mc.displayGuiScreen(new GuiCommandBlock((TileEntityCommandBlock)par1TileEntity));
        }
    }

    /**
     * Displays the GUI for interacting with a book.
     */
    @Override
    public void displayGUIBook(ItemStack par1ItemStack)
    {
        Item item = par1ItemStack.getItem();

        if (item == Item.writtenBook)
        {
            mc.displayGuiScreen(new GuiScreenBook(this, par1ItemStack, false));
        }
        else if (item == Item.writableBook)
        {
            mc.displayGuiScreen(new GuiScreenBook(this, par1ItemStack, true));
        }
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    @Override
    public void displayGUIChest(IInventory par1IInventory)
    {
        mc.displayGuiScreen(new GuiChest(inventory, par1IInventory));
    }

    /**
     * Displays the crafting GUI for a workbench.
     */
    @Override
    public void displayGUIWorkbench(int par1, int par2, int par3)
    {
        mc.displayGuiScreen(new GuiCrafting(inventory, worldObj, par1, par2, par3));
    }

    @Override
    public void displayGUIEnchantment(int par1, int par2, int par3, String par4Str)
    {
        mc.displayGuiScreen(new GuiEnchantment(inventory, worldObj, par1, par2, par3, par4Str));
    }

    /**
     * Displays the furnace GUI for the passed in furnace entity. Args: tileEntityFurnace
     */
    @Override
    public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace)
    {
        mc.displayGuiScreen(new GuiFurnace(inventory, par1TileEntityFurnace));
    }

    /**
     * Displays the GUI for interacting with a brewing stand.
     */
    @Override
    public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand)
    {
        mc.displayGuiScreen(new GuiBrewingStand(inventory, par1TileEntityBrewingStand));
    }

    /**
     * Displays the dipsenser GUI for the passed in dispenser entity. Args: TileEntityDispenser
     */
    @Override
    public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser)
    {
        mc.displayGuiScreen(new GuiDispenser(inventory, par1TileEntityDispenser));
    }

    @Override
    public void displayGUIMerchant(IMerchant par1IMerchant, String par2Str)
    {
        mc.displayGuiScreen(new GuiMerchant(inventory, par1IMerchant, worldObj, par2Str));
    }

    @Override
    public void displayGUIHopper(TileEntityHopper par1TileEntityHopper)
    {
        mc.displayGuiScreen(new GuiHopper(inventory, par1TileEntityHopper));
    }

    @Override
    public void displayGUIHopperMinecart(EntityMinecartHopper par1EntityMinecartHopper)
    {
        mc.displayGuiScreen(new GuiHopper(inventory, par1EntityMinecartHopper));
    }

    @Override
    public void displayGUIHorse(EntityHorse par1EntityHorse, IInventory par2IInventory)
    {
        mc.displayGuiScreen(new GuiScreenHorseInventory(inventory, par2IInventory, par1EntityHorse));
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    @Override
    public void onCriticalHit(Entity par1Entity)
    {
        mc.effectRenderer.addEffect(new EntityCrit2FX(mc.theWorld, par1Entity));
    }

    @Override
    public void onEnchantmentCritical(Entity par1Entity)
    {
        EntityCrit2FX entitycrit2fx = new EntityCrit2FX(mc.theWorld, par1Entity, "magicCrit");
        mc.effectRenderer.addEffect(entitycrit2fx);
    }

    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    @Override
    public void onItemPickup(Entity par1Entity, int par2)
    {
        mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, par1Entity, this, -0.5F));
    }

    /**
     * Returns if this entity is sneaking.
     */
    @Override
    public boolean isSneaking()
    {
        return movementInput.sneak && !sleeping;
    }

    /**
     * Updates health locally.
     */
    @Override
    public void setPlayerSPHealth(float par1)
    {
        float f = getHealth() - par1;

        if (f <= 0.0F)
        {
            setHealth(par1);

            if (f < 0.0F)
            {
                hurtResistantTime = maxHurtResistantTime / 2;
            }
        }
        else
        {
            lastDamage = f;
            setHealth(getHealth());
            hurtResistantTime = maxHurtResistantTime;
            damageEntity(DamageSource.generic, f);
            hurtTime = maxHurtTime = 10;
        }
    }

    /**
     * Add a chat message to the player
     */
    @Override
    public void addChatMessage(String par1Str)
    {
        mc.ingameGUI.getChatGUI().addTranslatedMessage(par1Str, new Object[0]);
    }

    /**
     * Adds a value to a statistic field.
     */
    @Override
    public void addStat(StatBase par1StatBase, int par2)
    {
        if (par1StatBase == null)
        {
            return;
        }

        if (par1StatBase.isAchievement())
        {
            Achievement achievement = (Achievement)par1StatBase;

            if (achievement.parentAchievement == null || mc.statFileWriter.hasAchievementUnlocked(achievement.parentAchievement))
            {
                if (!mc.statFileWriter.hasAchievementUnlocked(achievement))
                {
                    mc.guiAchievement.queueTakenAchievement(achievement);
                }

                mc.statFileWriter.readStat(par1StatBase, par2);
            }
        }
        else
        {
            mc.statFileWriter.readStat(par1StatBase, par2);
        }
    }

    private boolean isBlockTranslucent(int par1, int par2, int par3)
    {
        return worldObj.isBlockNormalCube(par1, par2, par3);
    }

    /**
     * Adds velocity to push the entity out of blocks at the specified x, y, z position Args: x, y, z
     */
    @Override
    protected boolean pushOutOfBlocks(double par1, double par3, double par5)
    {
        int i = MathHelper.floor_double(par1);
        int j = MathHelper.floor_double(par3);
        int k = MathHelper.floor_double(par5);
        double d = par1 - (double)i;
        double d1 = par5 - (double)k;

        if (isBlockTranslucent(i, j, k) || isBlockTranslucent(i, j + 1, k))
        {
            boolean flag = !isBlockTranslucent(i - 1, j, k) && !isBlockTranslucent(i - 1, j + 1, k);
            boolean flag1 = !isBlockTranslucent(i + 1, j, k) && !isBlockTranslucent(i + 1, j + 1, k);
            boolean flag2 = !isBlockTranslucent(i, j, k - 1) && !isBlockTranslucent(i, j + 1, k - 1);
            boolean flag3 = !isBlockTranslucent(i, j, k + 1) && !isBlockTranslucent(i, j + 1, k + 1);
            byte byte0 = -1;
            double d2 = 9999D;

            if (flag && d < d2)
            {
                d2 = d;
                byte0 = 0;
            }

            if (flag1 && 1.0D - d < d2)
            {
                d2 = 1.0D - d;
                byte0 = 1;
            }

            if (flag2 && d1 < d2)
            {
                d2 = d1;
                byte0 = 4;
            }

            if (flag3 && 1.0D - d1 < d2)
            {
                double d3 = 1.0D - d1;
                byte0 = 5;
            }

            float f = 0.1F;

            if (byte0 == 0)
            {
                motionX = -f;
            }

            if (byte0 == 1)
            {
                motionX = f;
            }

            if (byte0 == 4)
            {
                motionZ = -f;
            }

            if (byte0 == 5)
            {
                motionZ = f;
            }
        }

        return false;
    }

    /**
     * Set sprinting switch for Entity.
     */
    @Override
    public void setSprinting(boolean par1)
    {
        if (!sprint){
            par1 = false;
        }
        super.setSprinting(par1);
    }

    /**
     * Sets the current XP, total XP, and level number.
     */
    @Override
    public void setXPStats(float par1, int par2, int par3)
    {
        experience = par1;
        experienceTotal = par2;
        experienceLevel = par3;
    }

    @Override
    public void sendChatToPlayer(ChatMessageComponent par1ChatMessageComponent)
    {
        mc.ingameGUI.getChatGUI().printChatMessage((par1ChatMessageComponent.toStringWithFormatting(true)));
    }

    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    @Override
    protected void damageEntity(DamageSource par1DamageSource, float par2)
    {
        if (!par1DamageSource.isUnblockable() && isBlocking())
        {
            par2 = 1 + (int)par2 >> 1;
        }

        if (armor<2){
            par2 = applyArmorCalculations_old(par1DamageSource, par2);
        }else{
            par2 = applyArmorCalculations(par1DamageSource, par2);
        }
        par2 = applyPotionDamageCalculations(par1DamageSource, par2);
        addExhaustion(par1DamageSource.getHungerDamage());
        if (armor==2){
            par2 = applyArmorCalculations(par1DamageSource, par2);
            par2 = applyPotionDamageCalculations(par1DamageSource, par2);
        }
        setHealth(getHealth() - par2);
    }

    @Override
    public void sendPlayerAbilities()
    {
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (capabilities.disableDamage && !par1DamageSource.canHarmInCreative())
        {
            return false;
        }

        entityAge = 0;

        if (getHealth() <= 0)
        {
            return false;
        }

        if (isPlayerSleeping() && !worldObj.isRemote)
        {
            wakeUpPlayer(true, true, false);
        }

        Entity entity = par1DamageSource.getEntity();

        if (par1DamageSource.isDifficultyScaled())
        {
            if (worldObj.difficultySetting == 0)
            {
                par2 = 0;
            }

            if (worldObj.difficultySetting == 1)
            {
                par2 = par2 / 2 + 1;
            }

            if (worldObj.difficultySetting == 3)
            {
                par2 = (par2 * 3) / 2;
            }
        }

        if (par2 == 0)
        {
            return false;
        }

        Entity entity1 = par1DamageSource.getEntity();

        if ((entity1 instanceof EntityArrow) && ((EntityArrow)entity1).shootingEntity != null)
        {
            entity1 = ((EntityArrow)entity1).shootingEntity;
        }

        if (alertWolves && entity1 instanceof EntityLiving)
        {
            alertWolves((EntityLiving)entity1, false);
        }

        addStat(StatList.damageTakenStat, Math.round(par2 * 10F));
        if (worldObj.isRemote)
        {
            return false;
        }

        entityAge = 0;

        if (getHealth() <= 0)
        {
            return false;
        }

        if (par1DamageSource.isFireDamage() && isPotionActive(Potion.fireResistance))
        {
            return false;
        }

        limbSwingAmount = 1.5F;
        boolean flag = true;

        if ((float)hurtResistantTime > (float)maxHurtResistantTime / 2.0F)
        {
            if (par2 <= lastDamage)
            {
                return false;
            }

            damageEntity(par1DamageSource, par2 - lastDamage);
            lastDamage = par2;
            flag = false;
        }
        else
        {
            lastDamage = par2;
            prevHealth = getHealth();
            hurtResistantTime = maxHurtResistantTime;
            damageEntity(par1DamageSource, par2);
            hurtTime = maxHurtTime = 10;
        }

        attackedAtYaw = 0.0F;

        if (entity != null)
        {
            if (entity instanceof EntityLiving)
            {
                setRevengeTarget((EntityLiving)entity);
            } 
            if (entity instanceof EntityPlayer)
            {
                recentlyHit = 60;
                attackingPlayer = (EntityPlayer)entity;
            }
            else if (entity instanceof EntityWolf)
            {
                EntityWolf entitywolf = (EntityWolf)entity;

                if (entitywolf.isTamed())
                {
                    recentlyHit = 60;
                    attackingPlayer = null;
                }
            } 
        }

        if (flag)
        {
            worldObj.setEntityState(this, (byte)2);

            if (par1DamageSource != DamageSource.drown && !par1DamageSource.getDamageType().equals("explosionOld"))
            {
                setBeenAttacked();
            }

            if (entity != null)
            {
                double d = entity.posX - posX;
                double d1;

                for (d1 = entity.posZ - posZ; d * d + d1 * d1 < 0.0001D; d1 = (Math.random() - Math.random()) * 0.01D)
                {
                    d = (Math.random() - Math.random()) * 0.01D;
                }

                attackedAtYaw = (float)((Math.atan2(d1, d) * 180D) / Math.PI) - rotationYaw;
                knockBack(entity, par2, d, d1);
            }
            else
            {
                attackedAtYaw = (int)(Math.random() * 2D) * 180;
            }
        }

        if (getHealth() <= 0)
        {
            if (flag)
            {
                playSound(getDeathSound(), getSoundVolume(), getSoundPitch());
            }

            onDeath(par1DamageSource);
        }
        else if (flag)
        {
            playSound(getHurtSound(), getSoundVolume(), getSoundPitch());
        }

        return true;
    }

    @Override
    public void heal(float par1)
    {
        float f = getHealth();

        if (f > 0.0F)
        {
            setHealth(f + par1);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();
    }

    @Override
    public void sendMotionUpdates()
    {
    }

    /**
     * Called when player presses the drop item key
     */
    @Override
    public EntityItem dropOneItem(boolean par1)
    {
        return dropPlayerItemWithRandomChoice(inventory.decrStackSize(inventory.currentItem, !par1 || inventory.getCurrentItem() == null ? 1 : inventory.getCurrentItem().stackSize), false);
    }

    /**
     * Joins the passed in entity item with the world. Args: entityItem
     */
    @Override
    protected void joinEntityItemWithWorld(EntityItem entityitem)
    {
        worldObj.spawnEntityInWorld(entityitem);
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    @Override
    protected float getSoundPitch()
    {
        if (isChild())
        {
            return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F;
        }
        else
        {
            return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;
        }
    }

    @Override
    public void onLivingUpdate(){
        super.onLivingUpdate();
        if (inPortal)
        {
            if (timeInPortal >= 1.0F)
            {
                if (!worldObj.isRemote && mc.enableSP)
                {
                    timeUntilPortal = 10;
                    mc.sndManager.playSoundFX("portal.travel", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
                    byte byte0 = 0;

                    if (dimension == -1)
                    {
                        byte0 = 0;
                    }
                    else
                    {
                        byte0 = -1;
                    }

                    mc.usePortal(byte0);
                    triggerAchievement(AchievementList.portal);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        prevPosY += 1.6200000047683716D;
        lastTickPosY += 1.6200000047683716D;
        posY += 1.6200000047683716D;
        setPosition(posX, posY, posZ);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        posY -= 1.6200000047683716D;
        super.writeToNBT(par1NBTTagCompound);
        posY += 1.6200000047683716D;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound){
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("Riding")){
            Entity entity = EntityList.createEntityFromNBT(nbttagcompound.getCompoundTag("Riding"), worldObj);
            if (entity != null){
                entity.forceSpawn = true;
                worldObj.spawnEntityInWorld(entity);
                mountEntity(entity);
                entity.forceSpawn = false;
            }
        }
    }

    @Override
    public boolean isRidingHorse()
    {
        return ridingEntity != null && (ridingEntity instanceof EntityHorse);
    }

    @Override
    protected void func_110318_g()
    {
        if (isRidingHorse()){
            ((EntityHorse)ridingEntity).setJumpPower((int)(getHorseJumpPower() * 100F));
        }
    }

    @Override
    public void func_110322_i()
    {
        if (isRidingHorse()){
            ((EntityHorse)ridingEntity).openGUI(this);
        }
    }

    @Override
    public void mountEntity(Entity par1Entity)
    {
        super.mountEntity(par1Entity);
        if (ridingEntity != null){
            GameSettings gamesettings = mc.gameSettings;
            mc.ingameGUI.func_110326_a(I18n.getStringParams("mount.onboard", new Object[]
                    {
                        GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.keyCode)
                    }), false);
        }
    }
}
