package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.example.devanswers.DataBase.DeveloperAnswerModel;
import io.realm.RealmObject;
import io.realm.exceptions.RealmException;
import io.realm.internal.ColumnType;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.LinkView;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeveloperAnswerModelRealmProxy extends DeveloperAnswerModel {

    @Override
    public String getSuffix() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(Realm.columnIndices.get("DeveloperAnswerModel").get("suffix"));
    }

    @Override
    public void setSuffix(String value) {
        realm.checkIfValid();
        row.setString(Realm.columnIndices.get("DeveloperAnswerModel").get("suffix"), (String) value);
    }

    @Override
    public String getText() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(Realm.columnIndices.get("DeveloperAnswerModel").get("text"));
    }

    @Override
    public void setText(String value) {
        realm.checkIfValid();
        row.setString(Realm.columnIndices.get("DeveloperAnswerModel").get("text"), (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if(!transaction.hasTable("class_DeveloperAnswerModel")) {
            Table table = transaction.getTable("class_DeveloperAnswerModel");
            table.addColumn(ColumnType.STRING, "suffix");
            table.addColumn(ColumnType.STRING, "text");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_DeveloperAnswerModel");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if(transaction.hasTable("class_DeveloperAnswerModel")) {
            Table table = transaction.getTable("class_DeveloperAnswerModel");
            if(table.getColumnCount() != 2) {
                throw new IllegalStateException("Column count does not match");
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for(long i = 0; i < 2; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }
            if (!columnTypes.containsKey("suffix")) {
                throw new IllegalStateException("Missing column 'suffix'");
            }
            if (columnTypes.get("suffix") != ColumnType.STRING) {
                throw new IllegalStateException("Invalid type 'String' for column 'suffix'");
            }
            if (!columnTypes.containsKey("text")) {
                throw new IllegalStateException("Missing column 'text'");
            }
            if (columnTypes.get("text") != ColumnType.STRING) {
                throw new IllegalStateException("Invalid type 'String' for column 'text'");
            }
        }
    }

    public static List<String> getFieldNames() {
        return Arrays.asList("suffix", "text");
    }

    public static void populateUsingJsonObject(DeveloperAnswerModel obj, JSONObject json)
        throws JSONException {
        boolean standalone = obj.realm == null;
        if (!json.isNull("suffix")) {
            obj.setSuffix((String) json.getString("suffix"));
        }
        if (!json.isNull("text")) {
            obj.setText((String) json.getString("text"));
        }
    }

    public static void populateUsingJsonStream(DeveloperAnswerModel obj, JsonReader reader)
        throws IOException {
        boolean standalone = obj.realm == null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("suffix") && reader.peek() != JsonToken.NULL) {
                obj.setSuffix((String) reader.nextString());
            } else if (name.equals("text")  && reader.peek() != JsonToken.NULL) {
                obj.setText((String) reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }

    public static DeveloperAnswerModel copyOrUpdate(Realm realm, DeveloperAnswerModel object, boolean update, Map<RealmObject,RealmObject> cache) {
        return copy(realm, object, update, cache);
    }

    public static DeveloperAnswerModel copy(Realm realm, DeveloperAnswerModel newObject, boolean update, Map<RealmObject,RealmObject> cache) {
        DeveloperAnswerModel realmObject = realm.createObject(DeveloperAnswerModel.class);
        cache.put(newObject, realmObject);
        realmObject.setSuffix(newObject.getSuffix() != null ? newObject.getSuffix() : "");
        realmObject.setText(newObject.getText() != null ? newObject.getText() : "");
        return realmObject;
    }

    static DeveloperAnswerModel update(Realm realm, DeveloperAnswerModel realmObject, DeveloperAnswerModel newObject, Map<RealmObject, RealmObject> cache) {
        realmObject.setSuffix(newObject.getSuffix() != null ? newObject.getSuffix() : "");
        realmObject.setText(newObject.getText() != null ? newObject.getText() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("DeveloperAnswerModel = [");
        stringBuilder.append("{suffix:");
        stringBuilder.append(getSuffix());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{text:");
        stringBuilder.append(getText());
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        String realmName = realm.getPath();
        String tableName = row.getTable().getName();
        long rowIndex = row.getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeveloperAnswerModelRealmProxy aDeveloperAnswerModel = (DeveloperAnswerModelRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aDeveloperAnswerModel.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aDeveloperAnswerModel.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aDeveloperAnswerModel.row.getIndex()) return false;

        return true;
    }

}
