package teamvoy.com.task.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import teamvoy.com.task.R;

/**
 * Created by Lubomyr Shershun on 03.08.2015.
 * l.sherhsun@gmail.com
 */
public class RecipeCustomAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<Recipe> recipes;
    final Boolean fromInternet;
    ImageLoader imageLoader;
    ImageLoaderUtil imageLoaderUtil;


    public RecipeCustomAdapter(Context context, List<Recipe> recipes,Boolean fromInternet) {
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.recipes = recipes;
        this.fromInternet=fromInternet;

        imageLoaderUtil= new ImageLoaderUtil(context);
        imageLoader = imageLoaderUtil.getImageLoader();


    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_main, parent, false);
        }
        Recipe recipe = getRecipeConverted(position);

        ImageView img=(ImageView)view.findViewById(R.id.list_img);
        TextView header=(TextView)view.findViewById(R.id.list_header);
        TextView publisher=(TextView)view.findViewById(R.id.list_publisher);
        RatingBar rating =(RatingBar)view.findViewById(R.id.list_rating);

        imageLoader.displayImage(recipe.getImage_url(),img, imageLoaderUtil.getOptions());

        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

        header.setText(recipe.getTitle());
        publisher.setText(recipe.getPublisher());
        rating.setMax(100);
        rating.setStepSize(0.2f);
        rating.setRating(Float.parseFloat(recipe.getSocial_rank()));
        return view;
    }

    public Recipe getRecipeConverted(int position) {
        return (Recipe)getItem(position);
    }
}


