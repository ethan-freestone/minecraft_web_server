package web_server.controllers

import groovy.transform.CompileDynamic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Body

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Delete

import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import javax.annotation.Nullable

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType

import grails.gorm.transactions.Transactional

@CompileDynamic
class GORMController<T> {

    final Class<T> type;
    GORMController(Class<T> type) {
        this.type = type
    }

    @Transactional
    @Post('/')
    HttpResponse<T> saveResource(@Body T resource) {
        try {
            resource.save()
            return HttpResponse.ok( resource )
        }
        catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

    @Transactional
    @Get('/')
    HttpResponse<List> getResources(
        @Nullable @QueryValue('max') Integer max,
        @Nullable @QueryValue('sort') String sort = 'id',
        @Nullable @QueryValue('order') String order = 'asc'
    ) {
        try {
            List<T> rList = max ? type.list(max: max, sort: sort, order: order) : type.list()
            return HttpResponse.ok( rList )
        }
        catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

    @Transactional
    @Get('/{id}')
    HttpResponse<T> getResource(@PathVariable String id) {
        try {
            T resource = type.findById(id)
            return HttpResponse.ok( resource )
        }
        catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

    @Transactional
    @Put('/{id}')
    HttpResponse<T> updateResource(@PathVariable String id, @Body Map resource) {
        try {
            T dbResource = type.findById(id)
            resource.each { key, value ->
                // Go property by property, except for the id
                // TODO there's probably a better/safer way to do this
                if (key != 'id') {
                    dbResource[key] = value
                }
            }
            dbResource.save()
            return HttpResponse.ok( dbResource )
        }
        catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

    // Bulk endpoints
    @Transactional
    @Post('/bulk')
    HttpResponse<T> saveResources(@Body List<T> resources) {
        try {
            List<T> returnList = []
            resources.each { T resource ->
                resource.save()
                returnList.add(resource)
            }
            return HttpResponse.ok( returnList )
        }
        catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }
}