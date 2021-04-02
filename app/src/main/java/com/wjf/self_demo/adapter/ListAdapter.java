package com.wjf.self_demo.adapter;

import android.content.Context;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ItemListTestBinding;
import com.wjf.self_demo.entity.UserInfo;
import com.wjf.self_library.common.CommonAdapter;

import java.util.List;

/**
 * @author Wangjf2-DESKTOP
 */
public class ListAdapter extends CommonAdapter<ItemListTestBinding, UserInfo> {

    public ListAdapter(Context context, List<UserInfo> data) {
        super(context, data);
    }

    @Override
    protected int setLayout() {
        return R.layout.item_list_test;
    }

    @Override
    protected void setHolder(UserInfo entity, ItemListTestBinding holder) {
        holder.userName.setText(entity.getName());
        holder.userDes.setText(entity.getDes());
    }
}
