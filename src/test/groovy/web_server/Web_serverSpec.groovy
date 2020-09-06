package web_server

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification
import javax.inject.Inject

@MicronautTest
class Web_serverSpec extends Specification {

    @Inject
    EmbeddedApplication application

    void 'test it works'() {
        expect:
        application.running
    }

}