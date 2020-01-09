package de.se.team3.webservice.handlers

import io.javalin.http.Context

object CORSHandler {
    /**
     * Handles CORS OPTIONS requests: replies with status code 200 if authorization header is requested.
     */
    fun optionsRequest(ctx: Context) {
        if (ctx.header("Access-Control-Request-Headers") == "authorization")
            ctx.status(200)
    }
}
