def call(TAG_PREFIX)
{
    def util = new commonUtil();
    def tagName = util.generateTagForRepo(TAG_PREFIX)
    echo "Rolling out the new image with values ${CURAM_IMAGES}/uawebapp:${tagName}"
    sh """ kubectl set image deployment/${env.RELEASE_NAME}-uawebapp uawebapp=${CURAM_IMAGES}/uawebapp:${tagName} --kubeconfig ~/kubeconfig """
}
