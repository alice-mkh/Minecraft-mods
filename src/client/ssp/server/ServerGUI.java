package net.minecraft.src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ServerGUI extends JComponent
{
    /** Reference to the logger. */
    public static Logger logger = Logger.getLogger("Minecraft");
    private static boolean field_79008_b = false;
    private DedicatedServer field_79009_c;

    public static void func_79003_a(DedicatedServer par0DedicatedServer)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception exception) { }

        ServerGUI servergui = new ServerGUI(par0DedicatedServer);
        field_79008_b = true;
        JFrame jframe = new JFrame("Minecraft server");
        jframe.add(servergui);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
        jframe.addWindowListener(new ServerWindowAdapter(par0DedicatedServer));
    }

    public ServerGUI(DedicatedServer par1DedicatedServer)
    {
        field_79009_c = par1DedicatedServer;
        setPreferredSize(new Dimension(854, 480));
        setLayout(new BorderLayout());

        try
        {
            add(getLogComponent(), "Center");
            add(getStatsComponent(), "West");
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Returns a new JPanel with a new GuiStatsComponent inside.
     */
    private JComponent getStatsComponent()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(new GuiStatsComponent(field_79009_c), "North");
        jpanel.add(getPlayerListComponent(), "Center");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
        return jpanel;
    }

    /**
     * Returns a new JScrollPane with a new PlayerListBox inside.
     */
    private JComponent getPlayerListComponent()
    {
        PlayerListBox playerlistbox = new PlayerListBox(field_79009_c);
        JScrollPane jscrollpane = new JScrollPane(playerlistbox, 22, 30);
        jscrollpane.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
        return jscrollpane;
    }

    /**
     * Returns a new JPanel with a new GuiStatsComponent inside.
     */
    private JComponent getLogComponent()
    {
        JPanel jpanel = new JPanel(new BorderLayout());
        JTextArea jtextarea = new JTextArea();
        logger.addHandler(new GuiLogOutputHandler(jtextarea));
        JScrollPane jscrollpane = new JScrollPane(jtextarea, 22, 30);
        jtextarea.setEditable(false);
        JTextField jtextfield = new JTextField();
        jtextfield.addActionListener(new ServerGuiCommandListener(this, jtextfield));
        jtextarea.addFocusListener(new ServerGuiFocusAdapter(this));
        jpanel.add(jscrollpane, "Center");
        jpanel.add(jtextfield, "South");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
        return jpanel;
    }

    static DedicatedServer func_79004_a(ServerGUI par0ServerGUI)
    {
        return par0ServerGUI.field_79009_c;
    }
}
