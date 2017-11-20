package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

class PrevozAdapter extends ArrayAdapter<Prevoz> implements Filterable {

    ArrayList<Prevoz> prevozi;
    Context c;
    CustomFilter filterPrevozov;
    ArrayList<Prevoz> filterList;

    public PrevozAdapter(@NonNull Context context, ArrayList<Prevoz> prevozi) {
        super(context, R.layout.list_item, prevozi);
        this.filterList=prevozi;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_item, parent, false);

        TextView ime = customView.findViewById(R.id.ime);
        TextView cas = customView.findViewById(R.id.cas);
        TextView strosek = customView.findViewById(R.id.strosek);
        TextView iz = customView.findViewById(R.id.iz);
        TextView kam = customView.findViewById(R.id.kam);


        Prevoz prevoz = getItem(position);

        // Set data to them
        ime.setText(prevoz.getIme());
        cas.setText(prevoz.getCas());
        strosek.setText(String.valueOf(prevoz.getStrosek()));
        iz.setText(prevoz.getIz());
        kam.setText(prevoz.getKam());

        return customView;
    }

    @NonNull
    @Override
    public Filter getFilter() {

        if (filterPrevozov == null) {
            filterPrevozov = new CustomFilter();
        }

        return filterPrevozov;
    }

    // Inner class
    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length() > 0){
                // Contraint to upper for uniformity
                constraint=constraint.toString().toUpperCase();

                ArrayList<Prevoz> filters = new ArrayList<Prevoz>();

                // Za dobit zadetke
                for (int i=0; i<filterList.size(); i++){
                    if (filterList.get(i).getIz().toUpperCase().contains(constraint)){
                        Prevoz p = new Prevoz();
                        p = filterList.get(i);
                        filters.add(p);
                    }
                }

                results.count = filters.size();
                results.values = filters;
            } else {

                results.count = filterList.size();
                results.values = filterList;

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            prevozi = (ArrayList<Prevoz>) results.values;
            notifyDataSetChanged();

        }
    }

}
