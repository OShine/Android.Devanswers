package com.example.devanswers.DataBase;

import io.realm.RealmObject;

public class DeveloperAnswerModel extends RealmObject
{
    private String suffix;
    private String text;

    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String code)
    {
        this.suffix = code;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
