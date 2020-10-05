package web_server.domain

import groovy.transform.CompileDynamic
import grails.gorm.annotation.Entity
import io.micronaut.core.annotation.Introspected

import com.fasterxml.jackson.annotation.JsonFormat

@CompileDynamic
@Entity
@Introspected
class BasicGormEntity {

    String id

    @JsonFormat(locale='DEFAULT_LOCALE')
    Date dateCreated
    @JsonFormat(locale='DEFAULT_LOCALE')
    Date lastUpdated

    static mapping = {
        id generator: 'uuid'
    }

}
