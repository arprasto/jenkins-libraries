def call() {
    def STD_SERVER_BUILDS = "clean server"
    def util = new commonUtil();

    // build clean server
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
