package net.minecraft.src.nbxlite;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.nbxlite.MinecraftHook;
import net.minecraft.src.*;

public class GuiIndevSettings extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    private GuiButton typeButton;
    private GuiButton layerButton;
    private GuiSliderCustom widthxslider;
    private GuiSliderCustom widthzslider;
    private boolean layers;

    public GuiIndevSettings(GuiScreen guiscreen)
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
        controlList.add(typeButton = new GuiButton(2, width / 2 - 75, 110, 150, 20, mod_noBiomesX.lang.get("indevType")+mod_noBiomesX.lang.get(GeneratorList.typename[GeneratorList.typecurrent])));
        controlList.add(layerButton = new GuiButton(3, width / 2 - 75, 140, 150, 20, l));
        layerButton.drawButton = GeneratorList.typecurrent == 2;
        controlList.add(widthxslider = new GuiSliderCustom(16, (width / 2 - 155), height / 6 + 24, mod_noBiomesX.lang.get("width"), GuiSliderCustom.setSizeValue(mod_noBiomesX.IndevWidthX)));
        controlList.add(widthzslider = new GuiSliderCustom(16, (width / 2 + 5), height / 6 + 24, mod_noBiomesX.lang.get("length"), GuiSliderCustom.setSizeValue(mod_noBiomesX.IndevWidthZ)));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }if (guibutton.id == 1){
            GeneratorList.typecurrent = GeneratorList.typedefault;
            mod_noBiomesX.IndevWidthX=256;
            mod_noBiomesX.IndevWidthZ=256;
            mod_noBiomesX.IndevHeight=96;
            mc.displayGuiScreen(parentGuiScreen);
        }else if (guibutton.id == 0){
            mod_noBiomesX.IndevMapType=GeneratorList.typecurrent;
            mod_noBiomesX.IndevWidthX=widthxslider.getSizeValue();
            mod_noBiomesX.IndevWidthZ=widthzslider.getSizeValue();
            mc.displayGuiScreen(parentGuiScreen);
        }else if (guibutton.id == 2){
            if (GeneratorList.typecurrent<GeneratorList.typelength){
                GeneratorList.typecurrent++;
            }else{
                GeneratorList.typecurrent=0;
            }
            typeButton.displayString = mod_noBiomesX.lang.get("indevType")+mod_noBiomesX.lang.get(GeneratorList.typename[GeneratorList.typecurrent]);
            layerButton.drawButton = GeneratorList.typecurrent == 2;
            layers = false;
            layerButton.displayString = mod_noBiomesX.lang.get("oneLayer");
            mod_noBiomesX.IndevHeight = 96;
        }else if (guibutton.id == 3){
            layers = !layers;
            layerButton.displayString = layers ? mod_noBiomesX.lang.get("twoLayers") : mod_noBiomesX.lang.get("oneLayer");
            mod_noBiomesX.IndevHeight = layers ? 128 : 96;
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i, j, f);
    }
}
