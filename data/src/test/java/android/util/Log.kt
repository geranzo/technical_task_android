package android.util

@Suppress("unused")
object Log {
    private enum class Priority { VERBOSE, DEBUG, INFO, WARNING, ERROR }

    @JvmStatic
    @JvmOverloads
    fun v(tag: String, msg: String, t: Throwable? = null) = print(Priority.VERBOSE, tag, msg, t)

    @JvmStatic
    @JvmOverloads
    fun d(tag: String, msg: String, t: Throwable? = null) = print(Priority.DEBUG, tag, msg, t)

    @JvmStatic
    @JvmOverloads
    fun i(tag: String, msg: String, t: Throwable? = null) = print(Priority.INFO, tag, msg, t)

    @JvmStatic
    @JvmOverloads
    fun w(tag: String, msg: String, t: Throwable? = null) = print(Priority.WARNING, tag, msg, t)

    @JvmStatic
    @JvmOverloads
    fun e(tag: String, msg: String, t: Throwable? = null) = print(Priority.ERROR, tag, msg, t)

    @JvmStatic
    @JvmOverloads
    fun println(priority: Int, tag: String, msg: String, t: Throwable? = null) =
        print(
            priority = when (priority) {
                2 -> Priority.VERBOSE
                3 -> Priority.DEBUG
                4 -> Priority.INFO
                5 -> Priority.WARNING
                else -> Priority.ERROR
            },
            tag,
            msg,
            t
        )

    private fun print(priority: Priority, tag: String, msg: String, t: Throwable? = null): Int {
        println("$priority: $tag: $msg")
        t?.printStackTrace(System.out)
        return 0
    }
}
