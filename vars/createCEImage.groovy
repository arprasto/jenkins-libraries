def call(TAG_PREFIX) 
{
    def util = new commonUtil();
    util.dockerlogin();
    def tagName = util.generateTagForRepo(TAG_PREFIX)
    util.dockerbuild('uawebapp','UAWebApp',tagName,'--build-arg="CONTENT_DIR=/universal" --build-arg="BASE_REGISTRY=${DOCKER_REGISTRY}/"')
    util.dockerpush('uawebapp',tagName)
    util.dockerbuild('uawebapp','UAWebApp','latest','--build-arg="CONTENT_DIR=/universal" --build-arg="BASE_REGISTRY=${DOCKER_REGISTRY}/"')
    util.dockerpush('uawebapp','latest')
    sh """ echo "Created CE image" """
 }
