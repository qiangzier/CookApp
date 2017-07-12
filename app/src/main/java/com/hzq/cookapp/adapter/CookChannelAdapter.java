package com.hzq.cookapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzq.cookapp.MessageEvent;
import com.hzq.cookapp.R;
import com.hzq.cookapp.db.entity.CategoryEntity;
import com.hzq.cookapp.helper.ItemTouchHelperAdapter;
import com.hzq.cookapp.utils.ChannelManagerHelper;
import com.hzq.cookapp.viewmodel.CookChannelViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/7/5
 * @version:
 * @description:
 */

public class CookChannelAdapter extends RecyclerView.Adapter<CookChannelAdapter.ChannelViewHolder>
        implements ItemTouchHelperAdapter{

    public static final int TYPE_MY_CHANNEL_HEADER = 0;
    public static final int TYPE_MY_CHANNEL = 1;
    public static final int TYPE_OTHER_CHANNEL_HEADER = 2;
    public static final int TYPE_OTHER_CHANNEL = 3;

    private ItemTouchHelper itemTouchHelper;
    private CookChannelViewModel viewModel;

    public void setViewModel(CookChannelViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    private int myChannelLastPosition = 0;
    private boolean isEditMode;

    //touch点击开始时间
    private long startTime;

    private List mData = new ArrayList<>();

    public void setData(List<CategoryEntity> data) {
        analyzingData(data);
        notifyDataSetChanged();
    }

    public void analyzingData(List<CategoryEntity> list){
        mData.clear();
        List<CategoryEntity> selectData = new ArrayList<>();
        List<CategoryEntity> deselectData = new ArrayList<>();
        for (CategoryEntity entity : list) {
            if(TextUtils.equals("-100",entity.getParentId())) {
                continue;
            }
            if(entity.isAddMyChannel()){
                selectData.add(entity);
            }else{
                deselectData.add(entity);
            }
        }

        mData.add("我的频道");
        mData.addAll(selectData);
        mData.add("其他频道");
        mData.addAll(deselectData);
        myChannelLastPosition = 1 + selectData.size() - 1;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if(viewType == TYPE_MY_CHANNEL_HEADER){
            return new MyHeaderChannelViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.channel_my_header_item,parent,false));
        } else if(viewType == TYPE_OTHER_CHANNEL_HEADER) {
            return new OtherHeaderViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.channel_other_header_item,parent,false));
        } else if(viewType == TYPE_MY_CHANNEL) {
            final MyChannelViewHolder holder = new MyChannelViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_my_item,parent,false));
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isEditMode) {
                        ChannelManagerHelper.setMyChannelClick((RecyclerView) parent, holder, myChannelLastPosition, callback);
                    }else{
                        Context context = parent.getContext();
                        if(context instanceof Activity){
                            EventBus.getDefault().post(new MessageEvent(holder.getAdapterPosition()));
                            Activity a = (Activity) context;
                            a.finish();
                        }
                    }
                }
            });
            holder.name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isEditMode = !isEditMode;
                    notifyDataSetChanged();
                    itemTouchHelper.startDrag(holder);
                    return true;
                }
            });
            holder.name.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(isEditMode){
                        switch (MotionEventCompat.getActionMasked(event)){
                            case MotionEvent.ACTION_DOWN:
                                startTime = System.currentTimeMillis();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if(System.currentTimeMillis() - startTime > 30){
                                    itemTouchHelper.startDrag(holder);
                                }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                startTime = 0;
                                break;
                        }
                    }
                    return false;
                }
            });
            return holder;
        } else if(viewType == TYPE_OTHER_CHANNEL) {
            final OtherViewHolder holdler = new OtherViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_other_item,parent,false));
            holdler.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChannelManagerHelper.setOtherChannelClick((RecyclerView) parent,
                                holdler, myChannelLastPosition, callback);
                }
            });
            return holdler;
        }
        return null;
    }

    private ChannelManagerHelper.OnChannelMoveViewCallback callback = new ChannelManagerHelper.OnChannelMoveViewCallback() {
        @Override
        public void moveMyToOther(RecyclerView.ViewHolder holder) {
            int position = holder.getAdapterPosition();
            CategoryEntity entity = (CategoryEntity) mData.get(position);
            entity.setAddMyChannel(false);
            mData.remove(position);
            myChannelLastPosition--;
            int targetPosition = myChannelLastPosition + 2;
            mData.add(targetPosition,entity);
            notifyItemMoved(position,targetPosition);
            if(viewModel != null)
                viewModel.update(entity);
        }

        @Override
        public void moveOtherToMy(RecyclerView.ViewHolder holder) {
            int position = holder.getAdapterPosition();
            CategoryEntity entity = (CategoryEntity) mData.get(position);
            entity.setAddMyChannel(true);
            mData.remove(position);
            myChannelLastPosition++; //没有全局刷新，这里手动处理我的频道最后伊特item位置变化
            mData.add(myChannelLastPosition,entity);
            notifyItemMoved(position,myChannelLastPosition);
            if(viewModel != null)
                viewModel.update(entity);
        }

        @Override
        public void moveOtherToMyWithDelay(RecyclerView.ViewHolder holder) {
            final int position = holder.getAdapterPosition();
            CategoryEntity entity = (CategoryEntity) mData.get(position);
            entity.setAddMyChannel(true);
            mData.remove(position);
            mData.add(myChannelLastPosition + 1,entity);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyItemMoved(position,myChannelLastPosition + 1);
                    myChannelLastPosition++;
                }
            },360);
            if(viewModel != null)
                viewModel.update(entity);
        }
    };

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        Object o = mData.get(position);
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_MY_CHANNEL_HEADER:
                final MyHeaderChannelViewHolder headerChannelViewHolder = (MyHeaderChannelViewHolder) holder;
                headerChannelViewHolder.name.setText((String)o);
                headerChannelViewHolder.editTxt.setText(isEditMode ? "完成" : "编辑");
                headerChannelViewHolder.editTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isEditMode = !isEditMode;
                        notifyDataSetChanged();
                    }
                });
                break;
            case TYPE_OTHER_CHANNEL_HEADER:
                holder.name.setText((String)o);
                break;
            case TYPE_MY_CHANNEL:
                setMyChannel((MyChannelViewHolder) holder,(CategoryEntity) o);
                break;
            case TYPE_OTHER_CHANNEL:
                setOtherChannel((OtherViewHolder) holder,(CategoryEntity) o);
                break;
        }
    }

    private void setMyChannel(MyChannelViewHolder holder,CategoryEntity entity){
        holder.name.setText(entity.getName());
        holder.editImg.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    }

    private void setOtherChannel(OtherViewHolder holder, CategoryEntity entity){
        holder.name.setText(entity.getName());
    }

    @Override
    public int getItemViewType(int position) {
        Object o = mData.get(position);
        if(position == 0){
            return TYPE_MY_CHANNEL_HEADER;
        }else if(position != 0 && o instanceof String){
            return TYPE_OTHER_CHANNEL_HEADER;
        }else if(o instanceof CategoryEntity){
            CategoryEntity entity = (CategoryEntity) o;
            if(entity.isAddMyChannel()){
                return TYPE_MY_CHANNEL;
            }else{
                return TYPE_OTHER_CHANNEL;
            }
        }
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if(fromPosition == toPosition) return;
        if(fromPosition < mData.size() && toPosition < mData.size()){
            Collections.swap(mData,fromPosition,toPosition);
            notifyItemMoved(fromPosition,toPosition);

            Object o = mData.get(fromPosition);
            Object o1 = mData.get(toPosition);
            if(o instanceof CategoryEntity && o1 instanceof CategoryEntity){
                CategoryEntity from = (CategoryEntity) o;
                CategoryEntity to = (CategoryEntity) o1;

                int index = from.getOrderIndex();
                from.setOrderIndex(to.getOrderIndex());
                to.setOrderIndex(index);

                if(viewModel != null)
                    viewModel.update(from,to);
            }
        }
    }

    @Override
    public void onSwipe(int position) {

    }

    public static class ChannelViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public ChannelViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    private class MyHeaderChannelViewHolder extends ChannelViewHolder{
        TextView editTxt;
        public MyHeaderChannelViewHolder(View itemView) {
            super(itemView);
            editTxt = (TextView) itemView.findViewById(R.id.edit);
        }
    }

    private class MyChannelViewHolder extends ChannelViewHolder{
        ImageView editImg;
        public MyChannelViewHolder(View itemView) {
            super(itemView);
            editImg = (ImageView) itemView.findViewById(R.id.img_edit);
        }
    }

    private class OtherHeaderViewHolder extends ChannelViewHolder{

        public OtherHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class OtherViewHolder extends ChannelViewHolder{

        public OtherViewHolder(View itemView) {
            super(itemView);
        }
    }
}
