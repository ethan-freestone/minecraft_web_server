package web_server.domain

import groovy.transform.CompileDynamic
import grails.gorm.annotation.Entity
import io.micronaut.core.annotation.Introspected

import java.time.LocalDateTime

import com.fasterxml.jackson.annotation.JsonFormat

@CompileDynamic
@Entity
@Introspected
class BasicGormEntity {

    String id

    @JsonFormat(
        locale='DEFAULT_LOCALE',
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS"
    )
    LocalDateTime dateCreated
    @JsonFormat(
        locale='DEFAULT_LOCALE',
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS"
    )
    LocalDateTime lastUpdated

    static mapping = {
        id generator: 'uuid'
    }

}
