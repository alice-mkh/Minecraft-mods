package net.minecraft.src;

import java.util.List;
import java.util.Collections;
import java.lang.reflect.Field;

public class GuiWTFSettingsList extends GuiScreen{
    private GuiScreen parent;
    private GuiButton[] propButtons;
    private int id;

    public GuiWTFSettingsList(GuiScreen guiscreen, int i){
        parent = guiscreen;
        id = i;
    }

    public void updateScreen()
    {
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(new GuiButton(0, width / 2 - 75, height - 28, 150, 20, stringtranslate.translateKey("menu.returnToGame")));
        propButtons = new GuiButton[30];
        for (int i = 1; i <= mod_WTF.proplength[id]; i++){
            int x = width / 2 - 155;
            if (i % 2 == 0){
                x+=160;
            }
            int y = height / 6 - 10 + (((i-1)/2) * 30);
            controlList.add(propButtons[i] = new GuiButton(i+1, x, y, 150, 20, mod_WTF.propname[id][i]+": "+onOff(mod_WTF.propvalue[id][i])));
        }
    }
    
    private String onOff(boolean b){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        return b ? stringtranslate.translateKey("options.on") : stringtranslate.translateKey("options.off");
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }
        if (guibutton.id == 0){
            mc.displayGuiScreen(parent);
        }
        if (guibutton.id == 1){
            mc.displayGuiScreen(parent);
        }
        if (guibutton.id > 1){
            boolean b = !mod_WTF.propvalue[id][guibutton.id-1];
            mod_WTF.propvalue[id][guibutton.id-1]=b;
            try{
                mod_WTF.propfield[id][guibutton.id-1].setBoolean(mod_WTF.class, b);
            }catch(Exception ex){
                System.out.println(ex);
            }
            guibutton.displayString = mod_WTF.propname[id][guibutton.id-1]+": "+onOff(b);
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i,j,f);
    }
}