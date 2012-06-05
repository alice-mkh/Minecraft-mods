package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiButtonPage extends GuiButton
{
    public boolean right = false;
    private GuiScreen parent;

    public GuiButtonPage(int id, int w, int sh, int sw, boolean r, GuiScreen p)
    {
        super(id, r ? sw - w : 0, 0, w, sh, r ? ">" : "<");
        right = r;
        parent = p;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (!drawButton)
        {
            return;
        }
        FontRenderer fontrenderer = par1Minecraft.fontRenderer;
        boolean flag = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
        boolean flag2 = right ? par2 == xPosition + width - 1 : par2 == xPosition;
        int j = 0xe0e0e0;
        if (!enabled)
        {
            j = 0xffa0a0a0;
        }
        else if (flag)
        {
            j = 0xffffa0;
        }
        if(flag2){
            parent.actionPerformed(this);
        }
        drawRect(xPosition, yPosition, xPosition+width, yPosition+height, flag ? 0x90000000 : 0x80000000);
        drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, j);
    }
}
