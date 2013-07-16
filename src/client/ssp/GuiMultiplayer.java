package net.minecraft.src;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer extends GuiScreen
{
    /** Number of outstanding ThreadPollServers threads */
    private static int threadsPending;

    /** Lock object for use with synchronized() */
    private static Object lock = new Object();

    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    private GuiScreen parentScreen;

    /** Slot container for the server list */
    private GuiSlotServer serverSlotContainer;
    private ServerList internetServerList;

    /** Index of the currently selected server */
    private int selectedServer;
    private GuiButton field_96289_p;

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

    /** Instance of ServerData. */
    private ServerData theServerData;
    private LanServerList localNetworkServerList;
    private ThreadLanServerFind localServerFindThread;

    /** How many ticks this Gui is already opened */
    private int ticksOpened;
    private boolean field_74024_A;
    private List listofLanServers;

    public GuiMultiplayer(GuiScreen par1GuiScreen)
    {
        selectedServer = -1;
        listofLanServers = Collections.emptyList();
        parentScreen = par1GuiScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();

        if (!field_74024_A)
        {
            field_74024_A = true;
            internetServerList = new ServerList(mc);
            internetServerList.loadServerList();
            localNetworkServerList = new LanServerList();

            try
            {
                localServerFindThread = new ThreadLanServerFind(localNetworkServerList);
                localServerFindThread.start();
            }
            catch (Exception exception)
            {
                mc.getLogAgent().logWarning((new StringBuilder()).append("Unable to start LAN server detection: ").append(exception.getMessage()).toString());
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
        buttonList.add(field_96289_p = new GuiButton(7, width / 2 - 154, height - 28, 70, 20, I18n.func_135053_a("selectServer.edit")));
        buttonList.add(buttonDelete = new GuiButton(2, width / 2 - 74, height - 28, 70, 20, I18n.func_135053_a("selectServer.delete")));
        buttonList.add(buttonSelect = new GuiButton(1, width / 2 - 154, height - 52, 100, 20, I18n.func_135053_a("selectServer.select")));
        buttonList.add(new GuiButton(4, width / 2 - 50, height - 52, 100, 20, I18n.func_135053_a("selectServer.direct")));
        buttonList.add(new GuiButton(3, width / 2 + 4 + 50, height - 52, 100, 20, I18n.func_135053_a("selectServer.add")));
        buttonList.add(new GuiButton(8, width / 2 + 4, height - 28, 70, 20, I18n.func_135053_a("selectServer.refresh")));
        buttonList.add(new GuiButton(0, width / 2 + 4 + 76, height - 28, 75, 20, I18n.func_135053_a("gui.cancel")));
        boolean flag = selectedServer >= 0 && selectedServer < serverSlotContainer.getSize();
        buttonSelect.enabled = flag;
        field_96289_p.enabled = flag;
        buttonDelete.enabled = flag;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ticksOpened++;

        if (localNetworkServerList.getWasUpdated())
        {
            listofLanServers = localNetworkServerList.getLanServers();
            localNetworkServerList.setWasNotUpdated();
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);

        if (localServerFindThread != null)
        {
            localServerFindThread.interrupt();
            localServerFindThread = null;
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
            String s = internetServerList.getServerData(selectedServer).serverName;

            if (s != null)
            {
                deleteClicked = true;
                String s1 = I18n.func_135053_a("selectServer.deleteQuestion");
                String s2 = (new StringBuilder()).append("'").append(s).append("' ").append(I18n.func_135053_a("selectServer.deleteWarning")).toString();
                String s3 = I18n.func_135053_a("selectServer.deleteButton");
                String s4 = I18n.func_135053_a("gui.cancel");
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
            mc.displayGuiScreen(new GuiScreenServerList(this, theServerData = new ServerData(I18n.func_135053_a("selectServer.defaultName"), "")));
        }
        else if (par1GuiButton.id == 3)
        {
            addClicked = true;
            mc.displayGuiScreen(new GuiScreenAddServer(this, theServerData = new ServerData(I18n.func_135053_a("selectServer.defaultName"), "")));
        }
        else if (par1GuiButton.id == 7)
        {
            editClicked = true;
            ServerData serverdata = internetServerList.getServerData(selectedServer);
            theServerData = new ServerData(serverdata.serverName, serverdata.serverIP);
            theServerData.setHideAddress(serverdata.isHidingAddress());
            mc.displayGuiScreen(new GuiScreenAddServer(this, theServerData));
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
                internetServerList.removeServerData(par2);
                internetServerList.saveServerList();
                selectedServer = -1;
            }

            mc.displayGuiScreen(this);
        }
        else if (directClicked)
        {
            directClicked = false;

            if (par1)
            {
                connectToServer(theServerData);
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
                internetServerList.addServerData(theServerData);
                internetServerList.saveServerList();
                selectedServer = -1;
            }

            mc.displayGuiScreen(this);
        }
        else if (editClicked)
        {
            editClicked = false;

            if (par1)
            {
                ServerData serverdata = internetServerList.getServerData(selectedServer);
                serverdata.serverName = theServerData.serverName;
                serverdata.serverIP = theServerData.serverIP;
                serverdata.setHideAddress(theServerData.isHidingAddress());
                internetServerList.saveServerList();
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
            mc.gameSettings.hideServerAddress = !mc.gameSettings.hideServerAddress;
            mc.gameSettings.saveOptions();
            return;
        }

        if (isShiftKeyDown() && par2 == 200)
        {
            if (i > 0 && i < internetServerList.countServers())
            {
                internetServerList.swapServers(i, i - 1);
                selectedServer--;

                if (i < internetServerList.countServers() - 1)
                {
                    serverSlotContainer.func_77208_b(-serverSlotContainer.slotHeight);
                }
            }
        }
        else if (isShiftKeyDown() && par2 == 208)
        {
            if ((i >= 0) & (i < internetServerList.countServers() - 1))
            {
                internetServerList.swapServers(i, i + 1);
                selectedServer++;

                if (i > 0)
                {
                    serverSlotContainer.func_77208_b(serverSlotContainer.slotHeight);
                }
            }
        }
        else if (par2 == 28 || par2 == 156)
        {
            actionPerformed((GuiButton)buttonList.get(2));
        }
        else
        {
            super.keyTyped(par1, par2);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        lagTooltip = null;
        drawDefaultBackground();
        serverSlotContainer.drawScreen(par1, par2, par3);
        drawCenteredString(fontRenderer, I18n.func_135053_a("multiplayer.title"), width / 2, 20, 0xffffff);
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
        if (par1 < internetServerList.countServers())
        {
            connectToServer(internetServerList.getServerData(par1));
            return;
        }

        par1 -= internetServerList.countServers();

        if (par1 < listofLanServers.size())
        {
            LanServer lanserver = (LanServer)listofLanServers.get(par1);
            connectToServer(new ServerData(lanserver.getServerMotd(), lanserver.getServerIpPort()));
        }
    }

    private void connectToServer(ServerData par1ServerData)
    {
        mc.enableSP = false;
        mc.displayGuiScreen(new GuiConnecting(this, mc, par1ServerData));
    }

    private static void func_74017_b(ServerData par0ServerData) throws IOException
    {
        ServerAddress serveraddress = ServerAddress.func_78860_a(par0ServerData.serverIP);
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
            Packet254ServerPing packet254serverping = new Packet254ServerPing(74, serveraddress.getIP(), serveraddress.getPort());
            dataoutputstream.writeByte(packet254serverping.getPacketId());
            packet254serverping.writePacketData(dataoutputstream);

            if (datainputstream.read() != 255)
            {
                throw new IOException("Bad message");
            }

            String s = Packet.readString(datainputstream, 256);
            char ac[] = s.toCharArray();

            for (int i = 0; i < ac.length; i++)
            {
                if (ac[i] != '\247' && ac[i] != 0 && ChatAllowedCharacters.allowedCharacters.indexOf(ac[i]) < 0)
                {
                    ac[i] = '?';
                }
            }

            s = new String(ac);

            if (s.startsWith("\247") && s.length() > 1)
            {
                String as[] = s.substring(1).split("\0");

                if (MathHelper.parseIntWithDefault(as[0], 0) == 1)
                {
                    par0ServerData.serverMOTD = as[3];
                    par0ServerData.field_82821_f = MathHelper.parseIntWithDefault(as[1], par0ServerData.field_82821_f);
                    par0ServerData.gameVersion = as[2];
                    int j = MathHelper.parseIntWithDefault(as[4], 0);
                    int l = MathHelper.parseIntWithDefault(as[5], 0);

                    if (j >= 0 && l >= 0)
                    {
                        par0ServerData.populationInfo = (new StringBuilder()).append(EnumChatFormatting.GRAY).append("").append(j).append("").append(EnumChatFormatting.DARK_GRAY).append("/").append(EnumChatFormatting.GRAY).append(l).toString();
                    }
                    else
                    {
                        par0ServerData.populationInfo = (new StringBuilder()).append("").append(EnumChatFormatting.DARK_GRAY).append("???").toString();
                    }
                }
                else
                {
                    par0ServerData.gameVersion = "???";
                    par0ServerData.serverMOTD = (new StringBuilder()).append("").append(EnumChatFormatting.DARK_GRAY).append("???").toString();
                    par0ServerData.field_82821_f = 75;
                    par0ServerData.populationInfo = (new StringBuilder()).append("").append(EnumChatFormatting.DARK_GRAY).append("???").toString();
                }
            }
            else
            {
                String as1[] = s.split("\247");
                s = as1[0];
                int k = -1;
                int i1 = -1;

                try
                {
                    k = Integer.parseInt(as1[1]);
                    i1 = Integer.parseInt(as1[2]);
                }
                catch (Exception exception) { }

                par0ServerData.serverMOTD = (new StringBuilder()).append(EnumChatFormatting.GRAY).append(s).toString();

                if (k >= 0 && i1 > 0)
                {
                    par0ServerData.populationInfo = (new StringBuilder()).append(EnumChatFormatting.GRAY).append("").append(k).append("").append(EnumChatFormatting.DARK_GRAY).append("/").append(EnumChatFormatting.GRAY).append(i1).toString();
                }
                else
                {
                    par0ServerData.populationInfo = (new StringBuilder()).append("").append(EnumChatFormatting.DARK_GRAY).append("???").toString();
                }

                par0ServerData.gameVersion = "1.3";
                par0ServerData.field_82821_f = 73;
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

    static ServerList getInternetServerList(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.internetServerList;
    }

    static List getListOfLanServers(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.listofLanServers;
    }

    static int getSelectedServer(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.selectedServer;
    }

    static int getAndSetSelectedServer(GuiMultiplayer par0GuiMultiplayer, int par1)
    {
        return par0GuiMultiplayer.selectedServer = par1;
    }

    /**
     * Return buttonSelect GuiButton
     */
    static GuiButton getButtonSelect(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.buttonSelect;
    }

    /**
     * Return buttonEdit GuiButton
     */
    static GuiButton getButtonEdit(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.field_96289_p;
    }

    /**
     * Return buttonDelete GuiButton
     */
    static GuiButton getButtonDelete(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.buttonDelete;
    }

    static void func_74008_b(GuiMultiplayer par0GuiMultiplayer, int par1)
    {
        par0GuiMultiplayer.joinServer(par1);
    }

    static int getTicksOpened(GuiMultiplayer par0GuiMultiplayer)
    {
        return par0GuiMultiplayer.ticksOpened;
    }

    /**
     * Returns the lock object for use with synchronized()
     */
    static Object getLock()
    {
        return lock;
    }

    static int getThreadsPending()
    {
        return threadsPending;
    }

    static int increaseThreadsPending()
    {
        return threadsPending++;
    }

    static void func_82291_a(ServerData par0ServerData) throws IOException
    {
        func_74017_b(par0ServerData);
    }

    static int decreaseThreadsPending()
    {
        return threadsPending--;
    }

    static String getAndSetLagTooltip(GuiMultiplayer par0GuiMultiplayer, String par1Str)
    {
        return par0GuiMultiplayer.lagTooltip = par1Str;
    }
}
