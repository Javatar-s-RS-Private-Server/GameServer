package com.arandarkt.game.api.world.map

import kotlin.jvm.JvmOverloads

/**
 * Represents a point.
 * @author Emperor
 */
class Point
/**
 * Constructs a new `Point` `Object`.
 * @param x The x-coordinate.
 * @param y The y-coordinate.
 * @param direction The direction.
 */ @JvmOverloads constructor(
    /**
     * The x-coordinate.
     */
    val x: Int,
    /**
     * The y-coordinate.
     */
    val y: Int,
    /**
     * The direction for the next point.
     */
    val direction: Direction? = null,
    /**
     * The difference x between previous and current point.
     */
    val diffX: Int = 0,
    /**
     * The difference y between previous and current point.
     */
    val diffY: Int = 0
) {
    /**
     * Gets the x.
     * @return The x.
     */
    /**
     * Gets the y.
     * @return The y.
     */
    /**
     * Gets the diffX.
     * @return The diffX.
     */
    /**
     * Gets the diffY.
     * @return The diffY.
     */
    /**
     * Gets the direction.
     * @return The direction.
     */
    /**
     * Gets the runDisabled.
     * @return The runDisabled.
     */
    /**
     * Sets the runDisabled.
     * @param runDisabled The runDisabled to set.
     */
    /**
     * If we can't run during this point.
     */
    var isRunDisabled = false

    /**
     * Constructs a new `Point` `Object`.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param direction The direction.
     * @param diffX The difference x between previous and current point.
     * @param diffY The difference y between previous and current point.
     * @param runDisabled If running is disabled for this walking point.
     */
    constructor(x: Int, y: Int, direction: Direction?, diffX: Int, diffY: Int, runDisabled: Boolean) : this(
        x,
        y,
        direction,
        diffX,
        diffY
    ) {
        isRunDisabled = runDisabled
    }
    /**
     * Constructs a new `Point` `Object`.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param direction The direction.
     * @param diffX The difference x between previous and current point.
     * @param diffY The difference y between previous and current point.
     */
    /**
     * Constructs a new `Point` `Object`.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
}