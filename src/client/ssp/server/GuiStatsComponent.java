package net.minecraft.src;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JComponent;
import javax.swing.Timer;
import net.minecraft.server.MinecraftServer;

public class GuiStatsComponent extends JComponent
{
    private static final DecimalFormat field_79020_a = new DecimalFormat("########0.000");
    private int memoryUse[];

    /**
     * Counts the number of updates. Used as the index into the memoryUse array to display the latest value.
     */
    private int updateCounter;
    private String displayStrings[];
    private final MinecraftServer field_79017_e;

    public GuiStatsComponent(MinecraftServer par1MinecraftServer)
    {
        memoryUse = new int[256];
        updateCounter = 0;
        displayStrings = new String[10];
        field_79017_e = par1MinecraftServer;
        setPreferredSize(new Dimension(356, 246));
        setMinimumSize(new Dimension(356, 246));
        setMaximumSize(new Dimension(356, 246));
        (new Timer(500, new GuiStatsListener(this))).start();
        setBackground(Color.BLACK);
    }

    /**
     * Updates the stat values and calls paint to redraw the component.
     */
    private void updateStats()
    {
        long var1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.gc();
        this.displayStrings[0] = "Memory use: " + var1 / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
        this.displayStrings[1] = "Threads: " + TcpConnection.field_74471_a.get() + " + " + TcpConnection.field_74469_b.get();
        this.displayStrings[2] = "Avg tick: " + field_79020_a.format(this.func_79015_a(this.field_79017_e.field_71311_j) * 1.0E-6D) + " ms";
        this.displayStrings[3] = "Avg sent: " + (int)this.func_79015_a(this.field_79017_e.field_71300_f) + ", Avg size: " + (int)this.func_79015_a(this.field_79017_e.field_71301_g);
        this.displayStrings[4] = "Avg rec: " + (int)this.func_79015_a(this.field_79017_e.field_71313_h) + ", Avg size: " + (int)this.func_79015_a(this.field_79017_e.field_71314_i);

        if (this.field_79017_e.field_71305_c != null)
        {
            for (int var3 = 0; var3 < this.field_79017_e.field_71305_c.length; ++var3)
            {
                this.displayStrings[5 + var3] = "Lvl " + var3 + " tick: " + field_79020_a.format(this.func_79015_a(this.field_79017_e.field_71312_k[var3]) * 1.0E-6D) + " ms";

                if (this.field_79017_e.field_71305_c[var3] != null && this.field_79017_e.field_71305_c[var3].field_73059_b != null)
                {
                    this.displayStrings[5 + var3] = this.displayStrings[5 + var3] + ", " + this.field_79017_e.field_71305_c[var3].field_73059_b.makeString();
                }
            }
        }

        this.memoryUse[this.updateCounter++ & 255] = (int)(this.func_79015_a(this.field_79017_e.field_71301_g) * 100.0D / 12500.0D);
        this.repaint();
    }

    private double func_79015_a(long par1ArrayOfLong[])
    {
        long l = 0L;
        long al[] = par1ArrayOfLong;
        int i = al.length;

        for (int j = 0; j < i; j++)
        {
            long l1 = al[j];
            l += l1;
        }

        return (double)l / (double)par1ArrayOfLong.length;
    }

    public void paint(Graphics par1Graphics)
    {
        par1Graphics.setColor(new Color(0xffffff));
        par1Graphics.fillRect(0, 0, 356, 246);

        for (int i = 0; i < 256; i++)
        {
            int k = memoryUse[i + updateCounter & 0xff];
            par1Graphics.setColor(new Color(k + 28 << 16));
            par1Graphics.fillRect(i, 100 - k, 1, k);
        }

        par1Graphics.setColor(Color.BLACK);

        for (int j = 0; j < displayStrings.length; j++)
        {
            String s = displayStrings[j];

            if (s != null)
            {
                par1Graphics.drawString(s, 32, 116 + j * 16);
            }
        }
    }

    /**
     * Public static accessor to call updateStats.
     */
    static void update(GuiStatsComponent par0GuiStatsComponent)
    {
        par0GuiStatsComponent.updateStats();
    }
}
