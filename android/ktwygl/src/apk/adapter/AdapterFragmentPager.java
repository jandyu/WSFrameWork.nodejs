package apk.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class AdapterFragmentPager extends FragmentPagerAdapter implements RadioGroup.OnCheckedChangeListener, OnPageChangeListener
{
	private ViewPager viewPager;
	private ArrayList<Fragment> fragmentsList;
	private RadioGroup radioGroups; // 用于切换tab
	private int currentTabIndex; // 当前Tab页面索引
	public AdapterFragmentPager(FragmentManager fm)
	{
		super(fm);
	}
	
	public AdapterFragmentPager(FragmentManager fragmentManager, ArrayList<Fragment> fragmentsList, ViewPager viewPager, RadioGroup radioGroups)
	{
		super(fragmentManager);
		
		this.viewPager = viewPager;
		this.viewPager.setOnPageChangeListener(this);
		this.fragmentsList = fragmentsList;
		this.radioGroups = radioGroups;
		
		this.radioGroups.setOnCheckedChangeListener(this);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		//super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		return super.instantiateItem(container, position);
	}

	@Override
	public int getCount()
	{
		return fragmentsList.size();
	}
	
	@Override
	public Fragment getItem(int arg0)
	{
		return fragmentsList.get(arg0);
	}
	
	@Override
	public int getItemPosition(Object object)
	{
		return super.getItemPosition(object);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		for (int i = 0; i < this.radioGroups.getChildCount(); i++)
		{
			if (this.radioGroups.getChildAt(i).getId() == checkedId)
			{
				this.setCurrentTab(i);
			}
		}
	}
	
	public int getCurrentTab()
	{
		return currentTabIndex;
	}
	/**
	 * 当前页是否最后一页
	 * @return 是否最后一页
	 */
	public boolean isLastPage()
	{
		if(this.currentTabIndex == this.fragmentsList.size() - 1)
		{
			return true;
		}
		return false;
	}
	
	public void setCurrentTab(int index)
	{
		if(this.currentTabIndex != index)
		{
			this.currentTabIndex = index;
			
			this.viewPager.setCurrentItem(index);
			
			this.radioGroups.setOnCheckedChangeListener(null);
			this.radioGroups.check(this.radioGroups.getChildAt(this.currentTabIndex).getId());
			this.radioGroups.setOnCheckedChangeListener(this);
		}
	}

	public Fragment getCurrentFragment()
	{
		return this.fragmentsList.get(this.currentTabIndex);
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
		
	}

	@Override
	public void onPageSelected(int index)
	{
		this.setCurrentTab(index);
	}
	
}
