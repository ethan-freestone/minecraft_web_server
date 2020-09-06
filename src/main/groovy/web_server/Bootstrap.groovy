package web_server

import web_server.domain.Person
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener

import javax.inject.Singleton

@Singleton
class Bootstrap {

    @EventListener
    void onStartup(StartupEvent event) {
        Person kev = new Person(name: 'kevin', email: 'kevin@kevin.com')
        kev.save()
        Person fred = new Person(name: 'fred', email: 'fred@fred.com')
        fred.save()
    }
}