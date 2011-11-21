/*******************************************************************************
 * Copyright 2011 Ait-Said Sofian
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bumerang.util;

import com.bumerang.MusorFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MusorszamAdapter extends FragmentPagerAdapter {
	private int NUM_ITEMS;

	public MusorszamAdapter(FragmentManager fm, int NUM_ITEMS) {
		super(fm);
		this.NUM_ITEMS = NUM_ITEMS;
	}

	@Override
	public Fragment getItem(int arg0) {
		return new MusorFragment();
	}

	@Override
	public int getCount() {
		
		return NUM_ITEMS;
	}

}
