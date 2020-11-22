/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.erivera.apps.topcharts.utils;

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout
import kotlin.jvm.JvmOverloads;

class CollapsibleToolbar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), AppBarLayout.OnOffsetChangedListener {

    private var listener : OffsetChangedListener? = null

    private var _progress = -1F

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val totalRange = appBarLayout?.totalScrollRange?.toFloat()!!
        progress = -verticalOffset / totalRange
        if(_progress != progress){
            listener?.onOffsetChanged(verticalOffset, progress, totalRange)
            _progress = progress
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (parent as? AppBarLayout)?.addOnOffsetChangedListener(this)
    }

    fun setListener(listener: OffsetChangedListener?){
        this.listener = listener
    }

    interface OffsetChangedListener{
        fun onOffsetChanged(verticalOffset: Int, progress: Float, totalRange: Float)
    }
}