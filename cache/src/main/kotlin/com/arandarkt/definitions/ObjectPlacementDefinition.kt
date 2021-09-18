package com.arandarkt.definitions

class ObjectPlacementDefinition {

    val objects = mutableListOf<ObjectSpawn>()

    data class ObjectSpawn(val objectId: Int, val x: Int, val y: Int, val rotation: Int, val type: Int, val height: Int)
}