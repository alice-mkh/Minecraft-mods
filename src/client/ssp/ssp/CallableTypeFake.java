package net.minecraft.src.ssp;

import java.util.concurrent.Callable;

class CallableTypeFake implements Callable
{
    /** Gets Intergated Server type. */
    final FakeServer minecraftServerTypeFake;

    CallableTypeFake(FakeServer par1FakeServer)
    {
        minecraftServerTypeFake = par1FakeServer;
    }

    public String func_76973_a()
    {
        return "Singleplayer";
    }

    public Object call()
    {
        return func_76973_a();
    }
}
