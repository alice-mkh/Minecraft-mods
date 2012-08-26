package net.minecraft.src;

import java.util.Iterator;
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

    public GuiContainer(Container par1Container)
    {
        xSize = 176;
        ySize = 166;
        inventorySlots = par1Container;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        mc.thePlayer.craftingInventory = inventorySlots;
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
        Slot slot = null;
        int k = 240;
        int i1 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)i1 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (int l = 0; l < inventorySlots.inventorySlots.size(); l++)
        {
            Slot slot1 = (Slot)inventorySlots.inventorySlots.get(l);
            drawSlotInventory(slot1);

            if (isMouseOverSlot(slot1, par1, par2))
            {
                slot = slot1;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int j1 = slot1.xDisplayPosition;
                int k1 = slot1.yDisplayPosition;
                drawGradientRect(j1, k1, j1 + 16, k1 + 16, 0x80ffffff, 0x80ffffff);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }

        drawGuiContainerForegroundLayer();
        InventoryPlayer inventoryplayer = mc.thePlayer.inventory;

        if (inventoryplayer.getItemStack() != null)
        {
            GL11.glTranslatef(0.0F, 0.0F, 32F);
            zLevel = 200F;
            itemRenderer.zLevel = 200F;
            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), par1 - i - 8, par2 - j - 8);
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), par1 - i - 8, par2 - j - 8);
            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }

        if (inventoryplayer.getItemStack() == null && slot != null && slot.getHasStack() && tooltips)
        {
            ItemStack itemstack = slot.getStack();
            func_74184_a(itemstack, par1 - i, par2 - j);
        }

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    protected void func_74184_a(ItemStack par1ItemStack, int par2, int par3)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        List list = par1ItemStack.getItemNameandInformation();

        if (!list.isEmpty())
        {
            int i = 0;
            Iterator iterator = list.iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                String s = (String)iterator.next();
                int l = fontRenderer.getStringWidth(s);

                if (l > i)
                {
                    i = l;
                }
            }
            while (true);

            int j = par2 + 12;
            int k = par3 - 12;
            int i1 = i;
            int j1 = 8;

            if (list.size() > 1)
            {
                j1 += 2 + (list.size() - 1) * 10;
            }

            zLevel = 300F;
            itemRenderer.zLevel = 300F;
            if (oldtooltips){
                drawGradientRect(j - 3, k - 3, j + i1 + 3, k + j1 + 3, 0xc0000000, 0xc0000000);
            }else{
                int k1 = 0xf0100010;
                drawGradientRect(j - 3, k - 4, j + i1 + 3, k - 3, k1, k1);
                drawGradientRect(j - 3, k + j1 + 3, j + i1 + 3, k + j1 + 4, k1, k1);
                drawGradientRect(j - 3, k - 3, j + i1 + 3, k + j1 + 3, k1, k1);
                drawGradientRect(j - 4, k - 3, j - 3, k + j1 + 3, k1, k1);
                drawGradientRect(j + i1 + 3, k - 3, j + i1 + 4, k + j1 + 3, k1, k1);
                int l1 = 0x505000ff;
                int i2 = (l1 & 0xfefefe) >> 1 | l1 & 0xff000000;
                drawGradientRect(j - 3, (k - 3) + 1, (j - 3) + 1, (k + j1 + 3) - 1, l1, i2);
                drawGradientRect(j + i1 + 2, (k - 3) + 1, j + i1 + 3, (k + j1 + 3) - 1, l1, i2);
                drawGradientRect(j - 3, k - 3, j + i1 + 3, (k - 3) + 1, l1, l1);
                drawGradientRect(j - 3, k + j1 + 2, j + i1 + 3, k + j1 + 3, i2, i2);
            }

            for (int j2 = 0; j2 < list.size(); j2++)
            {
                String s1 = (String)list.get(j2);

                if (j2 == 0)
                {
                    s1 = (new StringBuilder()).append("\247").append(Integer.toHexString(par1ItemStack.getRarity().rarityColor)).append(s1).toString();
                }
                else
                {
                    s1 = (new StringBuilder()).append("\2477").append(s1).toString();
                }

                fontRenderer.drawStringWithShadow(s1, j, k, -1);

                if (j2 == 0)
                {
                    k += 2;
                }

                k += 10;
            }

            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }
    }

    protected void func_74190_a(String par1Str, int par2, int par3)
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
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer()
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
        boolean flag = false;
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
            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
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

            if (k != -1)
            {
                boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                handleMouseClick(slot, k, par3, flag1);
            }
        }
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

    protected void handleMouseClick(Slot par1Slot, int par2, int par3, boolean par4)
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
