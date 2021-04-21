

def getGITSource(SOURCE_REPO,CRED_SOURCE,USE_TAG) 
{
    
    if(USE_TAG == "true"){
        
      
        pulltag(SOURCE_REPO,CRED_SOURCE)
        
    }else {
        echo "No TAG"
       
        git credentialsId: CRED_SOURCE ,branch: params.BRANCH, url: SOURCE_REPO
       
    }
}

def pulltag(SOURCE_REPO,CRED_SOURCE)
{

    def TAG_TO_USE = params.TAG
    if(TAG_TO_USE!=null && TAG_TO_USE!=""){
        checkout([$class: 'GitSCM', 
            branches: [[name: "refs/tags/${TAG_TO_USE}"]], 
            poll: false,
            userRemoteConfigs: [[credentialsId: CRED_SOURCE, 
            url: SOURCE_REPO]]])
    }
    else{
        def latestTag = ""
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId:"${env.SOURCE_CREDS}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
            sh("git config credential.username ${env.USERNAME}")
            sh("git config credential.helper '!echo password=${env.PASSWORD}; echo'")
            latestTag =   sh(returnStdout: true, script: "git  ls-remote --exit-code --refs --sort='version:refname' ${env.SOURCE_REPO}  | tail --lines=1 |cut --delimiter='/' --fields=3").trim()
            
        }
        echo "The tag being used is ${latestTag} " 

        checkout([$class: 'GitSCM', 
            branches: [[name: "refs/tags/${latestTag}"]], 
            poll: false,
            userRemoteConfigs: [[credentialsId: CRED_SOURCE, 
            url: SOURCE_REPO]]])
    }
    
    
    
}

def createTAGOnRepo(){

    def CUSTOM_TAG_NAME = util.generateTagForRepo(env.TAG_PREFIX)

    dir(env.CURAM_DIR) {
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId:"${env.SOURCE_CREDS}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
            sh("git config credential.username ${env.USERNAME}")
            sh("git config credential.helper '!echo password=${env.PASSWORD}; echo'")
            sh("git tag -a ${CUSTOM_TAG_NAME} -m 'tag is pushed successfully'")
            sh("GIT_ASKPASS=true git push ${env.SOURCE_REPO} --tags")
        }
    }
 
}


