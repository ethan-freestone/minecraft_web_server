package web_server.controllers

import web_server.domain.Person
import groovy.transform.CompileDynamic
import io.micronaut.http.annotation.Controller

@CompileDynamic
@Controller('/person')
class PersonController extends GORMController<Person> {
    PersonController() {
        super(Person)
    }
}