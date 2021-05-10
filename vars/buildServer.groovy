def call() {
    def STD_SERVER_BUILDS = "clean server"
    def util = new commonUtil();

    // build clean server

    dir("${env.checkoutPath}/Curam"){
      sh """
          #!/bin/bash
          cat SetEnvironment.sh|grep SERVER_COMPONENT_ORDER>SERVER_COMPONENT_ORDER.sh
          cat SetEnvironment.sh|grep CLIENT_COMPONENT_ORDER>CLIENT_COMPONENT_ORDER.sh
          chmod +x *.sh
          ./SERVER_COMPONENT_ORDER.sh
          ./CLIENT_COMPONENT_ORDER.sh
      """
    }

    //util.buildCommand(env.SERVER_DIR,STD_SERVER_BUILDS)
    withAnt(installation: '1.10.6', jdk: 'jdk1.8.0_131') {
      dir(env.SERVER_DIR){
        sh """
            #!/bin/bash
            chmod +x build.sh
            ./build.sh ${STD_SERVER_BUILDS}
        """
      }
    }
    echo "server Build completed"
}
