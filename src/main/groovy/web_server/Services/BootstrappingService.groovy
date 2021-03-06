package web_server.services

import groovy.transform.CompileStatic
import javax.inject.Singleton

import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.discovery.event.ServiceReadyEvent
import io.micronaut.runtime.event.annotation.EventListener

import javax.inject.Inject
import grails.gorm.transactions.TransactionService

import web_server.domain.ServerConfig
import web_server.events.BootstrapCompleteEvent

@Singleton
@CompileStatic
class BootstrappingService {

    //Inject necessary services for GORM manipulation and event publishing
    @Inject TransactionService transactionService
    @Inject ApplicationEventPublisher eventPublisher

    @EventListener
    public void loadConfig(final ServiceReadyEvent event) {
        try {
            transactionService.withTransaction {
                ServerConfig path = new ServerConfig(
                    [
                        name: "path",
                        settingString: "d:\\Files\\Minecraft\\1.16.2 Server"
                    ]
                )
                path.save()
            }
        } catch (Exception e) {
            println("Whoops: ${e.message}")
        }
        eventPublisher.publishEvent(new BootstrapCompleteEvent())
    }
}
