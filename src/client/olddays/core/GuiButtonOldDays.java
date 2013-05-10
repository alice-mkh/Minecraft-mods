package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class GuiButtonOldDays extends GuiButton{
    protected int baseY;

    public GuiButtonOldDays(int id, int x, int y, int w, int h, String str){
        super(id, x, y, w, h, str);
        baseY = y;
    }

    public void scrolled(boolean canScroll, int scrolling){
        yPosition = baseY + (canScroll ? scrolling : 10);
    }

    protected boolean shouldBeHighlighted(){
        return false;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3){
        if (!drawButton){
            return;
        }
        if (shouldBeHighlighted()){
            drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, 0xffffffa0);
        }
        super.drawButton(par1Minecraft, par2, par3);
    }
}