package net.minecraft.src;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

final class ServerWindowAdapter extends WindowAdapter
{
    final DedicatedServer field_79002_a;

    ServerWindowAdapter(DedicatedServer par1DedicatedServer)
    {
        field_79002_a = par1DedicatedServer;
    }

    public void windowClosing(WindowEvent par1WindowEvent)
    {
        field_79002_a.setServerStopping();

        while (!field_79002_a.isServerStopped())
        {
            try
            {
                Thread.sleep(100L);
            }
            catch (InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }

        System.exit(0);
    }
}
