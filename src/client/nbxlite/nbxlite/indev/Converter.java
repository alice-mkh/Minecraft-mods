package net.minecraft.src.nbxlite.indev;

import net.minecraft.src.Chunk;
import net.minecraft.src.ExtendedBlockStorage;

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
                for (int y=0; y<Math.min(height, chunky); y++){
                    result[indexChunk(x,y,z)]=finiteWorld[indexIndev(x+(x1*16),y,z+(z1*16))];
                }
            }
        }
        return result;
    }

    public void fixDeepMaps(Chunk chunk, int x1, int z1){
        for (int x=0; x<16; x++){
            for (int z=0; z<16; z++){
                for (int y=128; y<height; y++){
                    ExtendedBlockStorage extendedblockstorage = chunk.func_48495_i()[y >> 4];
                    if (extendedblockstorage == null)
                    {
                        extendedblockstorage = chunk.func_48495_i()[y >> 4] = new ExtendedBlockStorage((y >> 4) << 4);
                    }
                    extendedblockstorage.func_48691_a(x, y & 0xf, z, finiteWorld[indexIndev(x+(x1*16), y, z+(z1*16))]);
                }
            }
        }
    }
}