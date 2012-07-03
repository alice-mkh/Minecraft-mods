package net.minecraft.src;

public class OldDaysPropertyBool extends OldDaysProperty{
    public boolean value;
    public boolean smpValue;

    public OldDaysPropertyBool(OldDaysModule m, int i, String s, boolean b, String f){
        super(m, i, s, TYPE_BOOLEAN, f);
        value = b;
    }

    public OldDaysPropertyBool(OldDaysModule m, int i, String s, boolean b, boolean smp, String f){
        this(m, i, s, b, f);
        smpValue = smp;
        allowedInSMP = false;
    }

    public String getButtonText(){
        return name+": "+(value ? "ON" : "OFF");
    }

    public void onChange(){
        super.onChange();
        try{
            field.set(module.getClass(), value);
            module.callback(id);
        }catch(Exception ex){
            disable();
        }
    }

    public void incrementValue(){
        value = !value;
    }
}