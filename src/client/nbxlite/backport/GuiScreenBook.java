package net.minecraft.src.backport;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.io.*;
import net.minecraft.src.*;

public class GuiScreenBook extends GuiScreen
{
    private final EntityPlayer field_55126_a;
    private final ItemStack field_55124_b;
    private final boolean field_55125_c;
    private boolean field_55122_d;
    private boolean field_55123_e;
    private int field_55121_f;
    private int field_55133_h;
    private int field_55134_i;
    private int field_55131_j;
    private int field_55132_k;
    private NBTTagList field_55129_l;
    private String field_55130_m;
    private GuiButtonNextPage field_55127_n;
    private GuiButtonNextPage field_55128_o;
    private GuiButton field_55135_w;
    private GuiButton field_55138_x;
    private GuiButton field_55137_y;
    private GuiButton field_55136_z;

    public GuiScreenBook(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, boolean par3)
    {
        field_55133_h = 192;
        field_55134_i = 192;
        field_55131_j = 1;
        field_55130_m = "";
        field_55126_a = par1EntityPlayer;
        field_55124_b = par2ItemStack;
        field_55125_c = par3;

        if (par2ItemStack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = par2ItemStack.getTagCompound();
            field_55129_l = nbttagcompound.getTagList("pages");

            if (field_55129_l != null)
            {
                field_55129_l = (NBTTagList)field_55129_l.copy();
                field_55131_j = field_55129_l.tagCount();

                if (field_55131_j < 1)
                {
                    field_55131_j = 1;
                }
            }
        }

        if (field_55129_l == null && par3)
        {
            field_55129_l = new NBTTagList("pages");
            field_55129_l.appendTag(new NBTTagString("1", ""));
            field_55131_j = 1;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        field_55121_f++;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        controlList.clear();
        Keyboard.enableRepeatEvents(true);

        if (field_55125_c)
        {
            controlList.add(field_55138_x = new GuiButton(3, width / 2 - 100, 4 + field_55134_i, 98, 20, StatCollector.translateToLocal("book.signButton")));
            controlList.add(field_55135_w = new GuiButton(0, width / 2 + 2, 4 + field_55134_i, 98, 20, StatCollector.translateToLocal("gui.done")));
            controlList.add(field_55137_y = new GuiButton(5, width / 2 - 100, 4 + field_55134_i, 98, 20, StatCollector.translateToLocal("book.finalizeButton")));
            controlList.add(field_55136_z = new GuiButton(4, width / 2 + 2, 4 + field_55134_i, 98, 20, StatCollector.translateToLocal("gui.cancel")));
        }
        else
        {
            controlList.add(field_55135_w = new GuiButton(0, width / 2 - 100, 4 + field_55134_i, 200, 20, StatCollector.translateToLocal("gui.done")));
        }

        int i = (width - field_55133_h) / 2;
        byte byte0 = 2;
        controlList.add(field_55127_n = new GuiButtonNextPage(1, i + 120, byte0 + 154, true));
        controlList.add(field_55128_o = new GuiButtonNextPage(2, i + 38, byte0 + 154, false));
        func_55116_g();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    private void func_55116_g()
    {
        field_55127_n.drawButton = !field_55123_e && (field_55132_k < field_55131_j - 1 || field_55125_c);
        field_55128_o.drawButton = !field_55123_e && field_55132_k > 0;
        field_55135_w.drawButton = !field_55125_c || !field_55123_e;

        if (field_55125_c)
        {
            field_55138_x.drawButton = !field_55123_e;
            field_55136_z.drawButton = field_55123_e;
            field_55137_y.drawButton = field_55123_e;
            field_55137_y.enabled = field_55130_m.trim().length() > 0;
        }
    }

    private void func_55118_a(boolean par1)
    {
        if (!field_55125_c || !field_55122_d)
        {
            return;
        }

        if (field_55129_l != null)
        {
            do
            {
                if (field_55129_l.tagCount() <= 1)
                {
                    break;
                }

                NBTTagString nbttagstring = (NBTTagString)field_55129_l.tagAt(field_55129_l.tagCount() - 1);

                if (nbttagstring.data != null && nbttagstring.data.length() != 0)
                {
                    break;
                }

                func_55176_a(field_55129_l.tagCount() - 1, field_55129_l);
            }
            while (true);

            if (field_55124_b.hasTagCompound())
            {
                NBTTagCompound nbttagcompound = field_55124_b.getTagCompound();
                nbttagcompound.setTag("pages", field_55129_l);
            }
            else
            {
                func_55397_a("pages", field_55129_l, field_55124_b);
            }

            String s = "MC|BEdit";

            if (par1)
            {
                s = "MC|BSign";
                func_55397_a("author", new NBTTagString("author", field_55126_a.username), field_55124_b);
                func_55397_a("title", new NBTTagString("title", field_55130_m.trim()), field_55124_b);
                field_55124_b.itemID = 131 + 256;
            }

            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

            try
            {
                MerchantRecipeList.writeItemStack(field_55124_b, dataoutputstream);
                Packet250CustomPayload pa = new Packet250CustomPayload();
                pa.data = bytearrayoutputstream.toByteArray();
                pa.channel = s;
                mc.getSendQueue().addToSendQueue(pa);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (!par1GuiButton.enabled)
        {
            return;
        }

        if (par1GuiButton.id == 0)
        {
            mc.displayGuiScreen(null);
            func_55118_a(false);
        }
        else if (par1GuiButton.id == 3 && field_55125_c)
        {
            field_55123_e = true;
        }
        else if (par1GuiButton.id == 1)
        {
            if (field_55132_k < field_55131_j - 1)
            {
                field_55132_k++;
            }
            else if (field_55125_c)
            {
                func_55114_m();

                if (field_55132_k < field_55131_j - 1)
                {
                    field_55132_k++;
                }
            }
        }
        else if (par1GuiButton.id == 2)
        {
            if (field_55132_k > 0)
            {
                field_55132_k--;
            }
        }
        else if (par1GuiButton.id == 5 && field_55123_e)
        {
            func_55118_a(true);
            mc.displayGuiScreen(null);
        }
        else if (par1GuiButton.id == 4 && field_55123_e)
        {
            field_55123_e = false;
        }

        func_55116_g();
    }

    private void func_55114_m()
    {
        if (field_55129_l == null || field_55129_l.tagCount() >= 50)
        {
            return;
        }
        else
        {
            field_55129_l.appendTag(new NBTTagString((new StringBuilder()).append("").append(field_55131_j + 1).toString(), ""));
            field_55131_j++;
            field_55122_d = true;
            return;
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);

        if (!field_55125_c)
        {
            return;
        }

        if (field_55123_e)
        {
            func_55117_c(par1, par2);
        }
        else
        {
            func_55119_b(par1, par2);
        }
    }

    private void func_55119_b(char par1, int par2)
    {
        switch (par1)
        {
            case 22:
                func_55113_c(GuiScreen.getClipboardString());
                return;
        }

        switch (par2)
        {
            case 14:
                String s = func_55120_n();

                if (s.length() > 0)
                {
                    func_55115_b(s.substring(0, s.length() - 1));
                }

                return;

            case 28:
                func_55113_c("\n");
                return;
        }

        if (ChatAllowedCharacters.isAllowedCharacter(par1))
        {
            func_55113_c(Character.toString(par1));
            return;
        }
        else
        {
            return;
        }
    }

    private void func_55117_c(char par1, int par2)
    {
        switch (par2)
        {
            case 14:
                if (field_55130_m.length() > 0)
                {
                    field_55130_m = field_55130_m.substring(0, field_55130_m.length() - 1);
                    func_55116_g();
                }

                return;

            case 28:
                if (field_55130_m.length() > 0)
                {
                    func_55118_a(true);
                    mc.displayGuiScreen(null);
                }

                return;
        }

        if (field_55130_m.length() >= 16 || !ChatAllowedCharacters.isAllowedCharacter(par1))
        {
            return;
        }

        field_55130_m += Character.toString(par1);
        func_55116_g();
        field_55122_d = true;
        return;
    }

    private String func_55120_n()
    {
        if (field_55129_l != null && field_55132_k >= 0 && field_55132_k < field_55129_l.tagCount())
        {
            NBTTagString nbttagstring = (NBTTagString)field_55129_l.tagAt(field_55132_k);
            return nbttagstring.toString();
        }
        else
        {
            return "";
        }
    }

    private void func_55115_b(String par1Str)
    {
        if (field_55129_l != null && field_55132_k >= 0 && field_55132_k < field_55129_l.tagCount())
        {
            NBTTagString nbttagstring = (NBTTagString)field_55129_l.tagAt(field_55132_k);
            nbttagstring.data = par1Str;
            field_55122_d = true;
        }
    }

    private void func_55113_c(String par1Str)
    {
        String s = func_55120_n();
        String s1 = (new StringBuilder()).append(s).append(par1Str).toString();
        int i = fontRenderer.splitStringWidth((new StringBuilder()).append(s1).append("\2470_").toString(), 118);

        if (i <= 118 && s1.length() < 256)
        {
            func_55115_b(s1);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        int i = mc.renderEngine.getTexture("/gui/book.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - field_55133_h) / 2;
        byte byte0 = 2;
        drawTexturedModalRect(j, byte0, 0, 0, field_55133_h, field_55134_i);

        if (field_55123_e)
        {
            String s = field_55130_m;

            if (field_55125_c)
            {
                if ((field_55121_f / 6) % 2 == 0)
                {
                    s = (new StringBuilder()).append(s).append("\2470_").toString();
                }
                else
                {
                    s = (new StringBuilder()).append(s).append("\2477_").toString();
                }
            }

            String s2 = StatCollector.translateToLocal("book.editTitle");
            int k = fontRenderer.getStringWidth(s2);
            fontRenderer.drawString(s2, j + 36 + (116 - k) / 2, byte0 + 16 + 16, 0);
            int i1 = fontRenderer.getStringWidth(s);
            fontRenderer.drawString(s, j + 36 + (116 - i1) / 2, byte0 + 48, 0);
            String s4 = String.format(StatCollector.translateToLocal("book.byAuthor"), new Object[]
                    {
                        field_55126_a.username
                    });
            int j1 = fontRenderer.getStringWidth(s4);
            fontRenderer.drawString((new StringBuilder()).append("\2478").append(s4).toString(), j + 36 + (116 - j1) / 2, byte0 + 48 + 10, 0);
            String s5 = StatCollector.translateToLocal("book.finalizeWarning");
            fontRenderer.drawSplitString(s5, j + 36, byte0 + 80, 116, 0);
        }
        else
        {
            String s1 = String.format(StatCollector.translateToLocal("book.pageIndicator"), new Object[]
                    {
                        Integer.valueOf(field_55132_k + 1), Integer.valueOf(field_55131_j)
                    });
            String s3 = "";

            if (field_55129_l != null && field_55132_k >= 0 && field_55132_k < field_55129_l.tagCount())
            {
                NBTTagString nbttagstring = (NBTTagString)field_55129_l.tagAt(field_55132_k);
                s3 = nbttagstring.toString();
            }

            if (field_55125_c)
            {
                boolean bidiFlag = false;
                try{
                    bidiFlag = ((Boolean)ModLoader.getPrivateValue(net.minecraft.src.FontRenderer.class, fontRenderer, 12));
                }catch(Exception ex){
                    System.out.println(ex);
                }
                if (bidiFlag)
                {
                    s3 = (new StringBuilder()).append(s3).append("_").toString();
                }
                else if ((field_55121_f / 6) % 2 == 0)
                {
                    s3 = (new StringBuilder()).append(s3).append("\2470_").toString();
                }
                else
                {
                    s3 = (new StringBuilder()).append(s3).append("\2477_").toString();
                }
            }

            int l = fontRenderer.getStringWidth(s1);
            fontRenderer.drawString(s1, ((j - l) + field_55133_h) - 44, byte0 + 16, 0);
            fontRenderer.drawSplitString(s3, j + 36, byte0 + 16 + 16, 116, 0);
        }

        super.drawScreen(par1, par2, par3);
    }

    public NBTBase func_55176_a(int par1, NBTTagList list)
    {
        List l = null;
        try{
            l = (List)ModLoader.getPrivateValue(net.minecraft.src.NBTTagList.class, list, 0);
        }catch(Exception ex){
            System.out.println(ex);
        }
        return (NBTBase)l.remove(par1);
    }

    public void func_55397_a(String par1Str, NBTBase par2NBTBase, ItemStack stack)
    {
        if (stack.stackTagCompound == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.stackTagCompound.setTag(par1Str, par2NBTBase);
    }
}
