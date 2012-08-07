package net.minecraft.src;

import java.util.Random;
import net.minecraft.client.Minecraft;

public class EntityPlayerSP extends EntityPlayer
{
    public NetHandlerSP sendQueue;

    public MovementInput movementInput;
    protected Minecraft mc;

    /**
     * Used to tell if the player pressed forward twice. If this is at 0 and it's pressed (And they are allowed to
     * sprint, aka enough food on the ground etc) it sets this to 7. If it's pressed and it's greater than 0 enable
     * sprinting.
     */
    protected int sprintToggleTimer;

    /** Ticks left before sprinting is disabled. */
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private MouseFilter field_71162_ch;
    private MouseFilter field_71160_ci;
    private MouseFilter field_71161_cj;

    public EntityPlayerSP(Minecraft par1Minecraft, World par2World, Session par3Session, int par4)
    {
        super(par2World);
        sprintToggleTimer = 0;
        sprintingTicksLeft = 0;
        field_71162_ch = new MouseFilter();
        field_71160_ci = new MouseFilter();
        field_71161_cj = new MouseFilter();
        mc = par1Minecraft;
        dimension = par4;

        if (par3Session != null && par3Session.username != null && par3Session.username.length() > 0)
        {
            skinUrl = (new StringBuilder()).append("http://skins.minecraft.net/MinecraftSkins/").append(StringUtils.func_76338_a(par3Session.username)).append(".png").toString();
        }

        username = par3Session.username;
        try{
            sendQueue = new NetHandlerSP(par1Minecraft, "", 0);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void incrementStat(StatBase par1StatBase, int par2)
    {
    }

    /**
     * Sends a chat message from the player. Args: chatMessage
     */
    public void sendChatMessage(String par1Str)
    {
        if (par1Str.startsWith("/"))
        {
            mc.getCommandManager().func_71556_a(this, par1Str.substring(1));
        }
        else
        {
            mc.ingameGUI.func_73827_b().func_73765_a((new StringBuilder()).append("<").append(username).append("> ").append(par1Str).toString());
        }
    }

    public void respawnPlayer()
    {
        mc.respawn(false, 0, false);
    }

    public void func_6420_o()
    {
    }

    public void travelToTheEnd(int par1)
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
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
    }

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
    protected boolean isClientWorld()
    {
        return true;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (sprintingTicksLeft > 0)
        {
            sprintingTicksLeft--;

            if (sprintingTicksLeft == 0)
            {
                setSprinting(false);
            }
        }

        if (sprintToggleTimer > 0)
        {
            sprintToggleTimer--;
        }

        if (mc.field_71442_b.func_78747_a())
        {
            posX = posZ = 0.5D;
            posX = 0.0D;
            posZ = 0.0D;
            rotationYaw = (float)ticksExisted / 12F;
            rotationPitch = 10F;
            posY = 68.5D;
            return;
        }

        if (!mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory))
        {
            mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
        }

        prevTimeInPortal = timeInPortal;

        if (inPortal)
        {
            if (mc.currentScreen != null)
            {
                mc.displayGuiScreen(null);
            }

            if (timeInPortal == 0.0F)
            {
                mc.sndManager.playSoundFX("portal.trigger", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            }

            timeInPortal += 0.0125F;

            if (timeInPortal >= 1.0F)
            {
                timeInPortal = 1.0F;

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


            inPortal = false;
        }
        else if (isPotionActive(Potion.confusion) && getActivePotionEffect(Potion.confusion).getDuration() > 60)
        {
            timeInPortal += 0.006666667F;

            if (timeInPortal > 1.0F)
            {
                timeInPortal = 1.0F;
            }
        }
        else
        {
            if (timeInPortal > 0.0F)
            {
                timeInPortal -= 0.05F;
            }

            if (timeInPortal < 0.0F)
            {
                timeInPortal = 0.0F;
            }
        }

        if (timeUntilPortal > 0)
        {
            timeUntilPortal--;
        }

        boolean flag = movementInput.jump;
        float f = 0.8F;
        boolean flag1 = movementInput.moveForward >= f;
        movementInput.updatePlayerMoveState();

        if (isUsingItem())
        {
            movementInput.moveStrafe *= 0.2F;
            movementInput.moveForward *= 0.2F;
            sprintToggleTimer = 0;
        }

        if (movementInput.sneak && ySize < 0.2F)
        {
            ySize = 0.2F;
        }

        pushOutOfBlocks(posX - (double)width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ + (double)width * 0.34999999999999998D);
        pushOutOfBlocks(posX - (double)width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ - (double)width * 0.34999999999999998D);
        pushOutOfBlocks(posX + (double)width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ - (double)width * 0.34999999999999998D);
        pushOutOfBlocks(posX + (double)width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ + (double)width * 0.34999999999999998D);
        boolean flag2 = (float)getFoodStats().getFoodLevel() > 6F || capabilities.allowFlying;

        if (onGround && !flag1 && movementInput.moveForward >= f && !isSprinting() && flag2 && !isUsingItem() && !isPotionActive(Potion.blindness))
        {
            if (sprintToggleTimer == 0)
            {
                sprintToggleTimer = 7;
            }
            else
            {
                setSprinting(true);
                sprintToggleTimer = 0;
            }
        }

        if (isSneaking())
        {
            sprintToggleTimer = 0;
        }

        if (isSprinting() && (movementInput.moveForward < f || isCollidedHorizontally || !flag2))
        {
            setSprinting(false);
        }

        if (capabilities.allowFlying && !flag && movementInput.jump)
        {
            if (flyToggleTimer == 0)
            {
                flyToggleTimer = 7;
            }
            else
            {
                capabilities.isFlying = !capabilities.isFlying;
                func_71016_p();
                flyToggleTimer = 0;
            }
        }

        if (capabilities.isFlying)
        {
            if (movementInput.sneak)
            {
                motionY -= 0.14999999999999999D;
            }

            if (movementInput.jump)
            {
                motionY += 0.14999999999999999D;
            }
        }

        super.onLivingUpdate();

        if (onGround && capabilities.isFlying)
        {
            capabilities.isFlying = false;
            func_71016_p();
        }
    }

    /**
     * Gets the player's field of view multiplier. (ex. when flying)
     */
    public float getFOVMultiplier()
    {
        float f = 1.0F;

        if (capabilities.isFlying)
        {
            f *= 1.1F;
        }

        f *= ((landMovementFactor * getSpeedModifier()) / speedOnGround + 1.0F) / 2.0F;

        if (isUsingItem() && getItemInUse().itemID == Item.bow.shiftedIndex)
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
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Score", score);
    }

    public void updateCloak()
    {
        playerCloakUrl = (new StringBuilder()).append("http://skins.minecraft.net/MinecraftCloaks/").append(StringUtils.func_76338_a(username)).append(".png").toString();
        cloakUrl = playerCloakUrl;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        score = par1NBTTagCompound.getInteger("Score");
    }

    /**
     * sets current screen to null (used on escape buttons of GUIs)
     */
    public void closeScreen()
    {
        super.closeScreen();
        mc.displayGuiScreen(null);
    }

    /**
     * Displays the GUI for editing a sign. Args: tileEntitySign
     */
    public void displayGUIEditSign(TileEntitySign par1TileEntitySign)
    {
        mc.displayGuiScreen(new GuiEditSign(par1TileEntitySign));
    }

    public void func_71048_c(ItemStack par1ItemStack)
    {
        Item item = par1ItemStack.getItem();

        if (item == Item.field_77823_bG)
        {
            mc.displayGuiScreen(new GuiScreenBook(this, par1ItemStack, false));
        }
        else if (item == Item.field_77821_bF)
        {
            mc.displayGuiScreen(new GuiScreenBook(this, par1ItemStack, true));
        }
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    public void displayGUIChest(IInventory par1IInventory)
    {
        mc.displayGuiScreen(new GuiChest(inventory, par1IInventory));
    }

    /**
     * Displays the crafting GUI for a workbench.
     */
    public void displayWorkbenchGUI(int par1, int par2, int par3)
    {
        mc.displayGuiScreen(new GuiCrafting(inventory, worldObj, par1, par2, par3));
    }

    public void displayGUIEnchantment(int par1, int par2, int par3)
    {
        mc.displayGuiScreen(new GuiEnchantment(inventory, worldObj, par1, par2, par3));
    }

    /**
     * Displays the furnace GUI for the passed in furnace entity. Args: tileEntityFurnace
     */
    public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace)
    {
        mc.displayGuiScreen(new GuiFurnace(inventory, par1TileEntityFurnace));
    }

    /**
     * Displays the GUI for interacting with a brewing stand.
     */
    public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand)
    {
        mc.displayGuiScreen(new GuiBrewingStand(inventory, par1TileEntityBrewingStand));
    }

    /**
     * Displays the dipsenser GUI for the passed in dispenser entity. Args: TileEntityDispenser
     */
    public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser)
    {
        mc.displayGuiScreen(new GuiDispenser(inventory, par1TileEntityDispenser));
    }

