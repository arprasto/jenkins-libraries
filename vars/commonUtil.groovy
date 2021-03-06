import java.time.*
import java.time.format.DateTimeFormatter
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

def generateTagForRepo(TAG_PREFIX)
{

    def now = LocalDateTime.now()
    def buildDate = now.format(DateTimeFormatter.ofPattern("yy.MM"))
    def buildNumber = currentBuild.number
    def tagName = "${TAG_PREFIX}-${buildDate}.${buildNumber}"
    return tagName
}

def setBaseEnv()
{

    env.RESOURCE_HOME="${env.WORKSPACE}@2/spm-resources/devops-jenkins-pipeline"
    env.RELEASE_PATH = "${env.WORKSPACE}@2/spm-code/EJBServer/release/"
    env.STATIC_CONTENT= "${env.WORKSPACE}@2/spm-code/webclient/build"
    env.RELEASE_FOLDER="${env.WORKSPACE}@2/resources"
    env.SPM_HOME="${env.WORKSPACE}@2/dockerstage"
    env.BASE_IMAGES="${env.DOCKER_REGISTRY}/${env.BASE_IMAGES_PATH}"
    env.CURAM_IMAGES="${env.DOCKER_REGISTRY}/${env.CURAM_IMAGES_PATH}/${params.BRANCH}"

}


def loadConfiguration(PATH)
{
    properties = new Properties()
    def propertiesFile = new File(PATH)
    properties.load(propertiesFile.newDataInputStream())
    Set<Object> keys = properties.keySet();
    for(Object k:keys){
        String key = (String)k;
        String value =(String) properties.getProperty(key)
        env."${key}" = "${value}"
    }
}

def downloadSourceCode(USE_TAG){
    if ( env.SCM_TYPE == "GIT" ) {
        dir(env.CURAM_DIR) {
            def gitUtil = new gitUtility()
            gitUtil.getGITSource(env.SOURCE_REPO,env.SOURCE_CREDS,USE_TAG)
        }

    }
    else if ( env.SCM_TYPE == "RTC" ) {
        dir(env.checkoutPath) {

        }
    }
    else {

        // Implement custom code repo retrieval

    }
}

def buildCommand(BUILD_FOLDER,COMMAND)
{
    dir(BUILD_FOLDER){
        sh """
            #!/bin/bash
            chmod +x build.sh
            ./build.sh ${COMMAND}
        """
    }
}

def SetEnvironment()
{
    env.CURAM_DIR="${env.checkoutPath}/Curam"
    env.J2EE_JAR="${env.UTILDIR}/dockerfiles/spm-curam-builder/spm-curam-builder/dependencies/j2ee.jar"
    env.JAVAMAIL_HOME="${env.UTILDIR}/dockerfiles/spm-curam-builder/spm-curam-builder/dependencies"
    env.SERVER_LOCALE_LIST="${env.SERVER_LOCALE_LIST}"
    env.LOCALE_LIST="${env.LOCALE_LIST}"
    env.SERVER_MODEL_NAME="${env.SERVER_MODEL_NAME}"
    env.SERVER_DIR="${env.CURAM_DIR}/EJBServer"
    env.CURAMSDEJ="${env.CURAM_DIR}/CuramSDEJ"
    env.CLIENT_PROJECT_NAME="${env.SERVER_MODEL_NAME}"
    env.CLIENT_DIR="${env.CURAM_DIR}/webclient"
    env.CURAMCDEJ="${env.CURAM_DIR}/CuramCDEJ"
    env.DOCMAKER_HOME="${env.CURAM_DIR}/DocMaker"
    env.SDEJ_BUILDFILE="${env.SERVER_DIR}/build.xml"
    env.CDEJ_BUILDFILE="${env.CLIENT_DIR}/build.xml"
    env.LANG="${env.LANG}"
    env.PATH="${env.PATH}:${env.HELM_HOME}"
    env.JAVA_TOOL_OPTIONS="-Dfile.encoding=ISO-8859-1"

    dir("${env.checkoutPath}/Curam"){
      def SERVER_COMPONENT_ORDER=sh(script: "cat SetEnvironment.sh|grep SERVER_COMPONENT_ORDER|cut -d'=' -f 2", returnStdout: true).toString().trim()
      def CLIENT_COMPONENT_ORDER=sh(script: "cat SetEnvironment.sh|grep CLIENT_COMPONENT_ORDER|cut -d'=' -f 2", returnStdout: true).toString().trim()
      env.SERVER_COMPONENT_ORDER="${SERVER_COMPONENT_ORDER}"
      env.CLIENT_COMPONENT_ORDER="${CLIENT_COMPONENT_ORDER}"
    }

    dir("${env.JENKINS_HOME}/ant_home")
    {
    sh """
        #!/bin/bash
        if [ -d "${env.JENKINS_HOME}/ant_home/apache-ant-1.10.6/bin" ]; then
          echo "ant installation already exists"
        else
            wget https://archive.apache.org/dist/ant/binaries/apache-ant-1.10.6-bin.zip
            unzip -x apache-ant-1.10.6-bin.zip
            if [ -z ${env.ANT_HOME} ]; then
                echo "setting ANT_HOME"
            fi
        fi
    """
    env.ANT_HOME="${env.JENKINS_HOME}/ant_home/apache-ant-1.10.6"
    env.PATH="${env.PATH}:${env.ANT_HOME}/bin"
    }
}

