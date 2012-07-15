package net.minecraft.src.backport;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.src.*;

public class GuiMerchant extends GuiContainer
{
    private IMerchant field_56488_h;
    private GuiButtonMerchant field_56489_i;
    private GuiButtonMerchant field_56486_j;
    private int field_56487_k;

    public GuiMerchant(InventoryPlayer par1InventoryPlayer, IMerchant par2IMerchant, World par3World)
    {
        super(new ContainerMerchant(par1InventoryPlayer, par2IMerchant, par3World));
        field_56487_k = 0;
        field_56488_h = par2IMerchant;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        controlList.add(field_56489_i = new GuiButtonMerchant(1, i + 120 + 27, (j + 24) - 1, true));
        controlList.add(field_56486_j = new GuiButtonMerchant(2, (i + 36) - 19, (j + 24) - 1, false));
        field_56489_i.enabled = false;
        field_56486_j.enabled = false;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everythin in front of the items)
     */
    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString(StatCollector.translateToLocal("entity.Villager.name"), 56, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        MerchantRecipeList merchantrecipelist = field_56488_h.func_56220_b(mc.thePlayer);

        if (merchantrecipelist != null)
        {
            field_56489_i.enabled = field_56487_k < merchantrecipelist.size() - 1;
            field_56486_j.enabled = field_56487_k > 0;
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        boolean flag = false;

        if (par1GuiButton == field_56489_i)
        {
            field_56487_k++;
            flag = true;
        }
        else if (par1GuiButton == field_56486_j)
        {
            field_56487_k--;
            flag = true;
        }

        if (flag)
        {
            ((ContainerMerchant)inventorySlots).func_56984_c(field_56487_k);
            if (mc.theWorld.isRemote)
            {
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
                try
                {
                    dataoutputstream.writeInt(field_56487_k);
                    Packet250CustomPayload p = new Packet250CustomPayload();
                    p.data = bytearrayoutputstream.toByteArray();
                    p.channel = "MC|TrSel";
                    mc.getSendQueue().addToSendQueue(p);
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int i = mc.renderEngine.getTexture("/gui/trading.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        MerchantRecipeList merchantrecipelist = field_56488_h.func_56220_b(mc.thePlayer);

        if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
        {
            int i = (width - xSize) / 2;
            int j = (height - ySize) / 2;
            GL11.glPushMatrix();
            int k = field_56487_k;
            MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k);
            ItemStack itemstack = merchantrecipe.func_57067_a();
            ItemStack itemstack1 = merchantrecipe.func_57065_b();
            ItemStack itemstack2 = merchantrecipe.func_57071_d();
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            itemRenderer.zLevel = 100F;
            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, i + 36, j + 24);
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, i + 36, j + 24);

            if (itemstack1 != null)
            {
                itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack1, i + 62, j + 24);
                itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack1, i + 62, j + 24);
            }

            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack2, i + 120, j + 24);
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack2, i + 120, j + 24);
            itemRenderer.zLevel = 0.0F;
            GL11.glDisable(GL11.GL_LIGHTING);

            if (func_56484_a(36, 24, 16, 16, par1, par2))
            {
                func_56482_a(itemstack, par1, par2);
            }
            else if (itemstack1 != null && func_56484_a(62, 24, 16, 16, par1, par2))
            {
                func_56482_a(itemstack1, par1, par2);
            }
            else if (func_56484_a(120, 24, 16, 16, par1, par2))
            {
                func_56482_a(itemstack2, par1, par2);
            }

            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
        }
    }

    protected void func_56482_a(ItemStack par1ItemStack, int par2, int par3)
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

            for (int j2 = 0; j2 < list.size(); j2++)
            {
                String s1 = (String)list.get(j2);

                if (j2 == 0)
                {
                    s1 = (new StringBuilder()).append("\247").append(Integer.toHexString(par1ItemStack.getRarity().nameColor)).append(s1).toString();
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

    protected boolean func_56484_a(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        int i = guiLeft;
        int j = guiTop;
        par5 -= i;
        par6 -= j;
        return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
    }

    public IMerchant func_56485_g()
    {
        return field_56488_h;
    }
}