    public void func_71030_a(IMerchant par1IMerchant)
    {
        mc.displayGuiScreen(new GuiMerchant(inventory, par1IMerchant, worldObj));
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    public void onCriticalHit(Entity par1Entity)
    {
        mc.effectRenderer.addEffect(new EntityCrit2FX(mc.field_71441_e, par1Entity));
    }

    public void onEnchantmentCritical(Entity par1Entity)
    {
        EntityCrit2FX entitycrit2fx = new EntityCrit2FX(mc.field_71441_e, par1Entity, "magicCrit");
        mc.effectRenderer.addEffect(entitycrit2fx);
    }

    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    public void onItemPickup(Entity par1Entity, int par2)
    {
        mc.effectRenderer.addEffect(new EntityPickupFX(mc.field_71441_e, par1Entity, this, -0.5F));
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking()
    {
        return movementInput.sneak && !sleeping;
    }

    /**
     * Updates health locally.
     */
    public void setHealth(int par1)
    {
        int i = getHealth() - par1;

        if (i <= 0)
        {
            setEntityHealth(par1);

            if (i < 0)
            {
                heartsLife = heartsHalvesLife / 2;
            }
        }
        else
        {
            lastDamage = i;
            setEntityHealth(getHealth());
            heartsLife = heartsHalvesLife;
            damageEntity(DamageSource.generic, i);
            hurtTime = maxHurtTime = 10;
        }
    }

    /**
     * Add a chat message to the player
     */
    public void addChatMessage(String par1Str)
    {
        mc.ingameGUI.func_73827_b().func_73757_a(par1Str, new Object[0]);
    }

    /**
     * Adds a value to a statistic field.
     */
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
    public void setSprinting(boolean par1)
    {
        super.setSprinting(par1);
        sprintingTicksLeft = par1 ? 600 : 0;
    }

    /**
     * Sets the current XP, total XP, and level number.
     */
    public void setXPStats(float par1, int par2, int par3)
    {
        experience = par1;
        experienceTotal = par2;
        experienceLevel = par3;
    }

    public void func_70006_a(String par1Str)
    {
        mc.ingameGUI.func_73827_b().func_73765_a(par1Str);
    }

    public boolean func_70003_b(String par1Str)
    {
        return worldObj.getWorldInfo().func_76086_u();
    }
}
