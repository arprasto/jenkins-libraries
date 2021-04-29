
def checkoutcode(RepositoryWorkspace,checkoutPath,credentialsId,RTCServerURI)
{
checkout([$class: 'RTCScm',
avoidUsingToolkit: false,
buildTool: '6.0.4',
buildType: [buildWorkspace: RepositoryWorkspace,
componentLoadConfig: 'loadAllComponents',
customizedSnapshotName: '',
loadDirectory: checkoutPath,
loadPolicy: 'useComponentLoadConfig', value: 'buildWorkspace'],
credentialsId: credentialsId,
overrideGlobal: false,
serverURI: RTCServerURI, timeout: 480])
}
