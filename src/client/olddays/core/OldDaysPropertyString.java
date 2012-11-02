package net.minecraft.src;

public class OldDaysPropertyString extends OldDaysProperty{
    public String value;
    public String smpValue;
    public String defaultValue;

    public OldDaysPropertyString(OldDaysModule m, int i, String str, String f){
        super(m, i, f);
        value = defaultValue = str;
        guitype = GUI_TYPE_FIELD;
    }

    public OldDaysPropertyString(OldDaysModule m, int i, String str, String smp, String f){
        this(m, i, str, f);
        smpValue = smp;
        allowedInSMP = false;
        m.isLocal = false;
    }

    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+value;
    }

    public void onChange(){
        try{
            field.set(module, value);
            module.callback(id);
        }catch(Exception ex){
            ex.printStackTrace();
            disable();
        }
    }

    public void updateValue(){
        try{
            value = ((String)field.get(module));
        }catch(Exception ex){
            ex.printStackTrace();
            disable();
            return;
        }
    }

    public void setSMPValue(){
        value = smpValue;
    }

    public void setDefaultValue(){
        value = defaultValue;
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

    public void incrementValue(){}
}