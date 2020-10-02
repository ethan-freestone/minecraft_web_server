package web_server.domain
import groovy.transform.CompileDynamic
import grails.gorm.annotation.Entity

@CompileDynamic
@Entity
class Person extends BasicGormEntity {
    String name
    Integer age
    String desc

    static constraints = {
        name nullable: false
        age nullable: true
        desc nullable: true
    }
}
