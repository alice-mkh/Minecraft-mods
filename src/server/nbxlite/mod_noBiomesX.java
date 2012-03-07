package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import java.io.*;

public class mod_noBiomesX extends BaseModMp{
    public mod_noBiomesX(){
        PropertyManager pmanager = new PropertyManager(new File("server.properties"));
        Generator = pmanager.getIntProperty("generator", 2);
        MapFeatures = pmanager.getIntProperty("features", 2);
        MapTheme = pmanager.getIntProperty("theme", 0);
        UseNewSpawning = pmanager.getBooleanProperty("new-spawning", false);
        if (Generator==0 && MapFeatures==0 && MapTheme!=1 && MapTheme!=3){
            SnowCovered = pmanager.getBooleanProperty("snow-covered", false);
        }else{
            SnowCovered = false;
        }
    }

    public void load(){}

    public String getVersion(){
        return "1.2.3";
    }

    public void SendGeneratorInfo(EntityPlayerMP entityplayermp)
    {
        int[] dataInt = new int[4];
        dataInt[0] = Generator;
        dataInt[1] = MapFeatures;
        dataInt[2] = MapTheme;
        if (SnowCovered){
            dataInt[3] = 1;
        }else{
            dataInt[3] = 0;
        }
        Packet230ModLoader packet = new Packet230ModLoader();
        packet.packetType = 0;
        packet.dataInt = dataInt;
        ModLoaderMp.SendPacketTo(this, entityplayermp, packet);
    }

    public void HandlePacket(Packet230ModLoader packet, EntityPlayerMP player)
    {
        switch(packet.packetType){
            case 1:{
                SendGeneratorInfo(player);
            }
        }
    }

    public void HandleLogin(EntityPlayerMP entityplayermp)
    {
        SendGeneratorInfo(entityplayermp);
    }

    public boolean hasClientSide()
    {
        return false;
    }

    public static int Generator = 1; //0 - alpha; 1 - halloween/beta; 2 - 1.0
    public static boolean OldSpawners = false;
    public static boolean GenerateLapis = true;
    public static boolean SnowCovered = false;
    public static boolean ClassicLight=true;
    public static boolean LeavesDecay=true;
    public static int MobSpawning=0;
    public static int MapTheme = 0;
    public static int MapFeatures = 2;
    public static boolean UseNewSpawning;
}