package net.minecraft.src;

public class GuiButtonProp extends GuiButton{
    public OldDaysProperty prop;

    public GuiButtonProp(int par1, int par2, int par3, OldDaysProperty p){
        super(par1, par2, par3, 150, 20, "");
        prop = p;
        enabled = !prop.isDisabled();
        drawButton = false;
        displayString = enabled ? p.getButtonText() : p.getDisabledButtonText();
    }
}