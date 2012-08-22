package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditSign extends GuiScreen
{
    /**
     * This String is just a local copy of the characters allowed in text rendering of minecraft.
     */
    private static final String allowedCharacters;

    /** The title string that is displayed in the top-center of the screen. */
    protected String screenTitle;

    /** Reference to the sign object. */
    private TileEntitySign entitySign;

    /** Counts the number of screen updates. */
    private int updateCounter;

    /** The number of the line that is being edited. */
    private int editLine;

    public GuiEditSign(TileEntitySign par1TileEntitySign)
    {
        screenTitle = "Edit sign message:";
        editLine = 0;
        entitySign = par1TileEntitySign;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        controlList.clear();
        Keyboard.enableRepeatEvents(true);
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120, "Done"));
        entitySign.setEditable(false);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        mc.getSendQueue().addToSendQueue(new Packet130UpdateSign(entitySign.xCoord, entitySign.yCoord, entitySign.zCoord, entitySign.signText));
        entitySign.setEditable(true);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        updateCounter++;
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (!par1GuiButton.enabled)
        {
            return;
        }

        if (par1GuiButton.id == 0)
        {
            entitySign.onInventoryChanged();
            mc.displayGuiScreen(null);
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 200)
        {
            editLine = editLine - 1 & 3;
        }

        if (par2 == 208 || par2 == 28)
        {
            editLine = editLine + 1 & 3;
        }

        if (par2 == 14 && entitySign.signText[editLine].length() > 0)
        {
            entitySign.signText[editLine] = entitySign.signText[editLine].substring(0, entitySign.signText[editLine].length() - 1);
        }

        if (!(allowedCharacters.indexOf(par1) < 0 || entitySign.signText[editLine].length() >= 15))
        {
            entitySign.signText[editLine] += par1;
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, screenTitle, width / 2, 40, 0xffffff);
        GL11.glPushMatrix();
        GL11.glTranslatef(width / 2, 0.0F, 50F);
        float f = 93.75F;
        GL11.glScalef(-f, -f, -f);
        GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
        Block block = entitySign.getBlockType();

        if (block == Block.signPost)
        {
            float f1 = (float)(entitySign.getBlockMetadata() * 360) / 16F;
            GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
        }
        else
        {
            int i = entitySign.getBlockMetadata();
            float f2 = 0.0F;

            if (i == 2)
            {
                f2 = 180F;
            }

            if (i == 4)
            {
                f2 = 90F;
            }

            if (i == 5)
            {
                f2 = -90F;
            }

            GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
        }

        if ((updateCounter / 6) % 2 == 0)
        {
            entitySign.lineBeingEdited = editLine;
        }

        TileEntityRenderer.instance.renderTileEntityAt(entitySign, -0.5D, -0.75D, -0.5D, 0.0F);
        entitySign.lineBeingEdited = -1;
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
    }

    static
    {
        allowedCharacters = ChatAllowedCharacters.allowedCharacters;
    }
}
