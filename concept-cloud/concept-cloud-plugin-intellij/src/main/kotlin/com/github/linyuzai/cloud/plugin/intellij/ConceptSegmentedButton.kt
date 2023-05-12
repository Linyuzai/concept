/*
package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ui.dsl.builder.Row

import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.properties.ObservableMutableProperty

import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign

*/
/**
 * Represents segmented button or combobox depending on number of buttons and screen reader mode. Screen reader mode always uses combobox
 *
 * @see Row.segmentedButton
 *//*

interface ConceptSegmentedButton<T> : ConceptCellBase<ConceptSegmentedButton<T>> {

    companion object {
        const val DEFAULT_MAX_BUTTONS_COUNT = 6
    }

    override fun visible(isVisible: Boolean): ConceptSegmentedButton<T>

    override fun enabled(isEnabled: Boolean): ConceptSegmentedButton<T>

    override fun horizontalAlign(horizontalAlign: ConceptHorizontalAlign): ConceptSegmentedButton<T>

    override fun verticalAlign(verticalAlign: ConceptVerticalAlign): ConceptSegmentedButton<T>

    override fun resizableColumn(): ConceptSegmentedButton<T>

    override fun gap(rightGap: ConceptRightGap): ConceptSegmentedButton<T>

    override fun customize(customGaps: ConceptGaps): ConceptSegmentedButton<T>

    fun items(items: Collection<T>): ConceptSegmentedButton<T>

    fun bind(property: ObservableMutableProperty<T>): ConceptSegmentedButton<T>

    fun whenItemSelected(parentDisposable: Disposable? = null, listener: (T) -> Unit): ConceptSegmentedButton<T>

    fun whenItemSelectedFromUi(parentDisposable: Disposable? = null, listener: (T) -> Unit): ConceptSegmentedButton<T>

    */
/**
     * Maximum number of buttons in segmented button. The component automatically turned into ComboBox if exceeded.
     * Default value is [DEFAULT_MAX_BUTTONS_COUNT]
     *//*

    fun maxButtonsCount(value: Int): ConceptSegmentedButton<T>
}
*/
