package net.minecraft.src;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class OldDaysPropertyCond extends OldDaysPropertyInt{
    public boolean boolValue;
    public boolean smpBoolValue;

    public OldDaysPropertyCond(OldDaysModule m, int i, int v, String f){
        super(m, i, v, f, 2);
        useNames = true;
        boolValue = getBoolValue(v);
    }

    public OldDaysPropertyCond(OldDaysModule m, int i, int v, boolean smp, String f){
        this(m, i, v, f);
        smpBoolValue = smp;
        allowedInSMP = false;
    }

    public boolean getBoolValue(int i){
        if (i == 1){
            try{
                Method method = module.getClass().getMethod(field.getName(), new Class[]{});
                boolean b = ((Boolean)method.invoke(module));
                return b;
            }catch(Exception ex){
                System.out.println(ex);
                return false;
            }
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
        super.setSMPValue();
        boolValue = smpBoolValue;
    }

    protected void disable(){
        super.disable();
        boolValue = smpBoolValue;
    }

    public void incrementValue(){
        super.incrementValue();
        boolValue = getBoolValue(value);
    }

    public void updateValue(){
        super.updateValue();
        boolValue = getBoolValue(value);
    }

    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+mod_OldDays.lang.get("gui."+(value==1 ? "auto" : (boolValue ? "on" : "off")));
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