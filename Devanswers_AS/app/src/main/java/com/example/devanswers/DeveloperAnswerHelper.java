package com.example.devanswers;

import com.example.devanswers.DataBase.DeveloperAnswerModel;

/**
 * Created by O'Shine on 10.04.2015.
 */
public final class DeveloperAnswerHelper {

    public static ParcelableDeveloperAnswer convertToParcelable(DeveloperAnswerModel developerAnswer) {

        return new ParcelableDeveloperAnswer(developerAnswer.getText(), developerAnswer.getSuffix());

    }

    public static DeveloperAnswerModel convertFromParcelable(ParcelableDeveloperAnswer developerAnswer) {

        return new DeveloperAnswerModel(developerAnswer.getText(), developerAnswer.getSuffix());
    }

}
