package com.erivera.apps.topcharts.dagger

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParentDependency @Inject constructor(val childDependency: ChildDependency)