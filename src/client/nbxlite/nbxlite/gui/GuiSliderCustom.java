package net.minecraft.src.nbxlite.gui;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.src.*;

public class GuiSliderCustom extends GuiButton
{
    /** The value of this slider control. */
    public float sliderValue;

    /** Is this slider control being dragged. */
    public boolean dragging;
    public String name;

    public GuiSliderCustom(int par1, int par2, int par3, String par5Str, float par6)
    {
        super(par1, par2, par3, 150, 20, par5Str);
        sliderValue = 1.0F;
        dragging = false;
        name = par5Str;
        sliderValue = par6;
        displayString = new StringBuilder().append(name).append(getDisplayValue()).toString();
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean par1)
    {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
    {
        if (!drawButton)
        {
            return;
        }

        if (dragging)
        {
            sliderValue = (float)(par2 - (xPosition + 4)) / (float)(width - 8);

            if (sliderValue < 0.0F)
            {
                sliderValue = 0.0F;
            }

            if (sliderValue > 1.0F)
            {
                sliderValue = 1.0F;
            }

            displayString = new StringBuilder().append(name).append(getDisplayValue()).toString();
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)), yPosition, 0, 66, 4, 20);
        drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)) + 4, yPosition, 196, 66, 4, 20);
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (super.mousePressed(par1Minecraft, par2, par3))
        {
            sliderValue = (float)(par2 - (xPosition + 4)) / (float)(width - 8);

            if (sliderValue < 0.0F)
            {
                sliderValue = 0.0F;
            }

            if (sliderValue > 1.0F)
            {
                sliderValue = 1.0F;
            }

            displayString = new StringBuilder().append(name).append(getDisplayValue()).toString();
            dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    public int getSizeValue(){
//         return ((int)(sliderValue*28)*16)+64;
        return (int)(sliderValue*192)+64;
    }

    public int getDisplayValue(){
//         return ((int)(sliderValue*28)*16)+64;
        return (int)(sliderValue*192)+32;
    }

    public static float setSizeValue(int i){
        i-=64;
        float f=(float)i;
        f/=192;
        return f;
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int par1, int par2)
    {
        dragging = false;
    }
}
