package web_server.controllers

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType

import javax.inject.Inject
import javax.inject.Singleton

import web_server.services.ShellAccessService

@CompileStatic
@Controller('/shell')
class ApplicationController {

    @Inject
    ShellAccessService shellAccessService

    @Get('/')
    @Produces(MediaType.APPLICATION_JSON)
    HttpResponse<?> index() {
        Map response = [:]
        if (shellAccessService.process != null) {
            response['log'] = shellAccessService.readConsoleFile()
            return HttpResponse.ok(response)
        } else {
            response['error'] = "Server not running"
            return HttpResponse.serverError()
        }
    }

    @Post('/start')
    @Produces(MediaType.APPLICATION_JSON)
    HttpResponse<?> start() {
        Map response = [:]
        if (shellAccessService.process == null) {
            shellAccessService.setupConsoleFile()
            shellAccessService.startShell()

            shellAccessService.processReader()
            shellAccessService.processWriter()
            sleep(3000)
        } else {
            response.error = 'Process already running'
        }

        return HttpResponse.ok(response)
    }

    @Post('/command')
    @Produces(MediaType.APPLICATION_JSON)
    HttpResponse<?> command(@Body Map commandMap) {
        String cmd = commandMap.cmd
        shellAccessService.addToPostedCommands(cmd)
        return HttpResponse.ok()
    }

}
