package com.erivera.apps.topcharts.dagger

import android.content.Context
import javax.inject.Inject

class ChildDependencyImpl @Inject constructor(val context: Context): ChildDependency