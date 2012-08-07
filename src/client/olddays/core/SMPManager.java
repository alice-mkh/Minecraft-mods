package net.minecraft.src;

public class SMPManager{
    public boolean needSettings;
    public mod_OldDays core;

    public SMPManager(mod_OldDays c){
        core = c;
        needSettings = true;
    }
 
    public void onTick(){
        if (mod_OldDays.getMinecraftInstance().field_71441_e.isRemote == needSettings){
            for (int i = 1; i < core.modules.size(); i++){
                OldDaysModule module = ((OldDaysModule)core.modules.get(i));
                core.saveman.loadModuleProperties(module.id);
                if (mod_OldDays.getMinecraftInstance().field_71441_e.isRemote){
                    setSMPSettings(module.id);
                    requestSettings(module.id);
                }
            }
            needSettings = !needSettings;
        }
    }

    public void requestSettings(int module){
//         Packet230ModLoader packet = new Packet230ModLoader();
//         packet.packetType = 1;
//         packet.dataInt = new int[]{module};
//         ModLoaderMp.sendPacket(core, packet);
    }

    public void setSMPSettings(int id){
        OldDaysModule module = core.getModuleById(id);
        for(int i = 1; i < module.properties.size(); i++){
            OldDaysProperty prop = module.getPropertyById(i);
            if (!prop.allowedInSMP){
                prop.setSMPValue();
                core.sendCallback(id, i);
            }
        }
    }
}