package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

public class GuiOldDaysBase extends GuiScreen implements IScrollingGui{
    public static String version = "OFF";

    public static final int TOOLTIP_OFFSET = 500;

    protected GuiScreen parent;
    protected boolean displayField;
    protected GuiTextField field;
    protected int tooltipTimer;
    protected int fieldId;
    protected String current;
    protected mod_OldDays core;
    protected int contentHeight;
    protected GuiScrolling scrollingGui;
    protected boolean restoreList;
    protected ArrayList<GuiOldDaysSeparator> separators;
    protected boolean hasSearchField;
    protected GuiTextFieldSearch searchField;

    public GuiOldDaysBase(GuiScreen guiscreen, mod_OldDays c){
        parent = guiscreen;
        displayField = false;
        fieldId = 0;
        tooltipTimer = 0;
        hasSearchField = false;
        core = c;
        scrollingGui = new GuiScrolling(this);
        restoreList = true;
        separators = new ArrayList<GuiOldDaysSeparator>();
        hasSearchField = false;
    }

    @Override
    public void updateScreen(){
        field.updateCursorCounter();
        if (hasSearchField){
            searchField.updateCursorCounter();
        }
    }

    @Override
    public void onGuiClosed(){
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        GuiButton button = new GuiButton(0, width / 2 - 75, height - 28, 150, 20, stringtranslate.translateKey("menu.returnToGame"));
        buttonList.add(button);
        if (hasSearchField){
            searchField = new GuiTextFieldSearch(fontRenderer, width / 2 - 153, height / 6 - 13, 306, 16);
            searchField.setMaxStringLength(999);
            searchField.setFocused(true);
            searchField.setCanLoseFocus(false);
        }
        updateList("");
    }

    protected GuiButtonProp addButton(int i, boolean b, int j, String name, boolean e){
        int x = width / 2 - 155;
        if (i % 2 != 0){
           x+=160;
        }
        int margin = 30;
        int top = b ? 25 : -5;
        int y = height / 6 - top + ((i/2) * margin);
        y += 10 * separators.size();
        int newContentHeight = (i / 2) * margin + 10 * separators.size();
        if (newContentHeight > contentHeight){
            contentHeight = newContentHeight;
        }
        GuiButtonProp button = new GuiButtonProp(j+1, x, y, false, name);
        button.enabled = e;
        buttonList.add(button);
        return button;
    }

    protected void addButton(int i, boolean b, int j, OldDaysProperty p){
        int offset = 3;
        int x = width / 2 - 155;
        int x2 = x - offset - 20;
        if (i % 2 != 0){
           x+=160;
           x2+=330 + (offset * 2);
        }
        int margin = 30;
        int top = b ? 25 : -5;
        int y = height / 6 - top + ((i/2) * margin);
        y += 10 * separators.size();
        int newContentHeight = (i / 2) * margin + 10 * separators.size();
        if (newContentHeight > contentHeight){
            contentHeight = newContentHeight;
        }
        GuiButtonProp button = new GuiButtonProp(j+1, x, y, p, false);
        buttonList.add(button);
        GuiButton tooltipButton = new GuiButtonProp(j+TOOLTIP_OFFSET+1, x2, y, p, true);
        buttonList.add(tooltipButton);
    }

    protected int addSeparator(int y, boolean b, String str){
        if (y % 2 == 1){
            y++;
        }
        int top = b ? 30 : 0;
        separators.add(new GuiOldDaysSeparator(height / 6 - top + y * 15 + 10 * separators.size(), str));
        return y;
    }

    protected void postInitGui(){
        field = new GuiTextField(fontRenderer, 0, 0, 150, 20);
        field.setMaxStringLength(999);
        Keyboard.enableRepeatEvents(hasSearchField);
        scrollingGui.calculateMinScrolling();
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
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i, j, f);
        for (GuiOldDaysSeparator s : separators){
            s.draw(fontRenderer, width);
        }
        if (displayField){
            field.drawTextBox();
        }
        scrollingGui.drawFrameAndScrollbar(height);
        List tempList = buttonList;
        ArrayList fakeButtonList = new ArrayList();
        for (int k = 0; k < buttonList.size(); k++){
            Object o = buttonList.get(k);
            if (o instanceof GuiButtonProp){
                continue;
            }
            fakeButtonList.add(o);
        }
        buttonList = fakeButtonList;
        super.drawScreen(i, j, f);
        buttonList = tempList;
        if (hasSearchField){
            searchField.drawTextBox();
        }
        String str = version.contains(":") ? version.split(":", 2)[0] : version;
        if (str.equals("OFF")){
            str = "";
        }
        fontRenderer.drawStringWithShadow(str, 2, 2, 0x505050);
    }

    protected void showField(boolean b, GuiButton button){
        displayField = b;
        Keyboard.enableRepeatEvents(b || hasSearchField);
        button.enabled = !b;
        field.setFocused(b);
        if (hasSearchField){
            searchField.setFocused(!b);
            searchField.setCanLoseFocus(b);
        }
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (hasSearchField && searchField.isFocused()){
            searchField.textboxKeyTyped(par1, par2);
            if (par1 == '\r' || par2 == 1 || ((par2 == 211 || par2 == 14) && searchField.getText().length() <= 0)){
                mc.displayGuiScreen(parent);
                return;
            }
            updateList(searchField.getText().trim());
            return;
        }else if (!displayField){
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

    @Override
    public boolean doesGuiPauseGame(){
        return Minecraft.getMinecraft().enableSP;
    }

    protected boolean isShiftPressed(){
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }

    @Override
    public int getContentHeight(){
        return contentHeight - 10;
    }

    @Override
    public void handleMouseInput(){
        scrollingGui.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3){
        List tempList = buttonList;
        ArrayList fakeButtonList = new ArrayList();
        for (int i = 0; i < buttonList.size(); i++){
            Object o = buttonList.get(i);
            if (o instanceof GuiButtonProp){
                continue;
            }
            fakeButtonList.add(o);
        }
        buttonList = fakeButtonList;
        super.mouseClicked(par1, par2, par3);
        if (restoreList){
            buttonList = tempList;
        }
        restoreList = true;
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

    @Override
    protected void func_85041_a(int i, int j, int k, long l){
        super.func_85041_a(i, j, k, l);
        scrollingGui.func_85041_a(i, j, k, l);
    }

    @Override
    public int getTop(){
        return height / 6 + (hasSearchField ? 7 : -25);
    }

    @Override
    public int getBottom(){
        return height - 35;
    }

    @Override
    public int getLeft(){
        return 0;
    }

    @Override
    public int getRight(){
        return width;
    }

    @Override
    public void scrolled(){
        for (Object button : buttonList){
            if (!(button instanceof GuiButtonProp)){
                continue;
            }
            ((GuiButtonProp)button).scrolled(scrollingGui.canBeScrolled(), scrollingGui.scrolling);
        }
        for (GuiOldDaysSeparator s : separators){
            s.scrolled(scrollingGui.canBeScrolled(), scrollingGui.scrolling);
        }
        if (displayField){
            showField(false, ((GuiButton)buttonList.get(fieldId)));
        }
        //FIXME: Field should scroll too.
    }

    protected void updateList(String str){}

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height){
        super.setWorldAndResolution(mc, width, height);
        scrollingGui.mc = mc;
    }
}