def call() {
    def STD_SERVER_BUILDS = "${params.serverBuildCommand}"
    def util = new commonUtil();
    util.SetEnvironment()

    //util.buildCommand(env.SERVER_DIR,STD_SERVER_BUILDS)
    echo "${env.SERVER_DIR}"
    withAnt(installation: '1.10.6', jdk: 'jdk1.8.0_131') {
      dir("${env.SERVER_DIR}"){
        sh """
            #!/bin/bash
            echo "continueing with environment"
            env
            chmod +x build.sh
            ./build.sh ${STD_SERVER_BUILDS}
        """
      }
    }
    echo "server Build completed"
}
