package net.minecraft.client;

import net.minecraft.src.Mod;

public class ClientBrandRetriever
{
    public ClientBrandRetriever()
    {
    }

    public static String getClientModName()
    {
        StringBuilder s = new StringBuilder();
        s.append("SSP for 1.4.4 and 1.4.5");
        for (Object o : Minecraft.getMinecraft().mods){
            Mod mod = (Mod)o;
            s.append("; ");
            s.append(mod.getModName());
            s.append(" ");
            s.append(mod.getModVersion());
        }
        return s.toString();
    }
}
