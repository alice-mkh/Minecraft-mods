package net.minecraft.client;

import net.minecraft.src.Minecraft;
import net.minecraft.src.ssp.Mod;

public class ClientBrandRetriever
{
    public ClientBrandRetriever()
    {
    }

    public static String getClientModName()
    {
        StringBuilder s = new StringBuilder();
        s.append("SSP 3.1 for 1.6.4");
        for (Mod mod : Minecraft.getMinecraft().mods){
            s.append("; ");
            s.append(mod.getModName());
            s.append(" ");
            s.append(mod.getModVersion());
        }
        return s.toString();
    }
}
