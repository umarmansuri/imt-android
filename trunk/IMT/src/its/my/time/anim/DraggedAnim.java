package its.my.time.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public class DraggedAnim extends Animation {
    float paddingWidth;
    View view;

    public DraggedAnim(View view, float paddingWidth) {
        this.view = view;
        this.paddingWidth = paddingWidth;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.width = (int) (view.getMeasuredWidth() + paddingWidth * interpolatedTime);
        params.leftMargin = (int) (-paddingWidth * interpolatedTime)/2;
        view.setLayoutParams(params);
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
            int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}