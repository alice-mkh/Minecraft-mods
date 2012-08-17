package net.minecraft.src;

import java.util.logging.*;
import javax.swing.JTextArea;
import javax.swing.text.Document;

public class GuiLogOutputHandler extends Handler
{
    private int field_79023_b[];
    private int field_79024_c;
    Formatter field_79025_a;
    private JTextArea field_79022_d;

    public GuiLogOutputHandler(JTextArea par1JTextArea)
    {
        field_79023_b = new int[1024];
        field_79024_c = 0;
        field_79025_a = new GuiLogFormatter(this);
        setFormatter(field_79025_a);
        field_79022_d = par1JTextArea;
    }

    public void close()
    {
    }

    public void flush()
    {
    }

    public void publish(LogRecord par1LogRecord)
    {
        int i = field_79022_d.getDocument().getLength();
        field_79022_d.append(field_79025_a.format(par1LogRecord));
        field_79022_d.setCaretPosition(field_79022_d.getDocument().getLength());
        int j = field_79022_d.getDocument().getLength() - i;

        if (field_79023_b[field_79024_c] != 0)
        {
            field_79022_d.replaceRange("", 0, field_79023_b[field_79024_c]);
        }

        field_79023_b[field_79024_c] = j;
        field_79024_c = (field_79024_c + 1) % 1024;
    }
}
