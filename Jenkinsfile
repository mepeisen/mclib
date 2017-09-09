pipeline {
   agent any
   tools {
      maven 'maven3.5.0'
      jdk 'jdk8'
   }
   stages {
      stage ('Initialization') {
          steps {
              echo 'Preparing for build'
          }
      }
      
      stage ('Build') {
          steps {
              sh 'mvn clean install' 
              archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
              
              step([$class: 'CheckStylePublisher',
                    canRunOnFailed: true,
                    defaultEncoding: '',
                    healthy: '',
                    pattern: '',
                    unHealthy: '',
                    useStableBuildAsReference: true
              ])
              step([$class: 'FindBugsPublisher'])
              step([$class: 'PmdPublisher'])
              step([$class: 'JacocoPublisher'])
          }
          post {
              always {
                  junit '**/target/surefire-reports/*.xml'
              }
              failure {
                  mail to: 'mep_eisen@web.de', subject: 'build of mclib failed', body: 'build of mclib failed'
              }
          }
      }
      
      stage ('Deploy') {
          when {
              expression {
                  currentBuild.result == null || currentBuild.result == 'SUCCESS'
              }
          }
          steps {
              sh 'mvn -Deisenschmiede.deployment=true -Dmaven.test.skip=true -DskipTests deploy -P !mclib.withcoverage,!mclib.withjavadoc,!mclib.withsourcereport,!mclib.withtestreport,!mclib.withjavadocreport,!mclib.withcheckstyle,!mclib.withpmd,!mclib.withfindbugs'
          }
          post {
              failure {
                  mail to: 'mep_eisen@web.de', subject: 'deploy of mclib failed', body: 'deploy of mclib failed'
              }
          }
      }
   }
}