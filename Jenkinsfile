#!/usr/bin/env groovy

podTemplate(label: 'build', containers: [
        containerTemplate(name: 'maven', image: 'maven:3.6.1-jdk-11-slim', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.14.6', command: 'cat', ttyEnabled: true)
], volumes: [
        hostPathVolume(mountPath: '/root/.m2/repository', hostPath: '/root/.m2')
]
) {
    node('build') {
        def deploymentId = UUID.randomUUID().toString()
        def buildNumber = env.BUILD_NUMBER
        def myRepo = checkout scm
        def gitBranch = myRepo.GIT_BRANCH
        def commit = myRepo.GIT_COMMIT
        def isMasterBranch = false;
        def isReleaseBranch = false;
        def packageVersion = "0.0.0";
        def isPr = false;
        def isFeatureBranch = false;
        def profile = 'master'
        if (gitBranch == 'master') {
            isMasterBranch = true;
        } else if (gitBranch =~ 'release.*') {
            isReleaseBranch = true;
            profile = 'release';
        } else if (env.CHANGE_ID) {
            isPr = true;
        } else {
            try {
                timeout(time: 15, unit: 'SECONDS') {
                    input message: "Do you need to build this branch ${gitBranch} ?", parameters: [
                            [$class: 'BooleanParameterDefinition', defaultValue: false, description: "Do you need to build this branch ${gitBranch} ?", name: 'Ready']]
                }
            } catch (err) {
                return;
            }

            isFeatureBranch = true;
        }


        stage('build') {
            container('maven') {
                configFileProvider(
                        [configFile(fileId: 'global-maven', variable: 'MAVEN_SETTINGS')]) {
                    sh """
                      mvn -P${profile} -s $MAVEN_SETTINGS -Dmaven.test.skip=true install
                      """
                }
            }
        }


        if (isFeatureBranch) {
            return;
        }

        if (isPr) {
            try {
                timeout(time: 15, unit: 'SECONDS') {
                    input message: "Ready to deploy PR ${gitBranch} ?", parameters: [
                            [$class: 'BooleanParameterDefinition', defaultValue: false, description: "Ready to deploy PR ${gitBranch} ?", name: 'Ready']]
                }
            } catch (err) {
                return;
            }
        }

        stage('Build Docker and Push to Nexus') {
            container('maven') {
                configFileProvider(
                        [configFile(fileId: 'global-maven', variable: 'MAVEN_SETTINGS')]) {
                    sh """
                      export BUILD_NUMBER=${buildNumber}
                      mvn -P${profile} -s $MAVEN_SETTINGS compile jib:build
                      """

                }
            }
        }
    }
}