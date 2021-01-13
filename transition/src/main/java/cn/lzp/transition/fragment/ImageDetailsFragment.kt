package cn.lzp.transition.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import cn.lzp.transition.databinding.FragmentImageDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


/**
 * @author li.zhipeng
 * */
class ImageDetailsFragment : Fragment() {

    private lateinit var mBinding: FragmentImageDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentImageDetailsBinding.inflate(layoutInflater, container, false)
        mBinding.root.setOnClickListener {
//            fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
        }

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("url")?.let {

            // 设置动画标签
            ViewCompat.setTransitionName(
                mBinding.imageDetail,
                "$it${arguments?.getInt("position")}"
            )

            Glide.with(this).load(it).addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment?.startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment?.startPostponedEnterTransition()
                    return false
                }

            }).into(mBinding.imageDetail)
        }
    }

}