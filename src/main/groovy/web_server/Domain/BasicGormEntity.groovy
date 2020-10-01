package web_server.domain
import groovy.transform.CompileDynamic
import grails.gorm.annotation.Entity

import com.fasterxml.jackson.annotation.JsonFormat

@CompileDynamic
@Entity
class BasicGormEntity {

    @JsonFormat(locale='DEFAULT_LOCALE')
    Date dateCreated
    @JsonFormat(locale='DEFAULT_LOCALE')
    Date lastUpdated

}
