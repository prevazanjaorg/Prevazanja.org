package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
        this.c=context;
        this.prevozi=prevozi;
        this.filterList=prevozi;
    }

    @Override
    public int getCount() {
        return prevozi.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (convertView==null)
            convertView = inflater.inflate(R.layout.list_item, parent, false);

        //Log.d("T4-prevozi(size):",String.valueOf(prevozi.size()));
        Log.d("T4-position:",String.valueOf(position));

        if (prevozi==null)
            prevozi = new ArrayList<Prevoz>();

        //if (prevozi!=null && position < prevozi.size()) {
            TextView ime = convertView.findViewById(R.id.ime);
            TextView cas = convertView.findViewById(R.id.cas);
            TextView strosek = convertView.findViewById(R.id.strosek);
            TextView iz = convertView.findViewById(R.id.iz);
            TextView kam = convertView.findViewById(R.id.kam);


            // Set data to them
            ime.setText(prevozi.get(position).getIme());
            cas.setText(prevozi.get(position).getCas());
            strosek.setText(String.valueOf(prevozi.get(position).getStrosek()));
            iz.setText(prevozi.get(position).getIz());
            kam.setText(prevozi.get(position).getKam());

            Log.d("T3-getview", "se izvaja");
        //}
        return convertView;
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

                ArrayList<Prevoz> filters = new ArrayList<>();

                // Za dobit zadetke
                for (int i=0; i<filterList.size(); i++){
                    if (filterList.get(i).getIz().toUpperCase().contains(constraint)){
                        Log.d("T3-constraint",constraint.toString());
                        Log.d("T3-getIz()",filterList.get(i).getIz().toUpperCase());
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
