package web_server.domain
import groovy.transform.CompileDynamic
import grails.gorm.annotation.Entity

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.*

@CompileDynamic
@Introspected
@Entity
class ServerConfig extends BasicGormEntity {

    // Unique
    @NotBlank
    String name

    String settingString

    static mapping = {
        name unique: true
    }

}
