package graph;

import java.util.Date;

public class Message {
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;


    public Message(String text) {
        this.asText = text;
        this.data = asText.getBytes();
        double value; //משתנה מקומי לבדיקת המרה
        try{ // ניסיון המרה של המחוזרת
           value = Double.parseDouble(text);
        }
        catch(NumberFormatException e){ //e זה המשתנה שנתפס בו החריגה
            value = Double.NaN;
        }
        this.asDouble = value;
        this.date = new Date();
    }
    public Message(double value) {
        this(Double.toString(value));
    }

    public Message(byte[] data) {
        this(new String(data));
    }
}

