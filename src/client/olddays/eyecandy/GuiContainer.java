package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiContainer extends GuiScreen
{
    public static boolean tooltips = true;
    public static boolean oldtooltips = false;

    /** Stacks renderer. Icons, stack size, health, etc... */
    protected static RenderItem itemRenderer = new RenderItem();

    /** The X size of the inventory window in pixels. */
    protected int xSize;

    /** The Y size of the inventory window in pixels. */
    protected int ySize;

    /** A list of the players inventory slots. */
    public Container inventorySlots;

    /**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiLeft;

    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiTop;
    private Slot theSlot;

    /** Used when touchscreen is enabled */
    private Slot clickedSlot;
    private boolean field_90018_r;

    /** Used when touchscreen is enabled */
    private ItemStack draggedStack;
    private int field_85049_r;
    private int field_85048_s;
    private Slot returningStackDestSlot;
    private long returningStackTime;

    /** Used when touchscreen is enabled */
    private ItemStack returningStack;
    private Slot field_92033_y;
    private long field_92032_z;

    public GuiContainer(Container par1Container)
    {
        xSize = 176;
        ySize = 166;
        clickedSlot = null;
        field_90018_r = false;
        draggedStack = null;
        field_85049_r = 0;
        field_85048_s = 0;
        returningStackDestSlot = null;
        returningStackTime = 0L;
        returningStack = null;
        field_92033_y = null;
        field_92032_z = 0L;
        inventorySlots = par1Container;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        mc.thePlayer.openContainer = inventorySlots;
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        int i = guiLeft;
        int j = guiTop;
        drawGuiContainerBackgroundLayer(par3, par1, par2);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        super.drawScreen(par1, par2, par3);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef(i, j, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        theSlot = null;
        int k = 240;
        int i1 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)i1 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (int l = 0; l < inventorySlots.inventorySlots.size(); l++)
        {
            Slot slot = (Slot)inventorySlots.inventorySlots.get(l);
            drawSlotInventory(slot);

            if (isMouseOverSlot(slot, par1, par2))
            {
                theSlot = slot;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int j1 = slot.xDisplayPosition;
                int k1 = slot.yDisplayPosition;
                drawGradientRect(j1, k1, j1 + 16, k1 + 16, 0x80ffffff, 0x80ffffff);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }

        drawGuiContainerForegroundLayer(par1, par2);
        InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
        ItemStack itemstack = draggedStack != null ? draggedStack : inventoryplayer.getItemStack();

        if (itemstack != null)
        {
            byte byte0 = 8;
            byte byte1 = ((byte)(draggedStack != null ? 16 : 8));

            if (draggedStack != null && field_90018_r)
            {
                itemstack = itemstack.copy();
                itemstack.stackSize = MathHelper.ceiling_float_int((float)itemstack.stackSize / 2.0F);
            }

            drawItemStack(itemstack, par1 - i - byte0, par2 - j - byte1);
        }

        if (returningStack != null)
        {
            float f = (float)(Minecraft.getSystemTime() - returningStackTime) / 100F;

            if (f >= 1.0F)
            {
                f = 1.0F;
                returningStack = null;
            }

            int l1 = returningStackDestSlot.xDisplayPosition - field_85049_r;
            int i2 = returningStackDestSlot.yDisplayPosition - field_85048_s;
            int j2 = field_85049_r + (int)((float)l1 * f);
            int k2 = field_85048_s + (int)((float)i2 * f);
            drawItemStack(returningStack, j2, k2);
        }

        if (inventoryplayer.getItemStack() == null && theSlot != null && theSlot.getHasStack() && tooltips)
        {
            ItemStack itemstack1 = theSlot.getStack();
            drawItemStackTooltip(itemstack1, (par1 - i) + 8, (par2 - j) + 8);
        }

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    private void drawItemStack(ItemStack par1ItemStack, int par2, int par3)
    {
        GL11.glTranslatef(0.0F, 0.0F, 32F);
        zLevel = 200F;
        itemRenderer.zLevel = 200F;
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, par1ItemStack, par2, par3);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, par1ItemStack, par2, par3 - (draggedStack != null ? 8 : 0));
        zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
    }

    protected void drawItemStackTooltip(ItemStack par1ItemStack, int par2, int par3)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        List list = par1ItemStack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        if (!list.isEmpty())
        {
            int i = 0;

            for (int j = 0; j < list.size(); j++)
            {
                int l = fontRenderer.getStringWidth((String)list.get(j));

                if (l > i)
                {
                    i = l;
                }
            }

            int k = par2 + 12;
            int i1 = par3 - 12;
            int j1 = i;
            int k1 = 8;

            if (list.size() > 1)
            {
                k1 += 2 + (list.size() - 1) * 10;
            }

            if (guiTop + i1 + k1 + 6 > height)
            {
                i1 = height - k1 - guiTop - 6;
            }

            zLevel = 300F;
            itemRenderer.zLevel = 300F;
            if (oldtooltips){
                drawGradientRect(k - 3, i1 - 3, k + j1 + 3, i1 + k1 + 3, 0xc0000000, 0xc0000000);
            }else{
                int l1 = 0xf0100010;
                drawGradientRect(k - 3, i1 - 4, k + j1 + 3, i1 - 3, l1, l1);
                drawGradientRect(k - 3, i1 + k1 + 3, k + j1 + 3, i1 + k1 + 4, l1, l1);
                drawGradientRect(k - 3, i1 - 3, k + j1 + 3, i1 + k1 + 3, l1, l1);
                drawGradientRect(k - 4, i1 - 3, k - 3, i1 + k1 + 3, l1, l1);
                drawGradientRect(k + j1 + 3, i1 - 3, k + j1 + 4, i1 + k1 + 3, l1, l1);
                int i2 = 0x505000ff;
                int j2 = (i2 & 0xfefefe) >> 1 | i2 & 0xff000000;
                drawGradientRect(k - 3, (i1 - 3) + 1, (k - 3) + 1, (i1 + k1 + 3) - 1, i2, j2);
                drawGradientRect(k + j1 + 2, (i1 - 3) + 1, k + j1 + 3, (i1 + k1 + 3) - 1, i2, j2);
                drawGradientRect(k - 3, i1 - 3, k + j1 + 3, (i1 - 3) + 1, i2, i2);
                drawGradientRect(k - 3, i1 + k1 + 2, k + j1 + 3, i1 + k1 + 3, j2, j2);
            }

            for (int k2 = 0; k2 < list.size(); k2++)
            {
                String s = (String)list.get(k2);

                if (k2 == 0)
                {
                    s = (new StringBuilder()).append("\247").append(Integer.toHexString(par1ItemStack.getRarity().rarityColor)).append(s).toString();
                }
                else
                {
                    s = (new StringBuilder()).append("\2477").append(s).toString();
                }

                fontRenderer.drawStringWithShadow(s, k, i1, -1);

                if (k2 == 0)
                {
                    i1 += 2;
                }

                i1 += 10;
            }

            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }
    }

    /**
     * Draws the text when mouse is over creative inventory tab. Params: current creative tab to be checked, current
     * mouse x position, current mouse y position.
     */
    protected void drawCreativeTabHoveringText(String par1Str, int par2, int par3)
    {
        if (!tooltips){
            return;
        }
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int i = fontRenderer.getStringWidth(par1Str);
        int j = par2 + 12;
        int k = par3 - 12;
        int l = i;
        byte byte0 = 8;
        zLevel = 300F;
        itemRenderer.zLevel = 300F;
        if (oldtooltips){
            drawGradientRect(j - 3, k - 3, j + l + 3, k + byte0 + 3, 0xc0000000, 0xc0000000);
        }else{
            int i1 = 0xf0100010;
            drawGradientRect(j - 3, k - 4, j + l + 3, k - 3, i1, i1);
            drawGradientRect(j - 3, k + byte0 + 3, j + l + 3, k + byte0 + 4, i1, i1);
            drawGradientRect(j - 3, k - 3, j + l + 3, k + byte0 + 3, i1, i1);
            drawGradientRect(j - 4, k - 3, j - 3, k + byte0 + 3, i1, i1);
            drawGradientRect(j + l + 3, k - 3, j + l + 4, k + byte0 + 3, i1, i1);
            int j1 = 0x505000ff;
            int k1 = (j1 & 0xfefefe) >> 1 | j1 & 0xff000000;
            drawGradientRect(j - 3, (k - 3) + 1, (j - 3) + 1, (k + byte0 + 3) - 1, j1, k1);
            drawGradientRect(j + l + 2, (k - 3) + 1, j + l + 3, (k + byte0 + 3) - 1, j1, k1);
            drawGradientRect(j - 3, k - 3, j + l + 3, (k - 3) + 1, j1, j1);
            drawGradientRect(j - 3, k + byte0 + 2, j + l + 3, k + byte0 + 3, k1, k1);
        }
        fontRenderer.drawStringWithShadow(par1Str, j, k, -1);
        zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected abstract void drawGuiContainerBackgroundLayer(float f, int i, int j);

    /**
     * Draws an inventory slot
     */
    private void drawSlotInventory(Slot par1Slot)
    {
        int i = par1Slot.xDisplayPosition;
        int j = par1Slot.yDisplayPosition;
        ItemStack itemstack = par1Slot.getStack();
        boolean flag = par1Slot == clickedSlot && draggedStack != null && !field_90018_r;

        if (par1Slot == clickedSlot && draggedStack != null && field_90018_r && itemstack != null)
        {
            itemstack = itemstack.copy();
            itemstack.stackSize /= 2;
        }

        zLevel = 100F;
        itemRenderer.zLevel = 100F;

        if (itemstack == null)
        {
            int k = par1Slot.getBackgroundIconIndex();

            if (k >= 0)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
                drawTexturedModalRect(i, j, (k % 16) * 16, (k / 16) * 16, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                flag = true;
            }
        }

        if (!flag)
        {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
        }

        itemRenderer.zLevel = 0.0F;
        zLevel = 0.0F;
    }

    /**
     * Returns the slot at the given coordinates or null if there is none.
     */
    private Slot getSlotAtPosition(int par1, int par2)
    {
        for (int i = 0; i < inventorySlots.inventorySlots.size(); i++)
        {
            Slot slot = (Slot)inventorySlots.inventorySlots.get(i);

            if (isMouseOverSlot(slot, par1, par2))
            {
                return slot;
            }
        }

        return null;
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        boolean flag = par3 == mc.gameSettings.keyBindPickBlock.keyCode + 100;

        if (par3 == 0 || par3 == 1 || flag)
        {
            Slot slot = getSlotAtPosition(par1, par2);
            int i = guiLeft;
            int j = guiTop;
            boolean flag1 = par1 < i || par2 < j || par1 >= i + xSize || par2 >= j + ySize;
            int k = -1;

            if (slot != null)
            {
                k = slot.slotNumber;
            }

            if (flag1)
            {
                k = -999;
            }

            if (mc.gameSettings.touchscreen && flag1 && mc.thePlayer.inventory.getItemStack() == null)
            {
                mc.displayGuiScreen(null);
                return;
            }

            if (k != -1)
            {
                if (mc.gameSettings.touchscreen)
                {
                    if (slot != null && slot.getHasStack())
                    {
                        clickedSlot = slot;
                        draggedStack = null;
                        field_90018_r = par3 == 1;
                    }
                    else
                    {
                        clickedSlot = null;
                    }
                }
                else if (flag)
                {
                    handleMouseClick(slot, k, par3, 3);
                }
                else
                {
                    boolean flag2 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                    handleMouseClick(slot, k, par3, flag2 ? 1 : 0);
                }
            }
        }
    }

    protected void func_85041_a(int par1, int par2, int par3, long par4)
    {
        if (clickedSlot == null || !mc.gameSettings.touchscreen)
        {
            return;
        }

        if (par3 == 0 || par3 == 1)
        {
            Slot slot = getSlotAtPosition(par1, par2);

            if (draggedStack == null)
            {
                if (slot != clickedSlot)
                {
                    draggedStack = clickedSlot.getStack().copy();
                }
            }
            else if (draggedStack.stackSize > 1 && slot != null && func_92031_b(slot))
            {
                long l = Minecraft.getSystemTime();

                if (field_92033_y == slot)
                {
                    if (l - field_92032_z > 500L)
                    {
                        handleMouseClick(clickedSlot, clickedSlot.slotNumber, 0, 0);
                        handleMouseClick(slot, slot.slotNumber, 1, 0);
                        handleMouseClick(clickedSlot, clickedSlot.slotNumber, 0, 0);
                        field_92032_z = l + 750L;
                        draggedStack.stackSize--;
                    }
                }
                else
                {
                    field_92033_y = slot;
                    field_92032_z = l;
                }
            }
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (clickedSlot == null || !mc.gameSettings.touchscreen)
        {
            return;
        }

        if (par3 == 0 || par3 == 1)
        {
            Slot slot = getSlotAtPosition(par1, par2);
            int i = guiLeft;
            int j = guiTop;
            boolean flag = par1 < i || par2 < j || par1 >= i + xSize || par2 >= j + ySize;
            int k = -1;

            if (slot != null)
            {
                k = slot.slotNumber;
            }

            if (flag)
            {
                k = -999;
            }

            if (draggedStack == null && slot != clickedSlot)
            {
                draggedStack = clickedSlot.getStack();
            }

            boolean flag1 = func_92031_b(slot);

            if (k != -1 && draggedStack != null && flag1)
            {
                handleMouseClick(clickedSlot, clickedSlot.slotNumber, par3, 0);
                handleMouseClick(slot, k, 0, 0);

                if (mc.thePlayer.inventory.getItemStack() != null)
                {
                    handleMouseClick(clickedSlot, clickedSlot.slotNumber, par3, 0);
                    field_85049_r = par1 - i;
                    field_85048_s = par2 - j;
                    returningStackDestSlot = clickedSlot;
                    returningStack = draggedStack;
                    returningStackTime = Minecraft.getSystemTime();
                }
                else
                {
                    returningStack = null;
                }
            }
            else if (draggedStack != null)
            {
                field_85049_r = par1 - i;
                field_85048_s = par2 - j;
                returningStackDestSlot = clickedSlot;
                returningStack = draggedStack;
                returningStackTime = Minecraft.getSystemTime();
            }

            draggedStack = null;
            clickedSlot = null;
        }
    }

    private boolean func_92031_b(Slot par1Slot)
    {
        boolean flag = par1Slot == null || !par1Slot.getHasStack();

        if (par1Slot != null && par1Slot.getHasStack() && draggedStack != null && ItemStack.areItemStackTagsEqual(par1Slot.getStack(), draggedStack))
        {
            flag |= par1Slot.getStack().stackSize + draggedStack.stackSize <= draggedStack.getMaxStackSize();
        }

        return flag;
    }

    /**
     * Returns if the passed mouse position is over the specified slot.
     */
    private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3)
    {
        return func_74188_c(par1Slot.xDisplayPosition, par1Slot.yDisplayPosition, 16, 16, par2, par3);
    }

    protected boolean func_74188_c(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        int i = guiLeft;
        int j = guiTop;
        par5 -= i;
        par6 -= j;
        return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
    }

    protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4)
    {
        if (par1Slot != null)
        {
            par2 = par1Slot.slotNumber;
        }

        mc.playerController.windowClick(inventorySlots.windowId, par2, par3, par4, mc.thePlayer);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1 || par2 == mc.gameSettings.keyBindInventory.keyCode)
        {
            mc.thePlayer.closeScreen();
        }

        func_82319_a(par2);

        if (par2 == mc.gameSettings.keyBindPickBlock.keyCode && theSlot != null && theSlot.getHasStack())
        {
            handleMouseClick(theSlot, theSlot.slotNumber, ySize, 3);
        }
    }

    protected boolean func_82319_a(int par1)
    {
        if (mc.thePlayer.inventory.getItemStack() == null && theSlot != null)
        {
            for (int i = 0; i < 9; i++)
            {
                if (par1 == 2 + i)
                {
                    handleMouseClick(theSlot, theSlot.slotNumber, i, 2);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        if (mc.thePlayer == null)
        {
            return;
        }
        else
        {
            inventorySlots.onCraftGuiClosed(mc.thePlayer);
            return;
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

        if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
        {
            mc.thePlayer.closeScreen();
        }
    }
}
