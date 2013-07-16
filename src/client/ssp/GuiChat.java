package net.minecraft.src;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.minecraft.src.ssp.EntityPlayerSP2;

public class GuiChat extends GuiScreen
{
    private String field_73898_b;

    /**
     * keeps position of which chat message you will select when you press up, (does not increase for duplicated
     * messages sent immediately after each other)
     */
    private int sentHistoryCursor;
    private boolean field_73897_d;
    private boolean field_73905_m;
    private int field_73903_n;
    private List field_73904_o;

    /** used to pass around the URI to various dialogues and to the host os */
    private URI clickedURI;

    /** Chat entry field */
    protected GuiTextField inputField;

    /**
     * is the text that appears when you press the chat key and the input box appears pre-filled
     */
    private String defaultInputFieldText;

    public GuiChat()
    {
        field_73898_b = "";
        sentHistoryCursor = -1;
        field_73904_o = new ArrayList();
        defaultInputFieldText = "";
    }

    public GuiChat(String par1Str)
    {
        field_73898_b = "";
        sentHistoryCursor = -1;
        field_73904_o = new ArrayList();
        defaultInputFieldText = "";
        defaultInputFieldText = par1Str;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        sentHistoryCursor = mc.ingameGUI.getChatGUI().getSentMessages().size();
        inputField = new GuiTextField(fontRenderer, 4, height - 12, width - 4, 12);
        inputField.setMaxStringLength(100);
        inputField.setEnableBackgroundDrawing(false);
        inputField.setFocused(true);
        inputField.setText(defaultInputFieldText);
        inputField.setCanLoseFocus(false);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        mc.ingameGUI.getChatGUI().resetScroll();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        inputField.updateCursorCounter();
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        field_73905_m = false;

        if (par2 == 15)
        {
            completePlayerName();
        }
        else
        {
            field_73897_d = false;
        }

        if (par2 == 1)
        {
            mc.displayGuiScreen(null);
        }
        else if (par2 == 28 || par2 == 156)
        {
            String s = inputField.getText().trim();

            if (s.length() > 0)
            {
                mc.ingameGUI.getChatGUI().addToSentMessages(s);

                if (!mc.handleClientCommand(s))
                {
                    mc.thePlayer.sendChatMessage(s);
                }
            }

            mc.displayGuiScreen(null);
        }
        else if (par2 == 200)
        {
            getSentHistory(-1);
        }
        else if (par2 == 208)
        {
            getSentHistory(1);
        }
        else if (par2 == 201)
        {
            mc.ingameGUI.getChatGUI().scroll(mc.ingameGUI.getChatGUI().func_96127_i() - 1);
        }
        else if (par2 == 209)
        {
            mc.ingameGUI.getChatGUI().scroll(-mc.ingameGUI.getChatGUI().func_96127_i() + 1);
        }
        else
        {
            inputField.textboxKeyTyped(par1, par2);
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            if (i > 1)
            {
                i = 1;
            }

            if (i < -1)
            {
                i = -1;
            }

            if (!isShiftKeyDown())
            {
                i *= 7;
            }

            mc.ingameGUI.getChatGUI().scroll(i);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0 && mc.gameSettings.chatLinks)
        {
            ChatClickData chatclickdata = mc.ingameGUI.getChatGUI().func_73766_a(Mouse.getX(), Mouse.getY());

            if (chatclickdata != null)
            {
                URI uri = chatclickdata.getURI();

                if (uri != null)
                {
                    if (mc.gameSettings.chatLinksPrompt)
                    {
                        clickedURI = uri;
                        mc.displayGuiScreen(new GuiConfirmOpenLink(this, chatclickdata.getClickedUrl(), 0, false));
                    }
                    else
                    {
                        func_73896_a(uri);
                    }

                    return;
                }
            }
        }

        inputField.mouseClicked(par1, par2, par3);
        super.mouseClicked(par1, par2, par3);
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (par2 == 0)
        {
            if (par1)
            {
                func_73896_a(clickedURI);
            }

            clickedURI = null;
            mc.displayGuiScreen(this);
        }
    }

