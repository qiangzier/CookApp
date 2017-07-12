package com.hzq.cookapp.helper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @author: hezhiqiang
 * @date: 2017/7/5
 * @version:
 * @description: RecyclerView拖拽排序
 */

public class ItemDragHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter itemTouchHelperAdapter;
    private boolean isLongPressDragEnabled = false;
    private boolean isSwipeEnabled = false;

    public ItemDragHelperCallback(ItemTouchHelperAdapter adapter){
        if(adapter == null)
            throw new NullPointerException("ItemTouchHelperAdapter is null");
        itemTouchHelperAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags;
        int swipeFlags = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager){
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        }else{
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        }
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //不同Type之间不可移动
        if(viewHolder.getItemViewType() != target.getItemViewType())
            return false;

        itemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        itemTouchHelperAdapter.onSwipe(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isSwipeEnabled;
    }

    public void setLongPressDragEnabled(boolean longPressDragEnabled) {
        isLongPressDragEnabled = longPressDragEnabled;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        isSwipeEnabled = swipeEnabled;
    }
}
