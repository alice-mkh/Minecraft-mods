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
        if (Generator==GEN_BIOMELESS && MapFeatures==FEATURES_ALPHA11201 && MapTheme!=THEME_HELL && MapTheme!=THEME_PARADISE){
            SnowCovered = pmanager.getBooleanProperty("snow-covered", false);
        }else{
            SnowCovered = false;
        }
        if (Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_INDEV || MapFeatures==FEATURES_CLASSIC)){
            IndevMapType = pmanager.getIntProperty("indev-map-type", 1);
            IndevWidthX = pmanager.getIntProperty("indev-width", 256);
            IndevWidthZ = pmanager.getIntProperty("indev-length", 256);
            IndevHeight = pmanager.getIntProperty("indev-height", 64);
        }
        GenerateNewOres = pmanager.getBooleanProperty("generate-new-ores", false);
    }

    public void load(){
        ModLoader.setInGameHook(this, true, true);
    }

    public String getVersion(){
        return "1.2.5";
    }

    public void onTickInGame(MinecraftServer minecraft){
        if (Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_INDEV || MapFeatures==FEATURES_CLASSIC)){
            tickPushing(minecraft);
        }
        return;
    }

    public boolean tickPushing(MinecraftServer minecraft){
        Entity entity;
        for (int k = 0; k < minecraft.getWorldManager(0).loadedEntityList.size(); k++)
        {
            entity = (Entity)minecraft.getWorldManager(0).loadedEntityList.get(k);
            pushBack(entity);
        }
        return true;
    }

    private void pushBack(Entity entity){
        if (MapFeatures==FEATURES_INDEV){
            if (entity.posX>IndevWidthX+8){
                entity.motionX-=(entity.posX-IndevWidthX)/950;
            }
            if (entity.posX<-8){
                entity.motionX-=(entity.posX)/950;
            }
            if (entity.posZ>IndevWidthZ+8){
                entity.motionZ-=(entity.posZ-IndevWidthZ)/950;
            }
            if (entity.posZ<-8){
                entity.motionZ-=(entity.posZ)/950;
            }
        }
        if (MapFeatures==FEATURES_CLASSIC){
            if (entity.posX>IndevWidthX){
                entity.motionX-=0.5;
            }
            if (entity.posX<0){
                entity.motionX+=0.5;
            }
            if (entity.posZ>IndevWidthZ){
                entity.motionZ-=0.5;
            }
            if (entity.posZ<0){
                entity.motionZ+=0.5;
            }
        }
    }

    public void SendGeneratorInfo(EntityPlayerMP entityplayermp)
    {
        int[] dataInt = new int[7];
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
        dataInt[4] = IndevHeight;
        dataInt[5] = IndevWidthX;
        dataInt[6] = IndevWidthZ;
        packet.dataInt = dataInt;
        String[] dataString = new String[1];
        dataString[0] = String.valueOf(ModLoader.getMinecraftServerInstance().getWorldManager(0).getSeed());
        packet.dataString = dataString;
        ModLoaderMp.sendPacketTo(this, entityplayermp, packet);
    }

    public void handlePacket(Packet230ModLoader packet, EntityPlayerMP player)
    {
        switch(packet.packetType){
            case 1:{
                SendGeneratorInfo(player);
            }
        }
    }

    public void handleLogin(EntityPlayerMP entityplayermp)
    {
        SendGeneratorInfo(entityplayermp);
    }

    public boolean hasClientSide()
    {
        return false;
    }

    public static int Generator = 1;
    public static boolean OldSpawners = false;
    public static boolean GenerateNewOres = true;
    public static boolean SnowCovered = false;
    public static boolean ClassicLight=true;
    public static boolean LeavesDecay=true;
    public static boolean RestrictSlimes=false;//Makes slimes not spawn higher than 16 blocks altitude
    public static int MapTheme = 0;
    public static int MapFeatures = 2;
    public static int IndevMapType=0;
    public static int IndevHeight = 64;
    public static int IndevWidthX = 256;
    public static int IndevWidthZ = 256;
    public static int IndevSpawnX;
    public static int IndevSpawnY;
    public static int IndevSpawnZ;
    public static byte[] IndevWorld;
    public static boolean UseNewSpawning;
    public static int DayNight = 2;//0 - none, 1 - old, 2 - new

    public static int GEN_BIOMELESS = 0;
    public static int GEN_OLDBIOMES = 1;
    public static int GEN_NEWBIOMES = 2;

    public static int FEATURES_ALPHA11201 = 0;
    public static int FEATURES_INFDEV0420 = 1;
    public static int FEATURES_INFDEV0227 = 2;
    public static int FEATURES_INDEV = 3;
    public static int FEATURES_CLASSIC = 4;
    public static int FEATURES_INFDEV0608 = 5;

    public static int FEATURES_ALPHA120 = 0;
    public static int FEATURES_BETA12 = 1;
    public static int FEATURES_BETA14 = 2;
    public static int FEATURES_BETA15 = 3;
    public static int FEATURES_BETA173 = 4;
    public static int FEATURES_SKY = 5;
    public static int FEATURES_JUNGLE = 6;

    public static int FEATURES_BETA181 = 0;
    public static int FEATURES_10 = 1;
    public static int FEATURES_11 = 2;
    public static int FEATURES_12 = 3;

    public static int THEME_NORMAL = 0;
    public static int THEME_HELL = 1;
    public static int THEME_WOODS = 2;
    public static int THEME_PARADISE = 3;

    public static int TYPE_INLAND = 0;
    public static int TYPE_ISLAND = 1;
    public static int TYPE_FLOATING = 2;
    public static int TYPE_FLAT = 3;
}