package net.minecraft.src.nbxlite.indev;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.Block;

public class g{
    public int a;
    public int b;
    public int c;
    public byte d[];
    public String f;
    public String g;
    public long h;
    public int i = 70;
    public int j = 90;
    public int k = 70;
    public int m = Block.waterMoving.blockID;
    List n = new ArrayList();
    public int s;
    public int t;
    public int u;
    public int v = 10079487;
    public int w = 16777215;
    public int x = 16777215;
    public int A = 15;
    public int B = 15;

    public g(){}

    public void a(){}

    public byte a(int i, int j, int k){
        return 0;
    }
/*
    public final void b()
        Random random;
        int i1;
        random = new Random();
        i1 = 0;
        int j1;
        int k1;
        int l1;
        int j2;
        int l2;
        int j3;
        g g1;
        do
        {
            i1++;
            j1 = random.nextInt(a / 2) + a / 4;
            k1 = random.nextInt(b / 2) + b / 4;
            l1 = a(j1, k1) + 1;
            if(i1 == 0xf4240)
            {
                i = j1;
                j = c + 100;
                k = k1;
                l = 180F;
                return;
            }
        } while(l1 < 4 || l1 <= (g1 = this).s);
        for(int i2 = j1 - 3; i2 <= j1 + 3; i2++)
        {
            for(int k2 = l1 - 1; k2 <= l1 + 2; k2++)
            {
                for(int i3 = k1 - 3 - 2; i3 <= k1 + 3; i3++)
                {
                    if(f(i2, k2, i3).a())
                    {
                        continue;
                    }
                }

            }

        }

        j2 = l1 - 2;
        l2 = j1 - 3;
//           goto _L1
        continue;
        l2++;
    }*/

    public final boolean a(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
        if ((paramInt1 <= 0) || (paramInt2 <= 0) || (paramInt3 <= 0) || (paramInt1 >= this.a - 1) || (paramInt2 >= this.c - 1) || (paramInt3 >= this.b - 1))
            return false;
        if (paramInt4 == this.d[((paramInt2 * this.b + paramInt3) * this.a + paramInt1)])
            return false;
        if ((paramInt4 == 0) && ((paramInt1 == 0) || (paramInt3 == 0) || (paramInt1 == this.a - 1) || (paramInt3 == this.b - 1)))
        {
            g localg = this;
            if (paramInt2 >= this.t)
            {
                localg = this;
                if (paramInt2 < this.s)
                paramInt4 = Block.waterMoving.blockID;
            }
        }
        int i1 = this.d[((paramInt2 * this.b + paramInt3) * this.a + paramInt1)];
        this.d[((paramInt2 * this.b + paramInt3) * this.a + paramInt1)] = (byte)paramInt4;
        f(paramInt1, paramInt2, paramInt3, 0);
        if (i1 != 0)
//             Block.blocksList[i1].b(this, paramInt1, paramInt2, paramInt3);
        if (paramInt4 != 0)
//             Block.blocksList[paramInt4].d(this, paramInt1, paramInt2, paramInt3);
        if ((Block.lightOpacity[i1] != Block.lightOpacity[paramInt4]) || (Block.lightValue[i1] != 0) || (Block.lightValue[paramInt4] != 0))
        {
//             this.M.a(paramInt1, paramInt3, 1, 1);
//             this.M.a(paramInt1, paramInt2, paramInt3, paramInt1 + 1, paramInt2 + 1, paramInt3 + 1);
        }
        for (paramInt4 = 0; paramInt4 < this.n.size(); paramInt4++){
//             ((d)this.n.get(paramInt4)).a(paramInt1, paramInt2, paramInt3);
        }
        return true;
    }


    public void a(int i, int j, int k, byte[] abyte0, byte[] abyte1){}

    public final void c(int i1, int j1, int k1, int l1){
        h(i1 - 1, j1, k1, l1);
        h(i1 + 1, j1, k1, l1);
        h(i1, j1 - 1, k1, l1);
        h(i1, j1 + 1, k1, l1);
        h(i1, j1, k1 - 1, l1);
        h(i1, j1, k1 + 1, l1);
    }

    public void d(){}

    public int d(int i, int j, int k){
        return 15;
    }

    public void f(int i, int j, int k, int l){}

    public void generateTree(int i, int j, int k){}

    private void h(int i1, int j1, int k1, int l1)
    {
        if(i1 < 0 || j1 < 0 || k1 < 0 || i1 >= a || j1 >= c || k1 >= b)
        {
            return;
        }
        Block x1;
        if((x1 = Block.blocksList[d[(j1 * b + k1) * a + i1]]) != null)
        {
//             x1.b(this, i1, j1, k1, l1);
        }
    }
}