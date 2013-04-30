package net.minecraft.src;

public class OldDaysPropertyString extends OldDaysProperty{
    public String value;
    public String smpValue;
    public String defaultValue;

    public OldDaysPropertyString(OldDaysModule m, int i, String str, String smp, String f){
        super(m, i, f);
        value = defaultValue = str;
        guitype = GUI_TYPE_FIELD;
        smpValue = smp;
    }

    @Override
    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+value;
    }

    @Override
    public void onChange(boolean flag){
        if (shouldSkipUpdates() && flag){
            return;
        }
        try{
            field.set(module, value);
            module.callback(id);
        }catch(Exception ex){
            if (flag){
                ex.printStackTrace();
                disable();
            }
        }
    }

    @Override
    public void updateValue(){
        try{
            value = ((String)field.get(module));
        }catch(Exception ex){
            ex.printStackTrace();
            disable();
            return;
        }
    }

    @Override
    public String getSMPValue(){
        return smpValue;
    }

    @Override
    public void setSMPValue(){
        value = smpValue;
    }

    @Override
    public String getDefaultValue(){
        return defaultValue;
    }

    @Override
    public void setDefaultValue(){
        value = defaultValue;
    }

    @Override
    public void loadFromString(String str){
        value = str;
    }

    @Override
    public String saveToString(){
        return value;
    }

    @Override
    public void incrementValue(){}
}