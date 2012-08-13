package net.minecraft.src;

public class SMPManager{
    public static final int PACKET_C2S_REQUEST = 0;

    public mod_OldDays core;

    public SMPManager(mod_OldDays c){
        core = c;
    }

    public void requestSettings(int module){
        setSMPSettings(module);
        core.sendPacketToServer(PACKET_C2S_REQUEST, ""+module);
    }

    public void requestSettings(){
        for (int i = 0; i < core.modules.size(); i++){
            setSMPSettings(i);
        }
        core.sendPacketToServer(PACKET_C2S_REQUEST, "all");
    }

    public void setSMPSettings(int id){
        OldDaysModule module = ((OldDaysModule)core.modules.get(id));
        for(int i = 0; i < module.properties.size(); i++){
            OldDaysProperty prop = module.getPropertyById(i + 1);
            if (!prop.allowedInSMP){
                prop.setSMPValue();
                module.last = prop.id;
                prop.onChange();
                core.texman.onTick();
            }
        }
    }
}