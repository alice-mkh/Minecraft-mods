package net.minecraft.src;

public final class ThreadDedicatedServer extends Thread
{
    final DedicatedServer field_79030_a;

    public ThreadDedicatedServer(DedicatedServer par1DedicatedServer)
    {
        field_79030_a = par1DedicatedServer;
    }

    public void run()
    {
        field_79030_a.stopServer();
    }
}
