package web_server.controllers

import web_server.domain.LogLine
import groovy.transform.CompileDynamic
import io.micronaut.http.annotation.Controller

@CompileDynamic
@Controller('/logs')
class LogLineController extends GORMController<LogLine> {
    LogLineController() {
        super(LogLine)
    }
}