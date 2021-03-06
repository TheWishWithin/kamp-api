package ch.leadrian.samp.kamp.core.api.amx

/**
 * Base class for cell arrays used when calling sampgdk_InvokeNative.
 */
sealed class CellArray(internal val values: IntArray) {

    val size: Int = values.size

    /**
     * Iterator for iterating over all [Int] values stored in this array.
     */
    operator fun iterator(): IntIterator = values.iterator()

    /**
     * Accesses the [Int] value stored at [index].
     */
    operator fun get(index: Int): Int = values[index]

}

/**
 * Class for immutable [IntArray]s.
 *
 * @see [CellArray]
 * @see [ImmutableCellArrayType]
 */
class ImmutableCellArray(values: IntArray) : CellArray(values) {

    companion object {

        /**
         * Simulates a constructor allowing varargs.
         */
        operator fun invoke(vararg values: Int): ImmutableCellArray = ImmutableCellArray(values)

    }

}

/**
 * Class for mutable [IntArray]s.
 *
 * @see [CellArray]
 * @see [MutableCellArrayType]
 */
class MutableCellArray(values: IntArray) : CellArray(values) {

    companion object {

        /**
         * Simulates a constructor allowing varargs.
         */
        operator fun invoke(vararg values: Int): MutableCellArray = MutableCellArray(values)

    }

    constructor(size: Int) : this(IntArray(size))

    /**
     * Sets the array value at [index] to [value].
     */
    operator fun set(index: Int, value: Int) {
        values[index] = value
    }

}
