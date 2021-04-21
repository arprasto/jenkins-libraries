def call() 
{
    def util = new commonUtil();
    util.dockerlogin()
    sh """ 
     docker pull wh-spm-blore-isl-team-repo-docker-local.artifactory.swg-devops.com/ibm/cute:2.4.1.0
    """
 }
