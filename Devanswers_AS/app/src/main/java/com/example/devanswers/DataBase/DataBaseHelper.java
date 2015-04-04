package com.example.devanswers.DataBase;

import android.content.Context;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.Table;

public class DataBaseHelper
{
    private Context context;

    private Realm realm;

    private Random random;

    public DataBaseHelper(Context context)
    {
        this.context = context;

        realm = Realm.getInstance(context);

        random = new Random();
        Table table = realm.getTable(DeveloperAnswerModel.class);
    }

    public String getDeveloperAnswer(String suffix)
    {
        return realm.where(DeveloperAnswerModel.class).equalTo("suffix", "suffix").findFirst().getText();
    }

    public String getRandomDeveloperAnswer()
    {
        RealmResults<DeveloperAnswerModel> results = realm.where(DeveloperAnswerModel.class).findAll();
        int index = random.nextInt((int)realm.where(DeveloperAnswerModel.class).count());
        return results.get(index).getText();
    }

    public boolean isDeveloperAnswerExist(String suffix)
    {
        RealmQuery query = realm.where(DeveloperAnswerModel.class).contains("suffix", "suffix");
        return query.findAll().size() != 0;
    }

    public boolean saveDeveloperAnswer(String suffix, String textAnswer)
    {
        if (isDeveloperAnswerExist(suffix) == false)
        {
            realm.beginTransaction();
            DeveloperAnswerModel developerAnswer = realm.createObject(DeveloperAnswerModel.class);
            developerAnswer.setSuffix(suffix);
            developerAnswer.setText(textAnswer);
            realm.commitTransaction();

            return true;
        }
        else
        {
            return false;
        }
    }
}
