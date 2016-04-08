package com.tisson.test;

import android.content.Context; 
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author kris lau 
 * @Time 201603023
 * @description 模仿支付宝可拖动九宫格功能
 * @blog http://blog.sina.com.cn/u/1964256004
 */

public class DragGrid extends GridView {
	/** 点击时候的X位置 */
	public int downX;
	/** 点击时候的Y位置 */
	public int downY;
	/** 点击时候对应整个界面的X位置 */
	public int windowX;
	/** 点击时候对应整个界面的Y位置 */
	public int windowY;
	/** 屏幕上的X */
	private int win_view_x;
	/** 屏幕上的Y */
	private int win_view_y;
	/** 拖动的里x的距离 */
	int dragOffsetX;
	/** 拖动的里Y的距离 */
	int dragOffsetY;
	/** 长按时候对应postion */
	public int dragPosition;
	/** Up后对应的ITEM的Position */
	private int dropPosition;
	/** 开始拖动的ITEM的Position */
	private int startPosition;
	/** item高 */
	private int itemHeight;
	/** item宽 */
	private int itemWidth;
	/** 拖动的时候对应ITEM的VIEW */
	private View dragImageView = null;
	/** 长按的时候ITEM的VIEW */
	private ViewGroup dragItemView = null;
	/** WindowManager管理器 */
	private WindowManager windowManager = null;
	/** */
	private WindowManager.LayoutParams windowParams = null;
	/** item总量 */
	private int itemTotalCount;
	/** 一行的ITEM数量 */
	private int nColumns = 4;
	/** 行数 */
	private int nRows;
	/** 剩余部分 */
	private int Remainder;
	/** 是否在移动 */
	private boolean isMoving = false;
	/** */
	private int holdPosition;
	/** 拖动的时候放大的倍数 */
	private double dragScale = 1.2D;
	/** 震动器 */
	private Vibrator mVibrator;
	/** 每个ITEM之间的水平间距 */
	private int mHorizontalSpacing = 15;
	/** 每个ITEM之间的竖直间距 */
	private int mVerticalSpacing = 15;
	/* 移动时候最后个动画的ID */
	private String LastAnimationID;
	private Context ctx;
	private boolean isReturnOnClickItem = false;

	public DragGrid(Context context) {
		super(context);
		init(context);
		this.ctx = context;
	}

