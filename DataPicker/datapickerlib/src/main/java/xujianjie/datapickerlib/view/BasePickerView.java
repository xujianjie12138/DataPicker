package xujianjie.datapickerlib.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import xujianjie.datapickerlib.R;
import xujianjie.datapickerlib.listener.OnDismissListener;
import xujianjie.datapickerlib.utils.PickerViewAnimateUtil;

public class BasePickerView
{
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

    private Context context;
    protected ViewGroup contentContainer;
    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;//附加View 的 根View

    private OnDismissListener onDismissListener;
    private boolean dismissing;

    private Animation outAnim;
    private Animation inAnim;
    private boolean isShowing;
    private int gravity = Gravity.BOTTOM;

    public BasePickerView(Context context)
    {
        this.context = context;

        initViews();
        init();
        initEvents();
    }

    private void initViews()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.pickerview_base, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentContainer = rootView.findViewById(R.id.content_container);
        contentContainer.setLayoutParams(params);
    }

    private void init()
    {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    private void initEvents()
    {
    }

    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view)
    {
        decorView.addView(view);
        contentContainer.startAnimation(inAnim);
    }

    /**
     * 添加这个View到Activity的根视图
     */
    public void show()
    {
        if (isShowing())
        {
            return;
        }
        isShowing = true;
        onAttached(rootView);
        rootView.requestFocus();
    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    private boolean isShowing()
    {
        return rootView.getParent() != null || isShowing;
    }

    protected void dismiss()
    {
        if (dismissing)
        {
            return;
        }

        dismissing = true;

        //消失动画
        outAnim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                decorView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dismissImmediately();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        contentContainer.startAnimation(outAnim);
    }

    private void dismissImmediately()
    {
        //从activity根视图移除
        decorView.removeView(rootView);
        isShowing = false;
        dismissing = false;
        if (onDismissListener != null)
        {
            onDismissListener.onDismiss(BasePickerView.this);
        }

    }

    private Animation getInAnimation()
    {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    private Animation getOutAnimation()
    {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public BasePickerView setOnDismissListener(OnDismissListener onDismissListener)
    {
        this.onDismissListener = onDismissListener;
        return this;
    }

    protected BasePickerView setOutSideCancelable(boolean isCancelable)
    {
        View view = rootView.findViewById(R.id.outmost_container);

        if (isCancelable)
        {
            view.setOnTouchListener(onCancelableTouchListener);
        }
        else
        {
            view.setOnTouchListener(null);
        }
        return this;
    }

    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                dismiss();
            }
            return false;
        }
    };

    protected View findViewById(int id)
    {
        return contentContainer.findViewById(id);
    }
}

