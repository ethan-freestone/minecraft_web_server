package web_server.controllers

import web_server.domain.MinecraftItem
import groovy.transform.CompileDynamic
import io.micronaut.http.annotation.Controller

@CompileDynamic
@Controller('/minecraftItem')
class MinecraftItemController extends GORMController<MinecraftItem> {
    MinecraftItemController() {
        super(MinecraftItem)
    }
}
