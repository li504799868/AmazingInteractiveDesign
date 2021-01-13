package cn.lzp.transition.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.transition.TransitionInflater
import androidx.viewpager.widget.ViewPager
import cn.lzp.transition.R
import cn.lzp.transition.databinding.FragmentViewpagerBinding


/**
 * @author li.zhipeng
 * */
class ImageViewPagerFragment : Fragment() {

    private lateinit var mBinding: FragmentViewpagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentViewpagerBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        mBinding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                Images.position = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
        mBinding.viewpager.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {

            override fun getCount(): Int = 100

            override fun getItem(position: Int): Fragment {
                return ImageDetailsFragment().apply {
                    this.arguments = Bundle().apply {
                        putString("url", Images.list[position % Images.list.size])
                        putInt("position", position)
                    }
                }
            }
        }

        mBinding.viewpager.setCurrentItem(arguments?.getInt("position") ?: 0, false)

        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String?>, sharedElements: MutableMap<String?, View?>
                ) {
                    val currentFragment = mBinding.viewpager.adapter?.instantiateItem(
                        mBinding.viewpager,
                        mBinding.viewpager.currentItem
                    ) as Fragment
                    val view = currentFragment.view ?: return
                    sharedElements[names[0]] = view.findViewById(R.id.image_detail)
                }
            }
        )
    }
}