package water.android.com.concrete.window;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import water.android.com.concrete.R;


/**
 * Created by EdgeDi
 * 2017/9/18 15:00
 */

public class CommonWindow extends BaseWindow {

    private MaxListView common_list;
    private List<CommonString> list;
    private CommonAdapter adapter;
    private OnContextListener onContextListener;

    public CommonWindow(Activity context) {
        super(context);
    }

    public CommonWindow(Activity context, int flag) {
        super(context, flag);
    }

    public CommonWindow(Activity context, List<? extends CommonString> list) {
        super(context);
        this.list = (List<CommonString>) list;
    }

    public CommonWindow(Activity context, int flag, List<? extends CommonString> list) {
        super(context, flag);
        this.list = (List<CommonString>) list;
    }

    public List<CommonString> getList() {
        return list;
    }

    public void setList(List<? extends CommonString> list) {
        this.list = (List<CommonString>) list;
    }

    public OnContextListener getOnContextListener() {
        return onContextListener;
    }

    public void setOnContextListener(OnContextListener onContextListener) {
        this.onContextListener = onContextListener;
    }

    @Override
    protected int getLayout() {
        return R.layout.window_common;
    }

    @Override
    protected void initUI() {
        common_list = bind(R.id.common_list);
        if (adapter == null) {
            adapter = new CommonAdapter(R.layout.window_common_item, getContext(), list);
            common_list.setAdapter(adapter);
        } else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void setListener() {
        bind(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hide();
            }
        });
        common_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getOnContextListener() != null) {
                    getOnContextListener().context(view, position, list.get(position).toResult());
                }
            }
        });
    }

    public interface OnContextListener {
        void context(View view, int position, String result);
    }
}