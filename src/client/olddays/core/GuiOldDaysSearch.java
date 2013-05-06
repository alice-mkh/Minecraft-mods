package net.minecraft.src;

import java.util.ArrayList;
import java.util.regex.*;
import org.lwjgl.input.Keyboard;

public class GuiOldDaysSearch extends GuiOldDaysSettings{

    public GuiOldDaysSearch(GuiScreen guiscreen, mod_OldDays core){
        super(guiscreen, core, -1);
        hasSearchField = true;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3){
        searchField.mouseClicked(par1, par2, par3);
        if (searchField.isFocused()){
            if (this instanceof GuiOldDaysPresets){
                if (field.isFocused()){
                    showField(false);
                }
            }else if (displayField && buttonList != null && fieldButton != null){
                fieldButton.prop.loadFromString(current);
                mod_OldDays.sendCallbackAndSave(fieldButton.prop.module.id, fieldButton.prop.id);
                showField(false);
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

    @Override
    protected void updateList(String str){
        scrollingGui.buttonList.clear();
        separators.clear();
        int count = 0;
        searchField.correct = true;
        Pattern pat = null;
        try{
            pat = Pattern.compile(str.toLowerCase());
        }catch(PatternSyntaxException ex){
            searchField.correct = false;
        }
        contentHeight = 0;
        if (searchField.correct){
            for (int i = 0; i < mod_OldDays.modules.size(); i++){
                OldDaysModule module = mod_OldDays.modules.get(i);
                boolean separator = true;
                for (int j = 0; j < module.properties.size(); j++){
                    OldDaysProperty prop = module.getPropertyById(j + 1);
                    if (matches(prop, pat)){
                        if (separator){
                            count = addSeparator(count, false, mod_OldDays.lang.get("module."+module.name.toLowerCase()));
                            separator = false;
                        }
                        addButton(count, false, count++, prop);
                    }
                }
            }
        }
        postInitGui();
    }
}