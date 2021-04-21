def call(isFullbuild){

    def util = new commonUtil();
  
    if(isFullbuild == "true"){
       util.buildCommand(env.SERVER_DIR,"database") 
    }else{
        echo "TODO  data migration if required"
    }


}