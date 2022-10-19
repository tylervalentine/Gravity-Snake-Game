package edu.moravian.csci215.gravitysnake

import android.graphics.PointF
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Checks if two points are closer than a certain range of each other.
 * @param a the first point
 * @param b the first point
 * @param range the maximum distance allowed between the points
 * @return true if the distance from a to b is less than range
 */
fun withinRange(a: PointF, b: PointF, range: Float): Boolean {
    val dx = a.x - b.x
    val dy = a.y - b.y
    return dx * dx + dy * dy < range * range
}

/**
 * Checks if any point in the list is within range of a point.
 * @return true if withinRange(a, b, range) is true for any of the points in the list
 */
fun anyWithinRange(pts: List<PointF>, point: PointF, range: Float, skip: Int = 0) =
    pts.subList(min(skip, pts.size), pts.size).any { withinRange(it, point, range) }

/**
 * Converts polar coordinates (r, θ) to Cartesian coordinates (x, y).
 * @param r the r (radius) coordinate
 * @param theta the theta (angle) coordinate
 * @return point of x and y
 */
fun polarToCartesian(r: Float, theta: Float) = PointF(cos(theta) * r, sin(theta) * r)

// Add useful operators and methods to PointF
operator fun PointF.plus(other: PointF): PointF = PointF(x + other.x, y + other.y)
val PointF.angle; get() = atan2(y, x)
val PointF.length; get() = length()
