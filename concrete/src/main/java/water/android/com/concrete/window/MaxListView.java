package water.android.com.concrete.window;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

import water.android.com.concrete.R;

/**
 * Created by EdgeDi
 * 2017/9/18 15:02
 */

public class MaxListView extends ListView {

    private int max = dip2px(200f);

    public MaxListView(Context context) {
        super(context);
    }

    public MaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.MaxListView);
        type.getDimension(R.styleable.MaxListView_max_height, dip2px(200f));
        type.recycle();
    }

    public MaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (max > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(max,
                    MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}