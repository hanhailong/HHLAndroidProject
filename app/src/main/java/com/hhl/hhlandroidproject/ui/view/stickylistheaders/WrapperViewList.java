package com.hhl.hhlandroidproject.ui.view.stickylistheaders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hhl.hhlandroidproject.ui.view.observable.ObservableScrollViewCallbacks;
import com.hhl.hhlandroidproject.ui.view.observable.ScrollState;
import com.hhl.hhlandroidproject.ui.view.swipemenulistview.SwipeMenu;
import com.hhl.hhlandroidproject.ui.view.swipemenulistview.SwipeMenuAdapter;
import com.hhl.hhlandroidproject.ui.view.swipemenulistview.SwipeMenuCreator;
import com.hhl.hhlandroidproject.ui.view.swipemenulistview.SwipeMenuLayout;
import com.hhl.hhlandroidproject.ui.view.swipemenulistview.SwipeMenuView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WrapperViewList extends ListView {

	interface LifeCycleListener {
		void onDispatchDrawOccurred(Canvas canvas);
	}

	private LifeCycleListener mLifeCycleListener;
	private List<View> mFooterViews;
	private int mTopClippingLength;
	private Rect mSelectorRect = new Rect();// for if reflection fails
	private Field mSelectorPositionField;
	private boolean mClippingToPadding = true;
    private boolean mBlockLayoutChildren = false;

	public WrapperViewList(Context context) {
		super(context);

		init();
        init2();
		// Use reflection to be able to change the size/position of the list
		// selector so it does not come under/over the header
		try {
			Field selectorRectField = AbsListView.class.getDeclaredField("mSelectorRect");
			selectorRectField.setAccessible(true);
			mSelectorRect = (Rect) selectorRectField.get(this);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				mSelectorPositionField = AbsListView.class.getDeclaredField("mSelectorPosition");
				mSelectorPositionField.setAccessible(true);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		if (view instanceof WrapperView) {
			view = ((WrapperView) view).mItem;
		}
		return super.performItemClick(view, position, id);
	}

	private void positionSelectorRect() {
		if (!mSelectorRect.isEmpty()) {
			int selectorPosition = getSelectorPosition();
			if (selectorPosition >= 0) {
				int firstVisibleItem = getFixedFirstVisibleItem();
				View v = getChildAt(selectorPosition - firstVisibleItem);
				if (v instanceof WrapperView) {
					WrapperView wrapper = ((WrapperView) v);
					mSelectorRect.top = wrapper.getTop() + wrapper.mItemTop;
				}
			}
		}
	}

	private int getSelectorPosition() {
		if (mSelectorPositionField == null) { // not all supported andorid
			// version have this variable
			for (int i = 0; i < getChildCount(); i++) {
				if (getChildAt(i).getBottom() == mSelectorRect.bottom) {
					return i + getFixedFirstVisibleItem();
				}
			}
		} else {
			try {
				return mSelectorPositionField.getInt(this);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		positionSelectorRect();
		if (mTopClippingLength != 0) {
			canvas.save();
			Rect clipping = canvas.getClipBounds();
			clipping.top = mTopClippingLength;
			canvas.clipRect(clipping);
			super.dispatchDraw(canvas);
			canvas.restore();
		} else {
			super.dispatchDraw(canvas);
		}
		mLifeCycleListener.onDispatchDrawOccurred(canvas);
	}

	void setLifeCycleListener(LifeCycleListener lifeCycleListener) {
		mLifeCycleListener = lifeCycleListener;
	}

	@Override
	public void addFooterView(View v) {
		super.addFooterView(v);
		addInternalFooterView(v);
	}

	@Override
	public void addFooterView(View v, Object data, boolean isSelectable) {
		super.addFooterView(v, data, isSelectable);
		addInternalFooterView(v);
	}

	private void addInternalFooterView(View v) {
		if (mFooterViews == null) {
			mFooterViews = new ArrayList<View>();
		}
		mFooterViews.add(v);
	}

	@Override
	public boolean removeFooterView(View v) {
		if (super.removeFooterView(v)) {
			mFooterViews.remove(v);
			return true;
		}
		return false;
	}

	boolean containsFooterView(View v) {
		if (mFooterViews == null) {
			return false;
		}
		return mFooterViews.contains(v);
	}

	void setTopClippingLength(int topClipping) {
		mTopClippingLength = topClipping;
	}

	int getFixedFirstVisibleItem() {
		int firstVisibleItem = getFirstVisiblePosition();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return firstVisibleItem;
		}

		// first getFirstVisiblePosition() reports items
		// outside the view sometimes on old versions of android
		for (int i = 0; i < getChildCount(); i++) {
			if (getChildAt(i).getBottom() >= 0) {
				firstVisibleItem += i;
				break;
			}
		}

		// work around to fix bug with firstVisibleItem being to high
		// because list view does not take clipToPadding=false into account
		// on old versions of android
		if (!mClippingToPadding && getPaddingTop() > 0 && firstVisibleItem > 0) {
			if (getChildAt(0).getTop() > 0) {
				firstVisibleItem -= 1;
			}
		}

		return firstVisibleItem;
	}

	@Override
	public void setClipToPadding(boolean clipToPadding) {
		mClippingToPadding = clipToPadding;
		super.setClipToPadding(clipToPadding);
	}

    public void setBlockLayoutChildren(boolean block) {
        mBlockLayoutChildren = block;
    }

    @Override
    protected void layoutChildren() {
        if (!mBlockLayoutChildren) {
            super.layoutChildren();
        }
    }


	private ObservableScrollViewCallbacks mCallbacks;
	private int mPrevFirstVisiblePosition;
	private int mPrevFirstVisibleChildHeight = -1;
	private int mPrevScrolledChildrenHeight;
	private SparseIntArray mChildrenHeights;
	private int mPrevScrollY;
	private int mScrollY;
	private ScrollState mScrollState;
	private boolean mFirstScroll;
	private boolean mDragging;

	private OnScrollListener mOriginalScrollListener;
	private OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (mOriginalScrollListener != null) {
				mOriginalScrollListener.onScrollStateChanged(view, scrollState);
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if (mOriginalScrollListener != null) {
				mOriginalScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
			// AbsListView#invokeOnItemScrollListener calls onScrollChanged(0, 0, 0, 0)
			// on Android 4.0+, but Android 2.3 is not. (Android 3.0 is unknown)
			// So call it with onScrollListener.
			onScrollChanged();
		}
	};

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		mPrevFirstVisiblePosition = ss.prevFirstVisiblePosition;
		mPrevFirstVisibleChildHeight = ss.prevFirstVisibleChildHeight;
		mPrevScrolledChildrenHeight = ss.prevScrolledChildrenHeight;
		mPrevScrollY = ss.prevScrollY;
		mScrollY = ss.scrollY;
		mChildrenHeights = ss.childrenHeights;
		super.onRestoreInstanceState(ss.getSuperState());
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.prevFirstVisiblePosition = mPrevFirstVisiblePosition;
		ss.prevFirstVisibleChildHeight = mPrevFirstVisibleChildHeight;
		ss.prevScrolledChildrenHeight = mPrevScrolledChildrenHeight;
		ss.prevScrollY = mPrevScrollY;
		ss.scrollY = mScrollY;
		ss.childrenHeights = mChildrenHeights;
		return ss;
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		// Don't set l to super.setOnScrollListener().
		// l receives all events through mScrollListener.
		mOriginalScrollListener = l;
	}

	public void setScrollViewCallbacks(ObservableScrollViewCallbacks listener) {
		mCallbacks = listener;
	}

	public int getCurrentScrollY() {
		return mScrollY;
	}

	private void init() {
		mChildrenHeights = new SparseIntArray();
		super.setOnScrollListener(mScrollListener);
	}

	private void onScrollChanged() {
		if (mCallbacks != null) {
			if (getChildCount() > 0) {
				int firstVisiblePosition = getFirstVisiblePosition();
				for (int i = getFirstVisiblePosition(), j = 0; i <= getLastVisiblePosition(); i++, j++) {
					if (mChildrenHeights.indexOfKey(i) < 0 || getChildAt(j).getHeight() != mChildrenHeights.get(i)) {
						mChildrenHeights.put(i, getChildAt(j).getHeight());
					}
				}

				View firstVisibleChild = getChildAt(0);
				if (firstVisibleChild != null) {
					if (mPrevFirstVisiblePosition < firstVisiblePosition) {
						// scroll down
						int skippedChildrenHeight = 0;
						if (firstVisiblePosition - mPrevFirstVisiblePosition != 1) {
							//LogUtils.v(TAG, "Skipped some children while scrolling down: " + (firstVisiblePosition - mPrevFirstVisiblePosition));
							for (int i = firstVisiblePosition - 1; i > mPrevFirstVisiblePosition; i--) {
								if (0 < mChildrenHeights.indexOfKey(i)) {
									skippedChildrenHeight += mChildrenHeights.get(i);
									//LogUtils.v(TAG, "Calculate skipped child height at " + i + ": " + mChildrenHeights.get(i));
								} else {
									//LogUtils.v(TAG, "Could not calculate skipped child height at " + i);
									// Approximate each item's height to the first visible child.
									// It may be incorrect, but without this, scrollY will be broken
									// when scrolling from the bottom.
									skippedChildrenHeight += firstVisibleChild.getHeight();
								}
							}
						}
						mPrevScrolledChildrenHeight += mPrevFirstVisibleChildHeight + skippedChildrenHeight;
						mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
					} else if (firstVisiblePosition < mPrevFirstVisiblePosition) {
						// scroll up
						int skippedChildrenHeight = 0;
						if (mPrevFirstVisiblePosition - firstVisiblePosition != 1) {
							//LogUtils.v(TAG, "Skipped some children while scrolling up: " + (mPrevFirstVisiblePosition - firstVisiblePosition));
							for (int i = mPrevFirstVisiblePosition - 1; i > firstVisiblePosition; i--) {
								if (0 < mChildrenHeights.indexOfKey(i)) {
									skippedChildrenHeight += mChildrenHeights.get(i);
									//LogUtils.v(TAG, "Calculate skipped child height at " + i + ": " + mChildrenHeights.get(i));
								} else {
									//LogUtils.v(TAG, "Could not calculate skipped child height at " + i);
									// Approximate each item's height to the first visible child.
									// It may be incorrect, but without this, scrollY will be broken
									// when scrolling from the bottom.
									skippedChildrenHeight += firstVisibleChild.getHeight();
								}
							}
						}
						mPrevScrolledChildrenHeight -= firstVisibleChild.getHeight() + skippedChildrenHeight;
						mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
					} else if (firstVisiblePosition == 0) {
						mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
					}
					if (mPrevFirstVisibleChildHeight < 0) {
						mPrevFirstVisibleChildHeight = 0;
					}
					mScrollY = mPrevScrolledChildrenHeight - firstVisibleChild.getTop();
					mPrevFirstVisiblePosition = firstVisiblePosition;

					//LogUtils.v(TAG, "first: " + firstVisiblePosition + " scrollY: " + mScrollY + " first height: " + firstVisibleChild.getHeight() + " first top: " + firstVisibleChild.getTop());
					mCallbacks.onScrollChanged(mScrollY, mFirstScroll, mDragging);
					if (mFirstScroll) {
						mFirstScroll = false;
					}

					if (mPrevScrollY < mScrollY) {
						//down
						mScrollState = ScrollState.UP;
					} else if (mScrollY < mPrevScrollY) {
						//up
						mScrollState = ScrollState.DOWN;
					} else {
						mScrollState = ScrollState.STOP;
					}
					mPrevScrollY = mScrollY;
				} else {
					//LogUtils.v(TAG, "first: " + firstVisiblePosition);
				}
			}
		}
	}


	static class SavedState extends BaseSavedState {
		int prevFirstVisiblePosition;
		int prevFirstVisibleChildHeight = -1;
		int prevScrolledChildrenHeight;
		int prevScrollY;
		int scrollY;
		SparseIntArray childrenHeights;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			prevFirstVisiblePosition = in.readInt();
			prevFirstVisibleChildHeight = in.readInt();
			prevScrolledChildrenHeight = in.readInt();
			prevScrollY = in.readInt();
			scrollY = in.readInt();
			childrenHeights = new SparseIntArray();
			final int numOfChildren = in.readInt();
			if (0 < numOfChildren) {
				for (int i = 0; i < numOfChildren; i++) {
					final int key = in.readInt();
					final int value = in.readInt();
					childrenHeights.put(key, value);
				}
			}
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(prevFirstVisiblePosition);
			out.writeInt(prevFirstVisibleChildHeight);
			out.writeInt(prevScrolledChildrenHeight);
			out.writeInt(prevScrollY);
			out.writeInt(scrollY);
			final int numOfChildren = childrenHeights == null ? 0 : childrenHeights.size();
			out.writeInt(numOfChildren);
			if (0 < numOfChildren) {
				for (int i = 0; i < numOfChildren; i++) {
					out.writeInt(childrenHeights.keyAt(i));
					out.writeInt(childrenHeights.valueAt(i));
				}
			}
		}

		public static final Creator<SavedState> CREATOR
				= new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}


    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;

    private int MAX_Y = 5;
    private int MAX_X = 3;
    private float mDownX;
    private float mDownY;
    private int mTouchState;
    private int mTouchPosition;
    private SwipeMenuLayout mTouchView;
    private OnSwipeListener mOnSwipeListener;

    private SwipeMenuCreator mMenuCreator;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private Interpolator mCloseInterpolator;
    private Interpolator mOpenInterpolator;

    private void init2() {
        MAX_X = dp2px(MAX_X);
        MAX_Y = dp2px(MAX_Y);
        mTouchState = TOUCH_STATE_NONE;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
            @Override
            public void createMenu(SwipeMenu menu) {
                if (mMenuCreator != null) {
                    mMenuCreator.create(menu);
                }
            }

            @Override
            public void onItemClick(SwipeMenuView view, SwipeMenu menu,
                                    int index) {
                boolean flag = false;
                if (mOnMenuItemClickListener != null) {
                    flag = mOnMenuItemClickListener.onMenuItemClick(
                            view.getPosition(), menu, index);
                }
                if (mTouchView != null && !flag) {
                    mTouchView.smoothCloseMenu();
                }
            }
        });
    }

    public void setCloseInterpolator(Interpolator interpolator) {
        mCloseInterpolator = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        mOpenInterpolator = interpolator;
    }

    public Interpolator getOpenInterpolator() {
        return mOpenInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return mCloseInterpolator;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (mCallbacks != null) {
//            switch (ev.getActionMasked()) {
//                case MotionEvent.ACTION_DOWN:
//                    //LogUtils.v(TAG, "onTouchEvent: ACTION_DOWN");
//                    mFirstScroll = mDragging = true;
//                    mCallbacks.onDownMotionEvent();
//                    break;
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
////                    LogUtils.v(TAG, "onTouchEvent: ACTION_UP|ACTION_CANCEL");
//                    mDragging = false;
//                    mCallbacks.onUpOrCancelMotionEvent(mScrollState);
//                    break;
//            }
//        }
//        return super.onTouchEvent(ev);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
            return super.onTouchEvent(ev);
        int action = MotionEventCompat.getActionMasked(ev);
        action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (mCallbacks != null){
                    mFirstScroll = mDragging = true;
                    mCallbacks.onDownMotionEvent();
                }

                int oldPos = mTouchPosition;
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchState = TOUCH_STATE_NONE;

                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

                if (mTouchPosition == oldPos && mTouchView != null
                        && mTouchView.isOpen()) {
                    mTouchState = TOUCH_STATE_X;
                    mTouchView.onSwipe(ev);
                    return true;
                }

                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                    mTouchView = null;
                    // return super.onTouchEvent(ev);
                    // try to cancel the touch event
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    onTouchEvent(cancelEvent);
                    return true;
                }
                if (view instanceof SwipeMenuLayout) {
                    mTouchView = (SwipeMenuLayout) view;
                }
                if (mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                    }
                    getSelector().setState(new int[] { 0 });
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                } else if (mTouchState == TOUCH_STATE_NONE) {
                    if (Math.abs(dy) > MAX_Y) {
                        mTouchState = TOUCH_STATE_Y;
                    } else if (dx > MAX_X) {
                        mTouchState = TOUCH_STATE_X;
                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipeStart(mTouchPosition);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                if (mCallbacks != null){
                    mDragging = false;
                    mCallbacks.onUpOrCancelMotionEvent(mScrollState);
                }

                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                        if (!mTouchView.isOpen()) {
                            mTouchPosition = -1;
                            mTouchView = null;
                        }
                    }
                    if (mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipeEnd(mTouchPosition);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mCallbacks != null){
                    mDragging = false;
                    mCallbacks.onUpOrCancelMotionEvent(mScrollState);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void smoothOpenMenu(int position) {
        if (position >= getFirstVisiblePosition()
                && position <= getLastVisiblePosition()) {
            View view = getChildAt(position - getFirstVisiblePosition());
            if (view instanceof SwipeMenuLayout) {
                mTouchPosition = position;
                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.smoothOpenMenu();
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public void setMenuCreator(SwipeMenuCreator menuCreator) {
        this.mMenuCreator = menuCreator;
    }

    public void setOnMenuItemClickListener(
            OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.mOnSwipeListener = onSwipeListener;
    }

    public static interface OnMenuItemClickListener {
        boolean onMenuItemClick(int position, SwipeMenu menu, int index);
    }

    public static interface OnSwipeListener {
        void onSwipeStart(int position);

        void onSwipeEnd(int position);
    }
}
