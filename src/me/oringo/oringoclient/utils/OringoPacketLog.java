// 
// Decompiled by Procyon v0.5.36
// 

package me.oringo.oringoclient.utils;

import java.awt.Component;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JFrame;

public class OringoPacketLog
{
    private static JFrame frame;
    private static JTextArea in;
    private static JTextArea out;
    
    public static void start() {
        (OringoPacketLog.frame = new JFrame("Oringo Packet Log")).setAlwaysOnTop(true);
        OringoPacketLog.frame.setSize(400, 200);
        final JTabbedPane tabbedPane = new JTabbedPane();
        OringoPacketLog.frame.add(tabbedPane);
        tabbedPane.addTab("In", OringoPacketLog.in = new JTextArea());
        tabbedPane.addTab("Out", OringoPacketLog.out = new JTextArea());
        OringoPacketLog.frame.setVisible(true);
    }
    
    public static void logIn(final String text) {
        OringoPacketLog.in.setText(OringoPacketLog.in.getText() + text + "\n");
        if (OringoPacketLog.in.getText().length() - OringoPacketLog.in.getText().replaceAll("\n", "").length() > 20) {
            OringoPacketLog.in.setText(OringoPacketLog.in.getText().substring(OringoPacketLog.in.getText().indexOf("\n") + 1));
        }
    }
    
    public static void logOut(final String text) {
        OringoPacketLog.out.setText(OringoPacketLog.out.getText() + text + "\n");
        if (OringoPacketLog.out.getText().length() - OringoPacketLog.out.getText().replaceAll("\n", "").length() > 20) {
            OringoPacketLog.out.setText(OringoPacketLog.out.getText().substring(OringoPacketLog.out.getText().indexOf("\n") + 1));
        }
    }
    
    public static boolean isEnabled() {
        return OringoPacketLog.frame != null && OringoPacketLog.frame.isVisible();
    }
    
    public static JFrame getFrame() {
        return OringoPacketLog.frame;
    }
    
    public static JTextArea getIn() {
        return OringoPacketLog.in;
    }
    
    public static JTextArea getOut() {
        return OringoPacketLog.out;
    }
    
    static {
        OringoPacketLog.frame = null;
        OringoPacketLog.in = null;
        OringoPacketLog.out = null;
    }
}
