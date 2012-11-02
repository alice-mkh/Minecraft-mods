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
    }

    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+(mod_OldDays.lang.get(value ? "gui.on" : "gui.off"));
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

    public void incrementValue(){
        value = !value;
    }

    public void updateValue(){
        try{
            value = ((Boolean)field.get(module));
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
        value = str.matches("^*([Oo][Nn]|[Tt][Rr][Uu][Ee]?|[Yy][Ee]?[SsPpAa]?[Hh]?)*$");
    }

    public String saveToString(){
        return ""+value;
    }
}