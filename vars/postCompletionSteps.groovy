def call(buildResult){

    def util = new commonUtil()
    def notifyUtil = new notifyUtility()

    if( buildResult == "SUCCESS" ) {

        util.tagOnComplete()
        echo "${buildResult}"
    }

    notifyUtil.notify(buildResult)




}
