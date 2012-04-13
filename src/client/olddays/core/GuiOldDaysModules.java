package net.minecraft.src;

import java.util.List;
import java.util.Collections;

public class GuiOldDaysModules extends GuiScreen{
    private GuiScreen parent;
    private GuiButton[] moduleButtons;

    public GuiOldDaysModules(GuiScreen guiscreen){
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
        mod_OldDays.addModules(this);
    }
    
    public void addModule(int i, String name){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        int margin = 30;
        controlList.add(moduleButtons[i]=new GuiButton(i+1, width / 2 - 75, height / 6 - 5 + (i*margin), 150, 20, name));
        if (!ModLoader.isModLoaded("mod_OldDays"+name)){
            moduleButtons[i].enabled = false;
            moduleButtons[i].drawButton = false;
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
            if (mod_OldDays.modulegui[guibutton.id-1] == null){
                mc.displayGuiScreen(new GuiOldDaysSettings(this,guibutton.id-1));
            }else{
                mc.displayGuiScreen(mod_OldDays.modulegui[guibutton.id-1]);
            }
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i,j,f);
    }
}