package com.example.ProjectMobileCalcMent.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Pair;

import com.example.ProjectMobileCalcMent.Entities.Score;

import java.util.ArrayList;
import java.util.List;

// ScoreDao.java
public class ScoreDao extends BaseDao<Score>{

    public static String userName = "USER_NAME";

    public static String score = "SCORE";

    public static String tableName = "SCORES";
    public ScoreDao(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected void putValues(ContentValues values, Score entity) {
        values.put(userName, entity.getUsername());
        values.put(score, entity.getScore());
    }

    @Override
    protected Score getEntity(Cursor cursor) {
        Score score = new Score();

        Integer indexUserName = cursor.getColumnIndex(userName);
        Integer indexScore = cursor.getColumnIndex(ScoreDao.score);
        score.setUsername(cursor.getString(indexUserName));
        score.setScore(cursor.getInt(indexScore));

        return score;
    }

    public List<Score> getAllScores() {
        String sortOrder = score + " DESC";
        return getEntities(sortOrder);
    }

}