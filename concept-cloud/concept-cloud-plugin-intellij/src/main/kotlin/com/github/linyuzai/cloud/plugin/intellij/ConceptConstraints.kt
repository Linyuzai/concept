/*
package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ui.dsl.gridLayout.Grid
import com.intellij.ui.dsl.gridLayout.GridLayout
import com.intellij.ui.dsl.gridLayout.GridLayoutComponentProperty

import javax.swing.JComponent

enum class ConceptHorizontalAlign {
    LEFT,
    CENTER,
    RIGHT,
    FILL
}

enum class ConceptVerticalAlign {
    TOP,
    CENTER,
    BOTTOM,
    FILL
}

data class ConceptConstraints(

    */
/**
     * Grid destination
     *//*

    val grid: Grid,

    */
/**
     * Cell x coordinate in [grid]
     *//*

    val x: Int,

    */
/**
     * Cell y coordinate in [grid]
     *//*

    val y: Int,

    */
/**
     * Columns number occupied by the cell
     *//*

    val width: Int = 1,

    */
/**
     * Rows number occupied by the cell
     *//*

    val height: Int = 1,

    */
/**
     * Horizontal alignment of content inside the cell
     *//*

    val horizontalAlign: ConceptHorizontalAlign = ConceptHorizontalAlign.LEFT,

    */
/**
     * Vertical alignment of content inside the cell
     *//*

    val verticalAlign: ConceptVerticalAlign = ConceptVerticalAlign.CENTER,

    */
/**
     * If true then vertical align is done by baseline:
     *
     * 1. All cells in the same grid row with [baselineAlign] true, [height] equals 1 and with the same [verticalAlign]
     * (except [VerticalAlign.FILL], which doesn't support baseline) are aligned by baseline together
     * 2. Sub grids (see [GridImpl.registerSubGrid]) with only one row and that contain cells only with [VerticalAlign.FILL] and another
     * specific [VerticalAlign] (at least one cell without fill align) have own baseline and can be aligned by baseline in parent grid
     *//*

    val baselineAlign: Boolean = false,

    */
/**
     * Gaps between grid cell bounds and components visual bounds (visual bounds is component bounds minus [visualPaddings])
     *//*

    val gaps: ConceptGaps = ConceptGaps.EMPTY,

    */
/**
     * Gaps between component bounds and its visual bounds. Can be used when component has focus ring outside of
     * its usual size. In such case components size is increased on focus size (so focus ring is not clipped)
     * and [visualPaddings] should be set to maintain right alignments
     *
     * 1. Layout manager aligns components by their visual bounds
     * 2. Cell size with gaps is calculated as component.bounds + [gaps] - [visualPaddings]
     * 3. Cells that contain [JComponent] with own [GridLayout] calculate and update [visualPaddings] automatically.
     * To disable this behaviour set [GridLayoutComponentProperty.SUB_GRID_AUTO_VISUAL_PADDINGS] to false
     *//*

    var visualPaddings: ConceptGaps = ConceptGaps.EMPTY,

    */
/**
     * All components from the same width group will have the same width equals to maximum width from the group.
     * Cannot be used together with [HorizontalAlign.FILL] or for sub-grids (see [GridLayout.addLayoutSubGrid])
     *//*

    val widthGroup: String? = null,

    */
/**
     * Component helper for custom behaviour
     *//*

    val componentHelper: ComponentHelper? = null
) {

    init {
        checkNonNegative("x", x)
        checkNonNegative("y", y)
        checkPositive("width", width)
        checkPositive("height", height)

        if (widthGroup != null && horizontalAlign == ConceptHorizontalAlign.FILL) {
            throw ConceptUiDslException("Width group cannot be used with horizontal align FILL: $widthGroup")
        }
    }
}

*/
/**
 * A helper for custom behaviour for components in cells
 *//*

interface ComponentHelper {

    */
/**
     * Returns custom baseline or null if default baseline calculation should be used
     *
     * @see JComponent.getBaseline
     *//*

    fun getBaseline(width: Int, height: Int): Int?
}
*/
