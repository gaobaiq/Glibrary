package com.gbq.library.widget.indicator.viewpager;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * 类说明：
 * Author: Kuzan
 * Date: 2017/8/11 10:44.
 */
public abstract class FragmentListPageAdapter extends PagerAdapter {
    private static final String TAG = "FragmentStatePagerAdapter";
    private static final boolean DEBUG = false;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    private SparseArray<Fragment.SavedState> mSavedState = new SparseArray<>();
    private SparseArray<Fragment> mFragments = new SparseArray<>();
    private Fragment mCurrentPrimaryItem = null;

    public FragmentListPageAdapter(FragmentManager fm) {
        mFragmentManager = fm;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract Fragment getItem(int position);

    @Override
    public void startUpdate(ViewGroup container) {
    }

    public Fragment getExitFragment(int position) {
//		if (mFragments.size() > position) {
//			return mFragments.get(position);
//		}
        return mFragments.get(position);
    }

    public Fragment getCurrentFragment() {
        return mCurrentPrimaryItem;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // If we already have this item instantiated, there is nothing
        // to do. This can happen when we are restoring the entire pager
        // from its saved state, where the fragment manager has already
        // taken care of restoring the fragments we previously had instantiated.
        Fragment f = mFragments.get(position);
        if (f != null) {
            return f;
        }

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        Fragment fragment = getItem(position);
//		if (DEBUG)
//			Log.v(TAG, "Adding item #" + position + ": f=" + fragment);
        Fragment.SavedState fss = mSavedState.get(position);
        if (fss != null) {
            fragment.setInitialSavedState(fss);
        }
        fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
        mFragments.put(position, fragment);
        mCurTransaction.add(container.getId(), fragment);

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        mSavedState.put(position, mFragmentManager.saveFragmentInstanceState(fragment));
        mFragments.remove(position);

        mCurTransaction.remove(fragment);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        Bundle state = null;
        if (mSavedState.size() > 0) {
            state = new Bundle();
            state.putSparseParcelableArray("states", mSavedState.clone());
        }
        int size = mFragments.size();
        for (int i = 0; i < size; i++) {
            int index = mFragments.keyAt(i);
            Fragment f = mFragments.valueAt(i);
            if (f != null && f.isAdded()) {
                if (state == null) {
                    state = new Bundle();
                }
                String key = "f" + index;
                mFragmentManager.putFragment(state, key, f);
            }
        }
        return state;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            mSavedState.clear();
            mFragments.clear();
            if (bundle.containsKey("states")) {
                mSavedState = bundle.getSparseParcelableArray("states");
            }
            Iterable<String> keys = bundle.keySet();
            for (String key : keys) {
                if (key.startsWith("f")) {
                    int index = Integer.parseInt(key.substring(1));
                    Fragment f = mFragmentManager.getFragment(bundle, key);
                    if (f != null) {
                        f.setMenuVisibility(false);
                        mFragments.put(index, f);
                    } else {
//						Log.w(TAG, "Bad fragment at key " + key);
                    }
                }
            }
        }
    }
}
