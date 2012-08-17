package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;

public class TextureCompassFX extends TextureFX
{
    /** A reference to the Minecraft object. */
    private Minecraft mc;
    private int compassIconImageData[];
    private double field_76868_i;
    private double field_76866_j;

    public TextureCompassFX(Minecraft par1Minecraft)
    {
        super(Item.compass.getIconFromDamage(0));
        compassIconImageData = new int[256];
        mc = par1Minecraft;
        tileImage = 1;

        try
        {
            BufferedImage bufferedimage = ImageIO.read((net.minecraft.client.Minecraft.class).getResource("/gui/items.png"));
            int i = (iconIndex % 16) * 16;
            int j = (iconIndex / 16) * 16;
            bufferedimage.getRGB(i, j, 16, 16, compassIconImageData, 0, 16);
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public void onTick()
    {
        for (int i = 0; i < 256; i++)
        {
            int j = compassIconImageData[i] >> 24 & 0xff;
            int k = compassIconImageData[i] >> 16 & 0xff;
            int l = compassIconImageData[i] >> 8 & 0xff;
            int i1 = compassIconImageData[i] >> 0 & 0xff;

            if (anaglyphEnabled)
            {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            imageData[i * 4 + 0] = (byte)k;
            imageData[i * 4 + 1] = (byte)l;
            imageData[i * 4 + 2] = (byte)i1;
            imageData[i * 4 + 3] = (byte)j;
        }

        double d = 0.0D;

        if (mc.field_71441_e != null && mc.field_71439_g != null)
        {
            ChunkCoordinates chunkcoordinates = mc.field_71441_e.getSpawnPoint();
            double d2 = (double)chunkcoordinates.posX - mc.field_71439_g.posX;
            double d4 = (double)chunkcoordinates.posZ - mc.field_71439_g.posZ;
            d = ((double)(mc.field_71439_g.rotationYaw - 90F) * Math.PI) / 180D - Math.atan2(d4, d2);

            if (!mc.field_71441_e.worldProvider.isSurfaceWorld())
            {
                d = Math.random() * Math.PI * 2D;
            }
        }

        double d1;

        for (d1 = d - field_76868_i; d1 < -Math.PI; d1 += (Math.PI * 2D)) { }

        for (; d1 >= Math.PI; d1 -= (Math.PI * 2D)) { }

        if (d1 < -1D)
        {
            d1 = -1D;
        }

        if (d1 > 1.0D)
        {
            d1 = 1.0D;
        }

        field_76866_j += d1 * 0.10000000000000001D;
        field_76866_j *= 0.80000000000000004D;
        field_76868_i += field_76866_j;
        double d3 = Math.sin(field_76868_i);
        double d5 = Math.cos(field_76868_i);

        for (int i2 = -4; i2 <= 4; i2++)
        {
            int k2 = (int)(8.5D + d5 * (double)i2 * 0.29999999999999999D);
            int i3 = (int)(7.5D - d3 * (double)i2 * 0.29999999999999999D * 0.5D);
            int k3 = i3 * 16 + k2;
            int i4 = 100;
            int k4 = 100;
            int i5 = 100;
            char c = '\377';

            if (anaglyphEnabled)
            {
                int k5 = (i4 * 30 + k4 * 59 + i5 * 11) / 100;
                int i6 = (i4 * 30 + k4 * 70) / 100;
                int k6 = (i4 * 30 + i5 * 70) / 100;
                i4 = k5;
                k4 = i6;
                i5 = k6;
            }

            imageData[k3 * 4 + 0] = (byte)i4;
            imageData[k3 * 4 + 1] = (byte)k4;
            imageData[k3 * 4 + 2] = (byte)i5;
            imageData[k3 * 4 + 3] = (byte)c;
        }

        for (int j2 = -8; j2 <= 16; j2++)
        {
            int l2 = (int)(8.5D + d3 * (double)j2 * 0.29999999999999999D);
            int j3 = (int)(7.5D + d5 * (double)j2 * 0.29999999999999999D * 0.5D);
            int l3 = j3 * 16 + l2;
            int j4 = j2 < 0 ? 100 : 255;
            int l4 = j2 < 0 ? 100 : 20;
            int j5 = j2 < 0 ? 100 : 20;
            char c1 = '\377';

            if (anaglyphEnabled)
            {
                int l5 = (j4 * 30 + l4 * 59 + j5 * 11) / 100;
                int j6 = (j4 * 30 + l4 * 70) / 100;
                int l6 = (j4 * 30 + j5 * 70) / 100;
                j4 = l5;
                l4 = j6;
                j5 = l6;
            }

            imageData[l3 * 4 + 0] = (byte)j4;
            imageData[l3 * 4 + 1] = (byte)l4;
            imageData[l3 * 4 + 2] = (byte)j5;
            imageData[l3 * 4 + 3] = (byte)c1;
        }
    }
}
