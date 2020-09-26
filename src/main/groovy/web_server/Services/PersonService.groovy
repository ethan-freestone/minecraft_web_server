package web_server.services

import web_server.domain.Person
import grails.gorm.services.Service
import groovy.transform.CompileStatic

import javax.validation.constraints.NotNull

@CompileStatic
@Service(Person)
abstract class PersonService {
    abstract int count()
    abstract List<Person> findAll()
    abstract List<Person> findAll(Map args)
    abstract Person find(@NotNull Long id)
    abstract Person save(Person person)
    abstract Person delete(@NotNull Long id)
}
