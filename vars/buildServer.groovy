def call() {
    def STD_SERVER_BUILDS = "clean server"
    def util = new commonUtil();

    // build clean server
    util.buildCommand(env.SERVER_DIR,STD_SERVER_BUILDS)
    echo "server Build completed"
}
