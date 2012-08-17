package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientToggleDownfall extends CommandToggleDownfall
{
    public CommandClientToggleDownfall()
    {
    }

    protected void func_71554_c()
    {
        Minecraft.getMinecraftInstance().field_71441_e.getWorldInfo().setRainTime(1);
    }
}
