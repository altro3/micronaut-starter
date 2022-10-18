package io.micronaut.starter.feature.crac

import groovy.xml.XmlSlurper
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared

class CracSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    Crac crac = beanContext.getBean(Crac)

    void 'test readme.md with feature crac contains links to micronaut docs'() {
        when:
        def output = generate(['crac'])
        def readme = output["README.md"]

        then:
        readme.contains("[Micronaut Support for CRaC (Coordinated Restore at Checkpoint) documentation](https://micronaut-projects.github.io/micronaut-crac/latest/guide)")
        readme.contains("[https://wiki.openjdk.org/display/CRaC](https://wiki.openjdk.org/display/CRaC)")
    }

    void "feature crac #desc for #applicationType"(ApplicationType applicationType) {
        expect:
        crac.supports(applicationType) == expected

        where:
        applicationType           | expected
        ApplicationType.CLI       | true
        ApplicationType.DEFAULT   | true
        ApplicationType.FUNCTION  | false
        ApplicationType.MESSAGING | false
        ApplicationType.GRPC      | false

        desc = expected ? "is supported" : "is not supported"
    }

    void "test #buildTool crac feature for #language adds plugin and dependency"() {
        when:
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["crac"])
                .render()

        then:
        template.contains('id("io.micronaut.crac") version')
        template.contains('implementation("io.micronaut.crac:micronaut-crac")')

        where:
        [language, buildTool] << [Language.values().toList(), [BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN]].combinations()
    }

    void "test maven crac feature adds dependency"() {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .features(["crac"])
                .render()
        def pom = new XmlSlurper().parseText(template)

        then:
        with(pom.dependencies.dependency.find { it.artifactId == "micronaut-crac" }) {
            scope.text() == 'compile'
            groupId.text() == 'io.micronaut.crac'
        }
    }
}