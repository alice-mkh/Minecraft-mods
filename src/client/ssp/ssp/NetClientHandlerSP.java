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
//         serverHandler = new NetServerHandlerSP(new EntityPlayerMP(null, par1Minecraft.field_71441_e, null, null));
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
        if (!("MC|TPack".equals(par1Packet250CustomPayload.channel)) &&
            !("MC|TrList".equals(par1Packet250CustomPayload.channel)) && 
            !("MC|BSign".equals(par1Packet250CustomPayload.channel))){
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

    public NetworkManager func_72548_f()
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
}
