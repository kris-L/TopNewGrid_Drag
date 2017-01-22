package com.tisson.test;

import java.util.ArrayList;  

import com.tisson.test.DragGrid.OnLongClickListen;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	private DragGrid dragGrid;

	public int[] imgs = { R.drawable.app_transfer, R.drawable.app_fund,
			R.drawable.app_phonecharge, R.drawable.app_creditcard,
			R.drawable.app_movie, R.drawable.app_lottery,
			R.drawable.app_facepay, R.drawable.app_close, R.drawable.app_plane };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		dragGrid = (DragGrid) findViewById(R.id.userGridView);
		
		dragGrid.setOnLongClickListener(onLongClickListen);
		ArrayList<ChannelItem> dragLists = new ArrayList<ChannelItem>();
		for (int i = 0; i < 9; i++) {
			ChannelItem item = new ChannelItem(i + 1, "item" + (i + 1),imgs[i]);
			if (i == 8)
				item.setName("其他");
			dragLists.add(item);
		}
		dragGrid.setAdapter(new DragAdapter(this, dragLists));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DragAdapter.selectedPos = -1;
	}
	
	
	OnLongClickListen onLongClickListen = new OnLongClickListen() {
		
		@Override
		public void onItemLongClickOK() {
			// TODO Auto-generated method stub
			System.out.println("长按监听在这做");
		}
	};
	
}
