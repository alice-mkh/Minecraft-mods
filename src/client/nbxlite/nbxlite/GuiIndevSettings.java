package net.minecraft.src.nbxlite;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.nbxlite.MinecraftHook;
import net.minecraft.src.*;

public class GuiIndevSettings extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    private GuiButton typeButton;

    public GuiIndevSettings(GuiScreen guiscreen)
    {
        parentGuiScreen = guiscreen;
        GeneratorList.typecurrent=GeneratorList.typedefault;
    }

    public void updateScreen()
    {
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, stringtranslate.translateKey("nbxlite.continue")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, stringtranslate.translateKey("gui.cancel")));
        controlList.add(typeButton = new GuiButton(2, width / 2 - 75, 110, 150, 20, stringtranslate.translateKey(GeneratorList.typename[GeneratorList.typecurrent])));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }if (guibutton.id == 1){
            mc.displayGuiScreen(parentGuiScreen);
        }else if (guibutton.id == 0){
            mod_noBiomesX.IndevMapType=GeneratorList.typecurrent;
            mc.displayGuiScreen(parentGuiScreen);
        }else if (guibutton.id == 2){
            StringTranslate stringtranslate = StringTranslate.getInstance();
            if (GeneratorList.typecurrent<GeneratorList.typelength){
                GeneratorList.typecurrent++;
            }else{
                GeneratorList.typecurrent=0;
            }
            typeButton.displayString = stringtranslate.translateKey(GeneratorList.typename[GeneratorList.typecurrent]);
//             generatorExtraDescription = stringtranslate.translateKey(GeneratorList.typedesc[GeneratorList.typecurrent]);
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i, j, f);
    }
}
