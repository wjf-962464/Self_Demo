package com.wjf.self_library.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public abstract class CommonAdapter<Holder extends ViewDataBinding, T>
        extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {
    protected final List<T> data;
    private final Context context;
    private Holder holder;

    protected CommonAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        return data == null ? 0 : data.size();
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
     * @param holder 容器
     */
    protected abstract void setHolder(T entity, Holder holder);

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
