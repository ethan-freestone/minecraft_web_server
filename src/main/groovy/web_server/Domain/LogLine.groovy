package web_server.domain
import groovy.transform.CompileDynamic
import grails.gorm.annotation.Entity

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.*

@CompileDynamic
@Introspected
@Entity
class LogLine extends BasicGormEntity {
    @NotBlank
    String serverTimeStamp
    @NotBlank
    String context
    @NotBlank
    String output
}
