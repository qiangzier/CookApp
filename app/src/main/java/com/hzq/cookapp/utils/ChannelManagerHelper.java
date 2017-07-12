package com.hzq.cookapp.utils;

import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author: hezhiqiang
 * @date: 2017/7/6
 * @version:
 * @description:
 */

public class ChannelManagerHelper {

    /**
     * 从我的频道移到其他频道
     * @param recyclerView
     * @param holder
     * @param myChannelLastPosition  我的频道最后一个item与其他频道第一个item中间间隔一个其他频道title（位置 + 1）
     * @param callback
     */
    public static void setMyChannelClick(RecyclerView recyclerView,
                                         RecyclerView.ViewHolder holder,
                                         int myChannelLastPosition,
                                         OnChannelMoveViewCallback callback){
        View targetView = recyclerView.getLayoutManager().findViewByPosition(myChannelLastPosition + 2);
        View currentView = recyclerView.getLayoutManager().findViewByPosition(holder.getAdapterPosition());
        // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
        // 如果在屏幕内,则添加一个位移动画
        if (recyclerView.indexOfChild(targetView) >= 0) {
            int targetX, targetY;

            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            int spanCount = ((GridLayoutManager) manager).getSpanCount();

            // 移动后 高度将变化 (我的频道Grid 最后一个item在新的一行第一个)
            if (myChannelLastPosition - 1 % spanCount == 0) {
                View preTargetView = recyclerView.getLayoutManager().findViewByPosition(myChannelLastPosition + 1);
                targetX = preTargetView.getLeft();
                targetY = preTargetView.getTop();
            } else {
                targetX = targetView.getLeft();
                targetY = targetView.getTop();
            }

            callback.moveMyToOther(holder);
            startAnimation(recyclerView, currentView, targetX, targetY);

        } else {
            callback.moveMyToOther(holder);
        }
    }

    public static void setOtherChannelClick(RecyclerView recyclerView,
                                            RecyclerView.ViewHolder holder,
                                            int myChannelLastPosition,
                                            OnChannelMoveViewCallback callback){
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int currentPosition = holder.getAdapterPosition();
        // 如果RecyclerView滑动到底部,移动的目标位置的y轴 - height
        View currentView = manager.findViewByPosition(currentPosition);
        // 目标位置的前一个item  即当前MyChannel的最后一个
        View preTargetView = manager.findViewByPosition(myChannelLastPosition);

        // 如果targetView不在屏幕内,则为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
        // 如果在屏幕内,则添加一个位移动画
        if (recyclerView.indexOfChild(preTargetView) >= 0) {
            int targetX = preTargetView.getLeft();
            int targetY = preTargetView.getTop();

            int targetPosition = myChannelLastPosition + 1 ;

            GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
            int spanCount = gridLayoutManager.getSpanCount();
            // target 在最后一行第一个
            if ((targetPosition - 1) % spanCount == 0) {
                View targetView = manager.findViewByPosition(targetPosition);
                targetX = targetView.getLeft();
                targetY = targetView.getTop();
            } else {
                targetX += preTargetView.getWidth();

                // 最后一个item可见
                if (gridLayoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1) {
                    // 最后的item在最后一行第一个位置
                    if ((adapter.getItemCount() - 1 - myChannelLastPosition) % spanCount == 0) {
                        // RecyclerView实际高度 > 屏幕高度 && RecyclerView实际高度 < 屏幕高度 + item.height
                        int firstVisiblePostion = gridLayoutManager.findFirstVisibleItemPosition();
                        if (firstVisiblePostion == 0) {
                            // FirstCompletelyVisibleItemPosition == 0 即 内容不满一屏幕 , targetY值不需要变化
                            // // FirstCompletelyVisibleItemPosition != 0 即 内容满一屏幕 并且 可滑动 , targetY值 + firstItem.getTop
                            if (gridLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                                int offset = (-recyclerView.getChildAt(0).getTop()) - recyclerView.getPaddingTop();
                                targetY += offset;
                            }
                        } else { // 在这种情况下 并且 RecyclerView高度变化时(即可见第一个item的 position != 0),
                            // 移动后, targetY值  + 一个item的高度
                            targetY += preTargetView.getHeight();
                        }
                    }
                } else {
                }
            }

            // 如果当前位置是otherChannel可见的最后一个
            // 并且 当前位置不在grid的第一个位置

            // 则 需要延迟250秒 notifyItemMove , 这是因为这种情况 , 并不触发ItemAnimator , 会直接刷新界面
            // 导致我们的位移动画刚开始,就已经notify完毕,引起不同步问题
            if (currentPosition == gridLayoutManager.findLastVisibleItemPosition()
                    && (currentPosition - myChannelLastPosition) % spanCount != 0
                    && (targetPosition - 1) % spanCount != 0) {
                callback.moveOtherToMyWithDelay(holder);
            } else {
                callback.moveOtherToMy(holder);
            }

            startAnimation(recyclerView,currentView,targetX,targetY);
        }else{
            callback.moveOtherToMy(holder);
        }
    }

    /**
     * 开始增删动画
     */
    private static void startAnimation(RecyclerView recyclerView, final View currentView, float targetX, float targetY) {
        final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        final ImageView mirrorView = addMirrorView(viewGroup, recyclerView, currentView);

        Animation animation = getTranslateAnimator(
                targetX - currentView.getLeft(), targetY - currentView.getTop());
        currentView.setVisibility(View.INVISIBLE);
        mirrorView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(mirrorView);
                if (currentView.getVisibility() == View.INVISIBLE) {
                    currentView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 获取位移动画
     */
    private static TranslateAnimation getTranslateAnimator(float targetX, float targetY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY);
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        translateAnimation.setDuration(360);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    private static ImageView addMirrorView(ViewGroup parent, RecyclerView recyclerView, View view) {
        /**
         * 我们要获取cache首先要通过setDrawingCacheEnable方法开启cache，然后再调用getDrawingCache方法就可以获得view的cache图片了。
         buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，若果cache没有建立，系统会自动调用buildDrawingCache方法生成cache。
         若想更新cache, 必须要调用destoryDrawingCache方法把旧的cache销毁，才能建立新的。
         当调用setDrawingCacheEnabled方法设置为false, 系统也会自动把原来的cache销毁。
         */
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        final ImageView mirrorView = new ImageView(recyclerView.getContext());
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int[] parenLocations = new int[2];
        recyclerView.getLocationOnScreen(parenLocations);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
        parent.addView(mirrorView, params);

        return mirrorView;
    }

    public interface OnChannelMoveViewCallback{

        void moveMyToOther(RecyclerView.ViewHolder holder);
        /**
         * 其他频道移到我的频道
         * @param holder
         */
        void moveOtherToMy(RecyclerView.ViewHolder holder);

        /**其他频道 移动到 我的频道 伴随延时*/
        void moveOtherToMyWithDelay(RecyclerView.ViewHolder holder);
    }


}
