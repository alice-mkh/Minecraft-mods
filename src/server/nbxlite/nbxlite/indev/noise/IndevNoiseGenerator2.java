package net.minecraft.src.nbxlite.indev.noise;

public final class IndevNoiseGenerator2 extends IndevNoiseGenerator
{
    private IndevNoiseGenerator a;
    private IndevNoiseGenerator b;

    public IndevNoiseGenerator2(IndevNoiseGenerator b1, IndevNoiseGenerator b2)
    {
        a = b1;
        b = b2;
    }

    public final double a(double d1, double d2)
    {
        return a.a(d1 + b.a(d1, d2), d2);
    }
}
