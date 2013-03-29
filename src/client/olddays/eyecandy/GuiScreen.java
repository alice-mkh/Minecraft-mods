package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiScreen extends Gui
{
    public static boolean oldbg = false;

    public static final boolean isMacOs;

    /** Reference to the Minecraft object. */
    protected Minecraft mc;

    /** The width of the screen object. */
    public int width;

    /** The height of the screen object. */
    public int height;

    /** A list of all the buttons in this container. */
    protected List buttonList;
    public boolean allowUserInput;

    /** The FontRenderer used by GuiScreen */
    protected FontRenderer fontRenderer;
    public GuiParticle guiParticles;

    /** The button that was just pressed. */
    private GuiButton selectedButton;
    private int eventButton;
    private long field_85043_c;
    private int field_92018_d;

    public GuiScreen()
    {
        buttonList = new ArrayList();
        allowUserInput = false;
        selectedButton = null;
        eventButton = 0;
        field_85043_c = 0L;
        field_92018_d = 0;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        for (int i = 0; i < buttonList.size(); i++)
        {
            GuiButton guibutton = (GuiButton)buttonList.get(i);
            guibutton.drawButton(mc, par1, par2);
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1)
        {
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }
    }

    /**
     * Returns a string stored in the system clipboard.
     */
    public static String getClipboardString()
    {
        try
        {
            java.awt.datatransfer.Transferable transferable = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

            if (transferable != null && transferable.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.stringFlavor))
            {
                return (String)transferable.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
            }
        }
        catch (Exception exception) { }

        return "";
    }

    /**
     * store a string in the system clipboard
     */
    public static void setClipboardString(String par0Str)
    {
        try
        {
            java.awt.datatransfer.StringSelection stringselection = new java.awt.datatransfer.StringSelection(par0Str);
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
        }
        catch (Exception exception) { }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            for (int i = 0; i < buttonList.size(); i++)
            {
                GuiButton guibutton = (GuiButton)buttonList.get(i);

                if (guibutton.mousePressed(mc, par1, par2))
                {
                    selectedButton = guibutton;
                    mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    actionPerformed(guibutton);
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
        if (selectedButton != null && par3 == 0)
        {
            selectedButton.mouseReleased(par1, par2);
            selectedButton = null;
        }
    }

    protected void func_85041_a(int i, int j, int k, long l)
    {
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton guibutton)
    {
    }

    /**
     * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call
     * Container.validate()
     */
    public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3)
    {
        guiParticles = new GuiParticle(par1Minecraft);
        mc = par1Minecraft;
        fontRenderer = par1Minecraft.fontRenderer;
        width = par2;
        height = par3;
        buttonList.clear();
        initGui();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
    }

    /**
     * Delegates mouse and keyboard input.
     */
    public void handleInput()
    {
        for (; Mouse.next(); handleMouseInput()) { }

        for (; Keyboard.next(); handleKeyboardInput()) { }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        int i = (Mouse.getEventX() * width) / mc.displayWidth;
        int j = height - (Mouse.getEventY() * height) / mc.displayHeight - 1;

        if (Mouse.getEventButtonState())
        {
            if (mc.gameSettings.touchscreen && field_92018_d++ > 0)
            {
                return;
            }

            eventButton = Mouse.getEventButton();
            field_85043_c = Minecraft.getSystemTime();
            mouseClicked(i, j, eventButton);
        }
        else if (Mouse.getEventButton() != -1)
        {
            if (mc.gameSettings.touchscreen && --field_92018_d > 0)
            {
                return;
            }

            eventButton = -1;
            mouseMovedOrUp(i, j, Mouse.getEventButton());
        }
        else if (eventButton != -1 && field_85043_c > 0L)
        {
            long l = Minecraft.getSystemTime() - field_85043_c;
            func_85041_a(i, j, eventButton, l);
        }
    }

    /**
     * Handles keyboard input.
     */
    public void handleKeyboardInput()
    {
        if (Keyboard.getEventKeyState())
        {
            int i = Keyboard.getEventKey();
            char c = Keyboard.getEventCharacter();

            if (i == 87)
            {
                mc.toggleFullscreen();
                return;
            }

            if (isMacOs && i == 28 && c == 0)
            {
                i = 29;
            }

            keyTyped(c, i);
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
    }

    /**
     * Draws either a gradient over the background screen (when it exists) or a flat gradient over background.png
     */
    public void drawDefaultBackground()
    {
        drawWorldBackground(0);
    }

    public void drawWorldBackground(int par1)
    {
        if (mc.theWorld != null)
        {
            drawGradientRect(0, 0, width, height, oldbg ? 0x60050500 : 0xc0101010, oldbg ? 0xa0303060 : 0xd0101010);
        }
        else
        {
            drawBackground(par1);
        }
    }

    /**
     * Draws the background (i is always 0 as of 1.2.2)
     */
    public void drawBackground(int par1)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        mc.renderEngine.bindTexture("/gui/background.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x404040);
        tessellator.addVertexWithUV(0.0D, height, 0.0D, 0.0D, (float)height / f + (float)par1);
        tessellator.addVertexWithUV(width, height, 0.0D, (float)width / f, (float)height / f + (float)par1);
        tessellator.addVertexWithUV(width, 0.0D, 0.0D, (float)width / f, par1);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, par1);
        tessellator.draw();
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return true;
    }

    public void confirmClicked(boolean flag, int i)
    {
    }

    public static boolean isCtrlKeyDown()
    {
        boolean flag = Keyboard.isKeyDown(28) && Keyboard.getEventCharacter() == 0;
        return Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157) || isMacOs && (flag || Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220));
    }

    public static boolean isShiftKeyDown()
    {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }

    static
    {
        isMacOs = Minecraft.getOs() == EnumOS.MACOS;
    }
}