	public DragGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		this.ctx = context;
	}

	public DragGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		this.ctx = context;
	}

	public void init(Context context) {
		mVibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		// 将布局文件中设置的间距dip转为px
		mHorizontalSpacing = DataTools.dip2px(context, mHorizontalSpacing);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			windowX = (int) ev.getX();
			windowY = (int) ev.getY();
			setOnItemClickListener(ev);
		}
		return super.onInterceptTouchEvent(ev);
	}

	private int downPos = -1;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean bool = true;
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		if (dragImageView == null) {
			// 移动时候的对应x,y位置
			bool = super.onTouchEvent(ev);
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = (int) ev.getX();
				windowX = (int) ev.getX();
				downY = (int) ev.getY();
				windowY = (int) ev.getY();
				//依据触摸点的坐标计算出点击的是ListView的哪个Item
				downPos = pointToPosition(downX, downY);
				System.out.println("选中项：" + DragAdapter.selectedPos + ",单击项："
						+ downPos);
                System.out.println("onTouchEvent DragAdapter.selectedPos="+DragAdapter.selectedPos);
                System.out.println("onTouchEvent downPos="+downPos);
				if (DragAdapter.selectedPos != -1
						&& downPos == DragAdapter.selectedPos) {
//					dragItemView = (ViewGroup) getChildAt(downPos
//							- getFirstVisiblePosition());
//					win_view_x = windowX - dragItemView.getLeft();// VIEW相对自己的X，半斤
//					win_view_y = windowY - dragItemView.getTop();// VIEW相对自己的y，半斤
//					//cache机制保存为bitmap
//					dragItemView.destroyDrawingCache();
//					dragItemView.setDrawingCacheEnabled(true);
//					Bitmap dragBitmap = Bitmap.createBitmap(dragItemView
//							.getDrawingCache());
//					mVibrator.vibrate(50);// 设置震动时间
//					startDrag(dragBitmap, (int) ev.getRawX(),
//							(int) ev.getRawY());
//					hideDropItem();
//					dragItemView.setVisibility(View.INVISIBLE);
					
					if (((ImageView) ((ViewGroup) getChildAt(DragAdapter.selectedPos))
							.findViewById(R.id.delete_icon)).getVisibility() == View.VISIBLE) {
						isReturnOnClickItem = true;
					}
					
					((ImageView) ((ViewGroup) getChildAt(DragAdapter.selectedPos))
							.findViewById(R.id.delete_icon))
							.setVisibility(View.GONE);
					DragAdapter.selectedPos = -1;
				} else if (DragAdapter.selectedPos != -1
						&& downPos != DragAdapter.selectedPos) {
					if (((ImageView) ((ViewGroup) getChildAt(DragAdapter.selectedPos))
							.findViewById(R.id.delete_icon)).getVisibility() == View.VISIBLE) {
						isReturnOnClickItem = true;
					}
					
					((ImageView) ((ViewGroup) getChildAt(DragAdapter.selectedPos))
							.findViewById(R.id.delete_icon))
							.setVisibility(View.GONE);
					DragAdapter.selectedPos = -1;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				requestDisallowInterceptTouchEvent(false);
				break;
			default:
				break;
			}
		}
		if (dragImageView != null
				&& dragPosition != AdapterView.INVALID_POSITION) {
			//长按后事件
			// 移动时候的对应x,y位置
			bool = super.onTouchEvent(ev);
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = (int) ev.getX();
				windowX = (int) ev.getX();
				downY = (int) ev.getY();
				windowY = (int) ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				onDrag(x, y, (int) ev.getRawX(), (int) ev.getRawY());
				if (!isMoving) {
					OnMove(x, y);
				}
				if (pointToPosition(x, y) != AdapterView.INVALID_POSITION) {
					break;
				}
				break;
			case MotionEvent.ACTION_UP:
				stopDrag();
				onDrop(downPos);
				requestDisallowInterceptTouchEvent(false);
				break;

			default:
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	/** 在拖动的情况 */
	private void onDrag(int x, int y, int rawx, int rawy) {
		if (dragImageView != null) {
			windowParams.alpha = 0.6f;
			// windowParams.x = rawx - itemWidth / 2;
			// windowParams.y = rawy - itemHeight / 2;
			windowParams.x = rawx - win_view_x;
			windowParams.y = rawy - win_view_y;
			windowManager.updateViewLayout(dragImageView, windowParams);
		}
	}

	/** 在松手下放的情况 */
	private void onDrop(int x, int y) {
		// 根据拖动到的x,y坐标获取拖动位置下方的ITEM对应的POSTION
		int tempPostion = pointToPosition(x, y);
		System.out.println("松手操作：" + x + "," + y + "," + tempPostion);
		// if (tempPostion != AdapterView.INVALID_POSITION) {
		dropPosition = tempPostion;
		DragAdapter mDragAdapter = (DragAdapter) getAdapter();
		// 显示刚拖动的ITEM
		mDragAdapter.setShowDropItem(true);
		if (dropPosition != -1)
			mDragAdapter.setHoldPosition(dropPosition);
		// 刷新适配器，让对应的ITEM显示
		mDragAdapter.notifyDataSetChanged();
		// }
	}

	/** 在松手下放的情况 */
	private void onDrop(int clickPos) {
		// 根据拖动到的x,y坐标获取拖动位置下方的ITEM对应的POSTION
		System.out.println("松手操作：" + clickPos);
		// if (tempPostion != AdapterView.INVALID_POSITION) {
		dropPosition = clickPos;
		DragAdapter mDragAdapter = (DragAdapter) getAdapter();
		// 显示刚拖动的ITEM
		mDragAdapter.setShowDropItem(true);
		//判断是否一定了位置
		if (dropPosition != -1)
			mDragAdapter.setHoldPosition(dropPosition);
		// 刷新适配器，让对应的ITEM显示
		mDragAdapter.notifyDataSetChanged();
		if (dropPosition != DragAdapter.selectedPos) {
			mDragAdapter.setReset(true);
		}else{
			mDragAdapter.setReset(false);
		}
		// }
	}

	private long lastTime = 0;

	/**
	 * 长按点击监听
	 * 
	 * @param ev
	 */
	public int clickPos = 0;
	public int clickX = 0;
	public int clickY = 0;
	public void setOnItemClickListener(final MotionEvent ev) {
		clickPos = pointToPosition((int) ev.getX(), (int) ev.getY());
		clickX = (int) ev.getX();
		clickY = (int) ev.getY();
		if ((clickPos != -1 && clickPos == DragAdapter.selectedPos)
				|| DragAdapter.selectedPos == -1) {

			setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					int x = (int) ev.getX();// 长安事件的X位置
					int y = (int) ev.getY();// 长安事件的y位置

					startPosition = position;// 第一次点击的postion
					dragPosition = position;
					System.out.println("setOnItemClickListener dragPosition="+dragPosition);
					if (startPosition == getCount() - 1) {
						return false;
					}
					ViewGroup dragViewGroup = (ViewGroup) getChildAt(dragPosition
							- getFirstVisiblePosition());
					TextView dragTextView = (TextView) dragViewGroup
							.findViewById(R.id.text_item);
					dragTextView.setSelected(true);
					dragTextView.setEnabled(false);
					ImageView dragIcon = (ImageView) dragViewGroup
							.findViewById(R.id.delete_icon);
					dragIcon.setVisibility(View.VISIBLE);
					itemHeight = dragViewGroup.getHeight();
					itemWidth = dragViewGroup.getWidth();
					itemTotalCount = DragGrid.this.getCount();
					int row = itemTotalCount / nColumns;// 算出行数
					Remainder = (itemTotalCount % nColumns);// 算出最后一行多余的数量
					if (Remainder != 0) {
						nRows = row + 1;
					} else {
						nRows = row;
					}
					// 如果特殊的这个不等于拖动的那个,并且不等于-1
					if (dragPosition != AdapterView.INVALID_POSITION) {
						// 释放的资源使用的绘图缓存。如果你调用buildDrawingCache()手动没有调用setDrawingCacheEnabled(真正的),你应该清理缓存使用这种方法。
						win_view_x = windowX - dragViewGroup.getLeft();// VIEW相对自己的X，半斤
						win_view_y = windowY - dragViewGroup.getTop();// VIEW相对自己的y，半斤
						dragOffsetX = (int) (ev.getRawX() - x);// 手指在屏幕的上X位置-手指在控件中的位置就是距离最左边的距离
						dragOffsetY = (int) (ev.getRawY() - y);// 手指在屏幕的上y位置-手指在控件中的位置就是距离最上边的距离
						dragItemView = dragViewGroup;
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								dragItemView.destroyDrawingCache();
								dragItemView.setDrawingCacheEnabled(true);
								Bitmap dragBitmap = Bitmap
										.createBitmap(dragItemView
												.getDrawingCache());
								mVibrator.vibrate(50);// 设置震动时间
								startDrag(dragBitmap, (int) ev.getRawX(),
										(int) ev.getRawY());
								hideDropItem();
								dragItemView.setVisibility(View.INVISIBLE);
								isMoving = false;
								//TODO:
								DragAdapter.selectedPos = dragPosition;
							}
						}, 50);
						requestDisallowInterceptTouchEvent(true);
						return true;
					}
					return false;
				}
			});
		} else {
			this.setOnItemLongClickListener(null);
		}
		
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("onItemClick");
				ViewGroup dragViewGroup = (ViewGroup) getChildAt(clickPos
						- getFirstVisiblePosition());
				ImageView deleteIcon = (ImageView) dragViewGroup
						.findViewById(R.id.delete_icon);
				int dragIconViewX = dragViewGroup.getRight() - deleteIcon.getWidth();
				int dragIconViewY = dragViewGroup.getTop();
				if (dragIconViewX<clickX && clickX<dragViewGroup.getRight() &&
						dragIconViewY < clickY && clickY < (dragIconViewY + deleteIcon.getHeight())) {
					DragAdapter mDragAdapter = (DragAdapter) getAdapter();
					mDragAdapter.remove(clickPos);
					mDragAdapter.setReset(true);
					return;
				}
				
				if (DragAdapter.selectedPos == -1 && isReturnOnClickItem) {
					isReturnOnClickItem = false;
					return;
				}
				
				if (System.currentTimeMillis() - lastTime >= 500)
					Toast.makeText(
							ctx,
							((DragAdapter) getAdapter()).channelList.get(
									position).getName()
									+ "," + position + "被单击",
							Toast.LENGTH_SHORT).show();
				
				lastTime = System.currentTimeMillis();
			}
		});
	}

	public void startDrag(Bitmap dragBitmap, int x, int y) {
		stopDrag();
		windowParams = new WindowManager.LayoutParams();// 获取WINDOW界面的
		// Gravity.TOP|Gravity.LEFT;这个必须加
		windowParams.gravity = Gravity.TOP | Gravity.LEFT;
		// windowParams.x = x - (int)((itemWidth / 2) * dragScale);
		// windowParams.y = y - (int) ((itemHeight / 2) * dragScale);
		// 得到preview左上角相对于屏幕的坐标
		windowParams.x = x - win_view_x;
		windowParams.y = y - win_view_y;
		// this.windowParams.x = (x - this.win_view_x + this.viewX);//位置的x值
		// this.windowParams.y = (y - this.win_view_y + this.viewY);//位置的y值
		// 设置拖拽item的宽和高
		windowParams.width = (int) (dragScale * dragBitmap.getWidth());// 放大dragScale倍，可以设置拖动后的倍数
		windowParams.height = (int) (dragScale * dragBitmap.getHeight());// 放大dragScale倍，可以设置拖动后的倍数
		this.windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		this.windowParams.format = PixelFormat.TRANSLUCENT;
		this.windowParams.windowAnimations = 0;
		ImageView iv = new ImageView(getContext());
		iv.setImageBitmap(dragBitmap);
		windowManager = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);// "window"
		windowManager.addView(iv, windowParams);
		dragImageView = iv;
	}

	/** 停止拖动 ，释放并初始化 */
	private void stopDrag() {
		if (dragImageView != null) {
			windowManager.removeView(dragImageView);
			dragImageView = null;
		}
	}

	/** 在ScrollView内，所以要进行计算高度 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/** 隐藏 放下 的ITEM */
	private void hideDropItem() {
		((DragAdapter) getAdapter()).setShowDropItem(false);
	}

	/** 获取移动动画 */
	public Animation getMoveAnimation(float toXValue, float toYValue) {
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF,
				toXValue, Animation.RELATIVE_TO_SELF, 0.0F,
				Animation.RELATIVE_TO_SELF, toYValue);// 当前位置移动到指定位置
		mTranslateAnimation.setFillAfter(true);// 设置一个动画效果执行完毕后，View对象保留在终止的位置。
		mTranslateAnimation.setDuration(300L);
		return mTranslateAnimation;
	}

	/** 移动的时候触发 */
	public void OnMove(int x, int y) {
		//拖动的VIEW下方的POSTION
		int dPosition = pointToPosition(x, y);
		// 判断下方的POSTION是否是最开始2个不能拖动的
		if (dPosition < getCount() - 1) {
			if ((dPosition == -1) || (dPosition == dragPosition)) {
				return;
			}
			dropPosition = dPosition;
			if (dragPosition != startPosition) {
				dragPosition = startPosition;
			}
			int movecount;
			// 拖动的=开始拖的，并且 拖动的 不等于放下的
			if ((dragPosition == startPosition)
					|| (dragPosition != dropPosition)) {
				// 移需要移动的动ITEM数量
				movecount = dropPosition - dragPosition;
			} else {
				// 移需要移动的动ITEM数量为0
				movecount = 0;
			}
			if (movecount == 0) {
				return;
			}

			int movecount_abs = Math.abs(movecount);

			if (dPosition != dragPosition) {
				// dragGroup设置为不可见
				ViewGroup dragGroup = (ViewGroup) getChildAt(dragPosition);
				dragGroup.setVisibility(View.INVISIBLE);
				float to_x = 1;// 当前下方positon
				float to_y;// 当前下方右边positon
				// x_vlaue移动的距离百分比（相对于自己长度的百分比）
				float x_vlaue = ((float) mHorizontalSpacing / (float) itemWidth) + 1.0f;
				// y_vlaue移动的距离百分比（相对于自己宽度的百分比）
				float y_vlaue = ((float) mVerticalSpacing / (float) itemHeight) + 1.0f;
				Log.d("x_vlaue", "x_vlaue = " + x_vlaue);
				for (int i = 0; i < movecount_abs; i++) {
					to_x = x_vlaue;
					to_y = y_vlaue;
					// 像左
					if (movecount > 0) {
						// 判断是不是同一行的
						holdPosition = dragPosition + i + 1;
						if (dragPosition / nColumns == holdPosition / nColumns) {
							to_x = -x_vlaue;
							to_y = 0;
						} else if (holdPosition % 4 == 0) {
							to_x = 3 * x_vlaue;
							to_y = -y_vlaue;
						} else {
							to_x = -x_vlaue;
							to_y = 0;
						}
					} else {
						// 向右,下移到上，右移到左
						holdPosition = dragPosition - i - 1;
						if (dragPosition / nColumns == holdPosition / nColumns) {
							to_x = x_vlaue;
							to_y = 0;
						} else if ((holdPosition + 1) % 4 == 0) {
							to_x = -3 * x_vlaue;
							to_y = y_vlaue;
						} else {
							to_x = x_vlaue;
							to_y = 0;
						}
					}
					ViewGroup moveViewGroup = (ViewGroup) getChildAt(holdPosition);
					Animation moveAnimation = getMoveAnimation(to_x, to_y);
					moveViewGroup.startAnimation(moveAnimation);
					// 如果是最后一个移动的，那么设置他的最后个动画ID为LastAnimationID
					if (holdPosition == dropPosition) {
						LastAnimationID = moveAnimation.toString();
					}
					moveAnimation.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							isMoving = true;
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							// 如果为最后个动画结束，那执行下面的方法
							if (animation.toString().equalsIgnoreCase(
									LastAnimationID)) {
								DragAdapter mDragAdapter = (DragAdapter) getAdapter();
								mDragAdapter.exchange(startPosition,
										dropPosition);
								downPos = dropPosition;
								startPosition = dropPosition;
								dragPosition = dropPosition;
								isMoving = false;
							}
						}
					});
				}
			}
		}
	}
}