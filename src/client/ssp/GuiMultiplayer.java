package net.minecraft.src;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer extends GuiScreen
{
    /** Number of outstanding ThreadPollServers threads */
    private static int threadsPending = 0;

    /** Lock object for use with synchronized() */
    private static Object lock = new Object();

    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    private GuiScreen parentScreen;

    /** Slot container for the server list */
    private GuiSlotServer serverSlotContainer;
    private ServerList field_74030_m;

    /** Index of the currently selected server */
    private int selectedServer;

    /** The 'Edit' button */
    private GuiButton buttonEdit;

    /** The 'Join Server' button */
    private GuiButton buttonSelect;

    /** The 'Delete' button */
    private GuiButton buttonDelete;

    /** The 'Delete' button was clicked */
    private boolean deleteClicked;

    /** The 'Add server' button was clicked */
    private boolean addClicked;

    /** The 'Edit' button was clicked */
    private boolean editClicked;

    /** The 'Direct Connect' button was clicked */
    private boolean directClicked;

    /** This GUI's lag tooltip text or null if no lag icon is being hovered. */
    private String lagTooltip;
    private ServerData field_74031_w;
    private LanServerList field_74041_x;
    private ThreadLanServerFind field_74040_y;
    private int field_74039_z;
    private boolean field_74024_A;
    private List field_74026_B;

    public GuiMultiplayer(GuiScreen par1GuiScreen)
    {
        selectedServer = -1;
        deleteClicked = false;
        addClicked = false;
        editClicked = false;
        directClicked = false;
        lagTooltip = null;
        field_74031_w = null;
        field_74026_B = Collections.emptyList();
        parentScreen = par1GuiScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        controlList.clear();

        if (!field_74024_A)
        {
            field_74024_A = true;
            field_74030_m = new ServerList(mc);
            field_74030_m.loadServerList();
            field_74041_x = new LanServerList();

            try
            {
                field_74040_y = new ThreadLanServerFind(field_74041_x);
                field_74040_y.start();
            }
            catch (Exception exception)
            {
                System.out.println((new StringBuilder()).append("Unable to start LAN server detection: ").append(exception.getMessage()).toString());
            }

            serverSlotContainer = new GuiSlotServer(this);
        }
        else
        {
            serverSlotContainer.func_77207_a(width, height, 32, height - 64);
        }

        initGuiControls();
    }

    /**
     * Populate the GuiScreen controlList
     */
    public void initGuiControls()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(buttonEdit = new GuiButton(7, width / 2 - 154, height - 28, 70, 20, stringtranslate.translateKey("selectServer.edit")));
        controlList.add(buttonDelete = new GuiButton(2, width / 2 - 74, height - 28, 70, 20, stringtranslate.translateKey("selectServer.delete")));
        controlList.add(buttonSelect = new GuiButton(1, width / 2 - 154, height - 52, 100, 20, stringtranslate.translateKey("selectServer.select")));
        controlList.add(new GuiButton(4, width / 2 - 50, height - 52, 100, 20, stringtranslate.translateKey("selectServer.direct")));
        controlList.add(new GuiButton(3, width / 2 + 4 + 50, height - 52, 100, 20, stringtranslate.translateKey("selectServer.add")));
        controlList.add(new GuiButton(8, width / 2 + 4, height - 28, 70, 20, stringtranslate.translateKey("selectServer.refresh")));
        controlList.add(new GuiButton(0, width / 2 + 4 + 76, height - 28, 75, 20, stringtranslate.translateKey("gui.cancel")));
        boolean flag = selectedServer >= 0 && selectedServer < serverSlotContainer.getSize();
        buttonSelect.enabled = flag;
        buttonEdit.enabled = flag;
        buttonDelete.enabled = flag;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        field_74039_z++;

        if (field_74041_x.func_77553_a())
        {
            field_74026_B = field_74041_x.func_77554_c();
            field_74041_x.func_77552_b();
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);

        if (field_74040_y != null)
        {
            field_74040_y.interrupt();
            field_74040_y = null;
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (!par1GuiButton.enabled)
        {
            return;
        }

        if (par1GuiButton.id == 2)
        {
            String s = field_74030_m.getServerData(selectedServer).serverName;

            if (s != null)
            {
                deleteClicked = true;
                StringTranslate stringtranslate = StringTranslate.getInstance();
                String s1 = stringtranslate.translateKey("selectServer.deleteQuestion");
                String s2 = (new StringBuilder()).append("'").append(s).append("' ").append(stringtranslate.translateKey("selectServer.deleteWarning")).toString();
                String s3 = stringtranslate.translateKey("selectServer.deleteButton");
                String s4 = stringtranslate.translateKey("gui.cancel");
                GuiYesNo guiyesno = new GuiYesNo(this, s1, s2, s3, s4, selectedServer);
                mc.displayGuiScreen(guiyesno);
            }
        }
        else if (par1GuiButton.id == 1)
        {
            joinServer(selectedServer);
        }
        else if (par1GuiButton.id == 4)
        {
            directClicked = true;
            mc.displayGuiScreen(new GuiScreenServerList(this, field_74031_w = new ServerData(StatCollector.translateToLocal("selectServer.defaultName"), "")));
        }
        else if (par1GuiButton.id == 3)
        {
            addClicked = true;
            mc.displayGuiScreen(new GuiScreenAddServer(this, field_74031_w = new ServerData(StatCollector.translateToLocal("selectServer.defaultName"), "")));
        }
        else if (par1GuiButton.id == 7)
        {
            editClicked = true;
            ServerData serverdata = field_74030_m.getServerData(selectedServer);
            mc.displayGuiScreen(new GuiScreenAddServer(this, field_74031_w = new ServerData(serverdata.serverName, serverdata.serverIP)));
        }
        else if (par1GuiButton.id == 0)
        {
            mc.displayGuiScreen(parentScreen);
        }
        else if (par1GuiButton.id == 8)
        {
            mc.displayGuiScreen(new GuiMultiplayer(parentScreen));
        }
        else
        {
            serverSlotContainer.actionPerformed(par1GuiButton);
        }
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (deleteClicked)
        {
            deleteClicked = false;

            if (par1)
            {
                field_74030_m.removeServerData(par2);
                field_74030_m.saveServerList();
                selectedServer = -1;
            }

            mc.displayGuiScreen(this);
        }
        else if (directClicked)
        {
            directClicked = false;

            if (par1)
            {
                func_74002_a(field_74031_w);
            }
            else
            {
                mc.displayGuiScreen(this);
            }
        }
        else if (addClicked)
        {
            addClicked = false;

            if (par1)
            {
                field_74030_m.addServerData(field_74031_w);
                field_74030_m.saveServerList();
                selectedServer = -1;
            }

            mc.displayGuiScreen(this);
        }
        else if (editClicked)
        {
            editClicked = false;

            if (par1)
            {
                ServerData serverdata = field_74030_m.getServerData(selectedServer);
                serverdata.serverName = field_74031_w.serverName;
                serverdata.serverIP = field_74031_w.serverIP;
                field_74030_m.saveServerList();
            }

            mc.displayGuiScreen(this);
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        int i = selectedServer;

        if (par2 == 59)
        {
            mc.gameSettings.field_80005_w = !mc.gameSettings.field_80005_w;
            mc.gameSettings.saveOptions();
            return;
        }

        if (isShiftKeyDown() && par2 == 200)
        {
            if (i > 0 && i < field_74030_m.countServers())
            {
                field_74030_m.swapServers(i, i - 1);
                selectedServer--;

                if (i < field_74030_m.countServers() - 1)
                {
                    serverSlotContainer.func_77208_b(-serverSlotContainer.slotHeight);
                }
            }
        }
        else if (isShiftKeyDown() && par2 == 208)
        {
            if (i < field_74030_m.countServers() - 1)
            {
                field_74030_m.swapServers(i, i + 1);
                selectedServer++;

                if (i > 0)
                {
                    serverSlotContainer.func_77208_b(serverSlotContainer.slotHeight);
                }
            }
        }
        else if (par1 == '\r')
        {
            actionPerformed((GuiButton)controlList.get(2));
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        lagTooltip = null;
        StringTranslate stringtranslate = StringTranslate.getInstance();
        drawDefaultBackground();
        serverSlotContainer.drawScreen(par1, par2, par3);
        drawCenteredString(fontRenderer, stringtranslate.translateKey("multiplayer.title"), width / 2, 20, 0xffffff);
        super.drawScreen(par1, par2, par3);

        if (lagTooltip != null)
        {
            func_74007_a(lagTooltip, par1, par2);
        }
    }

    /**
     * Join server by slot index
     */
    private void joinServer(int par1)
    {
        mc.enableSP = false;
        if (par1 < field_74030_m.countServers())
        {
            func_74002_a(field_74030_m.getServerData(par1));
            return;
        }

        par1 -= field_74030_m.countServers();

        if (par1 < field_74026_B.size())
        {
            LanServer lanserver = (LanServer)field_74026_B.get(par1);
            func_74002_a(new ServerData(lanserver.func_77487_a(), lanserver.func_77488_b()));
        }
    }

    private void func_74002_a(ServerData par1ServerData)
    {
        mc.displayGuiScreen(new GuiConnecting(mc, par1ServerData));
    }

    private void func_74017_b(ServerData par1ServerData) throws IOException
    {
        ServerAddress serveraddress = ServerAddress.func_78860_a(par1ServerData.serverIP);
        Socket socket = null;
        DataInputStream datainputstream = null;
        DataOutputStream dataoutputstream = null;

        try
        {
            socket = new Socket();
            socket.setSoTimeout(3000);
            socket.setTcpNoDelay(true);
            socket.setTrafficClass(18);
            socket.connect(new InetSocketAddress(serveraddress.getIP(), serveraddress.getPort()), 3000);
            datainputstream = new DataInputStream(socket.getInputStream());
            dataoutputstream = new DataOutputStream(socket.getOutputStream());
            dataoutputstream.write(254);

            if (datainputstream.read() != 255)
            {
                throw new IOException("Bad message");
            }

            String s = Packet.readString(datainputstream, 256);
            char ac[] = s.toCharArray();

            for (int i = 0; i < ac.length; i++)
            {
                if (ac[i] != '\247' && ChatAllowedCharacters.allowedCharacters.indexOf(ac[i]) < 0)
                {
                    ac[i] = '?';
                }
            }

            s = new String(ac);
            String as[] = s.split("\247");
            s = as[0];
            int j = -1;
            int k = -1;

            try
            {
                j = Integer.parseInt(as[1]);
                k = Integer.parseInt(as[2]);
            }
            catch (Exception exception) { }

            par1ServerData.serverMOTD = (new StringBuilder()).append("\2477").append(s).toString();

            if (j >= 0 && k > 0)
            {
                par1ServerData.field_78846_c = (new StringBuilder()).append("\2477").append(j).append("\2478/\2477").append(k).toString();
            }
            else
            {
                par1ServerData.field_78846_c = "\2478???";
            }
        }
        finally
        {
            try
            {
                if (datainputstream != null)
                {
                    datainputstream.close();
                }
            }
            catch (Throwable throwable) { }

            try
            {
                if (dataoutputstream != null)
                {
                    dataoutputstream.close();
                }
            }
            catch (Throwable throwable1) { }

            try
            {
                if (socket != null)
                {
                    socket.close();
                }
            }
            catch (Throwable throwable2) { }
        }
    }

    protected void func_74007_a(String par1Str, int par2, int par3)
    {
        if (par1Str == null)
        {
            return;
        }
        else
        {
            int i = par2 + 12;
            int j = par3 - 12;
            int k = fontRenderer.getStringWidth(par1Str);
            drawGradientRect(i - 3, j - 3, i + k + 3, j + 8 + 3, 0xc0000000, 0xc0000000);
            fontRenderer.drawStringWithShadow(par1Str, i, j, -1);
            return;
        }
    }

    static ServerList func_74006_a(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.field_74030_m;
    }

    static List func_74003_b(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.field_74026_B;
    }

    static int func_74020_c(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.selectedServer;
    }

    static int func_74015_a(GuiMultiplayer par0GuiMultiplayer, int par1)
    {
        return par0GuiMultiplayer.selectedServer = par1;
    }

    /**
     * Return buttonEdit GuiButton
     */
    static GuiButton getButtonEdit(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.buttonSelect;
    }

    /**
     * Return buttonDelete GuiButton
     */
    static GuiButton getButtonDelete(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.buttonEdit;
    }

    static GuiButton func_74019_f(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.buttonDelete;
    }

    static void func_74008_b(GuiMultiplayer par0GuiMultiplayer, int par1)
    {
        par0GuiMultiplayer.joinServer(par1);
    }

    static int func_74010_g(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.field_74039_z;
    }

    static Object func_74011_h()
    {
        return lock;
    }

    static int func_74012_i()
    {
        return threadsPending;
    }

    static int func_74021_j()
    {
        return threadsPending++;
    }

    static void func_74013_a(GuiMultiplayer par0GuiMultiplayer, ServerData par1ServerData) throws IOException
    {
        par0GuiMultiplayer.func_74017_b(par1ServerData);
    }

    static int func_74018_k()
    {
        return threadsPending--;
    }

    static String func_74009_a(GuiMultiplayer par0GuiMultiplayer, String par1Str)
    {
        return par0GuiMultiplayer.lagTooltip = par1Str;
    }
}
