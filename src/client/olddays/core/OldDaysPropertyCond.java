package net.minecraft.src;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class OldDaysPropertyCond extends OldDaysPropertyInt{
    public OldDaysPropertyCond(OldDaysModule m, int i, int v, int smp, String f){
        super(m, i, v, smp, f, 2);
        useNames = true;
        smpValue = smp;
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

    @Override
    public void onChange(boolean flag){
        if (shouldSkipUpdates() && flag){
            return;
        }
        try{
            field.set(module, getBoolValue());
            module.callback(id);
        }catch(Exception ex){
            if (flag){
                ex.printStackTrace();
                disable();
            }
        }
    }

    @Override
    public String getButtonText(){
        if (value == 1){
            return mod_OldDays.lang.get(getName()+".name")+": "+mod_OldDays.lang.get("gui.auto");
        }
        return mod_OldDays.lang.get(getName()+".name")+": "+mod_OldDays.lang.get("gui."+(getBoolValue() ? "on" : "off"));
    }

    @Override
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