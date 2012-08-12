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

    private void sendPacket(boolean dir, int id, EntityPlayerMP player, String... data){
        if (Minecraft.getMinecraftInstance().enableSP){
            return;
        }
        if (dir){
            if (player == null){
                ServerConfigurationManager man = MinecraftServer.func_71276_C().func_71203_ab();
                String[] players = man.func_72369_d();
                for (int i = 0; i < players.length; i++){
                    player = man.func_72361_f(players[i]);
                    player.field_71135_a.func_72567_b(new Packet300Custom(this, dir, id, data));
                }
            }else{
                player.field_71135_a.func_72567_b(new Packet300Custom(this, dir, id, data));
            }
            return;
        }
        Minecraft.getMinecraftInstance().getSendQueue().addToSendQueue(new Packet300Custom(this, dir, id, data));
    }

    public void sendPacketToServer(int id, String... data){
        sendPacket(false, id, null, data);
    }

    public void sendPacketToPlayer(EntityPlayerMP player, int id, String... data){
        sendPacket(true, id, player, data);
    }

    public void sendPacketToAll(int id, String... data){
        sendPacket(true, id, null, data);
    }

    protected void setUseTick(boolean game, boolean gui){
        usesTick = game;
        usesGUITick = gui;
    }
}