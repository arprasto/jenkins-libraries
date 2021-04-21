def call() 
{               
    echo "clean up SPM folders"
    sh """
    rm -rf dockerstage
    mkdir -p dockerstage
    cp -r /spm-kubernetes/dockerfiles dockerstage
    """
    
    echo "set up SPM release "
    sh """
    #!/bin/bash
    mkdir ${env.SPM_HOME}/dockerfiles/Liberty/content/release-stage
    unzip -qd ${env.SPM_HOME}/dockerfiles/Liberty/content/release-stage ${env.WORKSPACE}/resources/release.zip
    chmod +x dockerstage/dockerfiles/Liberty/content/release-stage/*.sh
    cp -vf ${env.CURAM_DIR}/EJBServer/project/properties/AppServer.properties  ${env.SPM_HOME}/dockerfiles/Liberty/content/release-stage/project/properties/AppServer.properties
    cp -vf ${env.CURAM_DIR}/EJBServer/project/properties/Bootstrap.properties  ${env.SPM_HOME}/dockerfiles/Liberty/content/release-stage/project/properties/Bootstrap.properties
    """
                    
    echo "Prepare and setup dependencies "
    sh """
    #!/bin/bash
    cd  ${CURAM_DIR}/EJBServer
    chmod +x build.sh
    ./build.sh internal.update.crypto.jar -Dcrypto.ext.dir=sample
    cp ${CURAM_DIR}/EJBServer/build/CryptoConfig.jar ${env.SPM_HOME}/dockerfiles/Liberty/content/release-stage/build/
    cd ${env.SPM_HOME}/dockerfiles/Liberty/content
    ant -f getJavaMail.xml
    cp ${env.IBM_MQ} ${env.SPM_HOME}/dockerfiles/Liberty/content/
    cp /opt/ant/apache-ant-1.10.6-bin.zip ${env.SPM_HOME}/dockerfiles/Liberty/content/
    """

    echo "Copy static content "
    sh """
    #!/bin/bash
    cp -vf ${env.WORKSPACE}/resources/StaticContent.zip ${env.SPM_HOME}/dockerfiles/HTTPServer/             
    """
                
}
