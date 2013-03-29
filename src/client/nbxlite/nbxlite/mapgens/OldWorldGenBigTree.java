package net.minecraft.src.nbxlite.mapgens;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public final class OldWorldGenBigTree extends WorldGenerator {
    private static byte[] otherCoordPairs = new byte[]{2, 0, 0, 1, 2, 1};
    private Random b = new Random();
    private World worldObj;
    private int[] basePos = new int[]{0, 0, 0};
    private int e = 0;
    private int f;
    private double g = 0.618D;
    private double h = 0.381D;
    private double i = 1.0D;
    private double j = 1.0D;
    private int k = 1;
    private int l = 12;
    private int m = 4;
    private int[][] n;


    private void a(int[] var1, int[] var2, int var3) {
        int[] var15 = new int[]{0, 0, 0};
        byte var4 = 0;

        byte var5;
        for(var5 = 0; var4 < 3; ++var4) {
            var15[var4] = var2[var4] - var1[var4];
            if(Math.abs(var15[var4]) > Math.abs(var15[var5])) {
                var5 = var4;
            }
        }

        if(var15[var5] != 0) {
            byte var14 = otherCoordPairs[var5];
            var4 = otherCoordPairs[var5 + 3];
            byte var6;
            if(var15[var5] > 0) {
                var6 = 1;
            } else {
                var6 = -1;
            }

            double var10 = (double)var15[var14] / (double)var15[var5];
            double var12 = (double)var15[var4] / (double)var15[var5];
            int[] var7 = new int[]{0, 0, 0};
            int var8 = 0;

            for(var3 = var15[var5] + var6; var8 != var3; var8 += var6) {
                var7[var5] = MathHelper.floor_double((double)(var1[var5] + (double)var8) + 0.5D);
                var7[var14] = MathHelper.floor_double((double)var1[var14] + (double)var8 * var10 + 0.5D);
                var7[var4] = MathHelper.floor_double((double)var1[var4] + (double)var8 * var12 + 0.5D);
                worldObj.setBlock(var7[0], var7[1], var7[2], Block.wood.blockID);
            }

        }
    }

    private int a(int[] var1, int[] var2) {
        int[] var3 = new int[]{0, 0, 0};
        byte var4 = 0;

        byte var5;
        for(var5 = 0; var4 < 3; ++var4) {
            var3[var4] = var2[var4] - var1[var4];
            if(Math.abs(var3[var4]) > Math.abs(var3[var5])) {
                var5 = var4;
            }
        }

        if(var3[var5] == 0) {
            return -1;
        } else {
            byte var14 = otherCoordPairs[var5];
            var4 = otherCoordPairs[var5 + 3];
            byte var6;
            if(var3[var5] > 0) {
                var6 = 1;
            } else {
                var6 = -1;
            }

            double var9 = (double)var3[var14] / (double)var3[var5];
            double var11 = (double)var3[var4] / (double)var3[var5];
            int[] var7 = new int[]{0, 0, 0};
            int var8 = 0;

            int var15;
            for(var15 = var3[var5] + var6; var8 != var15; var8 += var6) {
                var7[var5] = var1[var5] + var8;
                var7[var14] = (int)((double)var1[var14] + (double)var8 * var9);
                var7[var4] = (int)((double)var1[var4] + (double)var8 * var11);
                int var13 = worldObj.getBlockId(var7[0], var7[1], var7[2]);
                if(var13 != 0 && var13 != Block.leaves.blockID) {
                    break;
                }
            }

            return var8 == var15?-1:Math.abs(var8);
        }
    }

    public final void a(double var1, double var3, double var5) {
        this.l = 12;
        this.m = 5;
        this.i = 1.0D;
        this.j = 1.0D;
    }

    @Override
    public final boolean generate(World var1, Random var2, int var3, int var4, int var5) {
        worldObj = var1;
        long var6 = var2.nextLong();
        this.b.setSeed(var6);
        this.basePos[0] = var3;
        this.basePos[1] = var4;
        this.basePos[2] = var5;
        if(this.e == 0) {
            this.e = 5 + this.b.nextInt(this.l);
        }

        int var4lol = worldObj.getBlockId(this.basePos[0], this.basePos[1] - 1, this.basePos[2]);
        int[] var39 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
        int[] var41 = new int[]{this.basePos[0], this.basePos[1] + this.e - 1, this.basePos[2]};
        boolean var10000;
        if(var4lol != Block.grass.blockID && var4lol != Block.dirt.blockID) {
            var10000 = false;
        } else if((var5 = this.a(var39, var41)) == -1) {
            var10000 = true;
        } else if(var5 < 6) {
            var10000 = false;
        } else {
            this.e = var5;
            var10000 = true;
        }

        if(!var10000) {
            return false;
        } else {
            this.f = (int)((double)this.e * this.g);
            if(this.f >= this.e) {
                this.f = this.e - 1;
            }

            int var40;
            if((var40 = (int)(1.382D + Math.pow(this.j * (double)this.e / 13.0D, 2.0D))) <= 0) {
                var40 = 1;
            }

            int[][] var42 = new int[var40 * this.e][4];
            var4 = this.basePos[1] + this.e - this.m;
            var5 = 1;
            int var43 = this.basePos[1] + this.f;
            int var7 = var4 - this.basePos[1];
            var42[0][0] = this.basePos[0];
            var42[0][1] = var4;
            var42[0][2] = this.basePos[2];
            var42[0][3] = var43;
            --var4;

            int var8;
            int var11;
            while(var7 >= 0) {
                var8 = 0;
                float var59;
                if((double)var7 < (double)((float)this.e) * 0.3D) {
                    var59 = -1.618F;
                } else {
                    float var13 = (float)this.e / 2.0F;
                    float var14;
                    float var36;
                    if((var14 = (float)this.e / 2.0F - (float)var7) == 0.0F) {
                        var36 = var13;
                    } else if(Math.abs(var14) >= var13) {
                        var36 = 0.0F;
                    } else {
                        var36 = (float)Math.sqrt(Math.pow((double)Math.abs(var13), 2.0D) - Math.pow((double)Math.abs(var14), 2.0D));
                    }

                    var59 = var36 *= 0.5F;
                }

                float var9 = var59;
                if(var59 < 0.0F) {
                    --var4;
                    --var7;
                } else {
                    for(; var8 < var40; ++var8) {
                        double var19 = this.i * (double)var9 * ((double)this.b.nextFloat() + 0.328D);
                        double var21 = (double)this.b.nextFloat() * 2.0D * 3.14159D;
                        int var10 = (int)(var19 * Math.sin(var21) + (double)this.basePos[0] + 0.5D);
                        var11 = (int)(var19 * Math.cos(var21) + (double)this.basePos[2] + 0.5D);
                        int[] var12 = new int[]{var10, var4, var11};
                        int[] var53 = new int[]{var10, var4 + this.m, var11};
                        if(this.a(var12, var53) == -1) {
                            var53 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
                            double var30 = Math.sqrt(Math.pow((double)Math.abs(this.basePos[0] - var12[0]), 2.0D) + Math.pow((double)Math.abs(this.basePos[2] - var12[2]), 2.0D)) * this.h;
                            if((double)var12[1] - var30 > (double)var43) {
                                var53[1] = var43;
                            } else {
                                var53[1] = (int)((double)var12[1] - var30);
                            }

                            if(this.a(var53, var12) == -1) {
                                var42[var5][0] = var10;
                                var42[var5][1] = var4;
                                var42[var5][2] = var11;
                                var42[var5][3] = var53[1];
                                ++var5;
                            }
                        }
                    }

                    --var4;
                    --var7;
                }
            }

            this.n = new int[var5][4];
            System.arraycopy(var42, 0, this.n, 0, var5);
            var40 = 0;

            for(var3 = this.n.length; var40 < var3; ++var40) {
                var4 = this.n[var40][0];
                var5 = this.n[var40][1];
                var43 = this.n[var40][2];
                int var10001 = var4;
                var4 = var43;
                int var49 = var5;
                var8 = var10001;
                var5 = var5;

                for(int var56 = var49 + this.m; var5 < var56; ++var5) {
                    int var22 = var5 - var49;
                    float var20 = var22 >= 0 && var22 < this.m?(var22 != 0 && var22 != this.m - 1?3.0F:2.0F):-1.0F;
                    boolean var52 = true;
                    var52 = true;
                    float var51 = var20;
                    int var28 = (int)((double)var20 + 0.618D);
                    byte var29 = otherCoordPairs[1];
                    byte var58 = otherCoordPairs[4];
                    int[] var31 = new int[]{var8, var5, var4};
                    int[] var50 = new int[]{0, 0, 0};
                    var11 = -var28;

                    for(var50[1] = var31[1]; var11 <= var28; ++var11) {
                        var50[var29] = var31[var29] + var11;
                        int var55 = -var28;

                        while(var55 <= var28) {
                            if(Math.sqrt(Math.pow((double)Math.abs(var11) + 0.5D, 2.0D) + Math.pow((double)Math.abs(var55) + 0.5D, 2.0D)) > (double)var51) {
                                ++var55;
                            } else {
                                var50[var58] = var31[var58] + var55;
                                int var54 = worldObj.getBlockId(var50[0], var50[1], var50[2]);
                                if(var54 != 0 && var54 != Block.leaves.blockID) {
                                    ++var55;
                                } else {
                                    worldObj.setBlock(var50[0], var50[1], var50[2], Block.leaves.blockID);
                                    ++var55;
                                }
                            }
                        }
                    }
                }
            }

            var40 = this.basePos[0];
            var3 = this.basePos[1];
            var4 = this.basePos[1] + this.f;
            var5 = this.basePos[2];
            int[] var45 = new int[]{var40, var3, var5};
            int[] var48 = new int[]{var40, var4, var5};
            this.a(var45, var48, 17);
            if(this.k == 2) {
                ++var45[0];
                ++var48[0];
                this.a(var45, var48, 17);
                ++var45[2];
                ++var48[2];
                this.a(var45, var48, 17);
                var45[0] += -1;
                var48[0] += -1;
                this.a(var45, var48, 17);
            }

            var40 = 0;
            var3 = this.n.length;

            for(int[] var44 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]}; var40 < var3; ++var40) {
                int[] var47 = this.n[var40];
                var45 = new int[]{var47[0], var47[1], var47[2]};
                var44[1] = var47[3];
                var7 = var44[1] - this.basePos[1];
                if((double)var7 >= (double)this.e * 0.2D) {
                    this.a(var44, var45, 17);
                }
            }

            return true;
        }
    }

}