    private void func_73896_a(URI par1URI)
    {
        try
        {
            Class class1 = Class.forName("java.awt.Desktop");
            Object obj = class1.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            class1.getMethod("browse", new Class[]
                    {
                        java.net.URI.class
                    }).invoke(obj, new Object[]
                            {
                                par1URI
                            });
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    /**
     * Autocompletes player name
     */
    public void completePlayerName()
    {
        if (field_73897_d)
        {
            inputField.deleteFromCursor(inputField.func_73798_a(-1, inputField.getCursorPosition(), false) - inputField.getCursorPosition());

            if (field_73903_n >= field_73904_o.size())
            {
                field_73903_n = 0;
            }
        }
        else
        {
            int i = inputField.func_73798_a(-1, inputField.getCursorPosition(), false);
            field_73904_o.clear();
            field_73903_n = 0;
            String s = inputField.getText().substring(i).toLowerCase();
            String s1 = inputField.getText().substring(0, inputField.getCursorPosition());
            func_73893_a(s1, s);

            if (field_73904_o.isEmpty())
            {
                return;
            }

            field_73897_d = true;
            inputField.deleteFromCursor(i - inputField.getCursorPosition());
        }

        if (field_73904_o.size() > 1)
        {
            StringBuilder stringbuilder = new StringBuilder();
            String s2;

            for (Iterator iterator = field_73904_o.iterator(); iterator.hasNext(); stringbuilder.append(s2))
            {
                s2 = (String)iterator.next();

                if (stringbuilder.length() > 0)
                {
                    stringbuilder.append(", ");
                }
            }

            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(stringbuilder.toString(), 1);
        }

        inputField.writeText((String)field_73904_o.get(field_73903_n++));
    }

    private void func_73893_a(String par1Str, String par2Str)
    {
        if (par1Str.length() < 1)
        {
            return;
        }
        else if (!mc.enableSP)
        {
            mc.thePlayer.sendQueue.addToSendQueue(new Packet203AutoComplete(par1Str));
            field_73905_m = true;
            return;
        }

        if (mc.thePlayer instanceof EntityClientPlayerMP && !(mc.thePlayer instanceof EntityPlayerSP2))
        {
            EntityClientPlayerMP entityclientplayermp = (EntityClientPlayerMP)mc.thePlayer;
            entityclientplayermp.sendQueue.addToSendQueue(new Packet203AutoComplete(par1Str));
            field_73905_m = true;
        }
        else if (par1Str.startsWith("/"))
        {
            par1Str = par1Str.substring(1);
            List list = mc.getIntegratedServer().getCommandManager().getPossibleCommands(mc.thePlayer, par1Str);
            if (list != null)
            {
                String s;
                for (Iterator iterator = list.iterator(); iterator.hasNext(); field_73904_o.add(s))
                {
                    s = (String)iterator.next();
                    if (par1Str.length() == par2Str.length() - 1)
                    {
                        s = (new StringBuilder()).append("/").append(s).toString();
                    }
                }
            }
        }
    }

    /**
     * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
     * message from the current cursor position
     */
    public void getSentHistory(int par1)
    {
        int i = sentHistoryCursor + par1;
        int j = mc.ingameGUI.getChatGUI().getSentMessages().size();

        if (i < 0)
        {
            i = 0;
        }

        if (i > j)
        {
            i = j;
        }

        if (i == sentHistoryCursor)
        {
            return;
        }

        if (i == j)
        {
            sentHistoryCursor = j;
            inputField.setText(field_73898_b);
            return;
        }

        if (sentHistoryCursor == j)
        {
            field_73898_b = inputField.getText();
        }

        inputField.setText((String)mc.ingameGUI.getChatGUI().getSentMessages().get(i));
        sentHistoryCursor = i;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawRect(2, height - 14, width - 2, height - 2, 0x80000000);
        inputField.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }

    public void func_73894_a(String par1ArrayOfStr[])
    {
        if (field_73905_m)
        {
            field_73904_o.clear();
            String as[] = par1ArrayOfStr;
            int i = as.length;

            for (int j = 0; j < i; j++)
            {
                String s = as[j];

                if (s.length() > 0)
                {
                    field_73904_o.add(s);
                }
            }

            if (field_73904_o.size() > 0)
            {
                field_73897_d = true;
                completePlayerName();
            }
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return mc.enableSP;
    }
}
