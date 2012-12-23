package net.minecraft.src;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class NetClientHandlerSP extends NetClientHandler
{
    public NetServerHandlerSP serverHandler;

    public NetClientHandlerSP(Minecraft par1Minecraft)
    {
        super(par1Minecraft);
        playerInfoList = new ArrayList();
        currentServerMaxPlayers = 1;
//         serverHandler = new NetServerHandlerSP(new EntityPlayerMP(null, par1Minecraft.theWorld, null, null));
    }

    public void func_72547_c()
    {
    }

    /**
     * Processes the packets that have been read since the last call to this function.
     */
    public void processReadPackets()
    {
    }

    public boolean func_72469_b()
    {
        return false;
    }

    public void handleLogin(Packet1Login par1Packet1Login)
    {
        Minecraft.invokeModMethod("ModLoader", "clientConnect", new Class[]{NetClientHandler.class, Packet1Login.class}, this, par1Packet1Login);
    }

    public void handleKickDisconnect(Packet255KickDisconnect par1Packet255KickDisconnect)
    {
        Minecraft.invokeModMethod("ModLoader", "clientDisconnect", new Class[]{});
    }

    public void handleErrorMessage(String par1Str, Object par2ArrayOfObj[])
    {
        Minecraft.invokeModMethod("ModLoader", "clientDisconnect", new Class[]{});
    }

    public void disconnect()
    {
        Minecraft.invokeModMethod("ModLoader", "clientDisconnect", new Class[]{});
    }

    public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload)
    {
        if ("MC|AdvCdm".equals(par1Packet250CustomPayload.channel)){
            Minecraft mc = Minecraft.getMinecraft();
            if (Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed() && mc.thePlayer.capabilities.isCreativeMode)
            {
                try
                {
                    DataInputStream datainputstream3 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                    int j = datainputstream3.readInt();
                    int l = datainputstream3.readInt();
                    int j1 = datainputstream3.readInt();
                    String s1 = Packet.readString(datainputstream3, 256);
                    TileEntity tileentity = mc.theWorld.getBlockTileEntity(j, l, j1);

                    if (tileentity != null && (tileentity instanceof TileEntityCommandBlock))
                    {
                        ((TileEntityCommandBlock)tileentity).setCommand(s1);
                        mc.theWorld.markBlockForUpdate(j, l, j1);
                        mc.thePlayer.sendChatToPlayer((new StringBuilder()).append("Command set: ").append(s1).toString());
                    }
                }
                catch (Exception exception3)
                {
                    exception3.printStackTrace();
                }
            }
            else
            {
                mc.thePlayer.sendChatToPlayer(mc.thePlayer.translateString("advMode.notAllowed", new Object[0]));
            }
            return;
        }
        if (!("MC|TPack".equals(par1Packet250CustomPayload.channel)) &&
            !("MC|TrList".equals(par1Packet250CustomPayload.channel))){
            Minecraft.invokeModMethod("ModLoader", "clientCustomPayload", new Class[]{Packet250CustomPayload.class}, par1Packet250CustomPayload);
        }
    }

    /**
     * determine if it is a server handler
     */
    public boolean isServerHandler()
    {
        return false;
    }

    public INetworkManager func_72548_f()
    {
        return null;
    }

    /**
     * Adds the packet to the send queue
     */
    public void addToSendQueue(Packet par1Packet)
    {
        par1Packet.processPacket(this/*serverHandler*/);
    }

    public void handleAnimation(Packet18Animation par1Packet18Animation)
    {
    }

    public void handleClientCommand(Packet205ClientCommand packet205clientcommand)
    {
        if (packet205clientcommand.forceRespawn == 1)
        {
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(null);
            mc.respawn(mc.theWorld.isRemote, 0, true);
        }
    }
}
