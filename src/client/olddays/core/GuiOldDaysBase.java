package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

public class GuiOldDaysBase extends GuiScreen{
    public static String version = "OFF";

    public static final int SCROLLBAR_WIDTH = 6;
    public static final int TOOLTIP_OFFSET = 500;

    protected GuiScreen parent;
    protected boolean displayField;
    protected GuiTextField field;
    protected int tooltipTimer;
    protected int fieldId;
    protected String current;
    protected mod_OldDays core;

    protected int scrolling;
    protected int minScrolling;
    protected int maxScrolling;
    private boolean dragging;
    private boolean scrollbarDragging;
    private int clickY;
    protected int contentHeight;
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

        scrolling = 10;
        clickY = 0;
        minScrolling = 10;
        maxScrolling = 10;
        dragging = false;
        scrollbarDragging = false;
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
            Keyboard.enableRepeatEvents(true);
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
        Keyboard.enableRepeatEvents(hasSearchField);
        calculateMinScrolling();
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
        drawDirtRect(0, width, 0, getTop(), false, 0);
        drawDirtRect(0, width, getBottom(), height, false, 0);
        drawGradientRect(0, getTop(), width, getTop() + 5, 0xff000000, 0x00000000);
        drawGradientRect(0, getBottom() - 5, width, getBottom(), 0x00000000, 0xff000000);
        drawScrollbar();
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
        Keyboard.enableRepeatEvents(b && hasSearchField);
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

    public boolean canBeScrolled(){
        return minScrolling < maxScrolling;
    }

    protected int getContentHeight(){
        return contentHeight - 10;
    }

    @Override
    public void handleMouseInput(){
        if (canBeScrolled() && !mc.gameSettings.touchscreen){
            int l = Mouse.getEventDWheel();
            if (l != 0){
                if (l > 0){
                    l = -1;
                }else if (l < 0){
                    l = 1;
                }
                scrolling -= l * 10;
                if (scrolling < minScrolling){
                    scrolling = minScrolling;
                }
                if (!canBeScrolled() || scrolling > maxScrolling){
                    scrolling = maxScrolling;
                }
                scrolled();
            }
        }
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
        if (par2 < getTop() || par2 > getBottom()){
            return;
        }
        super.mouseClicked(par1, par2, par3);
        if (!canBeScrolled()){
            return;
        }
        dragging = true;
        if (par1 > width - SCROLLBAR_WIDTH){
            scrollbarDragging = true;
        }
        clickY = par2;
    }

    @Override
    public void mouseMovedOrUp(int par1, int par2, int par3){
        super.mouseMovedOrUp(par1, par2, par3);
        if (!canBeScrolled() || !dragging){
            return;
        }
        dragging = false;
        scrollbarDragging = false;
    }

    @Override
    protected void func_85041_a(int i, int j, int k, long l){
        super.func_85041_a(i, j, k, l);
        if (!canBeScrolled() || !dragging){
            return;
        }
        int delta = j - clickY;
        if (delta == 0){
            return;
        }
        if (scrollbarDragging){
            int bottomtop = getBottom() - getTop();
            float scrollMultiplier = -1.0F;
            int cHeight = getContentHeight() + 44;
            int l2 = cHeight - bottomtop + 4;
            if (l2 < 1){
                l2 = 1;
            }
            int k3 = (int)((float)(bottomtop * bottomtop) / (float)cHeight);
            if (k3 < 32){
                k3 = 32;
            }
            if (k3 > bottomtop - 8){
                k3 = bottomtop - 8;
            }
            scrollMultiplier /= (float)(bottomtop - k3) / (float)l2;
            delta *= scrollMultiplier;
        }
        if (scrolling + delta < minScrolling){
            scrolling = minScrolling;
        }else if (delta + scrolling > maxScrolling){
            scrolling = maxScrolling;
        }else{
            scrolling += delta;
        }
        clickY = j;
        scrolled();
    }

    public void calculateMinScrolling(){
        minScrolling = -getContentHeight() - 40 + getBottom() - getTop();
        if (scrolling < minScrolling){
            scrolling = minScrolling;
        }
        if (!canBeScrolled() || scrolling > maxScrolling){
            scrolling = maxScrolling;
        }
        scrolled();
    }

    public void drawScrollbar(){
        int top = getTop();
        int bottom = getBottom();
        int delta = bottom - top;
        int cHeight = getContentHeight() + 44;
        int j3 = cHeight - (delta - 4);
        int right = width;
        int left = right - SCROLLBAR_WIDTH;
        Tessellator tessellator = Tessellator.instance;
        if (j3 > 0){
            int size = (delta * delta) / cHeight;
            if (size < 32){
                size = 32;
            }
            if (size > delta){
                size = delta;
            }
            int k4 = ((-scrolling + maxScrolling) * (delta - size)) / j3 + top;
            if (k4 < top){
                k4 = top;
            }
            if (k4 + size > bottom){
                k4 = bottom - size;
            }
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV(left, bottom, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(right, bottom, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(right, top, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(left, top, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0x808080, 255);
            tessellator.addVertexWithUV(left, k4 + size, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(right, k4 + size, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(right, k4, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(left, k4, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0xc0c0c0, 255);
            tessellator.addVertexWithUV(left, k4 + size - 1, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(right - 1, k4 + size - 1, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(right - 1, k4, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(left, k4, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    private void drawDirtRect(int x1, int x2, int y1, int y2, boolean scrolling, int i)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        mc.renderEngine.bindTexture("/gui/background.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32F;
        tessellator.startDrawingQuads();
        float xOffset = (x1 % 32) / f;
        float yOffset = (y1 % 32) / f;
        tessellator.setColorOpaque_I(scrolling ? 0x202020 : 0x404040);
        tessellator.addVertexWithUV(x1, y2, 0.0D, xOffset, (float)(y2 - y1 - i) / f + yOffset);
        tessellator.addVertexWithUV(x2, y2, 0.0D, (float)(x2 - x1) / f + xOffset, (float)(y2 - y1 - i) / f + yOffset);
        tessellator.addVertexWithUV(x2, y1, 0.0D, (float)(x2 - x1) / f + xOffset, yOffset - i / f);
        tessellator.addVertexWithUV(x1, y1, 0.0D, xOffset, yOffset - i / f);
        tessellator.draw();
    }

    public int getTop(){
        return height / 6 + (hasSearchField ? 7 : -25);
    }

    public int getBottom(){
        return height - 35;
    }

    public void scrolled(){
        for (Object button : buttonList){
            if (!(button instanceof GuiButtonProp)){
                continue;
            }
            ((GuiButtonProp)button).scrolled(canBeScrolled(), scrolling);
        }
        for (GuiOldDaysSeparator s : separators){
            s.scrolled(canBeScrolled(), scrolling);
        }
        if (displayField){
            showField(false, ((GuiButton)buttonList.get(fieldId)));
        }
        //FIXME: Field should scroll too.
    }

    protected void updateList(String str){}
}