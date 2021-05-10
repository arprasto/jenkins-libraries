def call() {
    def STD_SERVER_BUILDS = "${params.serverBuildCommand}"
    def util = new commonUtil();
    util.SetEnvironment()

    //util.buildCommand(env.SERVER_DIR,STD_SERVER_BUILDS)
    echo "${env.SERVER_DIR}"
      dir("${env.SERVER_DIR}"){
        sh """
            #!/bin/bash
            echo "continueing with environment"
            env
            chmod +x ./build.sh
            ./build.sh ${STD_SERVER_BUILDS}
        """
      }
    echo "server Build completed"
}
