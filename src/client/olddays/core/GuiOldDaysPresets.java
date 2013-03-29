package net.minecraft.src;

import java.util.ArrayList;
import java.util.regex.*;
import org.lwjgl.input.Keyboard;

public class GuiOldDaysPresets extends GuiOldDaysSearch{
    private String[] presets;
    private boolean custom;

    public GuiOldDaysPresets(GuiScreen guiscreen, mod_OldDays core){
        super(guiscreen, core);
        custom = false;
        max = 8;
        presets = new String[]{};
        specialButtons = 5;
    }

    @Override
    public void initGui(){
        super.initGui();
        updateList("");
    }

    protected void updateList(String str){
        buttonList.clear();
        StringTranslate stringtranslate = StringTranslate.getInstance();
        buttonList.add(new GuiButton(0, width / 2 - 75, height - 28, 150, 20, stringtranslate.translateKey("menu.returnToGame")));
        presets = custom ? core.saveman.getCustomPresets() : core.saveman.getDefaultPresets();
        int count = 0;
        searchField.correct = true;
        Pattern pat = null;
        try{
            pat = Pattern.compile(str.toLowerCase());
        }catch(PatternSyntaxException ex){
            searchField.correct = false;
        }
        if (searchField.correct){
            for (int i = 0; i < presets.length; i++){
                if (pat.matcher(presets[i].toLowerCase()).find()){
                    addButton(count++, false, i, presets[i], true, true);
                }
            }
        }
        int y = height / 6 - 15 + ((11/2) * 30);
//         GuiButton save = new GuiButton(presets.length + 1, width / 2 - 155/*75*/, y, 150, 20, mod_OldDays.lang.get("gui.presets.save"));
//         save.enabled = custom;
//         buttonList.add(save);
        if (custom){
            addButton(count, false, presets.length, "+", custom, false);
            ((GuiButton)buttonList.get(buttonList.size() - 1)).drawButton = custom && presets.length < max;
        }
        GuiButton switch1 = new GuiButton(presets.length + 2, width / 2 - 75/*5*/, y, 75, 20, mod_OldDays.lang.get("gui.presets.default"));
        GuiButton switch2 = new GuiButton(presets.length + 3, width / 2 + 1/*81*/, y, 75, 20, mod_OldDays.lang.get("gui.presets.custom"));
        switch1.enabled = custom;
        switch2.enabled = !custom;
        buttonList.add(switch1);
        buttonList.add(switch2);
        postInitGui(presets.length + (custom ? 1 : 0));
        setPage(0);
    }

    protected void addButton(int i, boolean b, int j, String name, boolean e, boolean delete){
        int offset = 3;
        int x = width / 2 - 155;
        int x2 = x - offset - 20;
        int i2 = i % max;
        if (i2 % 2 != 0){
           x+=160;
           x2+=330 + (offset * 2);
        }
        int margin = 30;
        int top = b ? 15 : -15;
        int y = height / 6 - top + ((i2/2) * margin);
        GuiButton button = new GuiButton(j+1, x, y, 150, 20, name);
        button.enabled = e;
        buttonList.add(button);
        if (!delete){
            return;
        }
        GuiButton deleteButton = new GuiButton(j+TOOLTIP_OFFSET+1, x2, y, 20, 20, "x");
        deleteButton.enabled = custom;
        buttonList.add(deleteButton);
    }

    @Override
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
        if (guibutton.id == presets.length + 1 && custom){
            field = new GuiTextField(fontRenderer, guibutton.xPosition+2, guibutton.yPosition+2, 146, 16);
            showField(true, guibutton);
            current = mod_OldDays.lang.get("gui.presets.new");
            field.setText(current);
            fieldId = presets.length * 2 + 1;
            guibutton.enabled = false;
            return;
        }
        if (guibutton.id == presets.length + 2){
            custom = false;
            updateList(searchField.getText().trim());
            return;
        }
        if (guibutton.id == presets.length + 3){
            custom = true;
            updateList(searchField.getText().trim());
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
            if (par2 == 1){
                showField(false, ((GuiButton)buttonList.get(fieldId)));
                return;
            }
            if (par1 == '\r'){
                core.saveman.savePreset(current.trim());
                showField(false, ((GuiButton)buttonList.get(fieldId)));
                updateList(searchField.getText().trim());
                return;
            }
            super.keyTyped(par1, par2);
        }
    }
}