def slacknotification(MESSAGE,COLOR)
{
    slackSend channel: "${env.SLACK_CHANNEL}", color: "${COLOR}", message: "${MESSAGE}", teamDomain: "${env.SLACK_TEAM_DOMAIN}", tokenCredentialId: env.SLACK_CREDENTIAL
}

def notify(TYPE){
    def util = new commonUtil()
    def CUSTOM_TAG_NAME = util.generateTagForRepo(env.TAG_PREFIX)
    def MESSAGE =""
    def COLOR = ""
    if(TYPE=="SUCCESS"){
        MESSAGE = "${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was successful and tagged as  ${CUSTOM_TAG_NAME}"
        COLOR = "good"
        
    }
    else if(TYPE=="FAILURE"){
        MESSAGE = "${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was unsuccessful and needs your attention"
        COLOR = "danger"
    }
    callGenericNotify(MESSAGE,COLOR)

}

def callGenericNotify(MESSAGE,COLOR){
    if(env.NOTIFY_TYPE=="SLACK"){
        slacknotification(MESSAGE,COLOR)
    }

}
    
