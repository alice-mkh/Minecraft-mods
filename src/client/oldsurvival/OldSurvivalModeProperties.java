package net.minecraft.src;

import java.util.*;

public class OldSurvivalModeProperties extends Properties{
    public OldSurvivalModeProperties(){
    }

    public synchronized Enumeration keys(){
        Enumeration enumeration = super.keys();
        Vector vector = new Vector();
        for(; enumeration.hasMoreElements(); vector.add(enumeration.nextElement())) { }
        Collections.sort(vector);
        return vector.elements();
    }
}