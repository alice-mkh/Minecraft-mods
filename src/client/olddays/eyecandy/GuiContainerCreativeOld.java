package net.minecraft.src;

import java.util.List;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiContainerCreativeOld extends InventoryEffectRenderer
{
    private static final ResourceLocation bgResource = new ResourceLocation("olddays/allitems.png");

    private static InventoryBasic inventory = new InventoryBasic("tmp", false, 72);

    /** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
    private float currentScroll;

    /** True if the scrollbar is being dragged */
    private boolean isScrolling;

    /**
     * True if the left mouse button was held down last time drawScreen was called.
     */
    private boolean wasClicking;

    public GuiContainerCreativeOld(EntityPlayer par1EntityPlayer)
    {
        super(new OldContainerCreative(par1EntityPlayer));
        currentScroll = 0.0F;
        isScrolling = false;
        par1EntityPlayer.openContainer = inventorySlots;
        allowUserInput = true;
        par1EntityPlayer.addStat(AchievementList.openInventory, 1);
        ySize = 208;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        if (!mc.playerController.isInCreativeMode())
        {
            mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
        }
    }

    @Override
    protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4)
    {
        if (par1Slot != null)
        {
            if (par1Slot.inventory == inventory)
            {
                InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
                ItemStack itemstack1 = inventoryplayer.getItemStack();
                ItemStack itemstack4 = par1Slot.getStack();

                if (itemstack1 != null && itemstack4 != null && itemstack1.itemID == itemstack4.itemID)
                {
                    if (par3 == 0)
                    {
                        if (par4 == 3 || par4 == 1)
                        {
                            itemstack1.stackSize = itemstack1.getMaxStackSize();
                        }
                        else if (itemstack1.stackSize < itemstack1.getMaxStackSize())
                        {
                            itemstack1.stackSize++;
                        }
                    }
                    else if (itemstack1.stackSize <= 1)
                    {
                        inventoryplayer.setItemStack(null);
                    }
                    else
                    {
                        itemstack1.stackSize--;
                    }
                }
                else if (itemstack1 != null)
                {
                    inventoryplayer.setItemStack(null);
                }
                else if (itemstack4 == null)
                {
                    inventoryplayer.setItemStack(null);
                }
                else if (itemstack1 == null || itemstack1.itemID != itemstack4.itemID)
                {
                    inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack4));
                    ItemStack itemstack2 = inventoryplayer.getItemStack();

                    if (par4 == 3 || par4 == 1)
                    {
                        itemstack2.stackSize = itemstack2.getMaxStackSize();
                    }
                }
            }
            else
            {
                inventorySlots.slotClick(par1Slot.slotNumber, par3, par4, mc.thePlayer);
                ItemStack itemstack = inventorySlots.getSlot(par1Slot.slotNumber).getStack();
                mc.playerController.sendSlotPacket(itemstack, (par1Slot.slotNumber - inventorySlots.inventorySlots.size()) + 9 + 36);
            }
        }
        else
        {
            InventoryPlayer inventoryplayer1 = mc.thePlayer.inventory;

            if (inventoryplayer1.getItemStack() != null)
            {
                if (par3 == 0)
                {
                    mc.thePlayer.dropPlayerItem(inventoryplayer1.getItemStack());
                    mc.playerController.func_78752_a(inventoryplayer1.getItemStack());
                    inventoryplayer1.setItemStack(null);
                }

                if (par3 == 1)
                {
                    ItemStack itemstack3 = inventoryplayer1.getItemStack().splitStack(1);
                    mc.thePlayer.dropPlayerItem(itemstack3);
                    mc.playerController.func_78752_a(itemstack3);

                    if (inventoryplayer1.getItemStack().stackSize == 0)
                    {
                        inventoryplayer1.setItemStack(null);
                    }
                }
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        if (!mc.playerController.isInCreativeMode())
        {
            mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
        }
        else
        {
            super.initGui();
            buttonList.clear();
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everythin in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRenderer.drawString(StatCollector.translateToLocal("container.creative"), 8, 6, 0x404040);
    }

    /**
     * Handles mouse input.
     */
    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            int j = (((OldContainerCreative)inventorySlots).itemList.size() / 8 - 8) + 1;

            if (i > 0)
            {
                i = 1;
            }

            if (i < 0)
            {
                i = -1;
            }

            currentScroll -= (double)i / (double)j;

            if (currentScroll < 0.0F)
            {
                currentScroll = 0.0F;
            }

            if (currentScroll > 1.0F)
            {
                currentScroll = 1.0F;
            }

            ((OldContainerCreative)inventorySlots).scrollTo(currentScroll);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        boolean flag = Mouse.isButtonDown(0);
        int i = guiLeft;
        int j = guiTop;
        int k = i + 155;
        int l = j + 17;
        int i1 = k + 14;
        int j1 = l + 160 + 2;

        if (!wasClicking && flag && par1 >= k && par2 >= l && par1 < i1 && par2 < j1)
        {
            isScrolling = true;
        }

        if (!flag)
        {
            isScrolling = false;
        }

        wasClicking = flag;

        if (isScrolling)
        {
            currentScroll = (float)(par2 - (l + 8)) / ((float)(j1 - l) - 16F);

            if (currentScroll < 0.0F)
            {
                currentScroll = 0.0F;
            }

            if (currentScroll > 1.0F)
            {
                currentScroll = 1.0F;
            }

            ((OldContainerCreative)inventorySlots).scrollTo(currentScroll);
        }

        super.drawScreen(par1, par2, par3);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(bgResource);
        int j = guiLeft;
        int k = guiTop;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        int l = j + 155;
        int i1 = k + 17;
        int j1 = i1 + 160 + 2;
        drawTexturedModalRect(j + 154, k + 17 + (int)((float)(j1 - i1 - 17) * currentScroll), 0, 208, 16, 16);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 0)
        {
            mc.displayGuiScreen(new GuiAchievements(mc.statFileWriter));
        }

        if (par1GuiButton.id == 1)
        {
            mc.displayGuiScreen(new GuiStats(this, mc.statFileWriter));
        }
    }

    /**
     * Returns the creative inventory
     */
    static InventoryBasic getInventory()
    {
        return inventory;
    }
}
