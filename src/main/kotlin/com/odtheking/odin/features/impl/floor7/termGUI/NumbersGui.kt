package com.odtheking.odin.features.impl.floor7.termGUI

import com.odtheking.odin.features.impl.floor7.TerminalSolver
import com.odtheking.odin.utils.Colors
import com.odtheking.odin.utils.equalsOneOf
import com.odtheking.odin.utils.ui.rendering.NVGRenderer


object NumbersGui : TermGui() {

    override fun renderTerminal(slotCount: Int) {
        renderBackground(slotCount, 7)

        for (index in 9..slotCount) {
            if ((index % 9).equalsOneOf(0, 8)) continue

            val amount = TerminalSolver.currentTerm?.items?.get(index)?.count?.takeIf { it > 0 } ?: continue
            val solutionIndex = currentSolution.indexOf(index)

            val color = when (solutionIndex) {
                0 -> TerminalSolver.orderColor
                1 -> TerminalSolver.orderColor2
                2 -> TerminalSolver.orderColor3
                else -> Colors.TRANSPARENT
            }

            val (slotX, slotY) = renderSlot(index, color, TerminalSolver.orderColor)
            val slotSize = 55f * TerminalSolver.customTermSize
            val fontSize = 30f * TerminalSolver.customTermSize

            val textX = slotX + (slotSize - NVGRenderer.textWidth(amount.toString(), fontSize, NVGRenderer.defaultFont)) / 2f
            val textY = slotY + (slotSize + fontSize) / 2f - fontSize * 0.9f

            if (TerminalSolver.showNumbers && solutionIndex != -1)
                NVGRenderer.textShadow(amount.toString(), textX, textY, 30f * TerminalSolver.customTermSize, Colors.WHITE.rgba, NVGRenderer.defaultFont)
        }
    }
}