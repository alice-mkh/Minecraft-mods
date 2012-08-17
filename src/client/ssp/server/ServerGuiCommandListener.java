package net.minecraft.src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import net.minecraft.server.MinecraftServer;

class ServerGuiCommandListener implements ActionListener
{
    /** Text field. */
    final JTextField textField;

    /** Reference to the ServerGui object. */
    final ServerGUI mcServerGui;

    ServerGuiCommandListener(ServerGUI par1ServerGUI, JTextField par2JTextField)
    {
        mcServerGui = par1ServerGUI;
        textField = par2JTextField;
    }

    public void actionPerformed(ActionEvent par1ActionEvent)
    {
        String s = textField.getText().trim();

        if (s.length() > 0)
        {
            ServerGUI.func_79004_a(mcServerGui).func_71331_a(s, MinecraftServer.func_71276_C());
        }

        textField.setText("");
    }
}
