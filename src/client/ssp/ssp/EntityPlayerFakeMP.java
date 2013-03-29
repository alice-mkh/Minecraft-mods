package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class EntityPlayerFakeMP extends EntityPlayerMP
{
    private EntityPlayerSP2 realPlayer;

    /** set to getHealth */
    private int lastHealth = -99999999;

    /** set to foodStats.GetFoodLevel */
    private int lastFoodLevel = -99999999;

    /** set to foodStats.getSaturationLevel() == 0.0F each tick */
    private boolean wasHungry = true;

    /** Amount of experience the client was last set to */
    private int lastExperience = -99999999;

    /** de-increments onUpdate, attackEntityFrom is ignored if this >0 */
    private int initialInvulnerability = 60;

    /** must be between 3>x>15 (strictly between) */
    private int renderDistance = 0;

    /**
     * 0 is the held item, 1-4 is armor ; used to detect changes in getCurrentItemOrArmor
     */
    private ItemStack[] lastActiveItems = new ItemStack[] {null, null, null, null, null};

    /**
     * The currently in use window ID. Incremented every time a window is opened.
     */
    private int currentWindowId = 0;

    public EntityPlayerFakeMP(MinecraftServer par1MinecraftServer, String par3Str)
    {
        super(par1MinecraftServer, par3Str);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    }

    /**
     * Add experience levels to this player.
     */
    @Override
    public void addExperienceLevel(int par1)
    {
        realPlayer.addExperienceLevel(par1);
    }

    @Override
    public ItemStack[] getLastActiveItems()
    {
        return realPlayer.getLastActiveItems();
    }

    /**
     * sets the players height back to normal after doing things like sleeping and dieing
     */
    @Override
    protected void resetHeight()
    {
        realPlayer.yOffset = 0.0F;
    }

    @Override
    public float getEyeHeight()
    {
        return realPlayer.getEyeHeight();
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
    }

    @Override
    public void onUpdateEntity()
    {
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource par1DamageSource)
    {
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        return false;
    }

    @Override
    public void travelToDimension(int par1)
    {
    }

    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    @Override
    public void onItemPickup(Entity par1Entity, int par2)
    {
    }

    /**
     * Swings the item the player is holding.
     */
    @Override
    public void swingItem()
    {
        realPlayer.swingItem();
    }

    /**
     * Attempts to have the player sleep in a bed at the specified location.
     */
    @Override
    public EnumStatus sleepInBedAt(int par1, int par2, int par3)
    {
        return realPlayer.sleepInBedAt(par1, par2, par3);
    }

    /**
     * Wake up the player if they're sleeping.
     */
    @Override
    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
    {
        realPlayer.wakeUpPlayer(par1, par2, par3);
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    @Override
    public void mountEntity(Entity par1Entity)
    {
        realPlayer.mountEntity(par1Entity);
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    @Override
    protected void updateFallState(double par1, boolean par3) {}

    /**
     * likeUpdateFallState, but called from updateFlyingState, rather than moveEntity
     */
    @Override
    public void updateFlyingState(double par1, boolean par3)
    {
        realPlayer.updateFallState(par1, par3);
    }

    /**
     * Displays the crafting GUI for a workbench.
     */
    @Override
    public void displayGUIWorkbench(int par1, int par2, int par3)
    {
        realPlayer.displayGUIWorkbench(par1, par2, par3);
    }

    @Override
    public void displayGUIEnchantment(int par1, int par2, int par3, String par4Str)
    {
        realPlayer.displayGUIEnchantment(par1, par2, par3, par4Str);
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    @Override
    public void displayGUIChest(IInventory par1IInventory)
    {
        realPlayer.displayGUIChest(par1IInventory);
    }

    /**
     * Displays the furnace GUI for the passed in furnace entity. Args: tileEntityFurnace
     */
    @Override
    public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace)
    {
        realPlayer.displayGUIFurnace(par1TileEntityFurnace);
    }

    /**
     * Displays the dipsenser GUI for the passed in dispenser entity. Args: TileEntityDispenser
     */
    @Override
    public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser)
    {
        realPlayer.displayGUIDispenser(par1TileEntityDispenser);
    }

    /**
     * Displays the GUI for interacting with a brewing stand.
     */
    @Override
    public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand)
    {
        realPlayer.displayGUIBrewingStand(par1TileEntityBrewingStand);
    }

    @Override
    public void displayGUIMerchant(IMerchant par1IMerchant, String par2Str)
    {
        realPlayer.displayGUIMerchant(par1IMerchant, par2Str);
    }

    /**
     * inform the player of a change in a single slot
     */
    @Override
    public void sendSlotContents(Container par1Container, int par2, ItemStack par3ItemStack)
    {
    }

    @Override
    public void sendContainerToPlayer(Container par1Container)
    {
    }

    @Override
    public void sendContainerAndContentsToPlayer(Container par1Container, List par2List)
    {
    }

    /**
     * send information about the crafting inventory to the client(currently only for furnace times)
     */
    @Override
    public void sendProgressBarUpdate(Container par1Container, int par2, int par3)
    {
    }

    /**
     * sets current screen to null (used on escape buttons of GUIs)
     */
    @Override
    public void closeScreen()
    {
        realPlayer.closeScreen();
    }

    @Override
    public void updateHeldItem()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    /**
     * Adds a value to a statistic field.
     */
    @Override
    public void addStat(StatBase par1StatBase, int par2)
    {
        realPlayer.addStat(par1StatBase, par2);
    }

    @Override
    public void mountEntityAndWakeUp()
    {
    }

    /**
     * Add a chat message to the player
     */
    @Override
    public void addChatMessage(String par1Str)
    {
        StringTranslate var2 = StringTranslate.getInstance();
        String var3 = var2.translateKey(par1Str);
        this.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(var3));
    }

    /**
     * Move the entity to the coordinates informed, but keep yaw/pitch values.
     */
    @Override
    public void setPositionAndUpdate(double par1, double par3, double par5)
    {
        realPlayer.setPositionAndUpdate(par1, par3, par5);
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    @Override
    public void onCriticalHit(Entity par1Entity)
    {
        realPlayer.onCriticalHit(par1Entity);
    }

    @Override
    public void onEnchantmentCritical(Entity par1Entity)
    {
        realPlayer.onEnchantmentCritical(par1Entity);
    }

    /**
     * Sends the player's abilities to the server (if there is one).
     */
    @Override
    public void sendPlayerAbilities()
    {
    }

    @Override
    public WorldServer getServerForPlayer()
    {
        return null;
    }

    @Override
    public void setGameType(EnumGameType par1EnumGameType)
    {
        Minecraft.getMinecraft().setController(par1EnumGameType);
        Minecraft.getMinecraft().setGameMode(par1EnumGameType);
    }

    @Override
    public void sendChatToPlayer(String par1Str)
    {
        this.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(par1Str));
    }

    @Override
    public String getPlayerIP()
    {
        String var1 = this.playerNetServerHandler.netManager.getSocketAddress().toString();
        var1 = var1.substring(var1.indexOf("/") + 1);
        var1 = var1.substring(0, var1.indexOf(":"));
        return var1;
    }

    @Override
    public void updateClientInfo(Packet204ClientInfo par1Packet204ClientInfo)
    {
    }
}
