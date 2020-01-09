package de.se.team3.webservice.handlers

import io.javalin.http.Context

object CORSHandler {
    fun optionsRequest(ctx: Context) {
        if (ctx.header("Access-Control-Request-Headers") == "authorization")
            ctx.status(200)
    }

}