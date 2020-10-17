package web_server.domain
import groovy.transform.CompileDynamic
import grails.gorm.annotation.Entity

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.*
import javax.persistence.Transient

import java.util.regex.*

@CompileDynamic
@Introspected
@Entity
class MinecraftItem extends BasicGormEntity {

    @NotBlank
    String name

    @NotBlank
    String displayName

    static mapping = {
        name unique: true
    }

}
