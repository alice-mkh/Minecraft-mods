package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientExperience extends CommandXP
{
    public CommandClientExperience()
    {
    }

    protected EntityPlayer func_71543_a(String par1Str)
    {
        return Minecraft.getMinecraftInstance().field_71439_g;
    }

    protected String[] func_71542_c()
    {
        return (new String[]
                {
                    Minecraft.getMinecraftInstance().field_71439_g.username
                });
    }
}
