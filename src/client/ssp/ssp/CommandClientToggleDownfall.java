package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientToggleDownfall extends CommandToggleDownfall
{
    public CommandClientToggleDownfall()
    {
    }

    protected void func_71554_c()
    {
        Minecraft.getMinecraft().theWorld.getWorldInfo().setRainTime(1);
    }
}
