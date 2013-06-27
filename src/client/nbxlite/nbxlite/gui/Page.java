package net.minecraft.src.nbxlite.gui;

import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.IScrollingGui;
import net.minecraft.src.GuiButtonOldDays;
import net.minecraft.src.GuiScrolling;

public abstract class Page extends GuiScreen implements IScrollingGui{
    protected int leftmargin = 90;
    protected GuiScrolling scrollingGui;
    protected GuiNBXlite parent;
    private int contentHeight;

    public Page(GuiNBXlite parent){
        this.parent = parent;
        scrollingGui = new GuiScrolling(this);
        contentHeight = 0;
    }

    public void setMc(Minecraft mc){
        this.mc = mc;
        scrollingGui.mc = mc;
    }

    public abstract void initButtons();

    @Override
    public void scrolled(){
        contentHeight = 0;
    }

    public void updateButtonPosition(){}

    public void updateButtonVisibility(){}

    public void updateButtonText(){}

    public abstract void applySettings();

    public abstract void setDefaultSettings();

    public abstract void loadFromWorldInfo(WorldInfo w);

    public abstract String getString();

    public void postDrawScreen(int i, int j, float f){}

    @Override
    public int getLeft(){
        return width / 2 - 50;
    }

    @Override
    public int getRight(){
        return width / 2 + 220 > width ? width : width / 2 + 220;
    }

    @Override
    public int getTop(){
        return height / 6 - 15;
    }

    @Override
    public int getBottom(){
        return height - 35;
    }

    public boolean canBeScrolled(){
        return scrollingGui.canBeScrolled();
    }

    @Override
    public int getContentHeight(){
        return contentHeight;
    }

    public void mouseMovedOrUp2(int par1, int par2, int par3){
        mouseMovedOrUp(par1, par2, par3);
    }

    protected void handleMouse(int i, int j){
        scrollingGui.drag(i, j);
    }

    @Override
    public void handleMouseInput(){
        scrollingGui.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    public void mouseClicked(int par1, int par2, int par3){
        if (par1 < getLeft() || par1 > getRight() || par2 < getTop() || par2 > getBottom()){
            return;
        }
        super.mouseClicked(par1, par2, par3);
        scrollingGui.mouseClicked(par1, par2, par3);
    }

    @Override
    public void mouseMovedOrUp(int par1, int par2, int par3){
        super.mouseMovedOrUp(par1, par2, par3);
        scrollingGui.mouseMovedOrUp(par1, par2, par3);
    }

    public void calculateMinScrolling(){
        scrollingGui.calculateMinScrolling();
    }

    public void drawScrollingBackground(){
        if (!canBeScrolled()){
            return;
        }
        scrollingGui.drawScrollingBackground();
    }

    public void drawButtons(int i, int j){
        scrollingGui.drawButtons(i, j);
    }

    public void drawFrameAndScrollbar(){
        if (!scrollingGui.canBeScrolled()){
            return;
        }
        scrollingGui.drawFrameAndScrollbar(height);
    }

    @Override
    public void actionPerformedScrolling(GuiButton b){
        updateButtonVisibility();
        scrolled();
        updateButtonText();
        calculateMinScrolling();
    }

    protected final void addButton(GuiButtonOldDays b){
        scrollingGui.buttonList.add(b);
    }

    protected void setY(GuiButton b, int i){
        b.yPosition = height / 6 + scrollingGui.scrolling + i;
        if (i > contentHeight && b.drawButton){
            contentHeight = i;
        }
    }

    protected void drawTooltip(String title, String[] strings, int x, int y){
        drawTooltip(title, new String[][]{strings}, x, y);
    }

    protected void drawTooltip(String title, String[][] strings, int x, int y){
        int margin = 10;
        int length = 2;
        if (title == null){
            return;
        }
        int w = margin;
        int[] widths = new int[strings.length];
        for (int i = 0; i < strings.length; i++){
            String[] str = strings[i];
            if (str.length <= 0 || str[str.length - 1] == null || str[str.length - 1] == ""){
                return;
            }
            int w3 = 0;
            for (int j = 0; j < str.length; j++){
                int width = fontRenderer.getStringWidth(str[j].replace("<- ", "<").replaceAll("(§[0-9a-fk-or]|<-|->)", ""));
                if (widths[i] < width + margin){
                    widths[i] = width + margin;
                }
            }
            w += widths[i];
            if (length < str.length + 2){
                length = str.length + 2;
            }
        }
        int h = (length * 10) + margin;
        drawRect(x - w / 2, y - h / 2 - 1, x + w / 2, y + h / 2 - 1, 0xCC000000);
        int x2 = x - w / 2 + margin;
        drawString(fontRenderer, title, x - fontRenderer.getStringWidth(title.replace("<- ", "<").replaceAll("(§[0-9a-fk-or]|<-|->)", "")) / 2, y - length * 5, 0xffffff);
        for (int i = 0; i < strings.length; i++){
            for (int j = 0; j < strings[i].length; j++){
                String str = strings[i][j].replace("<-", "").replace("->", "");
                int y2 = y + ((j + 2) * 10) - (length * 5);
                drawString(fontRenderer, str, x2, y2, 0xffffff);
            }
            x2 +=widths[i];
        }
    }
}