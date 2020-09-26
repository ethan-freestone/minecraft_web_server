package web_server.controllers

import web_server.domain.Person
import web_server.services.PersonService

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType

import org.springframework.validation.*
import javax.inject.*

@CompileStatic
@Controller("/person")
class PersonController {

    @Inject PersonService personService

    @Post("/save")
    HttpResponse<Map> savePerson(@Body Person person) {
        try {
            return HttpResponse.ok( [ person: personService.save(person) ] as Map )
        }
        catch(Exception e) {
           println("Whoops")
        }
    }
}