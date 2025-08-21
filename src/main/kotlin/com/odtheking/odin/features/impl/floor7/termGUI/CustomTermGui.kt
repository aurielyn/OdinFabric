package com.odtheking.odin.features.impl.floor7.termGUI

import com.odtheking.odin.OdinMod.mc
import com.odtheking.odin.events.GuiEvent
import com.odtheking.odin.features.impl.floor7.TerminalSolver
import com.odtheking.odin.features.impl.floor7.TerminalSolver.hideClicked
import com.odtheking.odin.utils.Color
import com.odtheking.odin.utils.ui.animations.ColorAnimation
import com.odtheking.odin.utils.ui.isAreaHovered
import com.odtheking.odin.utils.ui.rendering.NVGRenderer
import net.minecraft.client.gui.screen.Screen
import kotlin.math.ceil
import kotlin.math.floor

abstract class TermGui {
    protected val itemIndexMap: MutableMap<Int, Box> = mutableMapOf()
    inline val currentSolution get() = TerminalSolver.currentTerm?.solution.orEmpty()
    val colorAnimations = mutableMapOf<Int, ColorAnimation>()

    abstract fun renderTerminal(slotCount: Int)

    protected fun renderBackground(slotCount: Int, slotWidth: Int) {
        val slotSize = 55f * TerminalSolver.customTermSize
        val gap = TerminalSolver.gap * TerminalSolver.customTermSize
        val totalSlotSpace = slotSize + gap

        val backgroundStartX = mc.window.width / 2f + -(slotWidth / 2f) * totalSlotSpace - 7.5f * TerminalSolver.customTermSize
        val backgroundStartY = mc.window.height / 2f + ((-getRowOffset(slotCount) + 0.5f) * totalSlotSpace) - 7.5f * TerminalSolver.customTermSize
        val backgroundWidth = slotWidth * totalSlotSpace + 15f * TerminalSolver.customTermSize
        val backgroundHeight = ((slotCount) / 9) * totalSlotSpace + 15f * TerminalSolver.customTermSize

        NVGRenderer.rect(backgroundStartX, backgroundStartY, backgroundWidth, backgroundHeight, TerminalSolver.backgroundColor.rgba, 12f)
    }

    protected fun renderSlot(index: Int, startColor: Color, endColor: Color): Pair<Float, Float> {
        val slotSize = 55f * TerminalSolver.customTermSize
        val totalSlotSpace = slotSize + TerminalSolver.gap * TerminalSolver.customTermSize

        val x = (index % 9 - 4) * totalSlotSpace + mc.window.width / 2f - slotSize / 2
        val y = (index / 9 - 2) * totalSlotSpace + mc.window.height / 2f - slotSize / 2

        itemIndexMap[index] = Box(x, y, slotSize, slotSize)

        val colorAnim = colorAnimations.getOrPut(index) { ColorAnimation(250) }

        NVGRenderer.rect(floor(x), floor(y), ceil(slotSize), ceil(slotSize), colorAnim.get(startColor, endColor, true).rgba, TerminalSolver.roundness)
        return x to y
    }

    fun mouseClicked(screen: Screen, button: Int) {
        getHoveredItem()?.let { slot ->
            TerminalSolver.currentTerm?.let {
                if (System.currentTimeMillis() - it.timeOpened >= 350 && !GuiEvent.CustomTermGuiClick(screen, slot, button).postAndCatch() && it.canClick(slot, button)) {
                    it.click(slot, button, hideClicked && !it.isClicked)
                    if (TerminalSolver.customAnimations) colorAnimations[slot]?.start()
                }
            }
        }
    }

    fun closeGui() {
        colorAnimations.clear()
    }

    open fun render() {
        setCurrentGui(this)
        itemIndexMap.clear()

        renderTerminal(TerminalSolver.currentTerm?.type?.windowSize?.minus(10) ?: 0)
    }

    private fun getRowOffset(slotCount: Int): Float {
        return when (slotCount) {
            in 0..9 -> 0f
            in 10..18 -> 1f
            in 19..27 -> 2f
            in 28..36 -> 2f
            in 37..45 -> 2f
            else -> 3f
        }
    }

    companion object {
        private var currentGui: TermGui? = null

        fun setCurrentGui(gui: TermGui) {
            currentGui = gui
        }

        fun getHoveredItem(): Int? =
            currentGui?.itemIndexMap?.entries?.find { isAreaHovered(it.value.x, it.value.y, it.value.w, it.value.h) }?.key
    }

    data class Box(val x: Float, val y: Float, val w: Float, val h: Float)
}