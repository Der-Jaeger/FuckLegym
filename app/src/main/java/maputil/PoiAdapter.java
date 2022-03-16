package maputil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentViewHolder;

import java.util.List;

import central.stu.fucklegym.R;

public class PoiAdapter extends ArrayAdapter<Poi> {
    private int resourceId;

    public PoiAdapter(Context context, int textViewRescourceId, List<Poi> objects){
        super(context, textViewRescourceId, objects);
        resourceId = textViewRescourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Poi poi = getItem(position);
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }else {
            view = convertView;
        }
        TextView title = (TextView) view.findViewById(R.id.poi_title);
        TextView latlng = (TextView) view.findViewById(R.id.poi_latlng);
        title.setText(poi.getTitle());
        latlng.setText(poi.getLatLonPoint().toString());
        return view;
    }
}

