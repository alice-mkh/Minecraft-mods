package net.minecraft.src;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class NetServerHandlerSP extends NetServerHandler
{
    private EntityPlayerMP field_72574_e;

    public NetServerHandlerSP(EntityPlayerMP par3EntityPlayerMP)
    {
        super(par3EntityPlayerMP);
        field_72574_e = par3EntityPlayerMP;
        par3EntityPlayerMP.playerNetServerHandler = this;
    }

    public EntityPlayerMP getPlayer()
    {
        return field_72574_e;
    }

    public void func_72570_d()
    {
    }

    public void func_72565_c(String par1Str)
    {
    }

    public void handleFlying(Packet10Flying par1Packet10Flying)
    {
    }

    public void func_72569_a(double par1, double par3, double par5, float par7, float par8)
    {
    }

    public void handleBlockDig(Packet14BlockDig par1Packet14BlockDig)
    {
    }

    public void handlePlace(Packet15Place par1Packet15Place)
    {
    }

    public void handleErrorMessage(String par1Str, Object par2ArrayOfObj[])
    {
    }

    public void unexpectedPacket(Packet par1Packet)
    {
    }

    public void sendPacketToPlayer(Packet par1Packet)
    {
        par1Packet.processPacket(net.minecraft.client.Minecraft.getMinecraft().getSendQueue());
    }

    public void handleBlockItemSwitch(Packet16BlockItemSwitch par1Packet16BlockItemSwitch)
    {
    }

    public void handleChat(Packet3Chat par1Packet3Chat)
    {
    }

    public void handleAnimation(Packet18Animation par1Packet18Animation)
    {
    }

    /**
     * runs registerPacket on the given Packet19EntityAction
     */
    public void handleEntityAction(Packet19EntityAction par1Packet19EntityAction)
    {
    }

    public void handleKickDisconnect(Packet255KickDisconnect par1Packet255KickDisconnect)
    {
    }

    public int func_72568_e()
    {
        return 1;
    }

    public void handleUseEntity(Packet7UseEntity par1Packet7UseEntity)
    {
    }

    public void func_72458_a(Packet205ClientCommand par1Packet205ClientCommand)
    {
    }

    public boolean func_72469_b()
    {
        return true;
    }

    /**
     * respawns the player
     */
    public void handleRespawn(Packet9Respawn packet9respawn)
    {
    }

    public void handleCloseWindow(Packet101CloseWindow par1Packet101CloseWindow)
    {
        field_72574_e.closeInventory();
    }

    public void handleWindowClick(Packet102WindowClick par1Packet102WindowClick)
    {
    }

    public void handleEnchantItem(Packet108EnchantItem par1Packet108EnchantItem)
    {
    }

    /**
     * Handle a creative slot packet.
     */
    public void handleCreativeSetSlot(Packet107CreativeSetSlot par1Packet107CreativeSetSlot)
    {
    }

    public void handleTransaction(Packet106Transaction par1Packet106Transaction)
    {
    }

    /**
     * Updates Client side signs
     */
    public void handleUpdateSign(Packet130UpdateSign par1Packet130UpdateSign)
    {
    }

    /**
     * Handle a keep alive packet.
     */
    public void handleKeepAlive(Packet0KeepAlive par1Packet0KeepAlive)
    {
    }

    /**
     * determine if it is a server handler
     */
    public boolean isServerHandler()
    {
        return true;
    }

    /**
     * Handle a player abilities packet.
     */
    public void handlePlayerAbilities(Packet202PlayerAbilities par1Packet202PlayerAbilities)
    {
    }

    public void func_72461_a(Packet203AutoComplete par1Packet203AutoComplete)
    {
    }

    public void func_72504_a(Packet204ClientInfo par1Packet204ClientInfo)
    {
    }

    public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload)
    {
        if (!("MC|TPack".equals(par1Packet250CustomPayload.channel)) &&
            !("MC|TrList".equals(par1Packet250CustomPayload.channel)) && 
            !("MC|BSign".equals(par1Packet250CustomPayload.channel))){
            net.minecraft.client.Minecraft.invokeModMethod("ModLoader", "serverCustomPayload", new Class[]{NetServerHandler.class, Packet250CustomPayload.class}, this, par1Packet250CustomPayload);
        }
    }
}
