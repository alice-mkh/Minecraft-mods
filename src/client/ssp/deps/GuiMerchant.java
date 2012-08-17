package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiMerchant extends GuiContainer
{
    private IMerchant field_74203_o;
    private GuiButtonMerchant field_74202_p;
    private GuiButtonMerchant field_74201_q;
    private int field_74200_r;

    public GuiMerchant(InventoryPlayer par1InventoryPlayer, IMerchant par2IMerchant, World par3World)
    {
        super(new ContainerMerchant(par1InventoryPlayer, par2IMerchant, par3World));
        field_74200_r = 0;
        field_74203_o = par2IMerchant;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        controlList.add(field_74202_p = new GuiButtonMerchant(1, i + 120 + 27, (j + 24) - 1, true));
        controlList.add(field_74201_q = new GuiButtonMerchant(2, (i + 36) - 19, (j + 24) - 1, false));
        field_74202_p.enabled = false;
        field_74201_q.enabled = false;
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
        MerchantRecipeList merchantrecipelist = field_74203_o.func_70934_b(mc.field_71439_g);

        if (merchantrecipelist != null)
        {
            field_74202_p.enabled = field_74200_r < merchantrecipelist.size() - 1;
            field_74201_q.enabled = field_74200_r > 0;
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        boolean flag = false;

        if (par1GuiButton == field_74202_p)
        {
            field_74200_r++;
            flag = true;
        }
        else if (par1GuiButton == field_74201_q)
        {
            field_74200_r--;
            flag = true;
        }

        if (flag)
        {
            ((ContainerMerchant)inventorySlots).func_75175_c(field_74200_r);
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

            try
            {
                dataoutputstream.writeInt(field_74200_r);
                mc.getSendQueue().addToSendQueue(new Packet250CustomPayload("MC|TrSel", bytearrayoutputstream.toByteArray()));
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
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
        MerchantRecipeList merchantrecipelist = field_74203_o.func_70934_b(mc.field_71439_g);

        if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
        {
            int i = (width - xSize) / 2;
            int j = (height - ySize) / 2;
            GL11.glPushMatrix();
            int k = field_74200_r;
            MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k);
            ItemStack itemstack = merchantrecipe.func_77394_a();
            ItemStack itemstack1 = merchantrecipe.func_77396_b();
            ItemStack itemstack2 = merchantrecipe.func_77397_d();
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

            if (func_74188_c(36, 24, 16, 16, par1, par2))
            {
                func_74184_a(itemstack, par1, par2);
            }
            else if (itemstack1 != null && func_74188_c(62, 24, 16, 16, par1, par2))
            {
                func_74184_a(itemstack1, par1, par2);
            }
            else if (func_74188_c(120, 24, 16, 16, par1, par2))
            {
                func_74184_a(itemstack2, par1, par2);
            }

            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
        }
    }

    public IMerchant func_74199_h()
    {
        return field_74203_o;
    }
}
