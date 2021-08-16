package com.wjf.self_demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ItemListIndexBinding;
import com.wjf.self_demo.entity.IndexListMenu;
import com.wjf.self_library.common.CommonAdapter;

import java.util.List;

/**
 * @author : Wangjf
 * @date : 2021/4/6
 */
public class IndexListAdapter extends CommonAdapter<ItemListIndexBinding, IndexListMenu> {


    @Override
    protected int setLayout() {
        return R.layout.item_list_index;
    }

    @Override
    protected void setHolder(IndexListMenu entity, ItemListIndexBinding view) {
        view.menuText.setText(entity.getDes());
        view.menuText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("data", entity.getData());
                        intent.setClass(context, entity.getGotoClass());
                        context.startActivity(intent);
                    }
                });
    }
}
