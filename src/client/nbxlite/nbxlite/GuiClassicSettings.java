package net.minecraft.src.nbxlite;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.nbxlite.MinecraftHook;
import net.minecraft.src.*;

public class GuiClassicSettings extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    private GuiButton[] widthButton;
    private GuiButton[] lengthButton;
    private boolean layers;

    public GuiClassicSettings(GuiScreen guiscreen)
    {
        parentGuiScreen = guiscreen;
        layers = false;
    }

    public void updateScreen()
    {
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        layers = mod_noBiomesX.IndevHeight==128;
        String l = layers ? mod_noBiomesX.lang.get("twoLayers") : mod_noBiomesX.lang.get("oneLayer");
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, mod_noBiomesX.lang.get("continue")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, stringtranslate.translateKey("gui.cancel")));
        widthButton = new GuiButton[4];
        lengthButton = new GuiButton[4];
        for (int i=0; i<4; i++){
            controlList.add(widthButton[i]=new GuiButton(10+i, (width / 2 - 82+(41*i)), height / 6 + 24, 40, 20, Integer.toString(GeneratorList.sizes[i])));
        }
        for (int i=0; i<4; i++){
            controlList.add(lengthButton[i]=new GuiButton(20+i, (width / 2 - 82+(41*i)), height / 6 + 54, 40, 20, Integer.toString(GeneratorList.sizes[i])));
        }
        widthButton[GeneratorList.xcurrent].enabled=false;
        lengthButton[GeneratorList.zcurrent].enabled=false;

//         controlList.add(widthxslider = new GuiSliderCustom(16, (width / 2 - 155), height / 6 + 24, mod_noBiomesX.lang.get("width"), GuiSliderCustom.setSizeValue(mod_noBiomesX.IndevWidthX)));
//         controlList.add(widthzslider = new GuiSliderCustom(17, (width / 2 + 5), height / 6 + 24, mod_noBiomesX.lang.get("length"), GuiSliderCustom.setSizeValue(mod_noBiomesX.IndevWidthZ)));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }if (guibutton.id == 1){
            GeneratorList.xcurrent = GeneratorList.xdefault;
            GeneratorList.zcurrent = GeneratorList.zdefault;
            mc.displayGuiScreen(parentGuiScreen);
        }else if (guibutton.id == 0){
            mod_noBiomesX.IndevWidthX=GeneratorList.sizes[GeneratorList.xcurrent];
            mod_noBiomesX.IndevWidthZ=GeneratorList.sizes[GeneratorList.zcurrent];
            mc.displayGuiScreen(parentGuiScreen);
        }else if (guibutton.id>=10 && guibutton.id<=14){
            GeneratorList.xcurrent = guibutton.id-10;
            widthButton[0].enabled = true;
            widthButton[1].enabled = true;
            widthButton[2].enabled = true;
            widthButton[3].enabled = true;
            guibutton.enabled = false;
        }else if (guibutton.id>=20 && guibutton.id<=24){
            GeneratorList.zcurrent = guibutton.id-20;
            lengthButton[0].enabled = true;
            lengthButton[1].enabled = true;
            lengthButton[2].enabled = true;
            lengthButton[3].enabled = true;
            guibutton.enabled = false;
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawString(fontRenderer, mod_noBiomesX.lang.get("width"), width / 2 - 120, height / 6 + 30, 0xa0a0a0);
        drawString(fontRenderer, mod_noBiomesX.lang.get("length"), width / 2 - 120, height / 6 + 60, 0xa0a0a0);
        super.drawScreen(i, j, f);
    }
}
