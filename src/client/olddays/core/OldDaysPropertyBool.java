package net.minecraft.src;

public class OldDaysPropertyBool extends OldDaysProperty{
    public boolean value;
    public boolean smpValue;

    public OldDaysPropertyBool(OldDaysModule m, int i, String s, boolean b, String f){
        super(m, i, s, TYPE_BOOLEAN, f);
        value = b;
        guitype = GUI_TYPE_BUTTON;
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
        if (isDisabled()){
            return;
        }
        try{
            field.set(module, value);
            module.callback(id);
        }catch(Exception ex){
            System.out.println(ex);
            disable();
        }
    }

    public void incrementValue(){
        value = !value;
    }

    public void setSMPValue(){
        value = smpValue;
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