package net.minecraft.src;

import java.io.IOException;

public class TextureCompassFX extends TextureFX
{
    /** A reference to the Minecraft object. */
    private Minecraft mc;
    private int compassIconImageData[];
    public double field_76868_i;
    public double field_76866_j;
    public static TextureCompassFX field_82391_c;

    public TextureCompassFX()
    {
        super("compass");
        compassIconImageData = new int[256];
        mc = Minecraft.getMinecraft();

        try
        {
            getImage("olddays/compass.png").getRGB(0, 0, 16, 16, compassIconImageData, 0, 16);
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }

        field_82391_c = this;
    }

    public void onTick()
    {
        if (mc.theWorld != null && mc.thePlayer != null)
        {
            func_82390_a(mc.thePlayer.posX, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, false, false);
        }
        else
        {
            func_82390_a(0.0D, 0.0D, 0.0D, true, false);
        }
    }

    public static void func_82390_a(double par0, double par2, double par4, boolean par6, boolean par7)
    {
        int ai[] = field_82391_c.compassIconImageData;
        byte abyte0[] = field_82391_c.imageData;

        for (int i = 0; i < 256; i++)
        {
            int j = ai[i] >> 24 & 0xff;
            int k = ai[i] >> 16 & 0xff;
            int l = ai[i] >> 8 & 0xff;
            int i1 = ai[i] >> 0 & 0xff;

            if (field_82391_c.anaglyphEnabled)
            {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int j2 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = j2;
            }

            abyte0[i * 4 + 0] = (byte)k;
            abyte0[i * 4 + 1] = (byte)l;
            abyte0[i * 4 + 2] = (byte)i1;
            abyte0[i * 4 + 3] = (byte)j;
        }

        double d = 0.0D;

        if (field_82391_c.mc.theWorld != null && !par6)
        {
            ChunkCoordinates chunkcoordinates = field_82391_c.mc.theWorld.getSpawnPoint();
            double d3 = (double)chunkcoordinates.posX - par0;
            double d5 = (double)chunkcoordinates.posZ - par2;
            d = ((par4 - 90D) * Math.PI) / 180D - Math.atan2(d5, d3);

            if (!field_82391_c.mc.theWorld.provider.isSurfaceWorld())
            {
                d = Math.random() * Math.PI * 2D;
            }
        }

        if (par7)
        {
            field_82391_c.field_76868_i = d;
        }
        else
        {
            double d1;

            for (d1 = d - field_82391_c.field_76868_i; d1 < -Math.PI; d1 += (Math.PI * 2D)) { }

            for (; d1 >= Math.PI; d1 -= (Math.PI * 2D)) { }

            if (d1 < -1D)
            {
                d1 = -1D;
            }

            if (d1 > 1.0D)
            {
                d1 = 1.0D;
            }

            field_82391_c.field_76866_j += d1 * 0.10000000000000001D;
            field_82391_c.field_76866_j *= 0.80000000000000004D;
            field_82391_c.field_76868_i += field_82391_c.field_76866_j;
        }

        double d2 = Math.sin(field_82391_c.field_76868_i);
        double d4 = Math.cos(field_82391_c.field_76868_i);

        for (int l1 = -4; l1 <= 4; l1++)
        {
            int k2 = (int)(8.5D + d4 * (double)l1 * 0.29999999999999999D);
            int i3 = (int)(7.5D - d2 * (double)l1 * 0.29999999999999999D * 0.5D);
            int k3 = i3 * 16 + k2;
            int i4 = 100;
            int k4 = 100;
            int i5 = 100;
            char c = '\377';

            if (field_82391_c.anaglyphEnabled)
            {
                int k5 = (i4 * 30 + k4 * 59 + i5 * 11) / 100;
                int i6 = (i4 * 30 + k4 * 70) / 100;
                int k6 = (i4 * 30 + i5 * 70) / 100;
                i4 = k5;
                k4 = i6;
                i5 = k6;
            }

            abyte0[k3 * 4 + 0] = (byte)i4;
            abyte0[k3 * 4 + 1] = (byte)k4;
            abyte0[k3 * 4 + 2] = (byte)i5;
            abyte0[k3 * 4 + 3] = (byte)c;
        }

        for (int i2 = -8; i2 <= 16; i2++)
        {
            int l2 = (int)(8.5D + d2 * (double)i2 * 0.29999999999999999D);
            int j3 = (int)(7.5D + d4 * (double)i2 * 0.29999999999999999D * 0.5D);
            int l3 = j3 * 16 + l2;
            int j4 = i2 < 0 ? 100 : 255;
            int l4 = i2 < 0 ? 100 : 20;
            int j5 = i2 < 0 ? 100 : 20;
            char c1 = '\377';

            if (field_82391_c.anaglyphEnabled)
            {
                int l5 = (j4 * 30 + l4 * 59 + j5 * 11) / 100;
                int j6 = (j4 * 30 + l4 * 70) / 100;
                int l6 = (j4 * 30 + j5 * 70) / 100;
                j4 = l5;
                l4 = j6;
                j5 = l6;
            }

            abyte0[l3 * 4 + 0] = (byte)j4;
            abyte0[l3 * 4 + 1] = (byte)l4;
            abyte0[l3 * 4 + 2] = (byte)j5;
            abyte0[l3 * 4 + 3] = (byte)c1;
        }
    }
}
