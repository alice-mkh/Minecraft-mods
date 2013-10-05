package net.minecraft.src;

import java.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiContainer extends GuiScreen
{
    public static boolean tooltips = true;
    public static boolean oldtooltips = false;

    protected static final ResourceLocation field_110408_a = new ResourceLocation("textures/gui/container/inventory.png");

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

    /** Used when touchscreen is enabled */
    private boolean isRightMouseClick;

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
    protected final Set field_94077_p = new HashSet();
    protected boolean field_94076_q;
    private int field_94071_C;
    private int field_94067_D;
    private boolean field_94068_E;
    private int field_94069_F;
    private long field_94070_G;
    private Slot field_94072_H;
    private int field_94073_I;
    private boolean field_94074_J;
    private ItemStack field_94075_K;

    public GuiContainer(Container par1Container)
    {
        xSize = 176;
        ySize = 166;
        inventorySlots = par1Container;
        field_94068_E = true;
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

            if (isMouseOverSlot(slot, par1, par2) && slot.func_111238_b())
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
            String s = null;

            if (draggedStack != null && isRightMouseClick)
            {
                itemstack = itemstack.copy();
                itemstack.stackSize = MathHelper.ceiling_float_int((float)itemstack.stackSize / 2.0F);
            }
            else if (field_94076_q && field_94077_p.size() > 1)
            {
                itemstack = itemstack.copy();
                itemstack.stackSize = field_94069_F;

                if (itemstack.stackSize == 0)
                {
                    s = (new StringBuilder()).append("").append(EnumChatFormatting.YELLOW).append("0").toString();
                }
            }

            drawItemStack(itemstack, par1 - i - byte0, par2 - j - byte1, s);
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
            drawItemStack(returningStack, j2, k2, null);
        }

        GL11.glPopMatrix();

        if (inventoryplayer.getItemStack() == null && theSlot != null && theSlot.getHasStack())
        {
            ItemStack itemstack1 = theSlot.getStack();
            drawItemStackTooltip(itemstack1, par1, par2);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    private void drawItemStack(ItemStack par1ItemStack, int par2, int par3, String par4Str)
    {
        GL11.glTranslatef(0.0F, 0.0F, 32F);
        zLevel = 200F;
        itemRenderer.zLevel = 200F;
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.getTextureManager(), par1ItemStack, par2, par3);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.getTextureManager(), par1ItemStack, par2, par3 - (draggedStack != null ? 8 : 0), par4Str);
        zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
    }

    protected void drawItemStackTooltip(ItemStack par1ItemStack, int par2, int par3)
    {
        List list = par1ItemStack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for (int i = 0; i < list.size(); i++)
        {
            if (i == 0)
            {
                list.set(i, (new StringBuilder()).append("\247").append(Integer.toHexString(par1ItemStack.getRarity().rarityColor)).append((String)list.get(i)).toString());
            }
            else
            {
                list.set(i, (new StringBuilder()).append(EnumChatFormatting.GRAY).append((String)list.get(i)).toString());
            }
        }

        func_102021_a(list, par2, par3);
    }

    /**
     * Draws the text when mouse is over creative inventory tab. Params: current creative tab to be checked, current
     * mouse x position, current mouse y position.
     */
    protected void drawCreativeTabHoveringText(String par1Str, int par2, int par3)
    {
        func_102021_a(Arrays.asList(new String[]
                {
                    par1Str
                }), par2, par3);
    }

    protected void func_102021_a(List par1List, int par2, int par3)
    {
        if (par1List.isEmpty() || !tooltips)
        {
            return;
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int i = 0;
        Iterator iterator = par1List.iterator();

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

        if (par1List.size() > 1)
        {
            j1 += 2 + (par1List.size() - 1) * 10;
        }

        if (j + i > width)
        {
            j -= 28 + i;
        }

        if (k + j1 + 6 > height)
        {
            k = height - j1 - 6;
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

        for (int j2 = 0; j2 < par1List.size(); j2++)
        {
            String s1 = (String)par1List.get(j2);
            fontRenderer.drawStringWithShadow(s1, j, k, -1);

            if (j2 == 0)
            {
                k += 2;
            }

            k += 10;
        }

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
        boolean flag = false;
        boolean flag1 = par1Slot == clickedSlot && draggedStack != null && !isRightMouseClick;
        ItemStack itemstack1 = mc.thePlayer.inventory.getItemStack();
        String s = null;

        if (par1Slot == clickedSlot && draggedStack != null && isRightMouseClick && itemstack != null)
        {
            itemstack = itemstack.copy();
            itemstack.stackSize /= 2;
        }
        else if (field_94076_q && field_94077_p.contains(par1Slot) && itemstack1 != null)
        {
            if (field_94077_p.size() == 1)
            {
                return;
            }

            if (Container.func_94527_a(par1Slot, itemstack1, true) && inventorySlots.canDragIntoSlot(par1Slot))
            {
                itemstack = itemstack1.copy();
                flag = true;
                Container.func_94525_a(field_94077_p, field_94071_C, itemstack, par1Slot.getStack() != null ? par1Slot.getStack().stackSize : 0);

                if (itemstack.stackSize > itemstack.getMaxStackSize())
                {
                    s = (new StringBuilder()).append(EnumChatFormatting.YELLOW).append("").append(itemstack.getMaxStackSize()).toString();
                    itemstack.stackSize = itemstack.getMaxStackSize();
                }

                if (itemstack.stackSize > par1Slot.getSlotStackLimit())
                {
                    s = (new StringBuilder()).append(EnumChatFormatting.YELLOW).append("").append(par1Slot.getSlotStackLimit()).toString();
                    itemstack.stackSize = par1Slot.getSlotStackLimit();
                }
            }
            else
            {
                field_94077_p.remove(par1Slot);
                func_94066_g();
            }
        }

        zLevel = 100F;
        itemRenderer.zLevel = 100F;

        if (itemstack == null)
        {
            Icon icon = par1Slot.getBackgroundIconIndex();

            if (icon != null)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
                drawTexturedModelRectFromIcon(i, j, icon, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                flag1 = true;
            }
        }

        if (!flag1)
        {
            if (flag)
            {
                drawRect(i, j, i + 16, j + 16, 0x80ffffff);
            }

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.getTextureManager(), itemstack, i, j);
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.getTextureManager(), itemstack, i, j, s);
        }

        itemRenderer.zLevel = 0.0F;
        zLevel = 0.0F;
    }

    private void func_94066_g()
    {
        ItemStack itemstack = mc.thePlayer.inventory.getItemStack();

        if (itemstack == null || !field_94076_q)
        {
            return;
        }

        field_94069_F = itemstack.stackSize;

        for (Iterator iterator = field_94077_p.iterator(); iterator.hasNext();)
        {
            Slot slot = (Slot)iterator.next();
            ItemStack itemstack1 = itemstack.copy();
            int i = slot.getStack() != null ? slot.getStack().stackSize : 0;
            Container.func_94525_a(field_94077_p, field_94071_C, itemstack1, i);

            if (itemstack1.stackSize > itemstack1.getMaxStackSize())
            {
                itemstack1.stackSize = itemstack1.getMaxStackSize();
            }

            if (itemstack1.stackSize > slot.getSlotStackLimit())
            {
                itemstack1.stackSize = slot.getSlotStackLimit();
            }

            field_94069_F -= itemstack1.stackSize - i;
        }
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
        Slot slot = getSlotAtPosition(par1, par2);
        long l = Minecraft.getSystemTime();
        field_94074_J = field_94072_H == slot && l - field_94070_G < 250L && field_94073_I == par3;
        field_94068_E = false;

        if (par3 == 0 || par3 == 1 || flag)
        {
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
                        isRightMouseClick = par3 == 1;
                    }
                    else
                    {
                        clickedSlot = null;
                    }
                }
                else if (!field_94076_q)
                {
                    if (mc.thePlayer.inventory.getItemStack() == null)
                    {
                        if (par3 == mc.gameSettings.keyBindPickBlock.keyCode + 100)
                        {
                            handleMouseClick(slot, k, par3, 3);
                        }
                        else
                        {
                            boolean flag2 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            byte byte0 = 0;

                            if (flag2)
                            {
                                field_94075_K = slot == null || !slot.getHasStack() ? null : slot.getStack();
                                byte0 = 1;
                            }
                            else if (k == -999)
                            {
                                byte0 = 4;
                            }

                            handleMouseClick(slot, k, par3, byte0);
                        }

                        field_94068_E = true;
                    }
                    else
                    {
                        field_94076_q = true;
                        field_94067_D = par3;
                        field_94077_p.clear();

                        if (par3 == 0)
                        {
                            field_94071_C = 0;
                        }
                        else if (par3 == 1)
                        {
                            field_94071_C = 1;
                        }
                    }
                }
            }
        }

        field_94072_H = slot;
        field_94070_G = l;
        field_94073_I = par3;
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    protected void mouseClickMove(int par1, int par2, int par3, long par4)
    {
        Slot slot = getSlotAtPosition(par1, par2);
        ItemStack itemstack = mc.thePlayer.inventory.getItemStack();

        if (clickedSlot != null && mc.gameSettings.touchscreen)
        {
            if (par3 == 0 || par3 == 1)
            {
                if (draggedStack == null)
                {
                    if (slot != clickedSlot)
                    {
                        draggedStack = clickedSlot.getStack().copy();
                    }
                }
                else if (draggedStack.stackSize > 1 && slot != null && Container.func_94527_a(slot, draggedStack, false))
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
        else if (field_94076_q && slot != null && itemstack != null && itemstack.stackSize > field_94077_p.size() && Container.func_94527_a(slot, itemstack, true) && slot.isItemValid(itemstack) && inventorySlots.canDragIntoSlot(slot))
        {
            field_94077_p.add(slot);
            func_94066_g();
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int par1, int par2, int par3)
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

        if (field_94074_J && slot != null && par3 == 0 && inventorySlots.func_94530_a(null, slot))
        {
            if (isShiftKeyDown())
            {
                if (slot != null && slot.inventory != null && field_94075_K != null)
                {
                    Iterator iterator = inventorySlots.inventorySlots.iterator();

                    do
                    {
                        if (!iterator.hasNext())
                        {
                            break;
                        }

                        Slot slot1 = (Slot)iterator.next();

                        if (slot1 != null && slot1.canTakeStack(mc.thePlayer) && slot1.getHasStack() && slot1.inventory == slot.inventory && Container.func_94527_a(slot1, field_94075_K, true))
                        {
                            handleMouseClick(slot1, slot1.slotNumber, par3, 1);
                        }
                    }
                    while (true);
                }
            }
            else
            {
                handleMouseClick(slot, k, par3, 6);
            }

            field_94074_J = false;
            field_94070_G = 0L;
        }
        else
        {
            if (field_94076_q && field_94067_D != par3)
            {
                field_94076_q = false;
                field_94077_p.clear();
                field_94068_E = true;
                return;
            }

            if (field_94068_E)
            {
                field_94068_E = false;
                return;
            }

            if (clickedSlot != null && mc.gameSettings.touchscreen)
            {
                if (par3 == 0 || par3 == 1)
                {
                    if (draggedStack == null && slot != clickedSlot)
                    {
                        draggedStack = clickedSlot.getStack();
                    }

                    boolean flag1 = Container.func_94527_a(slot, draggedStack, false);

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
            else if (field_94076_q && !field_94077_p.isEmpty())
            {
                handleMouseClick(null, -999, Container.func_94534_d(0, field_94071_C), 5);
                Slot slot2;

                for (Iterator iterator1 = field_94077_p.iterator(); iterator1.hasNext(); handleMouseClick(slot2, slot2.slotNumber, Container.func_94534_d(1, field_94071_C), 5))
                {
                    slot2 = (Slot)iterator1.next();
                }

                handleMouseClick(null, -999, Container.func_94534_d(2, field_94071_C), 5);
            }
            else if (mc.thePlayer.inventory.getItemStack() != null)
            {
                if (par3 == mc.gameSettings.keyBindPickBlock.keyCode + 100)
                {
                    handleMouseClick(slot, k, par3, 3);
                }
                else
                {
                    boolean flag2 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

                    if (flag2)
                    {
                        field_94075_K = slot == null || !slot.getHasStack() ? null : slot.getStack();
                    }

                    handleMouseClick(slot, k, par3, flag2 ? 1 : 0);
                }
            }
        }

        if (mc.thePlayer.inventory.getItemStack() == null)
        {
            field_94070_G = 0L;
        }

        field_94076_q = false;
    }

    /**
     * Returns if the passed mouse position is over the specified slot.
     */
    private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3)
    {
        return isPointInRegion(par1Slot.xDisplayPosition, par1Slot.yDisplayPosition, 16, 16, par2, par3);
    }

    /**
     * Args: left, top, width, height, pointX, pointY. Note: left, top are local to Gui, pointX, pointY are local to
     * screen
     */
    protected boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6)
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

        checkHotbarKeys(par2);

        if (theSlot != null && theSlot.getHasStack())
        {
            if (par2 == mc.gameSettings.keyBindPickBlock.keyCode)
            {
                handleMouseClick(theSlot, theSlot.slotNumber, 0, 3);
            }
            else if (par2 == mc.gameSettings.keyBindDrop.keyCode)
            {
                handleMouseClick(theSlot, theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
            }
        }
    }

    /**
     * This function is what controls the hotbar shortcut check when you press a number key when hovering a stack.
     */
    protected boolean checkHotbarKeys(int par1)
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
            inventorySlots.onContainerClosed(mc.thePlayer);
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
