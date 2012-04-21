package net.minecraft.src;

import java.util.List;
import java.util.Collections;
import java.lang.reflect.Field;

public class GuiOldDaysSettings extends GuiScreen{
    private GuiScreen parent;
    private GuiButton[] propButtons;
    private int id;

    public GuiOldDaysSettings(GuiScreen guiscreen, int i){
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
        for (int i = 1; i <= mod_OldDays.proplength[id]; i++){
            int x = width / 2 - 155;
            if (i % 2 == 0){
                x+=160;
            }
            int margin = 30;
            int top = 10;
            if (mod_OldDays.proplength[id] > 14){
                top += (margin / 1.8);
                margin -= 10;
            }else if (mod_OldDays.proplength[id] > 12){
                margin -= 5;
                top += (margin / 2);
            }else if (mod_OldDays.proplength[id] > 10){
                top += (margin / 3);
            }
            int y = height / 6 - top + (((i-1)/2) * margin);
            controlList.add(propButtons[i] = new GuiButton(i+1, x, y, 150, 20, mod_OldDays.propname[id][i]+": "+onOff(mod_OldDays.propvalue[id][i])));
            propButtons[i].enabled = !mod_OldDays.disabled[id][i] && !(mod_OldDays.propsmp[id][i]>=0 && ModLoader.getMinecraftInstance().theWorld.isRemote);
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
            boolean b = !mod_OldDays.propvalue[id][guibutton.id-1];
            mod_OldDays.propvalue[id][guibutton.id-1]=b;
            try{
                mod_OldDays.propfield[id][guibutton.id-1].setBoolean(mod_OldDays.class, b);
            }catch(Exception ex){
                System.out.println(ex);
            }
            mod_OldDays.saveModuleProperties(id);
            mod_OldDays.sendCallback(id, guibutton.id-1);
            guibutton.displayString = mod_OldDays.propname[id][guibutton.id-1]+": "+onOff(b);
        }
    }

    private void drawTooltip(String str, int x, int y, boolean smp){
        String str2 = str;
        if (smp){
            str2 = "";
        }
//         drawRect(x, y, x + 5 + fontRenderer.getStringWidth(str2), y + 13, 0x80000000);
//         drawString(fontRenderer, str2, x + 3, y + 3, smp ? 0xff0000 : 0xffffff);
        int top = 23;
        if (mod_OldDays.proplength[id] > 14){
            top += 16;
        }else if (mod_OldDays.proplength[id] > 12){
            top += 13;
        }else if (mod_OldDays.proplength[id] > 10){
            top += 10;
        }
       drawCenteredString(fontRenderer, str2, width / 2, height / 6 - top, smp ? 0xff0000 : 0xffffff);
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i,j,f);
        for (int k = 1; k < controlList.size(); k++){
            GuiButton button = ((GuiButton)controlList.get(k));
            if (i > button.xPosition && i < button.xPosition+150 && j > button.yPosition && j < button.yPosition+20){
                String str = mod_OldDays.propdesc[id][k];
                boolean smp = mod_OldDays.propsmp[id][k]>=0 && ModLoader.getMinecraftInstance().theWorld.isRemote;
                if (str == null && !smp){
                    return;
                }
                drawTooltip(str, i + 4, j - 13, smp);
            }
        }
    }
}