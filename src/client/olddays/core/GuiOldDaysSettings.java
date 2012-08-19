package net.minecraft.src;

import java.util.List;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;

public class GuiOldDaysSettings extends GuiOldDaysBase{
    private int id;
    private GuiButtonProp showTooltip;

    public GuiOldDaysSettings(GuiScreen guiscreen, int i){
        super(guiscreen);
        id = i;
        showTooltip = null;
    }

    public void initGui(){
        super.initGui();
        int count = mod_OldDays.getModuleById(id).properties.size();
        for (int i = 0; i < count; i++){
            addButton(i, true, i, mod_OldDays.getModuleById(id).getPropertyById(i + 1));
        }
        postInitGui(count);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);
        if (guibutton.id <= 0 || guibutton.id >= LEFT_ID){
            return;
        }
        if (!guibutton.enabled){
            return;
        }
        GuiButtonProp guibuttonprop = (GuiButtonProp)guibutton;
        actionPerformed(guibuttonprop);
    }

    protected void actionPerformed(GuiButtonProp guibutton)
    {
        displayField = false;
        OldDaysProperty prop = guibutton.prop;
        int m = prop.module.id;
        int p = prop.id;
        if (mod_OldDays.getPropertyGuiType(m, p) == OldDaysProperty.GUI_TYPE_BUTTON){
            prop.incrementValue();
        }else if (mod_OldDays.getPropertyGuiType(m, p) == OldDaysProperty.GUI_TYPE_FIELD){
            /*int offset = fontRenderer.getStringWidth(mod_OldDays.getPropertyName(m, p)+":")-2;
            offset += (150-fontRenderer.getStringWidth(mod_OldDays.getPropertyButtonText(m, p)))/2;
            if (fontRenderer.getStringWidth(mod_OldDays.getStringPropValue(m, p)>138-offset){
                offset = 0;
            }*/
            int offset = 0;
            field = new GuiTextField(fontRenderer, guibutton.xPosition+offset+2, guibutton.yPosition+2, 146-offset, 16);
            showField(true, guibutton);
            if (!mod_OldDays.getStringPropValue(m, p).equals("OFF")){
                current = mod_OldDays.getStringPropValue(m, p);
                field.setText(mod_OldDays.getStringPropValue(m, p));
            }else{
                current = "";
                field.setText("");
            }
            fieldId = guibutton.id;
            guibutton.enabled = false;
        }
        mod_OldDays.saveModuleProperties(m);
        mod_OldDays.sendCallbackAndSave(m, p);
        guibutton.enabled = !prop.isDisabled();
        guibutton.displayString = mod_OldDays.getPropertyButtonText(m, p);
        if (prop.guiRefresh){
            refresh();
        }
    }

    protected void showField(boolean b, GuiButton button){
        super.showField(b, button);
        if (!(button instanceof GuiButtonProp)){
            return;
        }
        if(!b){
            mod_OldDays.saveModuleProperties(((GuiButtonProp)button).prop.module.id);
        }
    }

    protected void mouseClicked(int par1, int par2, int par3){
        super.mouseClicked(par1, par2, par3);
        GuiButtonProp guibuttonprop = null;
        for (int i = 0; i < controlList.size(); i++){
            GuiButton guibutton = (GuiButton)controlList.get(i);
            if (guibutton.mousePressed(mc, par1, par2)){
                if (guibutton instanceof GuiButtonProp){
                    guibuttonprop = (GuiButtonProp)guibutton;
                }
            }
        }
        if (guibuttonprop == null){
            showTooltip = null;
            return;
        }
        if (par3 == 1){
            showTooltip = showTooltip == null ? guibuttonprop : null;
            return;
        }
        if (displayField){
            field.mouseClicked(par1, par2, par3);
            mod_OldDays.setStringPropValue(guibuttonprop.prop.module.id, guibuttonprop.prop.id, current);
            mod_OldDays.sendCallbackAndSave(guibuttonprop.prop.module.id, guibuttonprop.prop.id);
//             showField(false, guibuttonprop);
        }
    }

    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        if (!displayField){
            return;
        }
        field.textboxKeyTyped(par1, par2);
        String str = field.getText().trim();
        if (str==null || str.equals("")){
            str = "OFF";
        }
        if (!(controlList.get(fieldId) instanceof GuiButtonProp)){
            return;
        }
        GuiButtonProp button = ((GuiButtonProp)controlList.get(fieldId));
        if (par1 == '\r')
        {
            button.displayString = mod_OldDays.getPropertyButtonText(button.prop.module.id, button.prop.id);
            showField(false, button);
        }
        if (par2 == 1){
            mod_OldDays.setStringPropValue(button.prop.module.id, button.prop.id, current);
            mod_OldDays.sendCallbackAndSave(button.prop.module.id, button.prop.id);
            showField(false, ((GuiButton)controlList.get(fieldId)));
        }else{
            mod_OldDays.setStringPropValue(button.prop.module.id, button.prop.id, str);
            mod_OldDays.sendCallback(button.prop.module.id, button.prop.id);
        }
    }

    private void refresh(){
        for (int i = 0; i < controlList.size(); i++){
            if (!(controlList.get(i) instanceof GuiButtonProp)){
                continue;
            }
            GuiButtonProp button = ((GuiButtonProp)controlList.get(i));
            button.enabled = !button.prop.isDisabled();
            button.displayString = mod_OldDays.getPropertyButtonText(button.prop.module.id, i);
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        boolean show = false;
        for (int k = 0; k < controlList.size(); k++){
            if (!(controlList.get(k) instanceof GuiButtonProp)){
                continue;
            }
            GuiButtonProp button = ((GuiButtonProp)controlList.get(k));
            if (i > button.xPosition && i < button.xPosition+150 && j > button.yPosition && j < button.yPosition+20 && button.drawButton){
                show = true;
                break;
            }
        }
        if (!show){
            showTooltip = null;
        }
        if (showTooltip != null){
            drawTooltip(showTooltip.prop.getTooltip(), width / 2, height / 2);
        }
    }
}