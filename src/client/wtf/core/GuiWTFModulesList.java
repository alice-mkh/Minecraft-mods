package net.minecraft.src;

import java.util.List;
import java.util.Collections;

public class GuiWTFModulesList extends GuiScreen{
    private GuiScreen parent;
    private GuiButton[] moduleButtons;

    public GuiWTFModulesList(GuiScreen guiscreen){
        parent = guiscreen;
        moduleButtons = new GuiButton[10];
    }

    public void updateScreen()
    {
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(new GuiButton(0, width / 2 - 75, height - 28, 150, 20, stringtranslate.translateKey("menu.returnToGame")));
        addModule(0,"Actions");
        addModule(1,"Bugs");
        addModule(2,"Eyecandy");
    }
    
    public void addModule(int i, String name){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(moduleButtons[i]=new GuiButton(i+1, width / 2 - 75, height / 6 + 10 + (i*30), 150, 20, name));
        if (!ModLoader.isModLoaded("mod_WTF"+name)){
            moduleButtons[i].drawButton = false;
            moduleButtons[i].enabled = false;
        }
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }
        if (guibutton.id == 0)
        {
            mc.displayGuiScreen(parent);
        }else{
            mc.displayGuiScreen(new GuiWTFSettingsList(this,guibutton.id-1));
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i,j,f);
    }
}