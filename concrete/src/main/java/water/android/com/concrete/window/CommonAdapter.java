package water.android.com.concrete.window;

import android.content.Context;
import android.util.Log;

import java.util.List;

import water.android.com.concrete.ConcreteAdapter;
import water.android.com.concrete.R;
import water.android.com.concrete.holder.ViewHolder;

/**
 * Created by EdgeDi
 * 2017/9/18 15:14
 */

public class CommonAdapter extends ConcreteAdapter<CommonString> {

    public CommonAdapter(int layout, Context context, List<CommonString> list) {
        super(list, context, layout);
    }

    @Override
    public void initialise(ViewHolder holder, CommonString item, int position) {
        holder.setText(R.id.common_text, item.toResult());
    }
}
