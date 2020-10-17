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

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken

import java.io.*
import java.nio.charset.StandardCharsets

import web_server.domain.ServerConfig
import web_server.domain.MinecraftItem
import web_server.events.BootstrapCompleteEvent
import web_server.events.DataGenerationCompleteEvent

@Singleton
@CompileDynamic
class DataGeneratorService {

    //Inject necessary services for GORM manipulation and event publishing
    @Inject TransactionService transactionService
    @Inject ApplicationEventPublisher eventPublisher
    String path
    String separator = File.separator
    File serverDir

    @EventListener
    @Async
    void dataGeneratorManagement(final BootstrapCompleteEvent event) {

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
                serverDir = new File(path)
                println('Data generation has not been run--running data generation')
                runDataGenerator()
            }
        } else {
            println('Whoops: No path exists')
        }
    }

    @Async
    void runDataGenerator() {
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

    @EventListener
    @Async
    void itemIngester(final DataGenerationCompleteEvent event) {
        int itemCount = 0
        try {
            transactionService.withTransaction {
               itemCount = MinecraftItem.count()
            }
        } catch (Exception e) {
            println("Whoops: ${e.message}")
        }

        if (itemCount == 0) {
            println('Ingesting items')
            try {
                String jsonFilePath = "${path}${separator}generated${separator}reports${separator}registries.json"
                JsonReader jsonReader = new JsonReader(
                    new InputStreamReader(
                        new FileInputStream(jsonFilePath),
                        StandardCharsets.UTF_8
                    )
                )
                readJsonFile(jsonReader)
            } catch (Exception e) {
                println("Whoops: ${e.message}")
            }
        } else {
            println('Minecraft items already ingested')
        }
    }

    String minecraftNameToDisplayName(String mcName) {
        String output = mcName.replace("minecraft:", "").replace("_", " ")
        return toDisplayCase(output)
    }

    String toDisplayCase(String s) {
        final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
                                                    // to be capitalized
        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
        }
        return sb.toString();
    }

    // Read in JSON file line by line
    void readJsonFile(JsonReader jsonReader) {
        // Store level in JSON object, so we know when we're at the top route
        // TOP ROUTE IS LEVEL 1
        int level = 0;

        while(jsonReader.peek() != JsonToken.END_DOCUMENT) {
            JsonToken jsonToken = jsonReader.peek();

            switch (jsonToken) {
                case JsonToken.BEGIN_ARRAY:
                    level++
                    jsonReader.beginArray()
                    break;
                case JsonToken.END_ARRAY:
                    level--
                    jsonReader.endArray()
                    break;
                case JsonToken.BEGIN_OBJECT:
                    level++
                    jsonReader.beginObject()
                    break;
                case JsonToken.END_OBJECT:
                    level--
                    jsonReader.endObject()
                    break;
                case JsonToken.NAME:
                    // Skip all top level registries except minecraft:item
                    switch (level) {
                        case 1:
                            // We're at the top level, check if right object
                            if (jsonReader.nextName() != "minecraft:item") {
                                jsonReader.skipValue()
                            }
                            break;
                        case 3:
                            // This is now the name of a minecraft item
                            String name = jsonReader.nextName()
                            try {
                                transactionService.withTransaction {
                                    MinecraftItem mi = new MinecraftItem(
                                        [
                                            name: name,
                                            displayName: minecraftNameToDisplayName(name)
                                        ]
                                    )
                                    mi.save()
                                }
                            } catch (Exception e) {
                                println("Failure importing item: ${name}")
                            }
                        default:
                            jsonReader.skipValue()
                            break;
                    }
                    break;
                default:
                    jsonReader.skipValue()
                    break;
            }
        }
    }
}