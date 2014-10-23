//package org.springframework.beans.propertyeditors;
package net.sinequanon.spring;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Property editor for java.sql.Timestamp, supporting SimpleDateFormat.
 *
 * Using default Constructor uses the pattern yyyy-MM-dd
 * Using the constructor with String, you can use your own pattern.
 *
 * from http://adfinmunich.blogspot.de/2011/04/how-to-write-sqltimestamppropertyeditor.html
 *
 * @author alex
 */
public class SqlTimestampPropertyEditor extends PropertyEditorSupport {

    public static final String DEFAULT_BATCH_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final SimpleDateFormat sdf;

    /**
     * uses default pattern yyyy-MM-dd for date parsing.
     */
    public SqlTimestampPropertyEditor() {
        this.sdf = new SimpleDateFormat(SqlTimestampPropertyEditor.DEFAULT_BATCH_PATTERN);
    }

    /**
     * Uses the given pattern for dateparsing, see {@link SimpleDateFormat} for allowed patterns.
     *
     * @param pattern
     *            the pattern describing the date and time format
     * @see SimpleDateFormat#SimpleDateFormat(String)
     */
    public SqlTimestampPropertyEditor(String pattern) {
        this.sdf = new SimpleDateFormat(pattern);
    }

    /**
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        try {
            setValue(new Timestamp(this.sdf.parse(text).getTime()));
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
        }
    }

    /**
     * Format the Timestamp as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Timestamp value = (Timestamp) getValue();
        return (value != null ? this.sdf.format(value) : "");
    }
}
