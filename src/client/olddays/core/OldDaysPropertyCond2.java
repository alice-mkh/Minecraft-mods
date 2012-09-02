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
    }

    public int getValue(){
        if (value == -1){
            try{
                Method method = module.getClass().getMethod(field.getName());
                int i = ((Integer)method.invoke(module));
                return i;
            }catch(Exception ex){
                System.out.println(ex);
                return 0;
            }
        }
        return value;
    }

    public void onChange(){
        try{
            field.set(module, getValue());
            module.callback(id);
        }catch(Exception ex){
            System.out.println(ex);
            disable();
        }
    }

    public String getButtonText(){
        if (value == -1){
            return mod_OldDays.lang.get(getName()+".name")+": "+mod_OldDays.lang.get("gui.auto");
        }
        return super.getButtonText();
    }
}