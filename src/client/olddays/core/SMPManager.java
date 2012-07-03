package net.minecraft.src;

public class SMPManager{
    public boolean needSettings;
    public mod_OldDays core;

    public SMPManager(mod_OldDays c){
        core = c;
        needSettings = true;
    }
 
    public void onTick(){
    /*    if (ModLoader.getMinecraftInstance().theWorld.isRemote == needSettings){
            for (int i = 0; i < modules.length; i++){
                if (modules[i]!=null){
                    modulenum = i;
                    loadModuleProperties();
                    if (ModLoader.getMinecraftInstance().theWorld.isRemote){
                        setDefaultSMPSettings(i);
                        requestSettings(i);
                    }
                }
            }
            needSettings = !needSettings;
        }*/
    }

    public void requestSettings(int module){
        Packet230ModLoader packet = new Packet230ModLoader();
        packet.packetType = 1;
        packet.dataInt = new int[]{module};
        ModLoaderMp.sendPacket(core, packet);
    }
}