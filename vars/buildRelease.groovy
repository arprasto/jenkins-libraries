def call(RELEASE_SERVER_NAME,BUILD_FOLDER) {
    
    def util = new commonUtil();
    
    def CUSTOM_SERVER_BUILDS= "libertyEAR -Dcuram.ejbserver.app.name=${RELEASE_SERVER_NAME}"
    def SERVER_BUILDS="libertyEAR -Dserver.only=true \
		                   -Dear.name=${RELEASE_SERVER_NAME} \
		                   -DSERVER_MODEL_NAME=${RELEASE_SERVER_NAME} \
		                   -Dcuram.ejbserver.app.name=${RELEASE_SERVER_NAME}"
    def RELEASE_BUILD = "release -Dcreate.zip=true"
  
  
    sh """
    #!/bin/bash
    sed -i 's/name="Curam" requireServer="true"/name="Curam" requireServer="false"/g' \
    ${env.SERVER_DIR}/project/config/deployment_packaging.xml
    """	            
    util.buildCommand(env.SERVER_DIR,CUSTOM_SERVER_BUILDS)
    util.buildCommand(env.SERVER_DIR,SERVER_BUILDS)
    util.buildCommand(env.SERVER_DIR,RELEASE_BUILD)            	
  
	dir(BUILD_FOLDER){
	sh """
	#!/bin/bash
        rm -rf *
        cp -vf  ${env.RELEASE_PATH}/release.zip .
        cp -vf  ${env.CURAM_DIR}/webclient/build/StaticContent.zip .
         
	echo "Build complete"
        """
	}
}
