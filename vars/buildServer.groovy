def call() {
    def STD_SERVER_BUILDS = "${params.serverBuildCommand}"
    def util = new commonUtil();
    util.SetEnvironment()
    echo "${env.SERVER_DIR}"
    dir("${env.SERVER_DIR}"){
      sh """
          #!/bin/bash
          echo "continueing with environment"
          env
          ant -f ${env.SDEJ_BUILDFILE} -Dprp.noninternedstrings=true ${STD_SERVER_BUILDS}
      """
    }
    echo "server Build completed"
}
