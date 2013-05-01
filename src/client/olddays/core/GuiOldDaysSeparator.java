package net.minecraft.src;

public class GuiOldDaysSeparator extends Gui{
    public OldDaysProperty prop;
    public boolean help;
    public int baseY;
    public int scrolling;
    public String displayString;

    public GuiOldDaysSeparator(int y, String str){
        baseY = y;
        scrolling = 10;
        displayString = str;
    }

    public void scrolled(boolean canScroll, int scrolling){
        this.scrolling = canScroll ? scrolling : 10;
    }

    public void draw(FontRenderer fontrenderer, int w){
        int xPos = w / 2 - 178;
        int yPos = baseY + scrolling;
        int height = 10;
        int width = 356;
        drawRect(xPos, yPos, xPos + width, yPos + height, 0x80000000);
        drawCenteredString(fontrenderer, displayString, xPos + width / 2, yPos + (height - 8) / 2, 0xe0e0e0);
    }
}