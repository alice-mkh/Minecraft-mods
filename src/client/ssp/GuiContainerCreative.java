package net.minecraft.src;

import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiContainerCreative extends InventoryEffectRenderer
{
    private static InventoryBasic inventory = new InventoryBasic("tmp", 45);
    private static int field_74241_p;

    /** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
    private float currentScroll;

    /** True if the scrollbar is being dragged */
    private boolean isScrolling;

    /**
     * True if the left mouse button was held down last time drawScreen was called.
     */
    private boolean wasClicking;
    private GuiTextField field_74237_t;
    private List field_74236_u;
    private Slot field_74235_v;
    private boolean field_74234_w;

    public GuiContainerCreative(EntityPlayer par1EntityPlayer)
    {
        super(new ContainerCreative(par1EntityPlayer));
        currentScroll = 0.0F;
        isScrolling = false;
        field_74235_v = null;
        field_74234_w = false;
        par1EntityPlayer.craftingInventory = inventorySlots;
        allowUserInput = true;
        par1EntityPlayer.addStat(AchievementList.openInventory, 1);
        ySize = 136;
        xSize = 195;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (!mc.field_71442_b.isInCreativeMode())
        {
            mc.displayGuiScreen(new GuiInventory(mc.field_71439_g));
        }
    }

    protected void handleMouseClick(Slot par1Slot, int par2, int par3, boolean par4)
    {
        field_74234_w = true;

        if (par1Slot != null)
        {
            if (par1Slot == field_74235_v && par4)
            {
                for (int i = 0; i < mc.field_71439_g.inventorySlots.func_75138_a().size(); i++)
                {
                    mc.field_71442_b.sendSlotPacket(null, i);
                }
            }
            else if (field_74241_p == CreativeTabs.field_78036_m.func_78021_a())
            {
                if (par1Slot == field_74235_v)
                {
                    mc.field_71439_g.inventory.setItemStack(null);
                }
                else
                {
                    int j = SlotCreativeInventory.func_75240_a((SlotCreativeInventory)par1Slot).slotNumber;

                    if (par4)
                    {
                        mc.field_71442_b.sendSlotPacket(null, j);
                    }
                    else
                    {
                        mc.field_71439_g.inventorySlots.slotClick(j, par3, par4, mc.field_71439_g);
                        ItemStack itemstack1 = mc.field_71439_g.inventorySlots.getSlot(j).getStack();
                        mc.field_71442_b.sendSlotPacket(itemstack1, j);
                    }
                }
            }
            else if (par1Slot.inventory == inventory)
            {
                InventoryPlayer inventoryplayer = mc.field_71439_g.inventory;
                ItemStack itemstack2 = inventoryplayer.getItemStack();
                ItemStack itemstack5 = par1Slot.getStack();

                if (itemstack2 != null && itemstack5 != null && itemstack2.isItemEqual(itemstack5))
                {
                    if (par3 == 0)
                    {
                        if (par4)
                        {
                            itemstack2.stackSize = itemstack2.getMaxStackSize();
                        }
                        else if (itemstack2.stackSize < itemstack2.getMaxStackSize())
                        {
                            itemstack2.stackSize++;
                        }
                    }
                    else if (itemstack2.stackSize <= 1)
                    {
                        inventoryplayer.setItemStack(null);
                    }
                    else
                    {
                        itemstack2.stackSize--;
                    }
                }
                else if (itemstack5 == null || itemstack2 != null)
                {
                    inventoryplayer.setItemStack(null);
                }
                else
                {
                    boolean flag = false;

                    if (!flag)
                    {
                        inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack5));
                        ItemStack itemstack3 = inventoryplayer.getItemStack();

                        if (par4)
                        {
                            itemstack3.stackSize = itemstack3.getMaxStackSize();
                        }
                    }
                }
            }
            else
            {
                inventorySlots.slotClick(par1Slot.slotNumber, par3, par4, mc.field_71439_g);
                ItemStack itemstack = inventorySlots.getSlot(par1Slot.slotNumber).getStack();
                mc.field_71442_b.sendSlotPacket(itemstack, (par1Slot.slotNumber - inventorySlots.inventorySlots.size()) + 9 + 36);
            }
        }
        else
        {
            InventoryPlayer inventoryplayer1 = mc.field_71439_g.inventory;

            if (inventoryplayer1.getItemStack() != null)
            {
                if (par3 == 0)
                {
                    mc.field_71439_g.dropPlayerItem(inventoryplayer1.getItemStack());
                    mc.field_71442_b.func_78752_a(inventoryplayer1.getItemStack());
                    inventoryplayer1.setItemStack(null);
                }

                if (par3 == 1)
                {
                    ItemStack itemstack4 = inventoryplayer1.getItemStack().splitStack(1);
                    mc.field_71439_g.dropPlayerItem(itemstack4);
                    mc.field_71442_b.func_78752_a(itemstack4);

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
    public void initGui()
    {
        if (mc.field_71442_b.isInCreativeMode())
        {
            super.initGui();
            controlList.clear();
            Keyboard.enableRepeatEvents(true);
            field_74237_t = new GuiTextField(fontRenderer, guiLeft + 82, guiTop + 6, 89, fontRenderer.FONT_HEIGHT);
            field_74237_t.setMaxStringLength(15);
            field_74237_t.setEnableBackgroundDrawing(false);
            field_74237_t.func_73790_e(false);
            field_74237_t.func_73794_g(0xffffff);
            int i = field_74241_p;
            field_74241_p = -1;
            func_74227_b(CreativeTabs.field_78032_a[i]);
        }
        else
        {
            mc.displayGuiScreen(new GuiInventory(mc.field_71439_g));
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (field_74241_p != CreativeTabs.field_78027_g.func_78021_a())
        {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindChat.keyCode))
            {
                func_74227_b(CreativeTabs.field_78027_g);
            }
            else
            {
                super.keyTyped(par1, par2);
            }

            return;
        }

        if (field_74234_w)
        {
            field_74234_w = false;
            field_74237_t.setText("");
        }

        if (field_74237_t.textboxKeyTyped(par1, par2))
        {
            func_74228_j();
        }
        else
        {
            super.keyTyped(par1, par2);
        }
    }

    private void func_74228_j()
    {
        ContainerCreative containercreative = (ContainerCreative)inventorySlots;
        containercreative.itemList.clear();
        Item aitem[] = Item.itemsList;
        int i = aitem.length;

        for (int j = 0; j < i; j++)
        {
            Item item = aitem[j];

            if (item != null && item.func_77640_w() != null)
            {
                item.func_77633_a(item.shiftedIndex, null, containercreative.itemList);
            }
        }

        Iterator iterator = containercreative.itemList.iterator();
        String s = field_74237_t.getText().toLowerCase();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            ItemStack itemstack = (ItemStack)iterator.next();
            boolean flag = false;
            Iterator iterator1 = itemstack.getItemNameandInformation().iterator();

            do
            {
                if (!iterator1.hasNext())
                {
                    break;
                }

                String s1 = (String)iterator1.next();

                if (!s1.toLowerCase().contains(s))
                {
                    continue;
                }

                flag = true;
                break;
            }
            while (true);

            if (!flag)
            {
                iterator.remove();
            }
        }
        while (true);

        currentScroll = 0.0F;
        containercreative.scrollTo(0.0F);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everythin in front of the items)
     */
    protected void drawGuiContainerForegroundLayer()
    {
        CreativeTabs creativetabs = CreativeTabs.field_78032_a[field_74241_p];

        if (creativetabs.func_78019_g())
        {
            fontRenderer.drawString(creativetabs.func_78024_c(), 8, 6, 0x404040);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            int i = par1 - guiLeft;
            int j = par2 - guiTop;
            CreativeTabs acreativetabs[] = CreativeTabs.field_78032_a;
            int k = acreativetabs.length;

            for (int l = 0; l < k; l++)
            {
                CreativeTabs creativetabs = acreativetabs[l];

                if (func_74232_a(creativetabs, i, j))
                {
                    func_74227_b(creativetabs);
                    return;
                }
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    private boolean func_74226_k()
    {
        return field_74241_p != CreativeTabs.field_78036_m.func_78021_a() && CreativeTabs.field_78032_a[field_74241_p].func_78017_i() && ((ContainerCreative)inventorySlots).func_75184_d();
    }

    private void func_74227_b(CreativeTabs par1CreativeTabs)
    {
        int i = field_74241_p;
        field_74241_p = par1CreativeTabs.func_78021_a();
        ContainerCreative containercreative = (ContainerCreative)inventorySlots;
        containercreative.itemList.clear();
        par1CreativeTabs.func_78018_a(containercreative.itemList);

        if (par1CreativeTabs == CreativeTabs.field_78036_m)
        {
            Container container = mc.field_71439_g.inventorySlots;

            if (field_74236_u == null)
            {
                field_74236_u = containercreative.inventorySlots;
            }

            containercreative.inventorySlots = new ArrayList();

            for (int j = 0; j < container.inventorySlots.size(); j++)
            {
                SlotCreativeInventory slotcreativeinventory = new SlotCreativeInventory(this, (Slot)container.inventorySlots.get(j), j);
                containercreative.inventorySlots.add(slotcreativeinventory);

                if (j >= 5 && j < 9)
                {
                    int k = j - 5;
                    int i1 = k / 2;
                    int k1 = k % 2;
                    slotcreativeinventory.xDisplayPosition = 9 + i1 * 54;
                    slotcreativeinventory.yDisplayPosition = 6 + k1 * 27;
                    continue;
                }

                if (j >= 0 && j < 5)
                {
                    slotcreativeinventory.yDisplayPosition = -2000;
                    slotcreativeinventory.xDisplayPosition = -2000;
                    continue;
                }

                if (j >= container.inventorySlots.size())
                {
                    continue;
                }

                int l = j - 9;
                int j1 = l % 9;
                int l1 = l / 9;
                slotcreativeinventory.xDisplayPosition = 9 + j1 * 18;

                if (j >= 36)
                {
                    slotcreativeinventory.yDisplayPosition = 112;
                }
                else
                {
                    slotcreativeinventory.yDisplayPosition = 54 + l1 * 18;
                }
            }

            field_74235_v = new Slot(inventory, 0, 173, 112);
            containercreative.inventorySlots.add(field_74235_v);
        }
        else if (i == CreativeTabs.field_78036_m.func_78021_a())
        {
            containercreative.inventorySlots = field_74236_u;
            field_74236_u = null;
        }

        if (field_74237_t != null)
        {
            if (par1CreativeTabs == CreativeTabs.field_78027_g)
            {
                field_74237_t.func_73790_e(true);
                field_74237_t.setCanLoseFocus(false);
                field_74237_t.setFocused(true);
                field_74237_t.setText("");
                func_74228_j();
            }
            else
            {
                field_74237_t.func_73790_e(false);
                field_74237_t.setCanLoseFocus(true);
                field_74237_t.setFocused(false);
            }
        }

        currentScroll = 0.0F;
        containercreative.scrollTo(0.0F);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0 && func_74226_k())
        {
            int j = (((ContainerCreative)inventorySlots).itemList.size() / 9 - 5) + 1;

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

            ((ContainerCreative)inventorySlots).scrollTo(currentScroll);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        boolean flag = Mouse.isButtonDown(0);
        int i = guiLeft;
        int j = guiTop;
        int k = i + 175;
        int l = j + 18;
        int i1 = k + 14;
        int j1 = l + 112;

        if (!wasClicking && flag && par1 >= k && par2 >= l && par1 < i1 && par2 < j1)
        {
            isScrolling = func_74226_k();
        }

        if (!flag)
        {
            isScrolling = false;
        }

        wasClicking = flag;

        if (isScrolling)
        {
            currentScroll = ((float)(par2 - l) - 7.5F) / ((float)(j1 - l) - 15F);

            if (currentScroll < 0.0F)
            {
                currentScroll = 0.0F;
            }

            if (currentScroll > 1.0F)
            {
                currentScroll = 1.0F;
            }

            ((ContainerCreative)inventorySlots).scrollTo(currentScroll);
        }

        super.drawScreen(par1, par2, par3);
        CreativeTabs acreativetabs[] = CreativeTabs.field_78032_a;
        int k1 = acreativetabs.length;
        int l1 = 0;

        do
        {
            if (l1 >= k1)
            {
                break;
            }

            CreativeTabs creativetabs = acreativetabs[l1];

            if (func_74231_b(creativetabs, par1, par2))
            {
                break;
            }

            l1++;
        }
        while (true);

        if (field_74235_v != null && field_74241_p == CreativeTabs.field_78036_m.func_78021_a() && func_74188_c(field_74235_v.xDisplayPosition, field_74235_v.yDisplayPosition, 16, 16, par1, par2))
        {
            func_74190_a(StringTranslate.getInstance().translateKey("inventory.binSlot"), par1, par2);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.enableGUIStandardItemLighting();
        int i = mc.renderEngine.getTexture("/gui/allitems.png");
        CreativeTabs creativetabs = CreativeTabs.field_78032_a[field_74241_p];
        int j = mc.renderEngine.getTexture((new StringBuilder()).append("/gui/creative_inv/").append(creativetabs.func_78015_f()).toString());
        CreativeTabs acreativetabs[] = CreativeTabs.field_78032_a;
        int l = acreativetabs.length;

        for (int i1 = 0; i1 < l; i1++)
        {
            CreativeTabs creativetabs1 = acreativetabs[i1];
            mc.renderEngine.bindTexture(i);

            if (creativetabs1.func_78021_a() != field_74241_p)
            {
                func_74233_a(creativetabs1);
            }
        }

        mc.renderEngine.bindTexture(j);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        field_74237_t.drawTextBox();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k = guiLeft + 175;
        l = guiTop + 18;
        int j1 = l + 112;
        mc.renderEngine.bindTexture(i);

        if (creativetabs.func_78017_i())
        {
            drawTexturedModalRect(k, l + (int)((float)(j1 - l - 17) * currentScroll), 232 + (func_74226_k() ? 0 : 12), 0, 12, 15);
        }

        func_74233_a(creativetabs);

        if (creativetabs == CreativeTabs.field_78036_m)
        {
            GuiInventory.func_74223_a(mc, guiLeft + 43, guiTop + 45, 20, (guiLeft + 43) - par2, (guiTop + 45) - 30 - par3);
        }
    }

    protected boolean func_74232_a(CreativeTabs par1CreativeTabs, int par2, int par3)
    {
        int i = par1CreativeTabs.func_78020_k();
        int j = 28 * i;
        int k = 0;

        if (i == 5)
        {
            j = (xSize - 28) + 2;
        }
        else if (i > 0)
        {
            j += i;
        }

        if (par1CreativeTabs.func_78023_l())
        {
            k -= 32;
        }
        else
        {
            k += ySize;
        }

        return par2 >= j && par2 <= j + 28 && par3 >= k && par3 <= k + 32;
    }

    protected boolean func_74231_b(CreativeTabs par1CreativeTabs, int par2, int par3)
    {
        int i = par1CreativeTabs.func_78020_k();
        int j = 28 * i;
        int k = 0;

        if (i == 5)
        {
            j = (xSize - 28) + 2;
        }
        else if (i > 0)
        {
            j += i;
        }

        if (par1CreativeTabs.func_78023_l())
        {
            k -= 32;
        }
        else
        {
            k += ySize;
        }

        if (func_74188_c(j + 3, k + 3, 23, 27, par2, par3))
        {
            func_74190_a(par1CreativeTabs.func_78024_c(), par2, par3);
            return true;
        }
        else
        {
            return false;
        }
    }

    protected void func_74233_a(CreativeTabs par1CreativeTabs)
    {
        boolean flag = par1CreativeTabs.func_78021_a() == field_74241_p;
        boolean flag1 = par1CreativeTabs.func_78023_l();
        int i = par1CreativeTabs.func_78020_k();
        int j = i * 28;
        int k = 0;
        int l = guiLeft + 28 * i;
        int i1 = guiTop;
        byte byte0 = 32;

        if (flag)
        {
            k += 32;
        }

        if (i == 5)
        {
            l = (guiLeft + xSize) - 28;
        }
        else if (i > 0)
        {
            l += i;
        }

        if (flag1)
        {
            i1 -= 28;
        }
        else
        {
            k += 64;
            i1 += ySize - 4;
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        drawTexturedModalRect(l, i1, j, k, 28, byte0);
        zLevel = 100F;
        itemRenderer.zLevel = 100F;
        l += 6;
        i1 += 8 + (flag1 ? 1 : -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        ItemStack itemstack = new ItemStack(par1CreativeTabs.func_78016_d());
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, l, i1);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, l, i1);
        GL11.glDisable(GL11.GL_LIGHTING);
        itemRenderer.zLevel = 0.0F;
        zLevel = 0.0F;
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
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

    public int func_74230_h()
    {
        return field_74241_p;
    }

    /**
     * Returns the creative inventory
     */
    static InventoryBasic getInventory()
    {
        return inventory;
    }

    static
    {
        field_74241_p = CreativeTabs.field_78030_b.func_78021_a();
    }
}
