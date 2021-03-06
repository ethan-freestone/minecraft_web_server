package web_server.controllers

import groovy.transform.CompileDynamic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Body

import javax.validation.Valid
import org.springframework.validation.FieldError
import org.grails.datastore.mapping.validation.ValidationException


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
    final ArrayList<String> persistentProperties;

    GORMController(Class<T> type) {
        this.type = type
        // Grab list of non-inherited persistent properties for Class<T>
        this.persistentProperties = type.gormPersistentEntity.persistentProperties.findAll {
            it.isInherited() == false
        }.collect {
            it.name
        }
    }

    @Transactional
    @Post('/')
    HttpResponse<T> saveResource(@Body @Valid T resource) {
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

        // TODO implement FILTERS or PARAMS block to search on a particular prop
        try {
            List<T> rList = type.list(
                max: max ?: null,
                sort: sort ?: null,
                order: order ?: null
            )
            return HttpResponse.ok( rList )
        }
        catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

    @Transactional
    @Get('/count')
    HttpResponse<List> getResourceCount() {
        try {
            int rCount = type.count()
            return HttpResponse.ok( rCount )
        }
        catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

    @Transactional(readOnly=true)
    @Get('/{id}')
    HttpResponse<T> getResource(@PathVariable String id) {
        try {
            T resource = type.get(id)
            return HttpResponse.ok( resource )
        }
        catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

    @Transactional
    @Put('/{id}')
    HttpResponse<T> updateResource(@PathVariable String id, @Body Map resource) {
        T dbResource
        try {
            dbResource = type.get(id)
            // For each of the persistent properties, check incoming Map and bind to correct field
            persistentProperties.each {
                if(resource[it]) {
                    dbResource[it] = resource[it]
                }
            }
            dbResource.save()
            return HttpResponse.ok( dbResource )
        } catch (ValidationException e) {
            return HttpResponse.unprocessableEntity().body(
                [
                    person: dbResource,
                    errors: e.errors.allErrors.collect { FieldError err ->
                        [
                            field: err.field,
                            rejectedValue: err.rejectedValue,
                            message: err.defaultMessage
                        ]
                    }
                ]
            ) as HttpResponse<Map>
        } catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

    @Transactional
    @Delete('/{id}')
    HttpResponse deleteResource(@PathVariable String id) {
        try {
            T dbResource = type.get(id)
            dbResource.delete()
            return HttpResponse.ok( )
        } catch(Exception e) {
           println("Whoops ${e.message}")
        }
    }

    // Bulk endpoints
    @Transactional(rollbackFor=Exception.class)
    @Post('/bulk')
    HttpResponse<T> saveResources(@Body @Valid List<T> resources) {
        // Here we just throw an exception if any of the resources aren't valid,
        // so as to invoke the rollback and prevent the rest of the data from saving
        List<T> returnList = []
        resources.each { T resource ->
            resource.save()
            returnList.add(resource)
        }
        return HttpResponse.ok( returnList )
    }
}