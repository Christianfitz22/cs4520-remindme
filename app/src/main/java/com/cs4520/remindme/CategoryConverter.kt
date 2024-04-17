package com.cs4520.remindme

import androidx.compose.ui.graphics.Color
import com.cs4520.assignment5.R

class CategoryConverter {
    companion object {
        fun ToPlaceholder(category: Category): Int {
            if (category == Category.HOME) {
                return R.drawable.home_120
            }
            else if (category == Category.WORK) {
                return R.drawable.custom_apps_120
            }
            else if (category == Category.FAMILY) {
                return R.drawable.groups_120
            }
            else if (category == Category.PERSONAL) {
                return R.drawable.user_120
            }
            return 0
        }

        fun ToURL(category: Category): String {
            if (category == Category.HOME) {
                return "https://img.icons8.com/home"
            }
            else if (category == Category.WORK) {
                return "https://img.icons8.com/wrench"
            }
            else if (category == Category.FAMILY) {
                return "https://img.icons8.com/groups"
            }
            else if (category == Category.PERSONAL) {
                return "https://img.icons8.com/user"
            }
            return ""
        }

        fun ToColor(category: Category): Color {
            if (category == Category.HOME) {
                return Color(0xFF656FFF)
            }
            else if (category == Category.WORK) {
                return Color(0xFFE06666)
            }
            else if (category == Category.FAMILY) {
                return Color(0xFF4FB55C)
            }
            else if (category == Category.PERSONAL) {
                return Color(0xFFBB6BF6)
            }
            return Color(0xFF656FFF)
        }
    }
}