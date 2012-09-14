package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public abstract class Mod{
    public boolean usesTick;
    public boolean usesGUITick;
    protected boolean canUsePackets;

    public abstract String getModVersion();

    public abstract String getMcVersion();

    public abstract String getModName();

    public abstract void load();

    public void onLoadingSP(String par1Str, String par2Str){}

    public void onLoadingMP(){}

    public void onTick(){}

    public void onGUITick(GuiScreen screen){}

    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, int override){
        return false;
    }

    public void handlePacketFromClient(Packet300Custom packet, EntityPlayerMP player){
        System.out.println("Packet received:");
        System.out.println(" Mod: "+getModName());
        System.out.println(" Direction: Client -> Server");
        System.out.println(" Player: "+player.username);
        System.out.println(" ID: "+packet.getId());
        String[] data = packet.getData();
        System.out.println(" Data: "+data.length+" strings");
        for (int i = 0; i < data.length; i++){
            System.out.println("  "+data[i]);
        }
    }

    public final void handlePacketFromServer2(Packet300Custom packet){
        canUsePackets = true;
        handlePacketFromServer(packet);
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
                handlePacketFromServer2(packet);
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
                    player.serverForThisPlayer.sendPacketToPlayer(packet);
                }
            }else{
                player.serverForThisPlayer.sendPacketToPlayer(packet);
            }
            return;
        }
        if (!canUsePackets){
            return;
        }
        if (handler == null){
            handler = Minecraft.getMinecraft().getSendQueue();
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

    protected void setUseTick(boolean game, boolean gui){
        usesTick = game;
        usesGUITick = gui;
    }
}