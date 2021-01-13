package cn.lzp.transition.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import cn.lzp.transition.R
import cn.lzp.transition.databinding.FragmentListBinding
import cn.lzp.transition.databinding.ItemImageBinding
import com.bumptech.glide.Glide


/**
 * @author li.zhipeng
 * */
class ListFragment : Fragment() {

    private lateinit var mBinding: FragmentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)

        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String?>, sharedElements: MutableMap<String?, View?>
                ) {
                    val selectedViewHolder: RecyclerView.ViewHolder? =
                        mBinding.list.findViewHolderForAdapterPosition(Images.position)
                    if (selectedViewHolder?.itemView == null) {
                        return
                    }
                    sharedElements[names[0]] =
                        selectedViewHolder.itemView.findViewById(R.id.image)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentListBinding.inflate(inflater, container, false)
        initList()
        postponeEnterTransition()
        return mBinding.root
    }

    private fun initList() {
        mBinding.list.layoutManager = GridLayoutManager(requireContext(), 2)
        mBinding.list.adapter = ImageAdapter(
            Images.list
        )

        mBinding.list.addOnLayoutChangeListener(
            object : OnLayoutChangeListener {
                override fun onLayoutChange(
                    view: View,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    mBinding.list.removeOnLayoutChangeListener(this)
                    val layoutManager: RecyclerView.LayoutManager =
                        mBinding.list.layoutManager ?: return
                    val viewAtPosition =
                        layoutManager.findViewByPosition(Images.position)
                    if (viewAtPosition == null
                        || layoutManager.isViewPartiallyVisible(viewAtPosition, false, true)
                    ) {
                        mBinding.list.post { layoutManager.scrollToPosition(Images.position) }
                    }
                }
            })

    }

    private fun startDetailsFragment(imageView: ImageView, position: Int) {
        Images.position = position
        fragmentManager?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
            )
            ?.addSharedElement(imageView, imageView.transitionName)
            ?.replace(R.id.fragment_container, ImageViewPagerFragment().apply {
                this.arguments = Bundle().apply {
                    putInt("position", position)
                }
            }, "")
            ?.addToBackStack(null)
            ?.commit()
    }


    inner class ImageAdapter(private val data: List<String>) :
        RecyclerView.Adapter<ItemViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ItemViewHolder {
            return ItemViewHolder(
                ItemImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(data[position % data.size], position)
        }

        override fun getItemCount(): Int {
            return 100
        }

    }

    inner class ItemViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(s: String, position: Int) {
            // 设置动画标签
            ViewCompat.setTransitionName(binding.image, "$s$position")
            Glide.with(binding.root.context).load(s).into(binding.image)
            startPostponedEnterTransition()
            binding.root.setOnClickListener {
                startDetailsFragment(binding.image, position)
            }
        }

    }

}