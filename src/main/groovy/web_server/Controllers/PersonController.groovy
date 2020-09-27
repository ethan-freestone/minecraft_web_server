package web_server.controllers

import web_server.domain.Person

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType

import grails.gorm.transactions.Transactional

@Controller("/person")
class PersonController {

    @Transactional
    @Post("/save")
    HttpResponse<Person> savePerson(@Body Person person) {
        try {
            person.save()
            return HttpResponse.ok( person )
        }
        catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

    @Transactional
    @Get("/")
    HttpResponse<List> getPeople() {
        try {
            List<Person> pList = Person.findAll()
            return HttpResponse.ok( pList )
        }
        catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

}