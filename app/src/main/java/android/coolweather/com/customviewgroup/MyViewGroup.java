package android.coolweather.com.customviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mao on 2018/2/12.
 */

public class MyViewGroup extends ViewGroup {
    private Context mcontext;
    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext=context;
    }

    public MyViewGroup(Context context) {
        this(context,null);
    }

    /*
    * 使ViewGroup能够支持margin
    * */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(mcontext,attrs);
    }

    /*
    * 根据MyViewGroup的父容器建议的widthMeasureSpec，heightMeasureSpec来调用MyViewGroup的setMeasuredDimension（）；
    * 如果是widthMode==MeasureSpec.EXACTLY代表MyViewGroup布局参数设置为具体值或march_parent此时直接设值，
    * 否则若是MeasureSpec.AT_MOST代表MyViewGroup布局参数设置为wrap_content
    * 此时MyViewGroup的宽高应该刚好包裹子view
    * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此MyViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        int childCount=getChildCount();//MyViewGroup孩子的数量
        int width=0;
        int height=0;

        int twidth=0;
        int bwidth=0;
        int lheight=0;
        int rheight=0;

        for (int i=0;i<childCount;i++){//遍历所有的孩子
            View child=getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);//测量孩子
            int ch=child.getMeasuredHeight();//孩子的宽高
            int cw=child.getMeasuredWidth();
            MarginLayoutParams layoutParams=(MarginLayoutParams)child.getLayoutParams();
            if(i==0||i==1){
                twidth+=layoutParams.leftMargin+cw+layoutParams.rightMargin;
            }
            if (i==2||i==3){
                bwidth+=layoutParams.leftMargin+cw+layoutParams.rightMargin;
            }
            if (i==0||i==2){
                lheight+=layoutParams.topMargin+ch+layoutParams.bottomMargin;
            }
            if (i==1||i==3){
                rheight+=layoutParams.topMargin+ch+layoutParams.bottomMargin;
            }

            width=Math.max(twidth,bwidth);
            height=Math.max(lheight,rheight);

            setMeasuredDimension((widthMode==MeasureSpec.EXACTLY)?widthSize:width,
                    (heightMode==MeasureSpec.EXACTLY)?heightSize:height);
        }
    }

    /*
    * onLayout对其所有childView进行定位（设置childView的绘制区域）
    * */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount=getChildCount();

        for (int i=0;i<childCount;i++){
            View child=getChildAt(i);
            int cw=child.getMeasuredWidth();
            int ch=child.getMeasuredHeight();
            MarginLayoutParams layoutParams=(MarginLayoutParams)child.getLayoutParams();

            int ml=0;
            int mt=0;
            int mr=0;
            int mb=0;
            switch (i){
                case 0:
                    ml=layoutParams.leftMargin;
                    mt=layoutParams.topMargin;
                    break;
                case 1:
                    ml=getWidth()-cw-layoutParams.rightMargin;
                    mt=layoutParams.topMargin;
                    break;
                case 2:
                    ml=layoutParams.leftMargin;
                    mt=getHeight()-ch-layoutParams.bottomMargin;
                    break;
                case 3:
                    ml=getWidth()-cw-layoutParams.rightMargin;
                    mt=getHeight()-ch-layoutParams.bottomMargin;
                    break;
            }
            mr=ml+cw;
            mb=mt+ch;
            child.layout(ml,mt,mr,mb);
        }
    }
}
