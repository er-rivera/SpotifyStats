package com.erivera.apps.topcharts.dagger

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ParentDependency @Inject constructor(val childDependency: ChildDependency)