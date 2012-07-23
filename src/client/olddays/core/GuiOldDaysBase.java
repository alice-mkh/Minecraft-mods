package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiOldDaysBase extends GuiScreen{
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

    public GuiOldDaysBase(GuiScreen guiscreen){
        parent = guiscreen;
        max = 12;
        maxpage = 1;
        page = 0;
        displayField = false;
        fieldId = 0;
        tooltipTimer = 0;
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
        button.drawButton = false;
        button.enabled = e;
        controlList.add(button);
    }

    protected void postInitGui(int count){
        controlList.add(left = new GuiButtonPage(98, 30, height, width, false, this));
        controlList.add(right = new GuiButtonPage(99, 30, height, width, true, this));
        field = new GuiTextField(fontRenderer, 0, 0, 150, 20);
        Keyboard.enableRepeatEvents(false);
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
        if (guibutton.id == 98){
            setPage(page-1);
            return;
        }
        if (guibutton.id == 99){
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
        for (int i = 0; i < max; i++){
            int i2 = i+(page*max)+1;
            if (i2<=controlList.size()-2){
                ((GuiButton)controlList.get(i2)).drawButton = false;
            }
        }
        page = to;
        for (int i = 0; i < max; i++){
            int i2 = i+(page*max)+1;
            if (i2<=controlList.size()-2){
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
        Keyboard.enableRepeatEvents(b);
        button.enabled = !b;
        if(!b){
            current = "";
        }
    }
}