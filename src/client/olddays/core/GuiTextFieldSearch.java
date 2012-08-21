package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiTextFieldSearch extends Gui{
    protected boolean correct;
    /**
     * Have the font renderer from GuiScreen to render the textbox text into the screen.
     */
    private final FontRenderer fontRenderer;
    private final int xPos;
    private final int yPos;

    /** The width of this text field. */
    private final int width;
    private final int height;

    /** Have the current text beign edited on the textbox. */
    private String text;
    private int maxStringLength;
    private int cursorCounter;
    private boolean enableBackgroundDrawing;

    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    private boolean canLoseFocus;

    /**
     * If this value is true along isEnabled, keyTyped will process the keys.
     */
    private boolean isFocused;

    /**
     * If this value is true along isFocused, keyTyped will process the keys.
     */
    private boolean isEnabled;
    private int field_73816_n;
    private int cursorPosition;

    /** other selection position, maybe the same as the cursor */
    private int selectionEnd;
    private int enabledColor;
    private int disabledColor;
    private boolean field_73823_s;

    public GuiTextFieldSearch(FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5){
        correct = true;
        text = "";
        maxStringLength = 32;
        enableBackgroundDrawing = true;
        canLoseFocus = true;
        isFocused = false;
        isEnabled = true;
        field_73816_n = 0;
        cursorPosition = 0;
        selectionEnd = 0;
        enabledColor = 0xe0e0e0;
        disabledColor = 0x707070;
        field_73823_s = true;
        fontRenderer = par1FontRenderer;
        xPos = par2;
        yPos = par3;
        width = par4;
        height = par5;
    }

    /**
     * Increments the cursor counter
     */
    public void updateCursorCounter()
    {
        cursorCounter++;
    }

    /**
     * Sets the text of the textbox.
     */
    public void setText(String par1Str)
    {
        if (par1Str.length() > maxStringLength)
        {
            text = /*"§7"+"Search"+": "+(correct ? "§r" : "§4")+*/par1Str.substring(0, maxStringLength);
        }
        else
        {
            text = /*"§7"+"Search"+": "+(correct ? "§r" : "§4")+*/par1Str;
        }

        setCursorPositionEnd();
    }

    /**
     * Sets the text of the textbox.
     */
    public void setText2(String par1Str)
    {
        text = /*"§7"+"Search"+": "+(correct ? "§r" : "§4")+*/par1Str;
    }

    /**
     * Returns the text beign edited on the textbox.
     */
    public String getText()
    {
        return text;//.replace("§7", "").replace("Search", "").replace(": ", "").replace("§r", "").replace("§4", "");
    }

    /**
     * @return returns the text between the cursor and selectionEnd
     */
    public String getSelectedtext()
    {
        int i = cursorPosition >= selectionEnd ? selectionEnd : cursorPosition;
        int j = cursorPosition >= selectionEnd ? cursorPosition : selectionEnd;
        return getText().substring(i, j);
    }

    /**
     * replaces selected text, or inserts text at the position on the cursor
     */
    public void writeText(String par1Str)
    {
        String s = "";
        String s1 = ChatAllowedCharacters.filerAllowedCharacters(par1Str);
        int i = cursorPosition >= selectionEnd ? selectionEnd : cursorPosition;
        int j = cursorPosition >= selectionEnd ? cursorPosition : selectionEnd;
        int k = maxStringLength - getText().length() - (i - selectionEnd);
        int l = 0;

        if (getText().length() > 0)
        {
            s = (new StringBuilder()).append(s).append(getText().substring(0, i)).toString();
        }

        if (k < s1.length())
        {
            s = (new StringBuilder()).append(s).append(s1.substring(0, k)).toString();
            l = k;
        }
        else
        {
            s = (new StringBuilder()).append(s).append(s1).toString();
            l = s1.length();
        }

        if (getText().length() > 0 && j < getText().length())
        {
            s = (new StringBuilder()).append(s).append(getText().substring(j)).toString();
        }

        setText2(s);
        func_73784_d((i - selectionEnd) + l);
    }

    public void func_73779_a(int par1)
    {
        if (getText().length() == 0)
        {
            return;
        }

        if (selectionEnd != cursorPosition)
        {
            writeText("");
            return;
        }
        else
        {
            deleteFromCursor(getNthWordFromCursor(par1) - cursorPosition);
            return;
        }
    }

    /**
     * delete the selected text, otherwsie deletes characters from either side of the cursor. params: delete num
     */
    public void deleteFromCursor(int par1)
    {
        if (getText().length() == 0)
        {
            return;
        }

        if (selectionEnd != cursorPosition)
        {
            writeText("");
            return;
        }

        boolean flag = par1 < 0;
        int i = flag ? cursorPosition + par1 : cursorPosition;
        int j = flag ? cursorPosition : cursorPosition + par1;
        String s = "";

        if (i >= 0)
        {
            s = getText().substring(0, i);
        }

        if (j < getText().length())
        {
            s = (new StringBuilder()).append(s).append(getText().substring(j)).toString();
        }

        setText2(s);

        if (flag)
        {
            func_73784_d(par1);
        }
    }

    /**
     * see @getNthNextWordFromPos() params: N, position
     */
    public int getNthWordFromCursor(int par1)
    {
        return getNthWordFromPos(par1, getCursorPosition());
    }

    /**
     * gets the position of the nth word. N may be negative, then it looks backwards. params: N, position
     */
    public int getNthWordFromPos(int par1, int par2)
    {
        return func_73798_a(par1, getCursorPosition(), true);
    }

    public int func_73798_a(int par1, int par2, boolean par3)
    {
        int i = par2;
        boolean flag = par1 < 0;
        int j = Math.abs(par1);

        for (int k = 0; k < j; k++)
        {
            if (flag)
            {
                for (; par3 && i > 0 && getText().charAt(i - 1) == ' '; i--) { }

                for (; i > 0 && getText().charAt(i - 1) != ' '; i--) { }

                continue;
            }

            int l = getText().length();
            i = getText().indexOf(' ', i);

            if (i == -1)
            {
                i = l;
                continue;
            }

            for (; par3 && i < l && getText().charAt(i) == ' '; i++) { }
        }

        return i;
    }

    public void func_73784_d(int par1)
    {
        setCursorPosition(selectionEnd + par1);
    }

    /**
     * sets the position of the cursor to the provided index
     */
    public void setCursorPosition(int par1)
    {
        cursorPosition = par1;
        int i = getText().length();

        if (cursorPosition < 0)
        {
            cursorPosition = 0;
        }

        if (cursorPosition > i)
        {
            cursorPosition = i;
        }

        func_73800_i(cursorPosition);
    }

    /**
     * sets the cursors position to the beginning
     */
    public void setCursorPositionZero()
    {
        setCursorPosition(0);
    }

    /**
     * sets the cursors position to after the text
     */
    public void setCursorPositionEnd()
    {
        setCursorPosition(getText().length());
    }

    /**
     * Call this method from you GuiScreen to process the keys into textbox.
     */
    public boolean textboxKeyTyped(char par1, int par2)
    {
        if (!isEnabled || !isFocused)
        {
            return false;
        }

        switch (par1)
        {
            case 1:
                setCursorPositionEnd();
                func_73800_i(0);
                return true;

            case 3:
                GuiScreen.setClipboardString(getSelectedtext());
                return true;

            case 22:
                writeText(GuiScreen.getClipboardString());
                return true;

            case 24:
                GuiScreen.setClipboardString(getSelectedtext());
                writeText("");
                return true;
        }

        switch (par2)
        {
            case 203:
                if (GuiScreen.isShiftKeyDown())
                {
                    if (GuiScreen.isCtrlKeyDown())
                    {
                        func_73800_i(getNthWordFromPos(-1, getSelectionEnd()));
                    }
                    else
                    {
                        func_73800_i(getSelectionEnd() - 1);
                    }
                }
                else if (GuiScreen.isCtrlKeyDown())
                {
                    setCursorPosition(getNthWordFromCursor(-1));
                }
                else
                {
                    func_73784_d(-1);
                }

                return true;

            case 205:
                if (GuiScreen.isShiftKeyDown())
                {
                    if (GuiScreen.isCtrlKeyDown())
                    {
                        func_73800_i(getNthWordFromPos(1, getSelectionEnd()));
                    }
                    else
                    {
                        func_73800_i(getSelectionEnd() + 1);
                    }
                }
                else if (GuiScreen.isCtrlKeyDown())
                {
                    setCursorPosition(getNthWordFromCursor(1));
                }
                else
                {
                    func_73784_d(1);
                }

                return true;

            case 14:
                if (GuiScreen.isCtrlKeyDown())
                {
                    func_73779_a(-1);
                }
                else
                {
                    deleteFromCursor(-1);
                }

                return true;

            case 211:
                if (GuiScreen.isCtrlKeyDown())
                {
                    func_73779_a(1);
                }
                else
                {
                    deleteFromCursor(1);
                }

                return true;

            case 199:
                if (GuiScreen.isShiftKeyDown())
                {
                    func_73800_i(0);
                }
                else
                {
                    setCursorPositionZero();
                }

                return true;

            case 207:
                if (GuiScreen.isShiftKeyDown())
                {
                    func_73800_i(getText().length());
                }
                else
                {
                    setCursorPositionEnd();
                }

                return true;
        }

        if (ChatAllowedCharacters.isAllowedCharacter(par1))
        {
            writeText(Character.toString(par1));
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Args: x, y, buttonClicked
     */
    public void mouseClicked(int par1, int par2, int par3)
    {
        boolean flag = par1 >= xPos && par1 < xPos + width && par2 >= yPos && par2 < yPos + height;

        if (canLoseFocus)
        {
            setFocused(isEnabled && flag);
        }

        if (isFocused && par3 == 0)
        {
            int i = par1 - xPos;

            if (enableBackgroundDrawing)
            {
                i -= 4;
            }

            String s = fontRenderer.trimStringToWidth(getText().substring(field_73816_n), getWidth());
            setCursorPosition(fontRenderer.trimStringToWidth(s, i).length() + field_73816_n);
        }
    }

    /**
     * Draws the textbox
     */
    public void drawTextBox()
    {
        if (!func_73778_q())
        {
            return;
        }

        if (getEnableBackgroundDrawing())
        {
            drawRect(xPos - 2, yPos - 2, xPos + width + 2, yPos + height + 2, 0xff000000);
            drawRect(xPos - 1, yPos - 1, xPos + width + 1, yPos + height + 1, 0xffa0a0a0);
            drawRect(xPos, yPos, xPos + width, yPos + height, 0xff000000);
        }

        int i = correct ? (isEnabled ? enabledColor : disabledColor) : (isEnabled ? 0xffff0000 : 0xff990000);
        int j = cursorPosition - field_73816_n;
        int k = selectionEnd - field_73816_n;
        String s = fontRenderer.trimStringToWidth(text.substring(field_73816_n), getWidth());
        boolean flag = j >= 0 && j <= s.length();
        boolean flag1 = isFocused && (cursorCounter / 6) % 2 == 0 && flag;
        int l = enableBackgroundDrawing ? xPos + 4 : xPos;
        int i1 = enableBackgroundDrawing ? yPos + (height - 8) / 2 : yPos;
        int j1 = l;

        if (k > s.length())
        {
            k = s.length();
        }

        if (s.length() > 0)
        {
            String s1 = flag ? s.substring(0, j) : s;
            j1 = fontRenderer.drawStringWithShadow(s1, j1, i1, i);
        }

        boolean flag2 = cursorPosition < getText().length() || getText().length() >= getMaxStringLength();
        int k1 = j1;

        if (!flag)
        {
            k1 = j <= 0 ? l : l + width;
        }
        else if (flag2)
        {
            k1--;
            j1--;
        }

        if (s.length() > 0 && flag && j < s.length())
        {
            j1 = fontRenderer.drawStringWithShadow(s.substring(j), j1, i1, i);
        }

        if (flag1)
        {
            if (flag2)
            {
                Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + fontRenderer.FONT_HEIGHT, 0xffd0d0d0);
            }
            else
            {
                fontRenderer.drawStringWithShadow("_", k1, i1, i);
            }
        }

        if (k != j)
        {
            int l1 = l + fontRenderer.getStringWidth(s.substring(0, k));
            drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + fontRenderer.FONT_HEIGHT);
        }
    }

    /**
     * draws the vertical line cursor in the textbox
     */
    private void drawCursorVertical(int par1, int par2, int par3, int par4)
    {
        if (par1 < par3)
        {
            int i = par1;
            par1 = par3;
            par3 = i;
        }

        if (par2 < par4)
        {
            int j = par2;
            par2 = par4;
            par4 = j;
        }

        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(0.0F, 0.0F, 255F, 255F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_OR_REVERSE);
        tessellator.startDrawingQuads();
        tessellator.addVertex(par1, par4, 0.0D);
        tessellator.addVertex(par3, par4, 0.0D);
        tessellator.addVertex(par3, par2, 0.0D);
        tessellator.addVertex(par1, par2, 0.0D);
        tessellator.draw();
        GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void setMaxStringLength(int par1)
    {
        maxStringLength = par1;

        if (getText().length() > par1)
        {
            setText2(getText().substring(0, par1));
        }
    }

    /**
     * returns the maximum number of character that can be contained in this textbox
     */
    public int getMaxStringLength()
    {
        return maxStringLength;
    }

    /**
     * returns the current position of the cursor
     */
    public int getCursorPosition()
    {
        return cursorPosition;
    }

    /**
     * get enable drawing background and outline
     */
    public boolean getEnableBackgroundDrawing()
    {
        return enableBackgroundDrawing;
    }

    /**
     * enable drawing background and outline
     */
    public void setEnableBackgroundDrawing(boolean par1)
    {
        enableBackgroundDrawing = par1;
    }

    public void func_73794_g(int par1)
    {
        enabledColor = par1;
    }

    /**
     * setter for the focused field
     */
    public void setFocused(boolean par1)
    {
        if (par1 && !isFocused)
        {
            cursorCounter = 0;
        }

        isFocused = par1;
    }

    /**
     * getter for the focused field
     */
    public boolean isFocused()
    {
        return isFocused;
    }

    /**
     * the side of the selection that is not the cursor, maye be the same as the cursor
     */
    public int getSelectionEnd()
    {
        return selectionEnd;
    }

    /**
     * returns the width of the textbox depending on if the the box is enabled
     */
    public int getWidth()
    {
        return getEnableBackgroundDrawing() ? width - 8 : width;
    }

    public void func_73800_i(int par1)
    {
        int i = text.length();

        if (par1 > i)
        {
            par1 = i;
        }

        if (par1 < 0)
        {
            par1 = 0;
        }

        selectionEnd = par1;

        if (fontRenderer != null)
        {
            if (field_73816_n > i)
            {
                field_73816_n = i;
            }

            int j = getWidth();
            String s = fontRenderer.trimStringToWidth(text.substring(field_73816_n), j);
            int k = s.length() + field_73816_n;

            if (par1 == field_73816_n)
            {
                field_73816_n -= fontRenderer.trimStringToWidth(text, j, true).length();
            }

            if (par1 > k)
            {
                field_73816_n += par1 - k;
            }
            else if (par1 <= field_73816_n)
            {
                field_73816_n -= field_73816_n - par1;
            }

            if (field_73816_n < 0)
            {
                field_73816_n = 0;
            }

            if (field_73816_n > i)
            {
                field_73816_n = i;
            }
        }
    }

    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    public void setCanLoseFocus(boolean par1)
    {
        canLoseFocus = par1;
    }

    public boolean func_73778_q()
    {
        return field_73823_s;
    }

    public void func_73790_e(boolean par1)
    {
        field_73823_s = par1;
    }
}