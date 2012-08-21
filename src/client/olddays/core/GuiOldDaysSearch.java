package net.minecraft.src;

import java.util.ArrayList;
import java.util.regex.*;
import org.lwjgl.input.Keyboard;

public class GuiOldDaysSearch extends GuiOldDaysSettings{
    protected ArrayList props;
    protected GuiTextFieldSearch searchField;

    public GuiOldDaysSearch(GuiScreen guiscreen){
        super(guiscreen, -1);
        props = new ArrayList();
        max = 10;
        hasFields = true;
    }

    public void initGui(){
        searchField = new GuiTextFieldSearch(fontRenderer, width / 2 - 153, height / 6 - 13, 306, 16);
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
            if (controlList.get(fieldId) instanceof GuiButtonProp){
                GuiButtonProp button = ((GuiButtonProp)controlList.get(fieldId));
                button.prop.loadFromString(current);
                mod_OldDays.sendCallbackAndSave(button.prop.module.id, button.prop.id);
                showField(false, button);
             }
        }
        super.mouseClicked(par1, par2, par3);
    }

    protected boolean matches(OldDaysProperty prop, String str){
        str = str.toLowerCase();
        Pattern pat;
        try{
            pat = Pattern.compile(str);
        }catch(PatternSyntaxException ex){
            searchField.correct = false;
            return false;
        }
        searchField.correct = true;
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
        props.clear();
        for (int i = 0; i < mod_OldDays.modules.size(); i++){
            OldDaysModule module = mod_OldDays.modules.get(i);
            for (int j = 0; j < module.properties.size(); j++){
                OldDaysProperty prop = module.properties.get(j);
                if (matches(prop, str)){
                    props.add(prop);
                }
            }
        }
        controlList.clear();
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(new GuiButton(0, width / 2 - 75, height - 28, 150, 20, stringtranslate.translateKey("menu.returnToGame")));
        for (int i = 0; i < props.size(); i++){
            OldDaysProperty prop = ((OldDaysProperty)props.get(i));
            addButton(i, false, i, prop);
        }
        postInitGui(props.size());
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
    }
}