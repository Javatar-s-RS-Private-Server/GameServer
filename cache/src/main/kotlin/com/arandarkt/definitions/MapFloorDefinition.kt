package com.arandarkt.definitions

class MapFloorDefinition {

    val landscape: Array<Array<BooleanArray>> = Array(4) { Array(64) { BooleanArray(64) } }
    val mapscape: Array<Array<ByteArray>> = Array(4) { Array(64) { ByteArray(64) } }

}