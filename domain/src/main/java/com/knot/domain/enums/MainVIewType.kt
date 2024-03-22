package com.knot.domain.enums

enum class MainVIewType(val type:Int) {
    TOP(0), PARTICIPATING_KNOT(1), TODO_LIST(2);

    companion object {
        fun valueOf(value: Int): MainVIewType {
            return MainVIewType.values().find {
                it.type == value
            } ?: MainVIewType.TOP
        }
    }
}