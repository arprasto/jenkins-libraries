def call()
{                
    echo "Scaling down to zero"
    def util = new commonUtil();
    util.scale('deployment/${RELEASE_NAME}-apps-curam-consumer');
    util.scale('deployment/${RELEASE_NAME}-apps-curam-producer');
    util.scale('deployment/${RELEASE_NAME}-apps-rest-consumer');
    util.scale('deployment/${RELEASE_NAME}-apps-rest-producer');
    util.scale('deployment/${RELEASE_NAME}-xmlserver');
    util.scale('deployment/${RELEASE_NAME}-web');
    util.scale('deployment/${RELEASE_NAME}-uawebapp');
    /*removed scaledown of MQserver.
    util.scale('deployment/whmvp-mqserver-curam');
    util.scale('deployment/whmvp-mqserver-rest');
    */
   
 
}
