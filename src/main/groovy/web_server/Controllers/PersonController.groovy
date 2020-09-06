package web_server.controllers

import web_server.domain.Person
import io.micronaut.http.annotation.Controller
import groovy.transform.CompileStatic

@CompileStatic
@Controller("/person")
class PersonController extends RestfulController<Person> {

    PersonController( ) {
        super(Person)
    }
}