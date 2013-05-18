package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class GuiOldDaysSettings extends GuiOldDaysBase{
    private int id;
    protected GuiButtonProp showTooltip;
    protected GuiButtonProp showPropertyPage;
    protected GuiButton[] propertyPageButtons;

    public GuiOldDaysSettings(GuiScreen guiscreen, mod_OldDays core, int i){
        super(guiscreen, core);
        id = i;
        showTooltip = null;
    }

    @Override
    protected void updateList(String str){
        if (id >= 0){
            int count = mod_OldDays.getModuleById(id).properties.size();
            for (int i = 0; i < count; i++){
                addButton(i, true, i, mod_OldDays.getModuleById(id).getPropertyById(i + 1));
            }
        }
        postInitGui();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton){
        if (!guibutton.enabled){
            return;
        }
        super.actionPerformed(guibutton);
        if (showPropertyPage == null){
            return;
        }
        OldDaysPropertySet set = (OldDaysPropertySet)showPropertyPage.prop;
        if (guibutton == propertyPageButtons[set.value.length]){
            showPropertyPage = null;
            propertyPageButtons = null;
        }else if (guibutton == propertyPageButtons[set.value.length + 1]){
            set.changeTemplate(isShiftPressed());
            send(set);
            mod_OldDays.sendCallbackAndSave(set.module.id, set.id);
        }else{
            boolean shift = isShiftPressed();
            if (shift){
                set.decrementValue(-guibutton.id - 10);
            }else{
                set.incrementValue(-guibutton.id - 10);
            }
            send(set);
            mod_OldDays.sendCallbackAndSave(set.module.id, set.id);
        }
    }

    @Override
    public void actionPerformedScrolling(GuiButton b){
        GuiButtonProp guibutton = (GuiButtonProp)b;
        if (guibutton.help){
            showTooltip = guibutton;
            return;
        }
        displayField = false;
        OldDaysProperty prop = guibutton.prop;
        int m = prop.module.id;
        int p = prop.id;
        if (prop.highlight){
            prop.highlight = false;
            boolean shouldHighlightModule = false;
            for (int i = 1; i <= prop.module.properties.size(); i++){
                OldDaysProperty prop2 = prop.module.getPropertyById(i);
                if (prop2.highlight){
                    shouldHighlightModule = true;
                    break;
                }
            }
            prop.module.highlight = shouldHighlightModule;
        }
        if (prop.guitype == OldDaysProperty.GUI_TYPE_BUTTON){
            boolean shift = isShiftPressed();
            if (shift){
                prop.decrementValue();
            }else{
                prop.incrementValue();
            }
            send(prop);
        }else if (prop.guitype == OldDaysProperty.GUI_TYPE_FIELD){
            field = new GuiTextField(fontRenderer, guibutton.xPosition+2, guibutton.yPosition+2, 146, 16);
            field.setMaxStringLength(999);
            fieldButton = (GuiButtonProp)guibutton;
            showField(true);
            if (prop.saveToString().equals("OFF")){
                current = "";
                field.setText("");
            }else{
                current = prop.saveToString();
                field.setText(prop.saveToString());
            }
            guibutton.enabled = false;
        }else if (prop.guitype == OldDaysProperty.GUI_TYPE_PAGE){
            showPropertyPage = guibutton;
            OldDaysPropertySet set = (OldDaysPropertySet)prop;
            propertyPageButtons = new GuiButton[set.value.length + (set.shouldUseTemplates() ? 2 : 1)];
            for (int i = 0; i < set.value.length; i++){
                GuiButton button = new GuiButton(-i - 10, 0, 0, 150, 20, "");
                propertyPageButtons[i] = button;
            }
            GuiButton button = new GuiButton(-set.value.length - 10, 0, 0, 200, 20, mod_OldDays.lang.get("continue"));
            propertyPageButtons[set.value.length] = button;
            if (set.shouldUseTemplates()){
                GuiButton button2 = new GuiButton(-set.value.length - 11, 0, 0, 200, 20, "");
                propertyPageButtons[set.value.length + 1] = button2;
            }
        }
        mod_OldDays.sendCallbackAndSave(m, p);
        guibutton.enabled = !prop.isDisabled();
        guibutton.displayString = mod_OldDays.getPropertyButtonText(prop);
        if (prop.guiRefresh){
            refresh();
        }
    }

    protected void send(OldDaysProperty prop){
        if (Minecraft.getMinecraft().enableSP){
            return;
        }
        core.sendPacketToServer(SMPManager.PACKET_C2S_PROP, prop.module.id+" "+prop.id+" "+prop.saveToString());
    }

    @Override
    protected void showField(boolean b){
        super.showField(b);
        if(!b && fieldButton.prop != null){
            mod_OldDays.sendCallbackAndSave(fieldButton.prop.module.id, fieldButton.prop.id);
            send(fieldButton.prop);
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3){
        if (showPropertyPage != null){
            if (par3 == 0){
                for (int i = 0; i < propertyPageButtons.length; i++){
                    GuiButton guibutton = propertyPageButtons[i];
                    if (guibutton.mousePressed(mc, par1, par2)){
                        mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                        actionPerformed(guibutton);
                        if (propertyPageButtons == null){
                            break;
                        }
                    }
                }
            }
            return;
        }
        super.mouseClicked(par1, par2, par3);
        if (this instanceof GuiOldDaysPresets){
            if (displayField){
                field.mouseClicked(par1, par2, par3);
                if (!field.isFocused()){
                    showField(false);
                }
            }
            return;
        }
        if (displayField){
            field.mouseClicked(par1, par2, par3);
            if (!field.isFocused()){
                fieldButton.prop.loadFromString(current);
                mod_OldDays.sendCallbackAndSave(fieldButton.prop.module.id, fieldButton.prop.id);
                showField(false);
            }
        }
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        if (!displayField){
            return;
        }
        field.textboxKeyTyped(par1, par2);
        current = field.getText();
        String str = field.getText().trim();
        if (str==null || str.equals("")){
            str = "OFF";
        }
        if (par1 == '\r')
        {
            fieldButton.displayString = mod_OldDays.getPropertyButtonText(fieldButton.prop);
            showField(false);
        }
        if (fieldButton.prop == null){
            return;
        }
        if (par2 == 1){
            fieldButton.prop.loadFromString(current);
            mod_OldDays.sendCallbackAndSave(fieldButton.prop.module.id, fieldButton.prop.id);
        }else{
            fieldButton.prop.loadFromString(str);
            mod_OldDays.sendCallback(fieldButton.prop.module.id, fieldButton.prop.id);
        }
    }

    private void refresh(){
        for (int i = 0; i < buttonList.size(); i++){
            if (!(buttonList.get(i) instanceof GuiButtonProp)){
                continue;
            }
            if (((GuiButtonProp)buttonList.get(i)).help){
                continue;
            }
            GuiButtonProp button = ((GuiButtonProp)buttonList.get(i));
            button.enabled = !button.prop.isDisabled();
            button.displayString = mod_OldDays.getPropertyButtonText(button.prop);
        }
    }

    @Override
    public void drawScreen(int i, int j, float f){
        if (showPropertyPage != null){
            super.drawScreen(-1000, -1000, f);
            drawPropertyPage(showPropertyPage, width / 2, height / 2);
            for (int k = 0; k < propertyPageButtons.length; k++){
                GuiButton guibutton = propertyPageButtons[k];
                guibutton.drawButton(mc, i, j);
            }
        }else{
            super.drawScreen(i, j, f);
            if (showTooltip != null && (i <= showTooltip.xPosition || i >= showTooltip.xPosition+20 || j <= showTooltip.yPosition || j >= showTooltip.yPosition+20)){
                showTooltip = null;
            }
            if (showTooltip != null){
                drawTooltip(showTooltip.prop.getTooltip(), width / 2, height / 2);
            }
        }
    }

    protected void drawPropertyPage(GuiButtonProp propButton, int x, int y){
        OldDaysPropertySet set = (OldDaysPropertySet)propButton.prop;
        int margin0 = 5;
        int margin = margin0 + 20;
        int padding = 10;
        int w = 300 + padding * 3;
        int h = padding * 3 + margin0 + margin * (propertyPageButtons.length / 2 + (set.shouldUseTemplates() ? 2 : 1));
        drawRect(x - w / 2, y - h / 2 - 1, x + w / 2, y + h / 2 - 1, 0xCC000000);
        drawCenteredString(fontRenderer, set.getButtonText(), x, y - h / 2 + padding / 2, 0xffffff);
        for (int i = 0; i < propertyPageButtons.length; i++){
            GuiButton button = propertyPageButtons[i];
            button.xPosition = (i % 2 == 0) ? (x - 150 - padding / 2) : (x + padding / 2);
            button.yPosition = y - h / 2 + 2 * padding + margin * (i / 2);
            if (i < set.value.length){
                button.displayString = set.getValueButtonText(i);
                if (set.shouldUseTemplates()){
                    button.yPosition += margin + margin0;
                }
                button.enabled = set.shouldButtonsBeEnabled();
            }else if (i > set.value.length){
                button.displayString = set.getTemplateButtonText();
                button.xPosition = x - 100;
                button.yPosition = y - h / 2 + 2 * padding;
            }else{
                int i2 = i;
                if (i % 2 != 0){
                    i2++;
                }
                if (set.shouldUseTemplates()){
                    i2 += 2;
                }
                button.xPosition = x - 100;
                button.yPosition = y - h / 2 + 2 * padding + margin * (i2 / 2) + 3 * margin0;
            }
        }
    }
}