package org.mailzz.imgurgallery;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.mailzz.imgurgallery.models.ObjectForDetailView;

import java.util.List;

/**
 * AppWell.org
 * Created by dmitrijtrandin on 13.06.15.
 */
public class GridGalleryAdapter extends ArrayAdapter<ObjectForDetailView> {

    private static final String TAG = "GRID_GALLERY_ADAPTER";
    private Context context;
    private int layoutResourceId;
    private List<ObjectForDetailView> data;

    public GridGalleryAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = objects;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        String link = data.get(position).getImageLink();
        Glide.with(context).load(link)
                .centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .crossFade()
                .into(holder.image);
        return row;
    }

    static class ViewHolder {
        ImageView image;
    }
}
