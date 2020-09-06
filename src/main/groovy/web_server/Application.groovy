package web_server

import io.micronaut.runtime.Micronaut
import groovy.transform.CompileStatic

@CompileStatic
class Application {
    static void main(String[] args) {
        Micronaut.build(args)
                .packages('web_server.domain')
                .mainClass(Application.class)
                .start()
    }
}