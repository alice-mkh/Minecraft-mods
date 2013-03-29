package net.minecraft.src;

public class OldDaysPropertyBool extends OldDaysProperty{
    public boolean value;
    public boolean smpValue;
    public boolean defaultValue;

    public OldDaysPropertyBool(OldDaysModule m, int i, boolean b, String f){
        super(m, i, f);
        value = defaultValue = b;
        guitype = GUI_TYPE_BUTTON;
    }

    public OldDaysPropertyBool(OldDaysModule m, int i, boolean b, boolean smp, String f){
        this(m, i, b, f);
        smpValue = smp;
        allowedInSMP = false;
        m.isLocal = false;
    }

    @Override
    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+(mod_OldDays.lang.get(value ? "gui.on" : "gui.off"));
    }

    @Override
    public void onChange(){
        if (shouldSkipUpdates()){
            return;
        }
        try{
            field.set(module, value);
            module.callback(id);
        }catch(Exception ex){
            ex.printStackTrace();
            disable();
        }
    }

    @Override
    public void incrementValue(){
        value = !value;
    }

    @Override
    public void updateValue(){
        try{
            value = ((Boolean)field.get(module));
        }catch(Exception ex){
            ex.printStackTrace();
            disable();
            return;
        }
    }

    @Override
    public void setSMPValue(){
        value = smpValue;
    }

    @Override
    public String getDefaultValue(){
        return ""+defaultValue;
    }

    @Override
    public void setDefaultValue(){
        value = defaultValue;
    }

    @Override
    protected void disable(){
        super.disable();
        value = smpValue;
    }

    @Override
    public void loadFromString(String str){
        value = str.matches("^*([Oo][Nn]|[Tt][Rr][Uu][Ee]?|[Yy][Ee]?[SsPpAa]?[Hh]?)*$");
    }

    @Override
    public String saveToString(){
        return ""+value;
    }
}