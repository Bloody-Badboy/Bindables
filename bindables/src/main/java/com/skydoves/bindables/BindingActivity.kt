/*
 * Designed and developed by 2021 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.bindables

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author skydoves (Jaewoong Eum)
 *
 * Base class which extends [AppCompatActivity] for activities that wish to bind content layout with [DataBindingUtil].
 * Provides a [binding] property that extends [ViewDataBinding] from abstract information.
 * The [binding] property ensures to be initialized before being called [onCreate].
 *
 * @param T A generic class that extends [ViewDataBinding] and generated by DataBinding on compile time.
 * @property contentLayoutId A content layout Id for inflating as a content view.
 */
abstract class BindingActivity<T : ViewDataBinding> constructor(
  @LayoutRes private val contentLayoutId: Int
) : AppCompatActivity() {

  /** This interface is generated during compilation to contain getters for all used instance `BindingAdapters`. */
  protected var bindingComponent: DataBindingComponent? = DataBindingUtil.getDefaultComponent()

  /**
   * A data-binding property will be initialized before being called [onCreate].
   * And inflates using the [contentLayoutId] as a content view for activities.
   */
  @BindingOnly
  protected val binding: T by lazy(LazyThreadSafetyMode.NONE) {
    DataBindingUtil.setContentView(this, contentLayoutId, bindingComponent)
  }

  /**
   * An executable inline binding function that receives a binding receiver in lambda.
   *
   * @param block A lambda block will be executed with the binding receiver.
   * @return T A generic class that extends [ViewDataBinding] and generated by DataBinding on compile time.
   */
  @BindingOnly
  protected inline fun binding(block: T.() -> Unit): T {
    return binding.apply(block)
  }

  /**
   * Ensures the [binding] property should be executed before being called [onCreate].
   */
  init {
    addOnContextAvailableListener {
      binding.notifyChange()
    }
  }
}
