package net.minecraft.src;

public class GuiButtonProp extends GuiButton{
    public OldDaysProperty prop;
    public boolean help;

    public GuiButtonProp(int par1, int par2, int par3, OldDaysProperty p, boolean h){
        super(par1, par2, par3, h ? 20 : 150, 20, "");
        help = h;
        prop = p;
        enabled = h || !prop.isDisabled();
        drawButton = false;
        displayString = h ? "?" : (enabled ? p.getButtonText() : p.getDisabledButtonText());
    }
}