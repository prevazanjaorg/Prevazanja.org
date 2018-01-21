package nameplaceholder.prevazanjaorg;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Niko on 23. 12. 2017.
 */

class CustomSwipeAdapter extends PagerAdapter {
    private int[] images_resources={R.drawable.himeji,R.drawable.ic_launcher_background,R.drawable.chiang_mai,R.drawable.ulm};
    private Context ctx;
    private LayoutInflater layoutInflater;
    public CustomSwipeAdapter(Context ctx){
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        return images_resources.length;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view==((ConstraintLayout)object);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView =item_view.findViewById(R.id.image_view);
        TextView textView=(TextView) item_view.findViewById(R.id.image_count);
        imageView.setImageResource(images_resources[position]);
        position++;
        textView.setText(position+ "/"+getCount());
        container.addView(item_view);
        return item_view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout)object);
    }

}
