def call() 
 {           
    withSonarQubeEnv('sonar') {
    
    sh ''' ${scannerHome}/bin/sonar-scanner -Dproject.settings=./sonar-project.properties
           '''
      }
 }
