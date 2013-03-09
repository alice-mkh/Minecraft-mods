package net.minecraft.src;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class OldDaysPropertyCond extends OldDaysPropertyInt{
    public OldDaysPropertyCond(OldDaysModule m, int i, int v, String f){
        super(m, i, v, f, 2);
        useNames = true;
    }

    public OldDaysPropertyCond(OldDaysModule m, int i, int v, int smp, String f){
        this(m, i, v, f);
        allowedInSMP = false;
        m.isLocal = false;
    }

    public boolean getBoolValue(){
        if (value == 1){
            try{
                Method method = module.getClass().getMethod(field.getName());
                boolean b = ((Boolean)method.invoke(module));
                return b;
            }catch(Exception ex){
                ex.printStackTrace();
                return false;
            }
        }
        return value > 0;
    }

    public void onChange(){
        if (shouldSkipUpdates()){
            return;
        }
        try{
            field.set(module, getBoolValue());
            module.callback(id);
        }catch(Exception ex){
            ex.printStackTrace();
            disable();
        }
    }

    public String getButtonText(){
        if (value == 1){
            return mod_OldDays.lang.get(getName()+".name")+": "+mod_OldDays.lang.get("gui.auto");
        }
        return mod_OldDays.lang.get(getName()+".name")+": "+mod_OldDays.lang.get("gui."+(getBoolValue() ? "on" : "off"));
    }

    public String[] getTooltip(){
        ArrayList<String> list = new ArrayList<String>();
        list.add(mod_OldDays.lang.get(getName()+".name"));
        list.add("");
        int num = mod_OldDays.getDescriptionNumber(getName()+".desc");
        for (int i = 0; i < num; i++){
            list.add("§7"+mod_OldDays.lang.get(getName()+".desc"+(i+1)));
        }
        if (isDisabled()){
            if (num > 0){
                list.add("");
            }
            list.add("§4"+mod_OldDays.lang.get("gui.error"+getDisableReason()));
        }
        return list.toArray(new String[list.size()]);
    }
}