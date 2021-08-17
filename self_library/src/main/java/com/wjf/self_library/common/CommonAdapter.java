package com.wjf.self_library.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public abstract class CommonAdapter<Holder extends ViewDataBinding, T>
        extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {
    protected final List<T> data;
    protected Context context;
    private Holder holder;

    protected CommonAdapter() {
        this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View root = LayoutInflater.from(context).inflate(setLayout(), parent, false);
        holder = DataBindingUtil.bind(root);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final T entity = data.get(position);
        setHolder(entity, this.holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void submitList(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void resubmitList(List<T> data) {
        this.data.clear();
        submitList(data);
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    protected abstract int setLayout();

    /**
     * 绑定View
     *
     * @param entity 数据实体
     * @param view 容器
     */
    protected abstract void setHolder(T entity, Holder view);

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
