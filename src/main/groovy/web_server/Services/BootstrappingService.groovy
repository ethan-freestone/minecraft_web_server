package web_server.services

import groovy.transform.CompileStatic
import javax.inject.Singleton

import io.micronaut.discovery.event.ServiceReadyEvent
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.scheduling.annotation.Async

import javax.inject.Inject
import grails.gorm.transactions.TransactionService

import web_server.domain.ServerConfig


@Singleton
@CompileStatic
class BootstrappingService {

    @Inject TransactionService transactionService

    @EventListener
    @Async
    public void loadConfig(final ServiceReadyEvent event) {
        try {
            transactionService.withTransaction {
                ServerConfig path = new ServerConfig(
                    [
                        name: "path",
                        "settingString": "d:\\Files\\Minecraft\\1.16.2 Server"
                    ]
                )
                path.save()
            }
        } catch (Exception e) {
            println("Whoops: ${e.message}")
        }
    }
}