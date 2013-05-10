package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class GuiButtonProp extends GuiButtonOldDays{
    public OldDaysProperty prop;
    public boolean help;
    public boolean highlight;

    public GuiButtonProp(int par1, int par2, int par3, OldDaysProperty p, boolean h){
        super(par1, par2, par3, h ? 20 : 150, 20, "");
        help = h;
        prop = p;
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

    @Override
    protected boolean shouldBeHighlighted(){
        return !help && ((prop != null && prop.highlight) || highlight);
    }
}