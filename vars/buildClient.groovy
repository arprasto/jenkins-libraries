def call() {
    def STD_CLIENT_BUILDS = "${params.clientBuildCommand}"
    def util = new commonUtil();
    util.SetEnvironment()

    echo "${env.CLIENT_DIR}"
    env.ANT_OPTS="-Xmx1400m -Dcmp.maxmemory=1400m -Xbootclasspath/p:${env.CURAMCDEJ}/lib/ext/jar/serializer.jar:${env.CURAMCDEJ}/lib/ext/jar/xercesImpl.jar:${env.CURAMCDEJ}/lib/ext/jar/xalan.jar"

    if(STD_CLIENT_BUILDS.toString().trim() == ""){
      echo "skipping client builds"
    }else{
    dir("${env.CLIENT_DIR}"){
      sh """
          #!/bin/bash
          echo "continueing with environment"
          env
          ant -f $CDEJ_BUILDFILE ${STD_CLIENT_BUILDS}
      """
    }
    }
    echo "client Build completed"
}
