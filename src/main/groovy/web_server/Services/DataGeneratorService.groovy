package web_server.services

import groovy.transform.CompileStatic
import javax.inject.Singleton

import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.scheduling.annotation.Async

import web_server.events.BootstrapCompleteEvent

@Singleton
@CompileStatic
class DataGeneratorService {
    @EventListener
    @Async
    public void runDataGenerator(final BootstrapCompleteEvent event) {
        println("Bootstrapping complete, runDataGenerator method called")
        /* TODO check for generator folder on path, if none exists run the data generator ASYNC
         * can also have this check a GORM "block" entity count. If 0, can attempt to load from
         * generated file
         */
    }
}