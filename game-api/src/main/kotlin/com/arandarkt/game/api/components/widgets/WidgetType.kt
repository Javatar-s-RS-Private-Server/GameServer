package com.arandarkt.game.api.components.widgets

enum class WidgetType(
    val fixedPaneId: Int,
    val fixedChildId: Int
) {
    DEFAULT(548, 64),
    OVERLAY(548, 64),
    TAB(548, 86),
    SINGLE_TAB(548, 84),
    DIALOGUE(548, 79),
    WINDOW_PANE(548, 0),
    CS_CHATBOX(548, 79),
    CHATBOX(548, 90);
}