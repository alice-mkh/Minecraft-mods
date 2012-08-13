package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.input.Keyboard;

public class GuiInventory extends InventoryEffectRenderer
{
    public static boolean walking = false;
    public static boolean oldcreative = false;

    /**
     * x size of the inventory window in pixels. Defined as float, passed as int
     */
    private float xSize_lo;

    /**
     * y size of the inventory window in pixels. Defined as float, passed as int.
     */
    private float ySize_lo;

    public GuiInventory(EntityPlayer par1EntityPlayer)
    {
        super(par1EntityPlayer.inventorySlots);
        allowUserInput = true;
        par1EntityPlayer.addStat(AchievementList.openInventory, 1);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (mc.field_71442_b.isInCreativeMode())
        {
            if (oldcreative){
                try{
                    Class c = net.minecraft.src.GuiContainerCreativeOld.class;
                    Object o = c.getDeclaredConstructor(new Class[]{EntityPlayer.class}).newInstance(new Object[]{mc.field_71439_g});
                    mc.displayGuiScreen((GuiScreen)o);
                }catch(Exception ex){
                    oldcreative = false;
                }
            }else{
                mc.displayGuiScreen(new GuiContainerCreative(mc.field_71439_g));
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        controlList.clear();

        if (mc.field_71442_b.isInCreativeMode())
        {
            if (oldcreative){
                try{
                    Class c = net.minecraft.src.GuiContainerCreativeOld.class;
                    Object o = c.getDeclaredConstructor(new Class[]{EntityPlayer.class}).newInstance(new Object[]{mc.field_71439_g});
                    mc.displayGuiScreen((GuiScreen)o);
                }catch(Exception ex){
                    oldcreative = false;
                }
            }else{
                mc.displayGuiScreen(new GuiContainerCreative(mc.field_71439_g));
            }
        }
        else
        {
            super.initGui();
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everythin in front of the items)
     */
    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 86, 16, 0x404040);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        xSize_lo = par1;
        ySize_lo = par2;
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int i = mc.renderEngine.getTexture("/gui/inventory.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = guiLeft;
        int k = guiTop;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        func_74223_a(mc, j + 51, k + 75, 30, (float)(j + 51) - xSize_lo, (float)((k + 75) - 50) - ySize_lo);
    }

    public static void func_74223_a(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(par1, par2, 50F);
        GL11.glScalef(-par3, par3, par3);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f = par0Minecraft.field_71439_g.renderYawOffset;
        float f1 = par0Minecraft.field_71439_g.rotationYaw;
        float f2 = par0Minecraft.field_71439_g.rotationPitch;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(par5 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        par0Minecraft.field_71439_g.renderYawOffset = (float)Math.atan(par4 / 40F) * 20F;
        par0Minecraft.field_71439_g.rotationYaw = (float)Math.atan(par4 / 40F) * 40F;
        par0Minecraft.field_71439_g.rotationPitch = -(float)Math.atan(par5 / 40F) * 20F;
        par0Minecraft.field_71439_g.rotationYawHead = par0Minecraft.field_71439_g.rotationYaw;
        GL11.glTranslatef(0.0F, par0Minecraft.field_71439_g.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180F;
        RenderManager.instance.renderEntityWithPosYaw(par0Minecraft.field_71439_g, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        par0Minecraft.field_71439_g.renderYawOffset = f;
        par0Minecraft.field_71439_g.rotationYaw = f1;
        par0Minecraft.field_71439_g.rotationPitch = f2;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
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

    public void handleKeyboardInput(){
        super.handleKeyboardInput();
        if (!walking){
            return;
        }
        KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());
    }
}
