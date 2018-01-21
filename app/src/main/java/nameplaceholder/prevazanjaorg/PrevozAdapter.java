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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
        Log.d("filterListSize", "V inicializaciji: " + filterList.size());
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
        if (prevozi==null)
            prevozi = new ArrayList<Prevoz>();

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
        // Tukaj se izvaja dejansko filtriranje
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // V results se bodo shranila najdenja
            FilterResults results = new FilterResults();

            // Če contraint(query) obstaja ga damo v uppercase, da ne bo iskanje odvisno od velikih in malih črk
            if(constraint != null && constraint.length() > 0){
                Log.d("if", "query obstaja: " + constraint.toString());
                // Contraint to upper for uniformity
                constraint=constraint.toString().toUpperCase();
                // v filters bomo appendali najdene prevoze in jih potem združli v results(če je kaj bilo najdeno)
                ArrayList<Prevoz> filters = new ArrayList<>();

                // tukaj bo query razdeljen na posamezne besede(keywords)
                String[] splitSearch = constraint.toString().split(" ");

                // string kjer se bodo shranli vsi keywordi prevoza, da po njih iščem
                String keywords;

                // flag ki je true če so VSE iskalne besede bile najdene v keywords
                Boolean flag;
                Log.d("filterListSize", "Pred for: " + filterList.size());
                for (int i=0; i<filterList.size(); i++) {
                    keywords = filterList.get(i).getIz().toUpperCase();
                    keywords+= filterList.get(i).getKam().toUpperCase();
                    keywords+= filterList.get(i).getIme().toUpperCase();
                    keywords+= filterList.get(i).getDatum().toUpperCase();
                    keywords+= filterList.get(i).getMobitel().toUpperCase();
                    keywords+= filterList.get(i).getDan().toUpperCase();
                    flag = true;
                    Log.d("keywords",keywords);

                    // če je vsaka iskalna beseda v prevozu ga dodamo k filtriranim
                    for (String ss : splitSearch) {
                        if (keywords.contains(ss)) {
                            // če je flag že true(torej je tudi prejšnja iskana beseda bila najdena) potem damo spet true)
                            flag = (flag == true) ? true : false;
                        }
                        else
                            flag = false;
                    }

                    if (flag) {
                        Prevoz p = new Prevoz();
                        p = filterList.get(i);
                        filters.add(p);
                    }
                }

                /*
                // WORKING

                // Za dobit zadetke
                for (int i=0; i<filterList.size(); i++){

                    if (filterList.get(i).getIz().toUpperCase().contains(constraint)){
                        Prevoz p = new Prevoz();

                        p = filterList.get(i);
                        filters.add(p);
                    }
                }

                */

                results.count = filters.size();
                results.values = filters;
                Log.d("filterListSize", "Ko dodamo filtrirane v results: " + filterList.size());
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
