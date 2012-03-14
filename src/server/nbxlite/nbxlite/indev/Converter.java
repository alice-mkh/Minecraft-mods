package net.minecraft.src.nbxlite.indev;

public class Converter{
    public byte[] finiteWorld;
    public int height;
    public int width;
    public int length;
    public int chunky = 128;
    public int chunkz = 16;
    public Converter(byte[] world, int w, int l, int h){
        finiteWorld=world;
        height=h;
        width=w;
        length=l;
    }

    public int indexIndev(int x, int y, int z){
        return x+(y*length+z)*width;
    }

    public int indexChunk(int x, int y, int z){
        return y+z*chunky+x*chunky*chunkz;
    }

    public byte[] getChunkArray(int x1, int z1){
        byte[] result = new byte[32768];
        for (int x=0; x<16; x++){
            for (int z=0; z<16; z++){
                for (int y=0; y<height; y++){
                    result[indexChunk(x,y,z)]=finiteWorld[indexIndev(x+(x1*16),y,z+(z1*16))];
                }
            }
        }
        return result;
    }
}