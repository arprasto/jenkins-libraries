def call(USE_TAG) {
   
    def util = new commonUtil()

    // Download source code 
    util.downloadSourceCode(USE_TAG)

   // sh "printenv"
    echo "ENV  resources  download complete"

    def BOOT_PATH= "${env.CURAM_DIR}/EJBServer/project/properties/Bootstrap.properties"
    def APPSERV_PATH = "${env.CURAM_DIR}/EJBServer/project/properties/AppServer.properties"
    // use prefix bootstrap. For bootstrap.properties file and appserver. in the job specific config file to change the properties
    echo "calling update properties "
    // update bootstrap
    util.updateProperties(BOOT_PATH,'bootstrap.')
    //update Properties
	util.updateProperties(APPSERV_PATH,'appserver.')
	//sh "cp -vf ${env.RESOURCE_HOME}/resources/properties/AppServer.properties  ${env.CURAM_DIR}/EJBServer/project/properties/AppServer.properties"
                //TODO: Standardize bootstrap ..
	//sh "cp -vf ${env.WORKSPACE}/config/Bootstrap_db2.properties  ${env.CURAM_DIR}/EJBServer/project/properties/Bootstrap.properties"
	
   

}
