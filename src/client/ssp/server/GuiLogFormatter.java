package net.minecraft.src;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.*;

class GuiLogFormatter extends Formatter
{
    /** Reference to the GuiLogOutputHandler. */
    final GuiLogOutputHandler outputHandler;

    GuiLogFormatter(GuiLogOutputHandler par1GuiLogOutputHandler)
    {
        outputHandler = par1GuiLogOutputHandler;
    }

    public String format(LogRecord par1LogRecord)
    {
        StringBuilder stringbuilder = new StringBuilder();
        Level level = par1LogRecord.getLevel();

        if (level == Level.FINEST)
        {
            stringbuilder.append("[FINEST] ");
        }
        else if (level == Level.FINER)
        {
            stringbuilder.append("[FINER] ");
        }
        else if (level == Level.FINE)
        {
            stringbuilder.append("[FINE] ");
        }
        else if (level == Level.INFO)
        {
            stringbuilder.append("[INFO] ");
        }
        else if (level == Level.WARNING)
        {
            stringbuilder.append("[WARNING] ");
        }
        else if (level == Level.SEVERE)
        {
            stringbuilder.append("[SEVERE] ");
        }
        else if (level == Level.SEVERE)
        {
            stringbuilder.append("[").append(level.getLocalizedName()).append("] ");
        }

        stringbuilder.append(par1LogRecord.getMessage());
        stringbuilder.append('\n');
        Throwable throwable = par1LogRecord.getThrown();

        if (throwable != null)
        {
            StringWriter stringwriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringwriter));
            stringbuilder.append(stringwriter.toString());
        }

        return stringbuilder.toString();
    }
}
