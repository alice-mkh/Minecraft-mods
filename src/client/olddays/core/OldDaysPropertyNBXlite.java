package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class OldDaysPropertyNBXlite extends OldDaysPropertyInt{
    public boolean boolValue;
    public boolean smpValue2;

    public OldDaysPropertyNBXlite(OldDaysModule m, int i, int v, String f){
        super(m, i, v, f, 3);
        useNames = true;
        boolValue = getBoolValue(v);
    }

    public OldDaysPropertyNBXlite(OldDaysModule m, int i, int v, boolean smp, String f){
        this(m, i, v, f);
        smpValue2 = smp;
        allowedInSMP = false;
        fromOne = false;
    }

    public boolean getBoolValue(int i){
        if (i == 1){
            return true;
        }
        return i > 0;
    }

    public void onChange(){
        try{
            field.set(module, boolValue);
            module.callback(id);
        }catch(Exception ex){
            System.out.println(ex);
            disable();
        }
    }

    public void setSMPValue(){
        boolValue = smpValue2;
    }

    protected void disable(){
        super.disable();
        boolValue = smpValue2;
    }

    public void incrementValue(){
        super.incrementValue();
        boolValue = getBoolValue(value);
    }

    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+mod_OldDays.lang.get(value==1 ? "NBXlite" : (value==2 ? "gui.on" : "gui.off"));
    }

    public String[] getTooltip(){
        List list = new ArrayList();
        list.add(mod_OldDays.lang.get(getName()+".name"));
        list.add("");
        int num = mod_OldDays.getDescriptionNumber(getName()+".desc");
        for (int i = 0; i < num; i++){
            list.add("§7"+mod_OldDays.lang.get(getName()+".desc"+(i+1)));
        }
        if (useNames){
            for (int i = (fromOne ? 1 : 0); i < (fromOne ? count + 1 : count); i++){
                list.add("<- • §a"+mod_OldDays.lang.get(getName()+(i+1))+"§7: "+mod_OldDays.lang.get(getName()+(i+1)+".desc"));
            }
        }else{
            list.add("§7"+mod_OldDays.lang.get("gui.possible")+": §r"+(fromOne ? "1" : "0")+"-"+(fromOne ? count : count-1));
        }
        if (isDisabled()){
            if (num > 0 || (useNames || count > 0)){
                list.add("");
            }
            list.add("§4"+mod_OldDays.lang.get("gui.error"+getDisableReason()));
        }
        return (String[])list.toArray(new String[list.size()]);
    }
}