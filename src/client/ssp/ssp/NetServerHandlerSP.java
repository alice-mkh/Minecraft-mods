package net.minecraft.src.ssp;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Minecraft;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet0KeepAlive;
import net.minecraft.src.Packet101CloseWindow;
import net.minecraft.src.Packet102WindowClick;
import net.minecraft.src.Packet106Transaction;
import net.minecraft.src.Packet107CreativeSetSlot;
import net.minecraft.src.Packet108EnchantItem;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet130UpdateSign;
import net.minecraft.src.Packet14BlockDig;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.Packet16BlockItemSwitch;
import net.minecraft.src.Packet18Animation;
import net.minecraft.src.Packet19EntityAction;
import net.minecraft.src.Packet202PlayerAbilities;
import net.minecraft.src.Packet203AutoComplete;
import net.minecraft.src.Packet204ClientInfo;
import net.minecraft.src.Packet205ClientCommand;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Packet255KickDisconnect;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.Packet7UseEntity;
import net.minecraft.src.Packet9Respawn;

public class NetServerHandlerSP extends NetServerHandler
{
    private EntityPlayerMP field_72574_e;

    public NetServerHandlerSP(EntityPlayerMP par3EntityPlayerMP)
    {
        super(par3EntityPlayerMP);
        field_72574_e = par3EntityPlayerMP;
        par3EntityPlayerMP.playerNetServerHandler = this;
    }

    @Override
    public EntityPlayerMP getPlayer()
    {
        return field_72574_e;
    }

    @Override
    public void networkTick()
    {
    }

    @Override
    public void kickPlayerFromServer(String par1Str)
    {
    }

    @Override
    public void handleFlying(Packet10Flying par1Packet10Flying)
    {
    }

    @Override
    public void setPlayerLocation(double par1, double par3, double par5, float par7, float par8)
    {
    }

    @Override
    public void handleBlockDig(Packet14BlockDig par1Packet14BlockDig)
    {
    }

    @Override
    public void handlePlace(Packet15Place par1Packet15Place)
    {
    }

    @Override
    public void handleErrorMessage(String par1Str, Object par2ArrayOfObj[])
    {
    }

    @Override
    public void unexpectedPacket(Packet par1Packet)
    {
    }

    @Override
    public void sendPacketToPlayer(Packet par1Packet)
    {
        par1Packet.processPacket(Minecraft.getMinecraft().getNetHandler());
    }

    @Override
    public void handleBlockItemSwitch(Packet16BlockItemSwitch par1Packet16BlockItemSwitch)
    {
    }

    @Override
    public void handleChat(Packet3Chat par1Packet3Chat)
    {
    }

    @Override
    public void handleAnimation(Packet18Animation par1Packet18Animation)
    {
    }

    /**
     * runs registerPacket on the given Packet19EntityAction
     */
    @Override
    public void handleEntityAction(Packet19EntityAction par1Packet19EntityAction)
    {
    }

    @Override
    public void handleKickDisconnect(Packet255KickDisconnect par1Packet255KickDisconnect)
    {
    }

    @Override
    public int packetSize()
    {
        return 1;
    }

    @Override
    public void handleUseEntity(Packet7UseEntity par1Packet7UseEntity)
    {
    }

    @Override
    public void handleClientCommand(Packet205ClientCommand par1Packet205ClientCommand)
    {
    }

    /**
     * respawns the player
     */
    @Override
    public void handleRespawn(Packet9Respawn packet9respawn)
    {
    }

    @Override
    public void handleCloseWindow(Packet101CloseWindow par1Packet101CloseWindow)
    {
        field_72574_e.closeContainer();
    }

    @Override
    public void handleWindowClick(Packet102WindowClick par1Packet102WindowClick)
    {
    }

    @Override
    public void handleEnchantItem(Packet108EnchantItem par1Packet108EnchantItem)
    {
    }

    /**
     * Handle a creative slot packet.
     */
    @Override
    public void handleCreativeSetSlot(Packet107CreativeSetSlot par1Packet107CreativeSetSlot)
    {
    }

    @Override
    public void handleTransaction(Packet106Transaction par1Packet106Transaction)
    {
    }

    /**
     * Updates Client side signs
     */
    @Override
    public void handleUpdateSign(Packet130UpdateSign par1Packet130UpdateSign)
    {
    }

    /**
     * Handle a keep alive packet.
     */
    @Override
    public void handleKeepAlive(Packet0KeepAlive par1Packet0KeepAlive)
    {
    }

    /**
     * determine if it is a server handler
     */
    @Override
    public boolean isServerHandler()
    {
        return true;
    }

    /**
     * Handle a player abilities packet.
     */
    @Override
    public void handlePlayerAbilities(Packet202PlayerAbilities par1Packet202PlayerAbilities)
    {
    }

    @Override
    public void handleAutoComplete(Packet203AutoComplete par1Packet203AutoComplete)
    {
    }

    @Override
    public void handleClientInfo(Packet204ClientInfo par1Packet204ClientInfo)
    {
    }

    @Override
    public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload)
    {
        if (!("MC|BEdit".equals(par1Packet250CustomPayload.channel)) &&
            !("MC|BSign".equals(par1Packet250CustomPayload.channel)) &&
            !("MC|TrSel".equals(par1Packet250CustomPayload.channel)) &&
            !("MC|AdvCdm".equals(par1Packet250CustomPayload.channel)) &&
            !("MC|Beacon".equals(par1Packet250CustomPayload.channel)) &&
            !("MC|ItemName".equals(par1Packet250CustomPayload.channel))){
            Minecraft.invokeModMethod("ModLoader", "serverCustomPayload", new Class[]{NetServerHandler.class, Packet250CustomPayload.class}, this, par1Packet250CustomPayload);
        }
    }
}
