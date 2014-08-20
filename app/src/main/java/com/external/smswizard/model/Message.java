package com.external.smswizard.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "messages")
public class Message {
    public static final String ID_NAME = "id";
    public static final String PHONE_NAME = "phone";
    public static final String TEXT_NAME = "text";
    public static final String TIME_NAME = "time";

    @DatabaseField(id = true)
    public String id;

    @DatabaseField(canBeNull = false)
    public String phone;

    @DatabaseField(canBeNull = true)
    public String text = "";

    @DatabaseField(canBeNull = false)
    public long time = System.currentTimeMillis();

    /**
     * this constructor is needed for DAO
     */
    public Message() {}

    public Message(String id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    public Message(String id, String phone, String text) {
        this.id = id;
        this.phone = phone;
        this.text = text;
    }

    @Override
    public String toString() {
        return "[id=" + id + "; phone=" + phone + "; text=" + text + "]";
    }
}
