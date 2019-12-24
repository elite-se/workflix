package de.se.team3.webservice.handlers

import de.se.team3.logic.container.ProcessGroupContainer
import io.javalin.http.Context
import java.util.NoSuchElementException

object ProcessGroupHandler {
    fun delete(ctx: Context, processGroupId: Int) {
        try {
            ProcessGroupContainer.deleteProcessGroup(processGroupId)
        } catch (e: NoSuchElementException) {
            ctx.status(404).result("process group not found")
        }
    }
}
