package teamvoy.com.task.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Lubomyr Shershun on 04.08.2015.
 * l.sherhsun@gmail.com
 *
 * dont use this class methods inside MAIN thread
 */
public class JSONUtil {
    final String LOG_TAG=this.getClass().getSimpleName();

    private URLBuilder builder;
    private String ingredients=null, sort=null;



    public JSONUtil() {
        builder=new URLBuilder();
    }

    public JSONUtil(String ingredients, String sort) {
        this.ingredients = ingredients;
        this.sort = sort;
        builder=new URLBuilder();
    }
    private String getRecipeJsonData(URL url){
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        StringBuffer buffer;
        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Asynctask Error", e);
            // If the code didn't successfully get the recipe data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return buffer.toString();
    }
    public List<Recipe> getSearchList(int page) {
        List<Recipe> list=new ArrayList<>();
        Recipe recipe;
        URL url = null;
        try {
            url = new URL(builder.getSearchURL(page, ingredients, sort));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String recipeJsonData=getRecipeJsonData(url);


        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "recipes";
        final String OWM_PUBLISHER = "publisher";
        final String OWM_TITLE = "title";
        final String OWM_SOURCE_URL = "source_url";
        final String OWM_RECIPE_ID = "recipe_id";
        final String OWM_IMAGE_URL = "image_url";
        final String OWM_SOCIAL_RANK = "social_rank";
        final String OWM_PUBLISHER_URL = "publisher_url";

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(recipeJsonData);

        JSONArray recipesArray = jsonObject.getJSONArray(OWM_LIST);

            for (int i=0;i<recipesArray.length();i++) {
                recipe = new Recipe();

                JSONObject recipeObj=(JSONObject)recipesArray.get(i);
                recipe.setPublisher(recipeObj.getString(OWM_PUBLISHER));
                recipe.setTitle(recipeObj.getString(OWM_TITLE));
                recipe.setSource_url(recipeObj.getString(OWM_SOURCE_URL));
                recipe.setRecipe_id(recipeObj.getString(OWM_RECIPE_ID));
                recipe.setImage_url(recipeObj.getString(OWM_IMAGE_URL));
                recipe.setSocial_rank(recipeObj.getString(OWM_SOCIAL_RANK));
                recipe.setPublisher_url(recipeObj.getString(OWM_PUBLISHER_URL));
                list.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Recipe getRecipe(String rId)  {
        Recipe recipe;
        String recipeJsonData;
        URL url = null;
        try {
            url = new URL(builder.getGET_URL(rId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        recipeJsonData=getRecipeJsonData(url);
        final String OWM_LIST = "recipe";
        final String OWM_PUBLISHER = "publisher";
        final String OWM_TITLE = "title";
        final String OWM_SOURCE_URL = "source_url";
        final String OWM_RECIPE_ID = "recipe_id";
        final String OWM_IMAGE_URL = "image_url";
        final String OWM_SOCIAL_RANK = "social_rank";
        final String OWM_PUBLISHER_URL = "publisher_url";
        final String OWM_INGREDIENTS = "ingredients";

        JSONObject jsonObject = null;
        recipe=new Recipe();
        try {
            jsonObject = new JSONObject(recipeJsonData);
        JSONObject recipeObj=(JSONObject)jsonObject.getJSONObject(OWM_LIST);

        recipe.setPublisher(recipeObj.getString(OWM_PUBLISHER));
        recipe.setTitle(recipeObj.getString(OWM_TITLE));
        recipe.setSource_url(recipeObj.getString(OWM_SOURCE_URL));
        recipe.setRecipe_id(recipeObj.getString(OWM_RECIPE_ID));
        recipe.setImage_url(recipeObj.getString(OWM_IMAGE_URL));
        recipe.setSocial_rank(recipeObj.getString(OWM_SOCIAL_RANK));
        recipe.setPublisher_url(recipeObj.getString(OWM_PUBLISHER_URL));
        recipe.setIngredients(recipeObj.getString(OWM_INGREDIENTS));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipe;
    }
}
