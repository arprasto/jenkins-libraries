def call() {
    
    
    def STD_SERVER_BUILDS = "clean server"

    def STD_CLIENT_BUILDS = "clean client zip-static-content"

    def util = new commonUtil();

    // build clean server
    util.buildCommand(env.SERVER_DIR,STD_SERVER_BUILDS)

    // Build client

    util.buildCommand(env.CLIENT_DIR,STD_CLIENT_BUILDS)
 
    echo "Build complete"

}