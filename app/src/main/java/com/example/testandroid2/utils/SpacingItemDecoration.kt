package com.example.testandroid2.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(
    private val space: Int,
    private val spanCount: Int? = null,
    private val includeEdge: Boolean? = null
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        outRect.top = space

        if(spanCount == null || includeEdge == null) return
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.left =
                space - column * space / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * space / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = space
            }
            outRect.bottom = space // item bottom
        } else {
            outRect.left =
                column * space / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                space - (column + 1) * space / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = space // item top
            }
        }
    }

}