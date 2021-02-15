package com.geranzo.sliidetest.ui.main

import com.geranzo.domain.entity.User
import java.util.concurrent.TimeUnit

class ListItemUserViewModel(private val user: User) {
    val name = user.name
    val email = user.email

    // creation time (relative to now)
    val created: String
        get() {
            val deltaSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - user.createdAt)

            return "Created " + if (deltaSeconds < 60)
                "$deltaSeconds seconds ago"
            else {
                val deltaMinutes = TimeUnit.SECONDS.toMinutes(deltaSeconds)

                if (deltaMinutes < 60)
                    "$deltaMinutes minutes ago"
                else {
                    val deltaHours = TimeUnit.MINUTES.toHours(deltaMinutes)

                    if (deltaHours < 24)
                        "$deltaHours hours ago"
                    else
                        "${TimeUnit.HOURS.toDays(deltaHours)} days ago"
                }
            }
        }
}
