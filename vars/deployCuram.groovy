def call()
{
    def util = new commonUtil();
    util.getGITSource(env.SPM_REPO,'')
    sh """
        
        echo "copy the override file and deplying the application"
        cp -vf ${env.WORKSPACE}/config/spm-override.yaml helm-charts/spm-override.yaml
        
        helm install ${env.RELEASE_NAME} ${env.HELM_REPO} -f helm-charts/spm-override.yaml --kubeconfig ~/kubeconfig
        
    """
}
