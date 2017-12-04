package water.android.com.concrete.window;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import water.android.com.concrete.R;

/**
 * Created by EdgeDi
 * 2017/9/18 14:27
 */

public abstract class BaseWindow {

    private PopupWindow window;
    private Activity context;
    private View view;
    private int flag = 0;

    private OnWindowListener listener;

    public BaseWindow(Activity context) {
        this.context = context;
        views = new SparseArray<>();
    }

    public BaseWindow(Activity context, int flag) {
        this.context = context;
        this.flag = flag;
        views = new SparseArray<>();
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public OnWindowListener getListener() {
        return listener;
    }

    public void setListener(OnWindowListener listener) {
        this.listener = listener;
    }

    private void onCreate() {
        if (window == null) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(getLayout(), null);
                initUI();
                setListener();
            }
            window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            if (flag == 0) {
                window.setBackgroundDrawable(new BitmapDrawable());
                window.setOutsideTouchable(true);
            } else {
                window.setOutsideTouchable(true);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            window.setAnimationStyle(R.style.popup_window_anim);
            window.setTouchable(true);
            window.setFocusable(true);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (flag == 0) {
                        setLuminance(1.0f);
                    }
                    if (getListener() != null) getListener().dismiss();
                }
            });
        }
    }

    public void renovate() {
        if (view != null) {
            initUI();
            setListener();
        }
    }

    public void Show(View view) {
        onCreate();
        if (flag == 0) {
            setLuminance(0.5f);
        }
        window.showAsDropDown(view);
    }

    public void ShowBottom(View view) {
        onCreate();
        if (flag == 0) {
            setLuminance(0.5f);
        }
        window.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public void Hide() {
        if (window.isShowing()) {
            window.dismiss();
        }
    }

    private void setLuminance(float level) {
        WindowManager.LayoutParams lp = context.getWindow()
                .getAttributes();
        lp.alpha = level;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    private SparseArray<View> views;

    protected <T extends View> T bind(@IdRes int rid) {
        if (views.get(rid) == null) {
            views.append(rid, view.findViewById(rid));
        }
        return (T) views.get(rid);
    }

    @LayoutRes
    protected abstract int getLayout();

    protected abstract void initUI();

    protected abstract void setListener();

    public interface OnWindowListener {
        void dismiss();
    }

}