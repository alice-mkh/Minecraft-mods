package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class GuiOldDaysSettings extends GuiOldDaysBase{
    private int id;
    protected GuiButtonProp showTooltip;

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
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        if (showTooltip != null && (i <= showTooltip.xPosition || i >= showTooltip.xPosition+20 || j <= showTooltip.yPosition || j >= showTooltip.yPosition+20)){
            showTooltip = null;
        }
        if (showTooltip != null){
            drawTooltip(showTooltip.prop.getTooltip(), width / 2, height / 2);
        }
    }
}