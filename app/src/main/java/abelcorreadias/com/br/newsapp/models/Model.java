package abelcorreadias.com.br.newsapp.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public abstract class Model<T> implements Serializable {

    private static final String LOG_TAG = Model.class.getSimpleName();

    public T id;

    public JSONObject toJSON(){

        JSONObject result = new JSONObject();

        for(Field field : getClass().getDeclaredFields()){
            try {
                Object obj = field.get(this);

                //null objects just continue the loop
                if(obj == null) continue;

                if(obj.getClass().isArray()){
                    int arraySize = Array.getLength(obj);
                    JSONArray jsonArray = new JSONArray();
                    for(int i = 0; i < arraySize; i++){
                        Object innerObject = Array.get(obj, i);
                        if(innerObject instanceof Model){
                            JSONObject innerJson = ((Model) innerObject).toJSON();
                            jsonArray.put(innerJson);
                        }else{
                            jsonArray.put(innerObject);
                        }
                    }
                    result.put(field.getName(), jsonArray);
                }else if(obj instanceof Model){
                    JSONObject innerJson = ((Model) obj).toJSON();
                    result.put(field.getName(), innerJson);
                }else result.put(field.getName(), obj);

            } catch (IllegalAccessException e) {
                Log.e(LOG_TAG, e.toString());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.toString());
                e.printStackTrace();
            }
        }
        result.remove("serialVersionUID");
        return result;

    }

    public abstract String toString();

}
