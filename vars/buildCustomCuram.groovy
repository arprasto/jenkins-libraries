def call() {
    
    def util = new commonUtil();
    

    if ( env.CUSTOM_SERVER_BUILDS != "" ) {
        // build clean server
        util.buildCommand(env.SERVER_DIR,env.CUSTOM_SERVER_BUILDS)
        echo "calling server builds"
    }
   
    if ( env.CUSTOM_CLIENT_BUILDS != "" ) {
         // Build client
        util.buildCommand(env.CLIENT_DIR,env.CUSTOM_CLIENT_BUILDS)
        echo "calling client builds"
    }
    echo "Build complete"

}