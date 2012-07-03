package net.minecraft.src;

import java.util.List;
import java.util.Collections;
import java.lang.reflect.Field;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;

public class GuiOldDaysSettings extends GuiScreen{
    private GuiScreen parent;
    private int id;
    private GuiTextField field;
    private boolean displayField = false;
    private int fieldId = 0;
    private int page = 0;
    private int max = 12;
    private int maxpage = 1;
    private GuiButton left;
    private GuiButton right;
    private String current;

    public GuiOldDaysSettings(GuiScreen guiscreen, int i){
        parent = guiscreen;
        id = i;
    }

    public void updateScreen()
    {
        field.updateCursorCounter();
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(new GuiButton(0, width / 2 - 75, height - 28, 150, 20, stringtranslate.translateKey("menu.returnToGame")));
        int count = mod_OldDays.getModuleById(id).properties.size();
        for (int i = 1; i <= count; i++){
            int x = width / 2 - 155;
            int i2 = (i-1) % max;
            if (i2 % 2 != 0){
                x+=160;
            }
            int margin = 30;
            int top = 15;
            int y = height / 6 - top + (((i2)/2) * margin);
            GuiButton button = new GuiButton(i+1, x, y, 150, 20, mod_OldDays.getPropertyButtonText(id, i));
            button.enabled = !mod_OldDays.getModuleById(id).getPropertyById(i).isDisabled();
            button.drawButton = false;
            controlList.add(button);
        }
        Keyboard.enableRepeatEvents(false);
        controlList.add(left = new GuiButtonPage(98, 30, height, width, false, this));
        controlList.add(right = new GuiButtonPage(99, 30, height, width, true, this));
        field = new GuiTextField(fontRenderer, 0, 0, 150, 20);
        maxpage = (count-1) / max;
        setPage(0);
    }

    private void setPage(int to){
        if (to>maxpage || to<0){
            return;
        }
        for (int i = 0; i < max; i++){
            int i2 = i+(page*max)+1;
            if (i2<=controlList.size()-2){
                ((GuiButton)controlList.get(i2)).drawButton = false;
            }
        }
        page = to;
        for (int i = 0; i < max; i++){
            int i2 = i+(page*max)+1;
            if (i2<=controlList.size()-2){
                ((GuiButton)controlList.get(i2)).drawButton = true;
            }
        }
        left.drawButton = page>0;
        right.drawButton = page<maxpage;
        if (displayField){
            showField(false, ((GuiButton)controlList.get(fieldId)));
        }
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }
        if (guibutton.id == 0){
            mc.displayGuiScreen(parent);
            return;
        }
        if (guibutton.id == 1){
            mc.displayGuiScreen(parent);
            return;
        }
        if (guibutton.id == 98){
            setPage(page-1);
            return;
        }
        if (guibutton.id == 99){
            setPage(page+1);
            return;
        }
        if (guibutton.id > 1){
            displayField = false;
            if (mod_OldDays.getPropertyType(id, guibutton.id-1) == OldDaysProperty.TYPE_BOOLEAN ||
                mod_OldDays.getPropertyType(id, guibutton.id-1) == OldDaysProperty.TYPE_INTEGER){
                mod_OldDays.getModuleById(id).getPropertyById(guibutton.id-1).incrementValue();
            }else if (mod_OldDays.getPropertyType(id, guibutton.id-1) == OldDaysProperty.TYPE_STRING){
                /*int offset = fontRenderer.getStringWidth(mod_OldDays.getPropertyName(id, guibutton.id-1)+":")-2;
                offset += (150-fontRenderer.getStringWidth(mod_OldDays.getPropertyButtonText(id, guibutton.id-1)))/2;
                if (fontRenderer.getStringWidth(mod_OldDays.getStringPropValue(id, guibutton.id-1)>138-offset){
                    offset = 0;
                }*/
                int offset = 0;
                field = new GuiTextField(fontRenderer, guibutton.xPosition+offset+2, guibutton.yPosition+2, 146-offset, 16);
                showField(true, guibutton);
                field.setFocused(true);
                if (!mod_OldDays.getStringPropValue(id, guibutton.id-1).equals("OFF")){
                    current = mod_OldDays.getStringPropValue(id, guibutton.id-1);
                    field.setText(mod_OldDays.getStringPropValue(id, guibutton.id-1));
                }else{
                    current = "";
                    field.setText("");
                }
                fieldId = guibutton.id-1;
                guibutton.enabled = false;
            }
            mod_OldDays.saveModuleProperties(id);
            mod_OldDays.sendCallback(id, guibutton.id-1);
            guibutton.enabled = !mod_OldDays.getModuleById(id).getPropertyById(guibutton.id-1).isDisabled();
            guibutton.displayString = mod_OldDays.getPropertyButtonText(id, guibutton.id-1);
        }
    }

    private void showField(boolean b, GuiButton button){
        displayField = b;
        Keyboard.enableRepeatEvents(b);
        button.enabled = !b;
        if(!b){
            current = "";
            mod_OldDays.saveModuleProperties(id);
        }
    }

    protected void mouseClicked(int par1, int par2, int par3){
        if (displayField){
            field.mouseClicked(par1, par2, par3);
            mod_OldDays.setStringPropValue(id, fieldId, current);
            mod_OldDays.sendCallback(id, fieldId);
            showField(false, ((GuiButton)controlList.get(fieldId)));
        }
        super.mouseClicked(par1, par2, par3);
    }

    private void drawTooltip(String str, int x, int y, boolean smp){
        String str2 = str;
        if (smp){
            str2 = "";
        }
//         drawRect(x, y, x + 5 + fontRenderer.getStringWidth(str2), y + 13, 0x80000000);
//         drawString(fontRenderer, str2, x + 3, y + 3, smp ? 0xff0000 : 0xffffff);
        int top = 23;
       drawCenteredString(fontRenderer, str2, width / 2, height / 6 - top, smp ? 0xff0000 : 0xffffff);
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i,j,f);
        for (int k = 1; k < controlList.size(); k++){
            GuiButton button = ((GuiButton)controlList.get(k));
            if (i > button.xPosition && i < button.xPosition+150 && j > button.yPosition && j < button.yPosition+20){
                String str = mod_OldDays.getModuleById(id).getPropertyById(k).description;
                if (str == null){
                    return;
                }
                drawTooltip(str, i + 4, j - 13, false);
            }
        }
        if (displayField){
            field.drawTextBox();
        }
    }

    protected void keyTyped(char par1, int par2)
    {
        if (!displayField){
            super.keyTyped(par1, par2);
            return;
        }
        field.textboxKeyTyped(par1, par2);
        String str = field.getText().trim();
        if (str==null || str.equals("")){
            str = "OFF";
        }
        if (par2 == 1){
            mod_OldDays.setStringPropValue(id, fieldId, current);
            mod_OldDays.sendCallback(id, fieldId);
            GuiButton button = ((GuiButton)controlList.get(fieldId));
            showField(false, ((GuiButton)controlList.get(fieldId)));
        }else{
            mod_OldDays.setStringPropValue(id, fieldId, str);
            mod_OldDays.sendCallback(id, fieldId);
        }
        if (par1 == '\r')
        {
            GuiButton button = ((GuiButton)controlList.get(fieldId));
            button.displayString = mod_OldDays.getPropertyButtonText(id, fieldId);
            showField(false, button);
        }
    }
}