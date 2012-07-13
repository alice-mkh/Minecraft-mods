package net.minecraft.src;

public class OldDaysPropertyString extends OldDaysProperty{
    public String value;
    public String smpValue;

    public OldDaysPropertyString(OldDaysModule m, int i, String s, String str, String f){
        super(m, i, s, TYPE_STRING, f);
        value = str;
        guitype = GUI_TYPE_FIELD;
    }

    public OldDaysPropertyString(OldDaysModule m, int i, String s, String str, String smp, String f){
        this(m, i, s, str, f);
        smpValue = smp;
        allowedInSMP = false;
    }

    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+value;
    }

    public void onChange(){
        try{
            field.set(module, value);
            module.callback(id);
        }catch(Exception ex){
            System.out.println(ex);
            disable();
        }
    }

    public void setSMPValue(){
        value = smpValue;
    }

    protected void disable(){
        super.disable();
        value = smpValue;
    }

    public void loadFromString(String str){
        value = str;
    }

    public String saveToString(){
        return value;
    }
}