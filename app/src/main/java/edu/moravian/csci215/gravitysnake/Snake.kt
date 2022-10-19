package edu.moravian.csci215.gravitysnake

import android.graphics.PointF


/**
 * A complete Snake. This keeps track of all of the body parts and their
 * movement. There are several methods for dealing with collisions with the
 * snake as well.
 *
 * NOTE: This class is complete, but you may to need look over the public
 * methods to use them.
 *
 * @param initial the initial position of the snake head
 * @param startingLength the initial length of the snake (added over time)
 * @param dpToPxFactor the factor to convert dp to px
 */
class Snake(initial: PointF, startingLength: Int, private val dpToPxFactor: Float) {
    /**
     * The points that make up the body. The point at position 0 is the head.
     * Each of these points are stored using units of pixels
     */
    val body: MutableList<PointF> = arrayListOf(initial)

    /** The first point in the body on the snake */
    val head: PointF
        get() = body[0]

    /**
     * The distance to be travelled. This is needed since the snake actually
     * moves in discrete steps instead of continuously.
     */
    private var distToTravel = PointF(0f, 0f)

    /** The number of body pieces to add while the snake is moving forward. */
    private var piecesToAdd: Int = startingLength

    /** The size of each body piece in px. */
    private val bodyPieceSize = BODY_PIECE_SIZE_DP * dpToPxFactor

    /** The distance of each step move in px. */
    private val stepDist = STEP_DISTANCE_DP * dpToPxFactor

    /**
     * Moves the snake forward.
     * @param direction the direction of movement, in radians
     * @param distance the distance of the movement, in pixels
     */
    fun move(direction: Float, distance: Float) {
        // Update the distance to be travelled
        distToTravel += polarToCartesian(distance, direction)

        // Move the snake as much of the distance as possible
        var distTotal = distToTravel.length // total distance to travel
        if (distTotal >= stepDist) {
            val angle = distToTravel.angle // angle to travel at
            val stepDistVector = polarToCartesian(stepDist, angle) // step distance vector
            while (distTotal >= stepDist) { // while the distance to travel is at least one step
                // Remove this distance from the remaining distance to travel
                distTotal -= stepDist

                // Create and add the new head to the start of the body
                body.add(0, head + stepDistVector)

                // Remove the tail (if there are no pieces to be added)
                if (piecesToAdd == 0) {
                    body.removeAt(body.size - 1)
                } else {
                    piecesToAdd -= 1 // the tail is the new piece
                }
            }

            // Update the remaining distance
            distToTravel = polarToCartesian(distTotal, angle)
        }
    }

    /**
     * Increases the length of the snake by the given amount. This doesn't take
     * effect right away, but only after the snake has moved far enough for the
     * new body parts to be placed.
     * @param amount the amount to increase the length by
     */
    fun increaseLength(amount: Int) { piecesToAdd += amount }

    /**
     * Checks if the snake head intersects the any part of the snake. This
     * check allows for significant overlap. First off, the first several
     * section immediately following the head don't count at all. After that,
     * the body would have to be overlapping by 75% for it to count.
     * @return true if the head intersections the body
     */
    fun headIntersectsSelf() =
        anyWithinRange(
            body, head,
            HEAD_INTERSECTION_BODY_FACTOR*bodyPieceSize, HEAD_INTERSECTION_BODY_SKIP
        )

    /**
     * Checks if the snake head intersects the given circular item.
     * @param location the location of the item, in px
     * @param radius the radius of the item, in px
     * @return true if the snake intersections the given circular item
     */
    fun headIntersectsItem(location: PointF, radius: Float) =
        withinRange(head, location, bodyPieceSize + radius)

    /**
     * Checks if the snake head intersects any of the given circular items.
     * @param locations the locations of the items, in px
     * @param radius the radius of the items, in px
     * @return true if the snake intersections any of the given circular items
     */
    fun headIntersectsAnyItem(locations: List<PointF>, radius: Float) =
        anyWithinRange(locations, head, bodyPieceSize + radius)

    /**
     * Checks if the snake head is "out of bounds" of a rectangle that goes
     * from 0,0 to the given width and height. The snake is only out-of-bounds
     * when the middle of the head leaves.
     * @param width the width of the bounds in px
     * @param height the height of the bounds in px
     * @return true if the snake is out of bounds
     */
    fun headIsOutOfBounds(width: Int, height: Int) =
        head.x < 0 || head.y < 0 || head.x >= width || head.y >= height
        // If forcing whole head in bounds:
        //head.x < bodyPieceSize || head.y < bodyPieceSize || head.x + bodyPieceSize > width || head.y + bodyPieceSize > height

    /**
     * Checks if the snake head or body intersects the given circular item.
     * @param location the location of the item, in px
     * @param radius the radius of the item, in px
     * @return true if the snake intersections the given circular item
     */
    fun bodyIntersectsItem(location: PointF, radius: Float): Boolean =
        anyWithinRange(body, location, bodyPieceSize + radius)

    companion object {
        /** Radius of each body piece in dp  */
        const val BODY_PIECE_SIZE_DP = 15f

        /** Distance that is moved each actual movement, in dp  */
        const val STEP_DISTANCE_DP = 2.5f

        /** Number of pieces after the head to skip when checking for self-intersection  */
        const val HEAD_INTERSECTION_BODY_SKIP = 20

        /** Amount of overlap between the head and a body piece to be considered an intersection  */
        // For more aggressive/accurate it should be 2f
        const val HEAD_INTERSECTION_BODY_FACTOR = 0.5f
    }
}
