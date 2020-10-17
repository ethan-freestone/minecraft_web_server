package web_server.services

import groovy.transform.CompileDynamic
import javax.inject.Singleton

import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.scheduling.annotation.Async

import javax.inject.Inject
import grails.gorm.transactions.TransactionService

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import web_server.domain.ServerConfig
import web_server.events.BootstrapCompleteEvent
import web_server.events.DataGenerationCompleteEvent

@Singleton
@CompileDynamic
class DataGeneratorService {

    //Inject necessary services for GORM manipulation and event publishing
    @Inject TransactionService transactionService
    @Inject ApplicationEventPublisher eventPublisher

    @EventListener
    @Async
    void dataGeneratorManagement(final BootstrapCompleteEvent event) {
        String path
        String separator = File.separator

        try {
            transactionService.withTransaction {

                ServerConfig pathConfig = ServerConfig.findByName("path")
                path = pathConfig.settingString

            }
        } catch (Exception e) {
            println("Whoops: ${e.message}")
        }

        if (path) {
            /* Check for generator folder on path, if none exists run the data generator ASYNC
            * can also have this check a GORM "block" entity count. If 0, can attempt to load from
            * generated file
            */
            Path generatedDirPath = Paths.get(path + separator + 'generated')
            if (Files.isDirectory(generatedDirPath)) {
                // Data generator already run
                println('Data generation has already run')
                eventPublisher.publishEvent(new DataGenerationCompleteEvent())
            } else {
                // Data generator not run yet
                File serverDir = new File(path)
                println('Data generation has not been run--running data generation')
                runDataGenerator(serverDir)
            }
        } else {
            println('Whoops: No path exists')
        }
    }

    @Async
    void runDataGenerator(File serverDir) {
        ProcessBuilder pb = new ProcessBuilder(
            'java',
            '-cp',
            'server.jar',
            'net.minecraft.data.Main',
            '--reports'
        )
        pb.directory(serverDir)
        pb.redirectErrorStream(true)
        Process p = pb.start()
        p.waitFor()
        println('Data generation process complete')
        eventPublisher.publishEvent(new DataGenerationCompleteEvent())
    }
}