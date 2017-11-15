package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class AktivniPrevoziAdapter extends ArrayAdapter<nameplaceholder.prevazanjaorg.AktivniPrevozi> {
    public AktivniPrevoziAdapter(@NonNull Context context, ArrayList<nameplaceholder.prevazanjaorg.AktivniPrevozi> prevozi) {
        super(context, R.layout.list_item, prevozi);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_item, parent, false);
        nameplaceholder.prevazanjaorg.AktivniPrevozi prevoz = getItem(position);

        TextView ime = customView.findViewById(R.id.ime);
        TextView cas = customView.findViewById(R.id.cas);
        TextView strosek = customView.findViewById(R.id.strosek);

        ime.setText(prevoz.getIme());
        cas.setText(prevoz.getCas());
        strosek.setText(String.valueOf(prevoz.getStrosek()));

        return customView;
    }
}
