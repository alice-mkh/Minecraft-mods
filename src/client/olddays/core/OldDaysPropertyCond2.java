package net.minecraft.src;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class OldDaysPropertyCond2 extends OldDaysPropertyInt{
    public OldDaysPropertyCond2(OldDaysModule m, int i, int v, String f, int m2){
        super(m, i, v, f, -1, m2);
        useNames = true;
    }

    public OldDaysPropertyCond2(OldDaysModule m, int i, int v, int smp, String f, int m2){
        this(m, i, v, f, m2);
        allowedInSMP = false;
        m.isLocal = false;
    }

    public int getValue(){
        if (value == -1){
            try{
                Method method = module.getClass().getMethod(field.getName());
                int i = ((Integer)method.invoke(module));
                return i;
            }catch(Exception ex){
                ex.printStackTrace();
                disable();
                return 0;
            }
        }
        return value;
    }

    @Override
    public void onChange(boolean flag){
        if (shouldSkipUpdates() && flag){
            return;
        }
        try{
            field.set(module, getValue());
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
        if (value == -1){
            return mod_OldDays.lang.get(getName()+".name")+": "+mod_OldDays.lang.get("gui.auto");
        }
        return super.getButtonText();
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
        if (useNames){
            for (int i = 0; i <= max; i++){
                list.add("<- • §a"+mod_OldDays.lang.get(getName()+(i+1))+"§7: "+mod_OldDays.lang.get(getName()+(i+1)+".desc"));
                int num2 = mod_OldDays.getDescriptionNumber(getName()+(i+1)+".desc");
                for (int j = 0; j < num2; j++){
                    list.add("<-    §7"+mod_OldDays.lang.get(getName()+(i+1)+".desc"+(j+1)));
                }
            }
        }else{
            list.add("§7"+mod_OldDays.lang.get("gui.possible")+": §r"+0+"-"+max);
        }
        if (isDisabled()){
            if (num > 0 || useNames){
                list.add("");
            }
            list.add("§4"+mod_OldDays.lang.get("gui.error"+getDisableReason()));
        }
        return list.toArray(new String[list.size()]);
    }
}