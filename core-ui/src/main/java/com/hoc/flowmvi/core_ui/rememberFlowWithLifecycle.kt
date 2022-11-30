package com.hoc.flowmvi.core_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> rememberFlowWithLifecycle(
  flow: Flow<T>,
  lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
  minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = remember(flow, lifecycle, minActiveState) {
  flow.flowWithLifecycle(
    lifecycle = lifecycle,
    minActiveState = minActiveState
  )
}

@Suppress("ComposableNaming")
@Composable
fun <T> Flow<T>.collectInLaunchedEffectWithLifecycle(
  vararg keys: Any?,
  lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
  minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
  collector: suspend CoroutineScope.(T) -> Unit
) {
  val flow = this
  LaunchedEffect(flow, lifecycle, minActiveState, *keys) {
    lifecycle.repeatOnLifecycle(minActiveState) {
      flow.collect { collector(it) }
    }
  }
}
