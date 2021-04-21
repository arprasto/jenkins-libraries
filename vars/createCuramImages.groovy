def call(TAG_PREFIX) 
{
    def util = new commonUtil();
    util.dockerlogin();
    def tagName = util.generateTagForRepo(TAG_PREFIX)
    util.dockerbuild('xmlserver','XMLServer',tagName,'--build-arg="BASE_REGISTRY=${DOCKER_REGISTRY}/"')
    util.dockerpush('xmlserver',tagName)
    util.dockerbuild('xmlserver','XMLServer','latest','--build-arg="BASE_REGISTRY=${DOCKER_REGISTRY}/"')
    util.dockerpush('xmlserver','latest')
    util.dockerbuild('utilities','Utilities',tagName,'--build-arg="BASE_REGISTRY=${DOCKER_REGISTRY}/"')
    util.dockerpush('utilities',tagName)
    util.dockerbuild('utilities','Utilities','latest','--build-arg="BASE_REGISTRY=${DOCKER_REGISTRY}/"')
    util.dockerpush('utilities','latest')
    sh """ echo -e 'content/release-stage/ear' > .dockerignore """
    util.dockerbuild('batch','Batch',tagName,'')
    sh """ rm -f .dockerignore """
    util.dockerpush('batch',tagName)
    sh """ echo -e 'content/release-stage/ear' > .dockerignore """
    util.dockerbuild('batch','Batch','latest','')
    sh """ rm -f .dockerignore """
    util.dockerpush('batch','latest')
    util.dockerbuild('servercode','ServerEAR',tagName,'--build-arg="MQ_RA_LICENSE=--acceptLicense"')
    util.dockerpush('servercode',tagName)
    util.dockerbuild('servercode','ServerEAR','latest','--build-arg="MQ_RA_LICENSE=--acceptLicense"')
    util.dockerpush('servercode','latest')
    util.dockerbuild('curam','ClientEAR',tagName,'--build-arg=SERVERCODE_IMAGE=${CURAM_IMAGES}/servercode:'+tagName+' --build-arg="EAR_NAME=Curam"')   
    util.dockerpush('curam',tagName)
    util.dockerbuild('curam','ClientEAR','latest','--build-arg="SERVERCODE_IMAGE=${CURAM_IMAGES}/servercode:latest" --build-arg="EAR_NAME=Curam"')   
    util.dockerpush('curam','latest')
    util.dockerbuild('rest','ClientEAR',tagName,'--build-arg=SERVERCODE_IMAGE=${CURAM_IMAGES}/servercode:'+tagName+' --build-arg="EAR_NAME=Rest"')
    util.dockerpush('rest',tagName)
    util.dockerbuild('rest','ClientEAR','latest','--build-arg="SERVERCODE_IMAGE=${CURAM_IMAGES}/servercode:latest" --build-arg="EAR_NAME=Rest"')
    util.dockerpush('rest','latest')
    util.dockerbuild('httpd','StaticContent',tagName,'--build-arg="BASE_REGISTRY=${DOCKER_REGISTRY}/" --build-arg="CONTENT_DIR=/CuramStatic"')
    util.dockerpush('httpd',tagName)
    util.dockerbuild('httpd','StaticContent','latest','--build-arg="BASE_REGISTRY=${DOCKER_REGISTRY}/" --build-arg="CONTENT_DIR=/CuramStatic"')
    util.dockerpush('httpd','latest')
   
    sh """ echo "All images successfully created" """

}
