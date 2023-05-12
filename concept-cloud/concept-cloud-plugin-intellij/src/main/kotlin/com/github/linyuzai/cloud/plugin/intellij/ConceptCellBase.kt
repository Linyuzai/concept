/*
package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ui.dsl.builder.Panel

import com.intellij.ui.dsl.gridLayout.*
import com.intellij.ui.layout.*

enum class ConceptRightGap {
    */
/**
     * See [SpacingConfiguration.horizontalSmallGap]
     *//*

    SMALL,

    */
/**
     * See [SpacingConfiguration.horizontalColumnsGap]
     *//*

    COLUMNS
}

*/
/**
 * Common API for cells
 *//*

@ConceptLayoutDslMarker
interface ConceptCellBase<out T : ConceptCellBase<T>> {

    */
/**
     * Sets visibility of the cell and all children recursively.
     * The cell is invisible if there is an invisible parent
     *//*

    fun visible(isVisible: Boolean): ConceptCellBase<T>

    */
/**
     * Binds cell visibility to provided [predicate]
     *//*

    fun visibleIf(predicate: ComponentPredicate): ConceptCellBase<T>

    */
/**
     * Sets enabled state of the cell and all children recursively.
     * The cell is disabled if there is a disabled parent
     *//*

    fun enabled(isEnabled: Boolean): ConceptCellBase<T>

    */
/**
     * Binds cell enabled state to provided [predicate]
     *//*

    fun enabledIf(predicate: ComponentPredicate): ConceptCellBase<T>

    */
/**
     * Sets horizontal alignment of content inside the cell, [HorizontalAlign.LEFT] by default.
     * Use [HorizontalAlign.FILL] to stretch the content on whole cell width. In case the cell should occupy all
     * available width in parent mark the column as [resizableColumn]
     *
     * @see [resizableColumn]
     * @see [Constraints.horizontalAlign]
     *//*

    fun horizontalAlign(horizontalAlign: ConceptHorizontalAlign): ConceptCellBase<T>

    */
/**
     * Sets vertical alignment of content inside the cell, [VerticalAlign.CENTER] by default
     * Use [VerticalAlign.FILL] to stretch the content on whole cell height. In case the cell should occupy all
     * available height in parent mark the row as [Row.resizableRow]
     *
     * @see [Row.resizableRow]
     * @see [Constraints.verticalAlign]
     *//*

    fun verticalAlign(verticalAlign: ConceptVerticalAlign): ConceptCellBase<T>

    */
/**
     * Marks column of the cell as resizable: the column occupies all extra horizontal space in parent and changes size together with parent.
     * It's possible to have several resizable columns, which means extra space is shared between them.
     * There is no need to set resizable for cells in different rows but in the same column: it has no additional effect.
     * Note that horizontal size and placement of component in columns are managed by [horizontalAlign]
     *
     * @see [Grid.resizableColumns]
     *//*

    fun resizableColumn(): ConceptCellBase<T>

    */
/**
     * Separates the next cell in the current row with [rightGap]. [RightGap.SMALL] gap is set after row label automatically
     * by [Panel.row] methods.
     * Right gap is ignored for the last cell in a row
     *//*

    fun gap(rightGap: ConceptRightGap): ConceptCellBase<T>

    */
/**
     * Overrides all gaps around the cell by [customGaps]. Should be used rarely for very specific cases
     *//*

    fun customize(customGaps: ConceptGaps): ConceptCellBase<T>

}
*/
