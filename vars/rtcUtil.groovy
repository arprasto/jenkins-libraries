def checkoutcode(){
checkout([$class: 'RTCScm',
avoidUsingToolkit: false,
buildTool: '6.0.4',
buildType: [buildWorkspace: params.RepositoryWorkspace,
componentLoadConfig: 'loadAllComponents',
customizedSnapshotName: '',
loadDirectory: env.checkoutPath,
loadPolicy: 'useComponentLoadConfig', value: 'buildWorkspace'],
credentialsId: params.credentialsId,
overrideGlobal: false, 
serverURI: params.RTCServerURI, timeout: 480])
}
