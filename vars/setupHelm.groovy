def call() 
{
    def util = new commonUtil();
    sh """
        echo "uninstall deployed application"
        helm uninstall ${env.RELEASE_NAME} --kubeconfig ~/kubeconfig
        echo "update image library path "
                    
        sed -i "s|imageLibrary:.*|imageLibrary: ${env.CURAM_IMAGES_PATH}/${params.BRANCH} |g" ${env.WORKSPACE}/config/spm-override.yaml
        sed -i "s|library:.*|library: ${env.CURAM_IMAGES_PATH}/${params.BRANCH} |g" ${env.WORKSPACE}/config/spm-override.yaml
    """
    if(params.SPM_IMAGE_TAG != ""){
        sh """ sed -i "s|imageTag:.*|imageTag: ${params.SPM_IMAGE_TAG} |g" ${env.WORKSPACE}/config/spm-override.yaml """
    }
    if(params.CE_IMAGE_TAG != ""){
        sh """ sed -i "s|tag:.*|tag: ${params.CE_IMAGE_TAG} |g" ${env.WORKSPACE}/config/spm-override.yaml """
    }            
    sh """ helm repo list """

}
