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
        super(par1EntityPlayer.inventoryContainer);
        allowUserInput = true;
        par1EntityPlayer.addStat(AchievementList.openInventory, 1);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (mc.playerController.isInCreativeMode())
        {
            if (oldcreative){
                try{
                    Class c = net.minecraft.src.GuiContainerCreativeOld.class;
                    Object o = c.getDeclaredConstructor(EntityPlayer.class).newInstance(mc.thePlayer);
                    mc.displayGuiScreen((GuiScreen)o);
                }catch(Exception ex){
                    oldcreative = false;
                }
            }else{
                mc.displayGuiScreen(new GuiContainerCreative(mc.thePlayer));
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        buttonList.clear();

        if (mc.playerController.isInCreativeMode())
        {
            if (oldcreative){
                try{
                    Class c = net.minecraft.src.GuiContainerCreativeOld.class;
                    Object o = c.getDeclaredConstructor(EntityPlayer.class).newInstance(mc.thePlayer);
                    mc.displayGuiScreen((GuiScreen)o);
                }catch(Exception ex){
                    oldcreative = false;
                }
            }else{
                mc.displayGuiScreen(new GuiContainerCreative(mc.thePlayer));
            }
        }
        else
        {
            super.initGui();
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
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
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture("/gui/inventory.png");
        int i = guiLeft;
        int j = guiTop;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
        drawPlayerOnGui(mc, i + 51, j + 75, 30, (float)(i + 51) - xSize_lo, (float)((j + 75) - 50) - ySize_lo);
    }

    public static void drawPlayerOnGui(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(par1, par2, 50F);
        GL11.glScalef(-par3, par3, par3);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f = par0Minecraft.thePlayer.renderYawOffset;
        float f1 = par0Minecraft.thePlayer.rotationYaw;
        float f2 = par0Minecraft.thePlayer.rotationPitch;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(par5 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        par0Minecraft.thePlayer.renderYawOffset = (float)Math.atan(par4 / 40F) * 20F;
        par0Minecraft.thePlayer.rotationYaw = (float)Math.atan(par4 / 40F) * 40F;
        par0Minecraft.thePlayer.rotationPitch = -(float)Math.atan(par5 / 40F) * 20F;
        par0Minecraft.thePlayer.rotationYawHead = par0Minecraft.thePlayer.rotationYaw;
        if (par0Minecraft.oldlighting){
            par0Minecraft.thePlayer.entityBrightness = 1.0F;
        }
        GL11.glTranslatef(0.0F, par0Minecraft.thePlayer.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180F;
        RenderManager.instance.renderEntityWithPosYaw(par0Minecraft.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        if (par0Minecraft.oldlighting){
            par0Minecraft.thePlayer.entityBrightness = 0.0F;
        }
        par0Minecraft.thePlayer.renderYawOffset = f;
        par0Minecraft.thePlayer.rotationYaw = f1;
        par0Minecraft.thePlayer.rotationPitch = f2;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
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

    @Override
    public void handleKeyboardInput(){
        super.handleKeyboardInput();
        if (!walking){
            return;
        }
        KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());
    }
}
