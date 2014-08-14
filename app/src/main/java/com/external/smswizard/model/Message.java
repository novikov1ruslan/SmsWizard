package com.external.smswizard.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "messages")
public class Message {
    public static final String ID_NAME = "id";
    public static final String NUMBER_NAME = "number";
    public static final String TEXT_NAME = "text";
    public static final String TIME_NAME = "time";

    @DatabaseField(id = true)
    public String id;

    @DatabaseField(canBeNull = false)
    public String number;

    @DatabaseField(canBeNull = true)
    public String text = "";

    @DatabaseField(canBeNull = false)
    public long time = System.currentTimeMillis();

    /**
     * this constructor is needed for DAO
     */
    public Message() {}

    public Message(String id, String number) {
        this.id = id;
        this.number = number;
    }

    public Message(String id, String number, String text) {
        this.id = id;
        this.number = number;
        this.text = text;
    }

    @Override
    public String toString() {
        return "[id=" + id + "; number=" + number + "; text=" + text + "]";
    }
}
