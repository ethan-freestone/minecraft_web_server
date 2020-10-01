package web_server.controllers

import web_server.domain.Person
import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller

@Controller("/person")
class PersonController extends GORMController<Person> {

    PersonController() {
        super(Person)
    }
}