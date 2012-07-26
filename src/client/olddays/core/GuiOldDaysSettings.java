package net.minecraft.src;

import java.util.List;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;

public class GuiOldDaysSettings extends GuiOldDaysBase{
    private int id;

    public GuiOldDaysSettings(GuiScreen guiscreen, int i){
        super(guiscreen);
        id = i;
    }

    public void initGui(){
        super.initGui();
        int count = mod_OldDays.getModuleById(id).properties.size();
        for (int i = 0; i < count; i++){
            addButton(i, true, i, mod_OldDays.getPropertyButtonText(id, i+1), !mod_OldDays.getModuleById(id).getPropertyById(i+1).isDisabled());
        }
        postInitGui(count);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);
        if (guibutton.id <= 0 || guibutton.id >= 98){
            return;
        }
        displayField = false;
        OldDaysProperty prop = mod_OldDays.getModuleById(id).getPropertyById(guibutton.id);
        if (mod_OldDays.getPropertyGuiType(id, guibutton.id) == OldDaysProperty.GUI_TYPE_BUTTON){
            prop.incrementValue();
        }else if (mod_OldDays.getPropertyGuiType(id, guibutton.id) == OldDaysProperty.GUI_TYPE_FIELD){
            /*int offset = fontRenderer.getStringWidth(mod_OldDays.getPropertyName(id, guibutton.id)+":")-2;
            offset += (150-fontRenderer.getStringWidth(mod_OldDays.getPropertyButtonText(id, guibutton.id)))/2;
            if (fontRenderer.getStringWidth(mod_OldDays.getStringPropValue(id, guibutton.id)>138-offset){
                offset = 0;
            }*/
            int offset = 0;
            field = new GuiTextField(fontRenderer, guibutton.xPosition+offset+2, guibutton.yPosition+2, 146-offset, 16);
            showField(true, guibutton);
            field.setFocused(true);
            if (!mod_OldDays.getStringPropValue(id, guibutton.id).equals("OFF")){
                current = mod_OldDays.getStringPropValue(id, guibutton.id);
                field.setText(mod_OldDays.getStringPropValue(id, guibutton.id));
            }else{
                current = "";
                field.setText("");
            }
            fieldId = guibutton.id;
            guibutton.enabled = false;
        }
        mod_OldDays.saveModuleProperties(id);
        mod_OldDays.sendCallbackAndSave(id, guibutton.id);
        guibutton.enabled = !prop.isDisabled();
        guibutton.displayString = mod_OldDays.getPropertyButtonText(id, guibutton.id);
        if (prop.guiRefresh){
            refresh();
        }
    }

    protected void showField(boolean b, GuiButton button){
        super.showField(b, button);
        if(!b){
            mod_OldDays.saveModuleProperties(id);
        }
    }

    protected void mouseClicked(int par1, int par2, int par3){
        if (displayField){
            field.mouseClicked(par1, par2, par3);
            mod_OldDays.setStringPropValue(id, fieldId, current);
            mod_OldDays.sendCallbackAndSave(id, fieldId);
            showField(false, ((GuiButton)controlList.get(fieldId)));
        }
        super.mouseClicked(par1, par2, par3);
    }

    private void drawTooltip(int i, int x, int y){
        int margin = 10;
        String[] strings = mod_OldDays.getModuleById(id).getPropertyById(i).getTooltip();
        int length = strings.length;
        if (strings[length - 1] == null || strings[length - 1] == ""){
            return;
        }
        int w = 0;
        int w2 = 0;
        for (int j = 0; j < length; j++){
            int width = fontRenderer.getStringWidth(strings[j].replace("<- ", "<").replaceAll("(§[0-9a-fk-or]|<-|->)", ""));
           if (w < width + margin * 2){
                w = width + margin * 2;
                w2 = width / 2;
            }
        }
        int h = (length * 10) + margin;
        drawRect(x - w / 2, y - h / 2 - 1, x + w / 2, y + h / 2 - 1, 0xCC000000);
        for (int j = 0; j < length; j++){
            String str = strings[j].replace("<-", "").replace("->", "");
            int y2 = y + (j * 10) - (length * 5);
            if (strings[j].startsWith("<-") || strings[j].startsWith("§7<-") || strings[j].startsWith("§4<-")){
                drawString(fontRenderer, str, x - w / 2 + margin, y2, 0xffffff);
            }else if (strings[j].endsWith("->")){
                drawString(fontRenderer, str, x + w / 2 - margin - fontRenderer.getStringWidth(str), y2, 0xffffff);
            }else{
                drawString(fontRenderer, str, x - fontRenderer.getStringWidth(str.replace("<- ", "<").replaceAll("(§[0-9a-fk-or]|<-|->)", "")) / 2, y2, 0xffffff);
            }
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        for (int k = 1; k < controlList.size(); k++){
            GuiButton button = ((GuiButton)controlList.get(k));
            if (i > button.xPosition && i < button.xPosition+150 && j > button.yPosition && j < button.yPosition+20 && button.drawButton){
                try{
                    mod_OldDays.getModuleById(id).getPropertyById(k);
                }catch(Exception ex){
                    return;
                }
                if (tooltipTimer>=15){
                    drawTooltip(k, /*i + 4*/width / 2, /*j - 13*/height / 2);
                }else{
                    tooltipTimer++;
                }
                return;
            }
        }
        tooltipTimer = 0;
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
        if (par1 == '\r')
        {
            GuiButton button = ((GuiButton)controlList.get(fieldId));
            button.displayString = mod_OldDays.getPropertyButtonText(id, fieldId);
            showField(false, button);
        }
        if (par2 == 1){
            mod_OldDays.setStringPropValue(id, fieldId, current);
            mod_OldDays.sendCallbackAndSave(id, fieldId);
            GuiButton button = ((GuiButton)controlList.get(fieldId));
            showField(false, ((GuiButton)controlList.get(fieldId)));
        }else{
            mod_OldDays.setStringPropValue(id, fieldId, str);
            mod_OldDays.sendCallback(id, fieldId);
        }
    }

    private void refresh(){
        for (int i = 1; i < controlList.size() - 2; i++){
            GuiButton button = ((GuiButton)controlList.get(i));
            button.enabled = !mod_OldDays.getModuleById(id).getPropertyById(i).isDisabled();
            button.displayString = mod_OldDays.getPropertyButtonText(id, i);
        }
    }
}