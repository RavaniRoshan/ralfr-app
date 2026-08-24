package com.example.feature.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ChatMessage
import com.example.domain.model.Courier
import com.example.domain.model.OrderStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class MapPoint(val x: Float, val y: Float)

data class TrackingUiState(
    val status: OrderStatus = OrderStatus.OnTheWay,
    val etaRemaining: Duration = (6 * 60 + 30).seconds, // 06:30
    val progress: Float = 0.66f,
    val courier: Courier = Courier(),
    val courierPosition: MapPoint = MapPoint(0.48f, 0.68f),
    val courierBearing: Float = 345f,
    val routePoints: List<MapPoint> = listOf(
        MapPoint(0.50f, 0.32f), // Store
        MapPoint(0.49f, 0.45f),
        MapPoint(0.47f, 0.60f),
        MapPoint(0.44f, 0.72f), // Courier current
        MapPoint(0.38f, 0.85f), // Delivery Destination
    ),
    val storePosition: MapPoint = MapPoint(0.50f, 0.32f),
    val destinationPosition: MapPoint = MapPoint(0.38f, 0.85f),
    val messages: List<ChatMessage> = listOf(
        ChatMessage("msg_1", "Hello! I have picked up your Turbo order.", "09:18 pm", isIncoming = true),
        ChatMessage("msg_2", "Great, thanks! Please ring the bell at apt 402.", "09:19 pm", isIncoming = false),
        ChatMessage("msg_3", "thanks My bro", "09:20 pm", isIncoming = true)
    ),
    val isTurbo: Boolean = true,
    val isTipModalOpen: Boolean = false,
    val isHelpSheetOpen: Boolean = false,
    val isLocateActive: Boolean = false,
    val deliveryCode: String = "4829",
)

class OrderTrackingViewModel : ViewModel() {

    private val _state = MutableStateFlow(TrackingUiState())
    val state: StateFlow<TrackingUiState> = _state.asStateFlow()

    init {
        startEtaTimer()
        startCourierSimulation()
    }

    private fun startEtaTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _state.update { current ->
                    if (current.status.isLiveCountdown && current.etaRemaining > Duration.ZERO) {
                        current.copy(etaRemaining = current.etaRemaining - 1.seconds)
                    } else if (current.etaRemaining <= Duration.ZERO && current.status == OrderStatus.OnTheWay) {
                        current.copy(
                            status = OrderStatus.Arrived,
                            progress = 0.95f
                        )
                    } else {
                        current
                    }
                }
            }
        }
    }

    private fun startCourierSimulation() {
        viewModelScope.launch {
            var step = 0
            val path = listOf(
                MapPoint(0.48f, 0.66f) to 345f,
                MapPoint(0.47f, 0.63f) to 340f,
                MapPoint(0.46f, 0.60f) to 330f,
                MapPoint(0.45f, 0.64f) to 160f,
                MapPoint(0.47f, 0.68f) to 175f,
                MapPoint(0.48f, 0.68f) to 345f,
            )

            while (true) {
                delay(3500)
                if (_state.value.status == OrderStatus.OnTheWay) {
                    step = (step + 1) % path.size
                    val (nextPos, nextBearing) = path[step]
                    _state.update {
                        it.copy(
                            courierPosition = nextPos,
                            courierBearing = nextBearing
                        )
                    }
                }
            }
        }
    }

    fun setStatus(status: OrderStatus) {
        val newProgress = status.progressFraction
        val newEta = when (status) {
            OrderStatus.Confirming -> (12 * 60).seconds
            OrderStatus.Preparing -> (10 * 60 + 15).seconds
            OrderStatus.PickingUp -> (8 * 60 + 45).seconds
            OrderStatus.OnTheWay -> (6 * 60 + 30).seconds
            OrderStatus.Arrived -> (1 * 60).seconds
            OrderStatus.Delivered -> Duration.ZERO
        }
        _state.update {
            it.copy(
                status = status,
                progress = newProgress,
                etaRemaining = newEta
            )
        }
    }

    fun nextStatus() {
        val nextIndex = (_state.value.status.ordinal + 1) % OrderStatus.values().size
        setStatus(OrderStatus.values()[nextIndex])
    }

    fun prevStatus() {
        val prevIndex = if (_state.value.status.ordinal - 1 < 0) OrderStatus.values().size - 1 else _state.value.status.ordinal - 1
        setStatus(OrderStatus.values()[prevIndex])
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            text = text.trim(),
            timestamp = "09:21 pm",
            isIncoming = false
        )
        _state.update { current ->
            current.copy(messages = current.messages + newMsg)
        }

        // Simulate courier reply after 2 seconds
        viewModelScope.launch {
            delay(2000)
            val replies = listOf(
                "On it! See you in a few minutes.",
                "Got your note, I'm almost at the gate.",
                "Thanks! I will leave it by the door if requested."
            )
            val replyMsg = ChatMessage(
                id = "msg_${System.currentTimeMillis()}",
                text = replies.random(),
                timestamp = "09:22 pm",
                isIncoming = true
            )
            _state.update { it.copy(messages = it.messages + replyMsg) }
        }
    }

    fun addTip(tip: Int) {
        _state.update { current ->
            current.copy(
                courier = current.courier.copy(tipAmount = current.courier.tipAmount + tip),
                isTipModalOpen = false
            )
        }
    }

    fun setTipModalVisible(visible: Boolean) {
        _state.update { it.copy(isTipModalOpen = visible) }
    }

    fun setHelpSheetVisible(visible: Boolean) {
        _state.update { it.copy(isHelpSheetOpen = visible) }
    }

    fun triggerLocate() {
        _state.update { it.copy(isLocateActive = true) }
        viewModelScope.launch {
            delay(1200)
            _state.update { it.copy(isLocateActive = false) }
        }
    }
}
