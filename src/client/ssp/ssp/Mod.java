package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public abstract class Mod{
    public boolean usesTick;
    public boolean usesGUITick;

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

    public void handlePacketFromClient(Packet300Custom packet){
        System.out.println("Packet received:");
        System.out.println(" Mod: "+getModName());
        System.out.println(" Direction: Client -> Server");
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
        if (Minecraft.getMinecraftInstance().enableSP){
            if (dir){
                handlePacketFromServer(packet);
            }else{
                handlePacketFromClient(packet);
            }
            return;
        }
        if (dir){
            if (player == null){
                ServerConfigurationManager man = MinecraftServer.func_71276_C().func_71203_ab();
                String[] players = man.func_72369_d();
                for (int i = 0; i < players.length; i++){
                    player = man.func_72361_f(players[i]);
                    player.field_71135_a.func_72567_b(packet);
                }
            }else{
                player.field_71135_a.func_72567_b(packet);
            }
            return;
        }
        if (handler == null){
            handler = Minecraft.getMinecraftInstance().getSendQueue();
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

    protected void setUseTick(boolean game, boolean gui){
        usesTick = game;
        usesGUITick = gui;
    }
}