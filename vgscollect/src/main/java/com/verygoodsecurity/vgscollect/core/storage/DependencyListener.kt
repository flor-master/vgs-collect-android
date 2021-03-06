package com.verygoodsecurity.vgscollect.core.storage

import com.verygoodsecurity.vgscollect.core.model.state.Dependency

interface DependencyListener {
    fun dispatchDependencySetting(dependency: Dependency)
}