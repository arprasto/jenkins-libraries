def call(RepositoryWorkspace,checkoutPath,credentialsId,RTCServerURI){
def rtcutil = new rtcUtil()
rtcutil.checkoutcode(RepositoryWorkspace,checkoutPath,credentialsId,RTCServerURI);
}
