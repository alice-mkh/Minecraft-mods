package net.minecraft.src;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;

public class GuiOldDaysBase extends GuiScreen{
    public static final int LEFT_ID = 998;
    public static final int RIGHT_ID = 999;
    public static final int TOOLTIP_OFFSET = 500;

    protected GuiScreen parent;
    protected int max;
    protected int maxpage;
    protected int page;
    protected GuiButton left;
    protected GuiButton right;
    protected boolean displayField;
    protected GuiTextField field;
    protected int tooltipTimer;
    protected int fieldId;
    protected String current;
    protected boolean hasFields;
    protected mod_OldDays core;
    protected int specialButtons;

    public GuiOldDaysBase(GuiScreen guiscreen, mod_OldDays c){
        parent = guiscreen;
        max = 12;
        maxpage = 1;
        page = 0;
        displayField = false;
        fieldId = 0;
        tooltipTimer = 0;
        hasFields = false;
        core = c;
        specialButtons = 2;
    }

    public void updateScreen(){
        field.updateCursorCounter();
    }

    public void onGuiClosed(){
        Keyboard.enableRepeatEvents(false);
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(new GuiButton(0, width / 2 - 75, height - 28, 150, 20, stringtranslate.translateKey("menu.returnToGame")));
    }

    protected void addButton(int i, boolean b, int j, String name, boolean e){
        int x = width / 2 - 155;
        int i2 = i % max;
        if (i2 % 2 != 0){
           x+=160;
        }
        int margin = 30;
        int top = b ? 15 : -15;
        int y = height / 6 - top + ((i2/2) * margin);
        GuiButton button = new GuiButton(j+1, x, y, 150, 20, name);
        button.enabled = e;
        controlList.add(button);
    }

    protected void addButton(int i, boolean b, int j, OldDaysProperty p){
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
        GuiButtonProp button = new GuiButtonProp(j+1, x, y, p, false);
        controlList.add(button);
        GuiButton tooltipButton = new GuiButtonProp(j+TOOLTIP_OFFSET+1, x2, y, p, true);
        controlList.add(tooltipButton);
    }

    protected void postInitGui(int count){
        controlList.add(left = new GuiButtonPage(LEFT_ID, 30, height, width, false, this));
        controlList.add(right = new GuiButtonPage(RIGHT_ID, 30, height, width, true, this));
        field = new GuiTextField(fontRenderer, 0, 0, 150, 20);
        Keyboard.enableRepeatEvents(hasFields);
        maxpage = (count-1) / max;
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
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i,j,f);
        if (displayField){
            field.drawTextBox();
        }
    }

    protected void setPage(int to){
        if (to>maxpage || to<0){
            return;
        }
        for (int i = 0; i < max*2; i++){
            int i2 = i+(page*max*2)+1;
            if (i2<=controlList.size()-specialButtons){
                ((GuiButton)controlList.get(i2)).drawButton = false;
            }
        }
        page = to;
        for (int i = 0; i < max*2; i++){
            int i2 = i+(page*max*2)+1;
            if (i2<=controlList.size()-specialButtons){
                ((GuiButton)controlList.get(i2)).drawButton = true;
            }
        }
        left.drawButton = page>0;
        right.drawButton = page<maxpage;
        if (displayField){
            showField(false, ((GuiButton)controlList.get(fieldId)));
        }
    }

    protected void showField(boolean b, GuiButton button){
        displayField = b;
        Keyboard.enableRepeatEvents(b && hasFields);
        button.enabled = !b;
        field.setFocused(b);
//         if(!b){
//             current = "";
//         }
    }

    protected void keyTyped(char par1, int par2)
    {
        if (!displayField){
            super.keyTyped(par1, par2);
            if (par2 == 1 || par1 == '\0'){
                return;
            }
            GuiOldDaysSearch search = new GuiOldDaysSearch(this, core);
            mc.displayGuiScreen(search);
            search.keyTyped(par1, par2);
            return;
        }
    }

    protected void drawTooltip(String[] strings, int x, int y){
        int margin = 10;
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

    public boolean doesGuiPauseGame(){
        return Minecraft.getMinecraft().enableSP;
    }
}