def call() {
    def STD_CLIENT_BUILDS = "clean client external-client -Dapp=CitizenPortal"
    def util = new commonUtil();
    util.SetEnvironment()

    echo "${env.CLIENT_DIR}"
    withAnt(installation: '1.10.6', jdk: 'jdk1.8.0_131') {
      dir("${env.CLIENT_DIR}"){
        sh """
            #!/bin/bash
            echo "continueing with environment"
            env
            chmod +x build.sh
            ./build.sh ${STD_CLIENT_BUILDS}
        """
      }
    }
    echo "client Build completed"
}
