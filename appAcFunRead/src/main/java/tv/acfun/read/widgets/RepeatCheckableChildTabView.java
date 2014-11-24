package tv.acfun.read.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.harreke.easyapp.widgets.ChildTabView;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class RepeatCheckableChildTabView extends ChildTabView {
    private boolean mRepeatCheckable = false;
    private OnRepeatCheckedListener mRepeatCheckedListener = null;

    public RepeatCheckableChildTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRepeatCheckedListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mRepeatCheckable = isChecked();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (mRepeatCheckable) {
                        mRepeatCheckedListener.onRepeatChecked(this);
                    }
            }
        }

        return super.onTouchEvent(event);
    }

    public void setOnRepeatCheckedListener(OnRepeatCheckedListener repeatCheckedListener) {
        mRepeatCheckedListener = repeatCheckedListener;
    }

    public interface OnRepeatCheckedListener {
        public void onRepeatChecked(RepeatCheckableChildTabView childTabView);
    }
}