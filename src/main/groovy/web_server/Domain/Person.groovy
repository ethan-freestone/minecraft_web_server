package web_server.domain
import groovy.transform.CompileDynamic
import grails.gorm.annotation.Entity

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.*

@CompileDynamic
@Introspected
@Entity
class Person extends BasicGormEntity {
    @NotBlank
    String name

    @Max(100L)
    @Min(0L)
    Integer age
    String desc
}
