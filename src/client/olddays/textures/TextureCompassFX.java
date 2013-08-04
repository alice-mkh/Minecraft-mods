package net.minecraft.src;

public class TextureCompassFX extends TextureFX
{
    /** A reference to the Minecraft object. */
    private Minecraft mc;
    private int compassIconImageData[];
    public double field_76868_i;
    public double field_76866_j;

    public TextureCompassFX()
    {
        super("compass", true);
        mc = Minecraft.getMinecraft();
    }

    @Override
    protected void updateSize(){
        super.updateSize();
        compassIconImageData = getImageData("olddays/compass.png", size);
    }

    @Override
    public void onTick()
    {
        if (mc.theWorld != null && mc.thePlayer != null)
        {
            redraw(mc.thePlayer.posX, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, false, false);
        }
        else
        {
            redraw(0.0D, 0.0D, 0.0D, true, false);
        }
    }

    public void redraw(double par0, double par2, double par4, boolean par6, boolean par7)
    {
        int multiplier = size / 16;
        for (int i = 0; i < size * size; i++)
        {
            int j = compassIconImageData[i] >> 24 & 0xff;
            int k = compassIconImageData[i] >> 16 & 0xff;
            int l = compassIconImageData[i] >> 8 & 0xff;
            int i1 = compassIconImageData[i] >> 0 & 0xff;

            if (anaglyphEnabled)
            {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int j2 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = j2;
            }

            imageData[i] = j << 24 | k << 16 | l << 8 | i1;
        }

        double d = 0.0D;

        if (mc.theWorld != null && !par6)
        {
            ChunkCoordinates chunkcoordinates = mc.theWorld.getSpawnPoint();
            double d3 = (double)chunkcoordinates.posX - par0;
            double d5 = (double)chunkcoordinates.posZ - par2;
            d = ((par4 - 90D) * Math.PI) / 180D - Math.atan2(d5, d3);

            if (!mc.theWorld.provider.isSurfaceWorld())
            {
                d = Math.random() * Math.PI * 2D;
            }
        }

        if (par7)
        {
            field_76868_i = d;
        }
        else
        {
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
        }

        double d2 = Math.sin(field_76868_i);
        double d4 = Math.cos(field_76868_i);

        for (int l1 = -4 * multiplier; l1 <= 4 * multiplier; l1++)
        {
            int k2 = (int)(8.5D * multiplier + d4 * (double)l1 * 0.29999999999999999D);
            int i3 = (int)(7.5D * multiplier - d2 * (double)l1 * 0.29999999999999999D * 0.5D);
            int k3 = i3 * size + k2;
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

            imageData[k3] = c << 24 | i4 << 16 | k4 << 8 | i5;
        }

        for (int i2 = -8 * multiplier; i2 <= size; i2++)
        {
            int l2 = (int)(8.5D * multiplier + d2 * (double)i2 * 0.29999999999999999D);
            int j3 = (int)(7.5D * multiplier + d4 * (double)i2 * 0.29999999999999999D * 0.5D);
            int l3 = j3 * size + l2;
            int j4 = i2 < 0 ? 100 : 255;
            int l4 = i2 < 0 ? 100 : 20;
            int j5 = i2 < 0 ? 100 : 20;
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

            imageData[l3] = c1 << 24 | j4 << 16 | l4 << 8 | j5;
        }
    }
}
