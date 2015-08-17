package teamvoy.com.task.utils;

import android.net.Uri;

/**
 * Created by Lubomyr Shershun on 04.08.2015.
 * l.sherhsun@gmail.com
 */
public class URLBuilder {

    final String BASE_URL_SEARCH ="http://food2fork.com/api/search?key=";
    final String BASE_URL_GET="http://food2fork.com/api/get?key=";
    final String apiKey="c25382fd3c4b082e249a64dbead7be30";
    final String QUERY_PARAM = "q";
    final String SORT_PARAM = "sort";
    final String PAGE_PARAM = "page";
    final String RECIPE_ID = "rId";





    private int page =1;



    /**
     *
             * @param page           results page.
     *       * @param ingredients   (optional) ingredients, separate by comas.
     *       * @param sort          (optional) sorting. "r"=top rated, "t"=trendiness.

            */
    public String getSearchURL(int page, String ingredients, String sort){
        if (page>0) {
            if(ingredients!=null) {
                if (sort!=null) {
                    Uri builtUri = Uri.parse(BASE_URL_SEARCH + apiKey).buildUpon()
                            .appendQueryParameter(QUERY_PARAM, ingredients)
                            .appendQueryParameter(SORT_PARAM, sort)
                            .appendQueryParameter(PAGE_PARAM, ""+page)
                            .build();
                    return builtUri.toString();
                }
                else {
                    Uri builtUri = Uri.parse(BASE_URL_SEARCH + apiKey).buildUpon()
                            .appendQueryParameter(QUERY_PARAM, ingredients)
                            .appendQueryParameter(PAGE_PARAM, "" + page)
                            .build();
                    return builtUri.toString();
                }
            }
            else {
                if (sort!=null) {
                    Uri builtUri = Uri.parse(BASE_URL_SEARCH + apiKey).buildUpon()
                            .appendQueryParameter(SORT_PARAM, sort)
                            .appendQueryParameter(PAGE_PARAM, ""+page)
                            .build();
                    return builtUri.toString();
                }
                else {
                    Uri builtUri = Uri.parse(BASE_URL_SEARCH + apiKey).buildUpon()
                            .appendQueryParameter(PAGE_PARAM, "" + page)
                            .build();
                    return builtUri.toString();
                }
            }

        }
        return null;
    }

    /**
     *
     * @param rId           id of recipe needed.
     */
    public String getGET_URL(String rId) {
        Uri builtUri = Uri.parse(BASE_URL_GET + apiKey).buildUpon()
                .appendQueryParameter(RECIPE_ID, rId)
                .build();
        return builtUri.toString();
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}