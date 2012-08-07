package net.minecraft.src;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public abstract class InventoryEffectRenderer extends GuiContainer
{
    private boolean field_74222_o;

    public InventoryEffectRenderer(Container par1Container)
    {
        super(par1Container);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();

        if (!mc.field_71439_g.getActivePotionEffects().isEmpty())
        {
            guiLeft = 160 + (width - xSize - 200) / 2;
            field_74222_o = true;
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);

        if (field_74222_o)
        {
            displayDebuffEffects();
        }
    }

    /**
     * Displays debuff/potion effects that are currently being applied to the player
     */
    private void displayDebuffEffects()
    {
        int i = guiLeft - 124;
        int j = guiTop;
        Collection collection = mc.field_71439_g.getActivePotionEffects();

        if (collection.isEmpty())
        {
            return;
        }

        int k = mc.renderEngine.getTexture("/gui/inventory.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        int l = 33;

        if (collection.size() > 5)
        {
            l = 132 / (collection.size() - 1);
        }

        for (Iterator iterator = mc.field_71439_g.getActivePotionEffects().iterator(); iterator.hasNext();)
        {
            PotionEffect potioneffect = (PotionEffect)iterator.next();
            Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(k);
            drawTexturedModalRect(i, j, 0, 166, 140, 32);

            if (potion.hasStatusIcon())
            {
                int i1 = potion.getStatusIconIndex();
                drawTexturedModalRect(i + 6, j + 7, 0 + (i1 % 8) * 18, 198 + (i1 / 8) * 18, 18, 18);
            }

            String s = StatCollector.translateToLocal(potion.getName());

            if (potioneffect.getAmplifier() == 1)
            {
                s = (new StringBuilder()).append(s).append(" II").toString();
            }
            else if (potioneffect.getAmplifier() == 2)
            {
                s = (new StringBuilder()).append(s).append(" III").toString();
            }
            else if (potioneffect.getAmplifier() == 3)
            {
                s = (new StringBuilder()).append(s).append(" IV").toString();
            }

            fontRenderer.drawStringWithShadow(s, i + 10 + 18, j + 6, 0xffffff);
            String s1 = Potion.getDurationString(potioneffect);
            fontRenderer.drawStringWithShadow(s1, i + 10 + 18, j + 6 + 10, 0x7f7f7f);
            j += l;
        }
    }
}
