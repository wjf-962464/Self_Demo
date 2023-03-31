package com.wjf.self_demo.activity

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import coil.EventListener
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.wjf.self_demo.databinding.ActivityPicLoadBinding
import com.wjf.self_demo.databinding.ItemListPicLoadBinding
import org.jxxy.debug.corekit.common.BaseActivity
import org.jxxy.debug.corekit.recyclerview.SingleTypeAdapter
import org.jxxy.debug.corekit.recyclerview.SingleViewHolder
import org.jxxy.debug.corekit.util.castToTarget

// import org.jxxy.debug.corekit.util.load

class PicLoadActivity : BaseActivity<ActivityPicLoadBinding>() {
    override fun bindLayout(): ActivityPicLoadBinding =
        ActivityPicLoadBinding.inflate(layoutInflater)

    private val adapter by lazy { PicLoadAdapter() }
    private val resList = mutableListOf<String>()
    override fun initView() {
        view.recyclerView.adapter = adapter
        for (i in 0..5) {
            resList.add("https://image.yonghuivip.com/image/16553965317886087fbdcd8848de96cf702422046b9775b69b065.gif?w=1053&h=339")
            resList.add("https://hbimg.b0.upaiyun.com/a29799b8226e8686cf2449c9f61d92956e6c731f16975d-ic0px8_fw658")
            resList.add("http://image.yonghuivip.com/yh-image-library/80E805E8-5990-447D-8190-2F27D2087D30.gif?w=250&h=250")
            resList.add("http://image.yonghuivip.com/image/16799981679442484aecf43ee6ff54d3ea6858e6ea091475896d0.gif?w=1053&h=570")
        }
        adapter.submitData(resList)

        // 设置全局唯一实例
//        Coil.setImageLoader(imageLoader)
    }

    override fun subscribeUi() {
    }

    class PicLoadAdapter : SingleTypeAdapter() {
        override fun createViewHolder(
            viewType: Int,
            inflater: LayoutInflater,
            parent: ViewGroup
        ): SingleViewHolder<ViewBinding, Any>? {
            return PicLoadHolder(
                ItemListPicLoadBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            ).castToTarget()
        }
    }
}

class PicLoadHolder(view: ItemListPicLoadBinding) :
    SingleViewHolder<ItemListPicLoadBinding, String>(
        view
    ) {
    private val imageLoader = ImageLoader.Builder(itemView.context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .eventListener(object : EventListener {
            override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                super.onSuccess(request, result)
            }

            override fun onError(request: ImageRequest, result: ErrorResult) {
                super.onError(request, result)
            }
        })
        .build()

    override fun setHolder(entity: String, context: Context) {
        view.icon.load(entity, imageLoader)
//        view.icon.load(entity)
    }
}
