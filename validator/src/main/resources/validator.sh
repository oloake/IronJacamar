#!/bin/sh
java -classpath ironjacamar-validator-cli.jar:jbossxb.jar:jboss-logging-spi.jar:jboss-common-core.jar:jboss-metadata-rar.jar:jboss-metadata-common.jar:jboss-reflect.jar:jboss-mdr.jar:ironjacamar-spec-api.jar:papaki-core.jar:javassist.jar:ironjacamar-common-impl.jar:ironjacamar-validator.jar org.jboss.jca.validator.cli.Main $*
