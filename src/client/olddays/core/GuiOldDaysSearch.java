package net.minecraft.src;

import java.util.ArrayList;
import java.util.regex.*;
import org.lwjgl.input.Keyboard;

public class GuiOldDaysSearch extends GuiOldDaysSettings{
    protected GuiTextFieldSearch searchField;

    public GuiOldDaysSearch(GuiScreen guiscreen, mod_OldDays core){
        super(guiscreen, core, -1);
        max = 10;
        hasFields = true;
    }

    public void initGui(){
        searchField = new GuiTextFieldSearch(fontRenderer, width / 2 - 153, height / 6 - 13, 306, 16);
        searchField.setMaxStringLength(999);
        searchField.setFocused(true);
        searchField.setCanLoseFocus(false);
        Keyboard.enableRepeatEvents(true);
    }

    protected void showField(boolean b, GuiButton button){
        super.showField(b, button);
        searchField.setFocused(!b);
        searchField.setCanLoseFocus(b);
    }

    protected void mouseClicked(int par1, int par2, int par3){
        searchField.mouseClicked(par1, par2, par3);
        if (searchField.isFocused()){
            if (this instanceof GuiOldDaysPresets){
                if (field.isFocused()){
                    showField(false, ((GuiButton)controlList.get(fieldId)));
                }
            }else if (controlList.get(fieldId) != null && controlList.get(fieldId) instanceof GuiButtonProp){
                GuiButtonProp button = ((GuiButtonProp)controlList.get(fieldId));
                button.prop.loadFromString(current);
                mod_OldDays.sendCallbackAndSave(button.prop.module.id, button.prop.id);
                showField(false, button);
            }
        }
        super.mouseClicked(par1, par2, par3);
    }

    protected boolean matches(OldDaysProperty prop, Pattern pat){
        if (pat.matcher(prop.getButtonText().toLowerCase()).find()){
            return true;
        }
        String[] tooltip = prop.getTooltip();
        for (int i = 0; i < tooltip.length; i++){
            if (pat.matcher(tooltip[i].toLowerCase()).find()){
                return true;
            }
        }
        return false;
    }

    protected void updateList(String str){
        controlList.clear();
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(new GuiButton(0, width / 2 - 75, height - 28, 150, 20, stringtranslate.translateKey("menu.returnToGame")));
        int count = 0;
        searchField.correct = true;
        Pattern pat = null;
        try{
            pat = Pattern.compile(str.toLowerCase());
        }catch(PatternSyntaxException ex){
            searchField.correct = false;
        }
        if (searchField.correct){
            for (int i = 0; i < mod_OldDays.modules.size(); i++){
                OldDaysModule module = mod_OldDays.modules.get(i);
                for (int j = 0; j < module.properties.size(); j++){
                    OldDaysProperty prop = module.getPropertyById(j + 1);
                    if (matches(prop, pat)){
                        addButton(count, false, count++, prop);
                    }
                }
            }
        }
        postInitGui(count);
        setPage(0);
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
        if (guibutton.id == LEFT_ID){
            setPage(page-1);
            return;
        }
        if (guibutton.id == RIGHT_ID){
            setPage(page+1);
            return;
        }
        GuiButtonProp guibuttonprop = (GuiButtonProp)guibutton;
        actionPerformed(guibuttonprop);
    }

    protected void keyTyped(char par1, int par2){
        if (searchField.isFocused()){
            searchField.textboxKeyTyped(par1, par2);
            if (par1 == '\r' || par2 == 1 || ((par2 == 211 || par2 == 14) && searchField.getText().length() <= 0)){
                mc.displayGuiScreen(parent);
                return;
            }
            updateList(searchField.getText().trim());
            return;
        }else{
            super.keyTyped(par1, par2);
        }
    }

    public void updateScreen(){
        super.updateScreen();
        searchField.updateCursorCounter();
    }

    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        searchField.drawTextBox();
        if (showTooltip != null){
            drawTooltip(showTooltip.prop.getTooltip(), width / 2, height / 2);
        }
    }
}