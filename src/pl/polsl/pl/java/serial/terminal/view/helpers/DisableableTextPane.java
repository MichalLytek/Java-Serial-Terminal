package pl.polsl.pl.java.serial.terminal.view.helpers;

import javax.swing.JTextPane;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Custom extension of standard JTextPane.
 * Has ability to become readonly field.
 * 
 * @author Michał Lytek
 */
public class DisableableTextPane extends JTextPane {
    
    /** Holds information about writabilty status */
    private boolean isWritable = false;
    
    /**
     * Creates new instance and extends JTextPane with custom document filter.
     */
    public DisableableTextPane() {
        ((AbstractDocument) DisableableTextPane.this.getStyledDocument()).setDocumentFilter(new CustomDocumentFilter());
    }
    
    /**
     * Setter to the writability field.
     * @param isWritable true to enable normal behaviour, false to set readonly
     */
    public void setWritable(boolean isWritable) {
        this.isWritable = isWritable;
    }
    
    /**
     * Private class extending standard DocumentFilter.
     * It checks if the field is writable and if it is, it call super method.
     */
    private class CustomDocumentFilter extends DocumentFilter {
        
            @Override
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length) {
                if (isWritable) {
                    try {
                        super.remove(fb, offset, length);
                    } catch (BadLocationException ex) {
                    }
                }
            }

            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) {
                if (isWritable) {
                    try {
                        super.insertString(fb, offset, string, attr);
                    } catch (BadLocationException ex) {
                    }
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) {
                if (isWritable) {
                    try {
                        super.replace(fb, offset, length, text, attrs);
                    } catch (BadLocationException ex) {
                    }
                }

            }
        }
}
