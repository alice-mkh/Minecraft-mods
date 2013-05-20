package net.minecraft.src;

import java.util.ArrayList;
import java.util.regex.*;
import org.lwjgl.input.Keyboard;

public class GuiOldDaysPresets extends GuiOldDaysSearch{
    private String[] presets;
    private boolean custom;
    private GuiButton switch1;
    private GuiButton switch2;

    public GuiOldDaysPresets(GuiScreen guiscreen, mod_OldDays core){
        super(guiscreen, core);
        custom = false;
        presets = new String[]{};
    }

    protected void updateList(String str){
        scrollingGui.buttonList.clear();
        separators.clear();
        presets = custom ? core.saveman.getCustomPresets() : core.saveman.getDefaultPresets();
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
            for (int i = 0; i < presets.length; i++){
                if (pat.matcher(presets[i].toLowerCase()).find()){
                    addButton(count++, true, i, presets[i], true, true);
                }
            }
        }
        if (custom && str.length() <= 0){
            addButton(count, true, -4, "+", custom, false);
            ((GuiButton)buttonList.get(buttonList.size() - 1)).drawButton = custom;
        }
        postInitGui();
    }

    @Override
    protected void addCustomButtons(){
        int y = height / 6 + 150;
        switch1 = new GuiButton(-1, width / 2 - 75, height - 58, 75, 20, mod_OldDays.lang.get("gui.presets.default"));
        switch2 = new GuiButton(-2, width / 2 + 1, height - 58, 75, 20, mod_OldDays.lang.get("gui.presets.custom"));
        switch1.enabled = custom;
        switch2.enabled = !custom;
        buttonList.add(switch1);
        buttonList.add(switch2);
    }

    protected void addButton(int i, boolean b, int j, String name, boolean e, boolean delete){
        int offset = 3;
        int x = width / 2 - 155;
        int x2 = x - offset - 20;
        if (i % 2 != 0){
           x+=160;
           x2+=330 + (offset * 2);
        }
        int margin = 30;
        int top = !b ? 25 : -5;
        int y = height / 6 - top + (i / 2) * margin;
        y += 10 * separators.size();
        int newContentHeight = (i / 2) * margin + 10 * separators.size();
        if (newContentHeight > contentHeight){
            contentHeight = newContentHeight;
        }
        GuiButtonProp button = new GuiButtonProp(j+1, x, y, false, name);
        button.enabled = e;
        scrollingGui.buttonList.add(button);
        if (!delete){
            return;
        }
        GuiButtonProp deleteButton = new GuiButtonProp(j+TOOLTIP_OFFSET+1, x2, y, true, "x");
        deleteButton.enabled = custom;
        deleteButton.drawButton = custom;
        scrollingGui.buttonList.add(deleteButton);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton){
        if (!guibutton.enabled){
            return;
        }
        super.actionPerformed(guibutton);
        if (guibutton.id == -1){
            custom = false;
            switch1.enabled = false;
            switch2.enabled = true;
            updateList(searchField.getText().trim());
            return;
        }
        if (guibutton.id == -2){
            custom = true;
            switch1.enabled = true;
            switch2.enabled = false;
            updateList(searchField.getText().trim());
            return;
        }
    }

    @Override
    public void actionPerformedScrolling(GuiButton guibutton){
        if (guibutton.id == -3 && custom){
            field = new GuiTextField(fontRenderer, guibutton.xPosition+2, guibutton.yPosition+2, 146, 16);
            fieldButton = (GuiButtonProp)guibutton;
            showField(true);
            current = mod_OldDays.lang.get("gui.presets.new");
            field.setText(current);
            guibutton.enabled = false;
            return;
        }
        if (guibutton.id > TOOLTIP_OFFSET){
            core.saveman.deletePreset(presets[guibutton.id - TOOLTIP_OFFSET - 1]);
            updateList(searchField.getText().trim());
            return;
        }
        core.saveman.loadPreset(presets[guibutton.id - 1], custom);
    }

    @Override
    protected void keyTyped(char par1, int par2){
        if (searchField.isFocused()){
            searchField.textboxKeyTyped(par1, par2);
            if (par2 == 1){
                mc.displayGuiScreen(parent);
                return;
            }
            updateList(searchField.getText().trim());
            return;
        }else{
            if (par1 == '\r' || par2 == 28){
                core.saveman.savePreset(current.trim());
                showField(false);
                updateList(searchField.getText().trim());
                return;
            }
            if (par2 == 1){
                showField(false);
                return;
            }
            super.keyTyped(par1, par2);
        }
    }

    @Override
    public int getBottom(){
        return super.getBottom() - 25;
    }
}