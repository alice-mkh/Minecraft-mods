package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class GuiButtonProp extends GuiButton{
    public OldDaysProperty prop;
    public boolean help;
    public int baseY;
    public boolean highlight;

    public GuiButtonProp(int par1, int par2, int par3, OldDaysProperty p, boolean h){
        super(par1, par2, par3, h ? 20 : 150, 20, "");
        help = h;
        prop = p;
        baseY = par3;
        enabled = h || !prop.isDisabled();
        highlight = false;
        displayString = h ? "?" : (enabled ? p.getButtonText() : p.getDisabledButtonText());
    }

    public GuiButtonProp(int par1, int par2, int par3, boolean h, String str){
        super(par1, par2, par3, h ? 20 : 150, 20, str);
        help = h;
        baseY = par3;
        highlight = false;
    }

    public void scrolled(boolean canScroll, int scrolling){
        yPosition = baseY + (canScroll ? scrolling : 10);
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3){
        if (!drawButton){
            return;
        }
        if (!help && ((prop != null && prop.highlight) || highlight)){
            drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, 0xffffffa0);
        }
        super.drawButton(par1Minecraft, par2, par3);
    }
}