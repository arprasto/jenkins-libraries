def call(newRolloutValues) 
{
    def util = new commonUtil();
    echo "Rolling out the new image with values ${newRolloutValues}"
    sh """
    helm repo update
    helm upgrade ${env.RELEASE_NAME} ${env.HELM_REPO} --reuse-values --set ${newRolloutValues}  --kubeconfig ~/kubeconfig
    """
}
