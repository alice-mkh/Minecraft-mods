package net.minecraft.src;

public class GuiButtonProp extends GuiButton{
    public OldDaysProperty prop;
    public boolean help;
    public int baseY;

    public GuiButtonProp(int par1, int par2, int par3, OldDaysProperty p, boolean h){
        super(par1, par2, par3, h ? 20 : 150, 20, "");
        help = h;
        prop = p;
        baseY = par3;
        enabled = h || !prop.isDisabled();
        displayString = h ? "?" : (enabled ? p.getButtonText() : p.getDisabledButtonText());
    }

    public GuiButtonProp(int par1, int par2, int par3, boolean h, String str){
        super(par1, par2, par3, h ? 20 : 150, 20, str);
        help = h;
        baseY = par3;
    }

    public void scrolled(boolean canScroll, int scrolling){
        yPosition = baseY + (canScroll ? scrolling : 10);
    }
}