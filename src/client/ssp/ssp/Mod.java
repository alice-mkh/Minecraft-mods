package net.minecraft.src.ssp;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.Minecraft;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.ServerConfigurationManager;

public abstract class Mod{
    public boolean usesTick;
    public boolean usesGUITick;
    public boolean canUsePackets;

    public abstract String getModVersion();

    public abstract String getMcVersion();

    public abstract String getModName();

    public abstract void load();

    public void onLoadingSP(String par1Str, String par2Str){}

    public void onLoadingMP(){}

    public void onTick(boolean worldExists){}

    public void onGUITick(GuiScreen screen){}

    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, Icon override){
        return false;
    }

    public void handlePacketFromClient(Packet300Custom packet, EntityPlayerMP player){
        System.out.println("Packet received:");
        System.out.println(" Mod: "+getModName());
        System.out.println(" Direction: Client -> Server");
        System.out.println(" Player: "+player.getEntityName());
        System.out.println(" ID: "+packet.getId());
        String[] data = packet.getData();
        System.out.println(" Data: "+data.length+" strings");
        for (int i = 0; i < data.length; i++){
            System.out.println("  "+data[i]);
        }
    }

    public void handlePacketFromServer(Packet300Custom packet){
        System.out.println("Packet received:");
        System.out.println(" Mod: "+getModName());
        System.out.println(" Direction: Server -> Client");
        System.out.println(" ID: "+packet.getId());
        String[] data = packet.getData();
        System.out.println(" Data: "+data.length+" strings");
        for (int i = 0; i < data.length; i++){
            System.out.println("  "+data[i]);
        }
    }

    private void sendPacket(boolean dir, int id, EntityPlayerMP player, NetClientHandler handler, String... data){
        Packet300Custom packet = new Packet300Custom(this, dir, id, data);
        if (Minecraft.getMinecraft().enableSP){
            if (dir){
                handlePacketFromServer(packet);
            }else{
                handlePacketFromClient(packet, player);
            }
            return;
        }
        if (dir){
            if (player == null){
                ServerConfigurationManager man = MinecraftServer.getServer().getConfigurationManager();
                String[] players = man.getAllUsernames();
                for (int i = 0; i < players.length; i++){
                    player = man.getPlayerForUsername(players[i]);
                    player.playerNetServerHandler.sendPacketToPlayer(packet);
                }
            }else{
                player.playerNetServerHandler.sendPacketToPlayer(packet);
            }
            return;
        }
        if (!canUsePackets){
            return;
        }
        if (handler == null){
            handler = Minecraft.getMinecraft().getNetHandler();
        }
        handler.addToSendQueue(packet);
    }

    public void sendPacketToServer(int id, String... data){
        sendPacket(false, id, null, null, data);
    }

    public void sendPacketToServer(int id, NetClientHandler handler, String... data){
        sendPacket(false, id, null, handler, data);
    }

    public void sendPacketToPlayer(EntityPlayerMP player, int id, String... data){
        sendPacket(true, id, player, null, data);
    }

    public void sendPacketToAll(int id, String... data){
        sendPacket(true, id, null, null, data);
    }

    public void onLoginClient(){}

    public void onLoginServer(EntityPlayerMP player){}

    public void onInitClient(){}

    public void refreshTextures(){}

    public void updateTextures(){}

    public void onInitPlayer(EntityClientPlayerMP player, GameSettings settings){}

    protected void setUseTick(boolean game, boolean gui){
        usesTick = game;
        usesGUITick = gui;
    }

    public void addSPCommands(ClientCommandManager manager){}
}