/*
pre-request
ANT_HOME
job-config.properties

#!/bin/bash
export SERVER_LOCALE_LIST=en_US,en_GB,en
export LOCALE_LIST=en_US,en_GB,en
export SERVER_MODEL_NAME=Curam
export SERVER_DIR=$CURAM_DIR/EJBServer
export CURAMSDEJ=$CURAM_DIR/CuramSDEJ
export CLIENT_PROJECT_NAME=Curam
export CLIENT_DIR=$CURAM_DIR/webclient
export CURAMCDEJ=$CURAM_DIR/CuramCDEJ
export CLIENT_COMPONENT_ORDER=custom,PlatformConfig,CommonIntake,Intake,ReferralsLite,PCR,CREOLEProgramRecommendation,SummaryViews,CitizenContextViewer,WorkspaceServices,CitizenWorkspaceAdmin,FundPM,DecisionAssist,DynamicEvidence,CEFWidgets,CMIS,IntelligentEvidenceGathering,IEGAdmin,Datastore,Editors,SupervisorWorkspace,Verification,ServicePlans,sample,CTMInfrastructure,SamplePublicAccess,AdvancedEvidenceSharing,EvidenceBroker,CuramFinancialAdapter,CuramMDAdapter,Advisor,EvidenceSharing,PDC,EvidenceFlow,ValidationManager,SmartNavigator
export SERVER_COMPONENT_ORDER=custom,ISCommon,HCRAppeal,HCR,Navigator,FederalExchange,HCROnline,HCRCommon,CAAssessmentTracking,AssessmentTr

*/

def dockerlogin()
{
    withCredentials([usernamePassword(credentialsId: env.DOCKER_CREDS, passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_REG')])
    {
        echo "Logging into docker repo"
        sh """ docker login ${env.DOCKER_REGISTRY} -u=$DOCKER_REG -p=$DOCKER_PASS """
    }
}

def dockerbuild(IMAGE,FILE,tagName,BUILD_ARG)
{
    echo "create ${IMAGE} image"
	dir('dockerfiles'){
	    if(IMAGE != 'httpd' && IMAGE != 'uawebapp'){
	        dir('Liberty'){
		    if(BUILD_ARG != '')
		    sh """ docker build --tag ${CURAM_IMAGES}/${IMAGE}:${tagName} --file ${FILE}.Dockerfile ${BUILD_ARG} . """
		    else
		    sh """ docker build --tag ${CURAM_IMAGES}/${IMAGE}:${tagName} --file ${FILE}.Dockerfile . """
		}
	    }
	    else{
		dir('HTTPServer'){
		    sh """ docker build --tag ${CURAM_IMAGES}/${IMAGE}:${tagName} --file ${FILE}.Dockerfile ${BUILD_ARG} . """
		}
	    }
	}

}

def dockerpush(IMAGE,tagName)
{
    echo "push ${IMAGE} image"
    sh """docker push  ${CURAM_IMAGES}/${IMAGE}:${tagName}"""
}

def scale(POD_NAME)
{
    sh """ kubectl scale --replicas=0 ${POD_NAME} --kubeconfig ~/kubeconfig """
}


def updateProperties(UPDATE_FILE_PATH,PREFIX_PROPERTY){
    def workspace = env.WORKSPACE
	def JOB_CONFIG= "${workspace}/config/jobs/${JOB_NAME}-config.properties"
    echo "${JOB_CONFIG}"
    echo "reading job properties"
    try{
        def properties = new Properties()
        File propertiesFile = new File(JOB_CONFIG)
        properties.load(propertiesFile.newDataInputStream())

        echo " reading Bootstrap properties"

        def updateFileproperties = new Properties()

        File readPropFile = new File(UPDATE_FILE_PATH)
        updateFileproperties.load(readPropFile.newDataInputStream())
        def appserverProperties = new Properties()

        echo "changing properties in ${UPDATE_FILE_PATH}"
        Set<Object> keys = properties.keySet();


        for(Object k:keys){
            String key = (String)k;

            if(key.startsWith(PREFIX_PROPERTY)){
                String splitKey = key.substring(PREFIX_PROPERTY.length())
                String value = properties.getProperty(key)

                echo " setting ${splitKey} with value ${value} "
                updateFileproperties.setProperty(splitKey, value)
            }
        }
        echo "After update"
        //echo "${updateFileproperties}"
        echo "Writing file Bootstrap"
        File writeFileOut = new File(UPDATE_FILE_PATH)
        updateFileproperties.store(writeFileOut.newWriter(),null)
    }  catch(FileNotFoundException ex) {

        echo "NO property file found or property file with the wrong name,using existing properties"

    }
}

def updateBootstrapProperties(PROPERTY,VALUE){
    try{
        def bootstrapPath = "${env.checkoutPath}/Curam/EJBServer/project/properties/Bootstrap.properties"

        def File f = new File(bootstrapPath);
        if (!f.exists()){
        //echo "creating file ${bootstrapPath}"
        sh """ echo ''>${bootstrapPath} """
        }

        def properties = new Properties()
        File propertiesFile = new File(bootstrapPath)
        properties.load(propertiesFile.newDataInputStream())
        echo "setting ${PROPERTY}=${VALUE}"
        properties.setProperty("${PROPERTY}","${VALUE}")
        PrintWriter out = new PrintWriter( propertiesFile.newWriter("UTF-8") )
        Set<String> keys = properties.keySet()
        for( String key : keys ) {
            String newValue = properties.getProperty(key)
            out.println( key+"="+newValue  )
            }
        out.close()
    }  catch(FileNotFoundException ex) {
        echo "NO property file found or property file with the wrong name,using existing properties"
    }
}

def tagOnComplete(){

    if ( env.SCM_TYPE == "GIT" ) {
        def gitUtil =  new gitUtility()
        gitUtil.createTAGOnRepo()
    }

}
