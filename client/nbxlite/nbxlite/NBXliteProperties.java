package net.minecraft.src.nbxlite;

import java.util.*;

public class NBXliteProperties extends Properties{
    public NBXliteProperties(){
    }

    public synchronized Enumeration keys(){
        Enumeration enumeration = super.keys();
        Vector vector = new Vector();
        for(; enumeration.hasMoreElements(); vector.add(enumeration.nextElement())) { }
        Collections.sort(vector);
        return vector.elements();
    }
}