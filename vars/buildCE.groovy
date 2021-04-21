def call() 
{
    def util = new commonUtil();
    util.getGITSource(env.CE_REPO,env.SOURCE_CREDS)
    echo "Installing modules"
    sh 'npm install'
    sh 'npm rebuild node-sass'
    echo "Building CE"
    sh 'npm run build'
    sh 'mkdir -p dockerstage'
    sh 'cp -r /spm-kubernetes/dockerfiles dockerstage'
    sh 'cp -r build dockerstage/dockerfiles/HTTPServer/build'
    echo "CE-Build complete"
}
